package kr.co.testapp0501.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerAlbumItemBinding
import kr.co.testapp0501.model.recycler.RecyclerAlbumData

class RecyclerAlbumActivityAdapter constructor(private val context: Context, private val items: MutableList<RecyclerAlbumData>): RecyclerView.Adapter<RecyclerAlbumActivityAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerAlbumItemBinding = RecyclerAlbumItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.recycler_album_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        // 앨범 관련
        holder.binding.tvAlbumName.text = items[position].albumName // 글쓴이
        holder.binding.tvAlbumUploadDate.text = items[position].albumUploadDate // 업로드 시간
        holder.binding.tvAlbumTitle.text = items[position].albumTitle // 제목
        holder.binding.tvAlbumContents.text = items[position].albumContents // 내용

        setAlbumPicture(holder, position)
    }
    override fun getItemCount(): Int = items.size

    // 앨범 이미지 갯수마다 화면 UI 변경
    private fun setAlbumPicture(holder: VH, position: Int){

        val albumItem = items[position]

        // 등록된 프로필이 없을 때
        when {
            albumItem.albumUploadPicture.isEmpty() -> {
                Glide.with(context).load(R.drawable.img_group_general).into(holder.binding.imgAlbumUploadPictureFirst)
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 270f)
            }
            albumItem.albumUploadPicture.size == 1 -> {
                Glide.with(context).load(albumItem.albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 270f)
            }
            albumItem.albumUploadPicture.size == 2 -> {
                Glide.with(context).load(albumItem.albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst) // 첫째 업로드 사진
                Glide.with(context).load(albumItem.albumUploadPicture[1]).into(holder.binding.imgAlbumUploadPictureSecond) // 둘째 업로드 사진
                holder.binding.layoutAlbumUploadPicture.layoutParams.height = dpToPx(context, 180f)
                holder.binding.imgAlbumPictureLine.visibility = View.VISIBLE
                holder.binding.layoutAlbumUploadPictureSecond.visibility = View.VISIBLE
            }
            else -> {
                val count = albumItem.albumUploadPicture.size - 2
                holder.binding.tvPictureCount.text = count.toString()

                Glide.with(context).load(albumItem.albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
                Glide.with(context).load(albumItem.albumUploadPicture[1]).into(holder.binding.imgAlbumUploadPictureSecond)
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
























