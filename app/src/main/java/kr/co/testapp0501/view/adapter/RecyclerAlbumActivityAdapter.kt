package kr.co.testapp0501.view.adapter

import android.content.Context
import android.graphics.LightingColorFilter
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerAlbumItemBinding
import kr.co.testapp0501.model.album.AlbumModel

class RecyclerAlbumActivityAdapter constructor(private val context: Context, private val items: MutableList<AlbumModel>): RecyclerView.Adapter<RecyclerAlbumActivityAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerAlbumItemBinding = RecyclerAlbumItemBinding.bind(itemView)
    }

    // (1) 리스너 인터페이스
    interface OnItemClickListener {
        fun profileImgClick(v: View, position: Int) // 프로필 이미지 클릭
        fun pictureClick(v: View, position: Int) // 앨범 업로드 이미지 클릭
        fun albumSetClick(v: View, position: Int) // 앨범 설정 클릭
        fun likeClick(v: View, position: Int) // 좋아요 클릭
        fun commentClick(v: View, position: Int) // 댓글 클릭
    }
    // (2) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (3) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.recycler_album_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.binding.imgAlbumProfile.setOnClickListener { itemClickListener.profileImgClick(holder.binding.imgAlbumProfile, position) } // 프로필 이미지
        holder.binding.layoutAlbumUploadPicture.setOnClickListener { itemClickListener.pictureClick(holder.binding.layoutAlbumUploadPicture, position) } // 앨범 업로드 이미지
        holder.binding.albumSet.setOnClickListener { itemClickListener.albumSetClick(holder.binding.albumSet, position) } // 앨범 설정 이미지
        holder.binding.layoutLike.setOnClickListener { itemClickListener.likeClick(holder.binding.layoutLike, position) } // 좋아요
        holder.binding.layoutComment.setOnClickListener { itemClickListener.commentClick(holder.binding.layoutComment, position) } // 댓글

        // 앨범 관련
        holder.binding.tvAlbumName.text = items[position].albumName // 글쓴이
        holder.binding.tvAlbumUploadDate.text = items[position].albumUploadDate // 업로드 시간
        holder.binding.tvAlbumTitle.text = items[position].albumTitle // 제목
        holder.binding.tvAlbumContents.text = items[position].albumContents // 내용
        holder.binding.tvLikeCount.text = items[position].albumLike.toString() // 좋아요
        holder.binding.tvCommentCount.text = items[position].albumComment.toString() // 댓글

        // 앨범 이미지 갯수에 따라 업로드 이미지 UI 변경
        setAlbumPictureUI(holder, position)
        // 좋아요, 댓글 갯수마다 업로드 이미지 UI 변경
        setLikeAndCommentUI(holder, position)
    }
    override fun getItemCount(): Int = items.size

    // 좋아요, 댓글 갯수에 따라 UI 변경
    private fun setLikeAndCommentUI(holder: VH, position: Int){
        when { // 좋아요
            items[position].albumLike == 0 -> {
                holder.binding.tvLike.visibility = View.VISIBLE
                holder.binding.tvLikeCount.visibility = View.GONE
            }
            items[position].albumLike > 0 ->{
                holder.binding.tvLike.visibility = View.GONE
                holder.binding.tvLikeCount.visibility = View.VISIBLE
            }
        }

        when { // 댓글
            items[position].albumComment == 0 -> {
                holder.binding.tvComment.visibility = View.VISIBLE
                holder.binding.tvCommentCount.visibility = View.GONE
            }
            items[position].albumComment > 0 ->{
                holder.binding.tvComment.visibility = View.GONE
                holder.binding.tvCommentCount.visibility = View.VISIBLE
            }
        }
    }

    // 앨범 이미지 갯수마다 업로드 이미지 UI 변경
    private fun setAlbumPictureUI(holder: VH, position: Int){

        val albumPosition = items[position]

        // 등록된 프로필이 없을 때
        when {
            albumPosition.albumUploadPicture.isEmpty() -> {
                Glide.with(context).load(R.drawable.img_group_general).into(holder.binding.imgAlbumUploadPictureFirst)
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 270f)
            }
            albumPosition.albumUploadPicture.size == 1 -> {
                Glide.with(context).load(albumPosition.albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 270f)
            }
            albumPosition.albumUploadPicture.size == 2 -> {
                Glide.with(context).load(albumPosition.albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst) // 첫째 업로드 사진
                Glide.with(context).load(albumPosition.albumUploadPicture[1]).into(holder.binding.imgAlbumUploadPictureSecond) // 둘째 업로드 사진
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 180f)
                holder.binding.imgAlbumPictureLine.visibility = View.VISIBLE
                holder.binding.layoutAlbumUploadPictureSecond.visibility = View.VISIBLE
            }
            else -> {
                val count = albumPosition.albumUploadPicture.size - 2
                holder.binding.tvPictureCount.text = count.toString()

                Glide.with(context).load(albumPosition.albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
                Glide.with(context).load(albumPosition.albumUploadPicture[1]).into(holder.binding.imgAlbumUploadPictureSecond)
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 180f)

                holder.binding.imgAlbumPictureLine.visibility = View.VISIBLE
                holder.binding.layoutPictureCount.visibility = View.VISIBLE
                holder.binding.layoutAlbumUploadPictureSecond.visibility = View.VISIBLE

                holder.binding.imgAlbumUploadPictureSecond.colorFilter = LightingColorFilter(
                    ContextCompat.getColor(context, R.color.picture_dark),
                    ContextCompat.getColor(context, R.color.picture_alpha)
                )
            }
        }
    }

    // dp 값을 px 단위로 변환하는 함수
    fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}
























