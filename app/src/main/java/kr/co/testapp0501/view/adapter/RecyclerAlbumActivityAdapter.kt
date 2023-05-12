package kr.co.testapp0501.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {

        // 앨범 관련
        holder.binding.tvAlbumName.text = items[position].albumName // 글쓴이
        holder.binding.tvAlbumUploadDate.text = items[position].albumUploadDate // 업로드 시간
        holder.binding.tvAlbumTitle.text = items[position].albumTitle // 제목
        holder.binding.tvAlbumContents.text = items[position].albumContents // 내용

        // 등록된 프로필이 없을 때
        if (items[position].albumProfileImg == "") Glide.with(context).load(R.drawable.img_profile).into(holder.binding.imgAlbumProfile)
        // 등록된 프로필이 있을 때
        else Glide.with(context).load(items[position].albumProfileImg).into(holder.binding.imgAlbumProfile)

        val count : Int

        // 등록된게 없을 때 [업로드 사진]
        if (items[position].albumUploadPicture.isEmpty()) Glide.with(context).load(R.drawable.img_group_general).into(holder.binding.imgAlbumUploadPictureFirst)

        else if (items[position].albumUploadPicture.size == 1){ // 이미지가 1개일 때
            Glide.with(context).load(items[position].albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
        }
        // 등록된게 있을 때 [업로드 사진]
        else if (items[position].albumUploadPicture.size == 2){ // 이미지가 2개일 때
            Glide.with(context).load(items[position].albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
            Glide.with(context).load(items[position].albumUploadPicture[1]).into(holder.binding.imgAlbumUploadPictureSecond)
            holder.binding.imgAlbumPictureLine.visibility = View.VISIBLE
            holder.binding.layoutAlbumUploadPictureSecond.visibility = View.VISIBLE
        }
        else if (items[position].albumUploadPicture.size > 2){ // 이미지가 2개 이상일 때
            count = items[position].albumUploadPicture.size - 2
            holder.binding.imgPictureCount.text = "+ $count"

            Glide.with(context).load(items[position].albumUploadPicture[0]).into(holder.binding.imgAlbumUploadPictureFirst)
            Glide.with(context).load(items[position].albumUploadPicture[1]).into(holder.binding.imgAlbumUploadPictureSecond)

            holder.binding.imgAlbumPictureLine.visibility = View.VISIBLE
            holder.binding.imgPictureCount.visibility = View.VISIBLE
            holder.binding.layoutAlbumUploadPictureSecond.visibility = View.VISIBLE

            // 업로드된 2번째 이미지의 밝기조절
            holder.binding.imgAlbumUploadPictureSecond.colorFilter = LightingColorFilter(
                ContextCompat.getColor(context, R.color.picture_dark),
                ContextCompat.getColor(context, R.color.picture_alpha)
            )
        }
    }

    override fun getItemCount(): Int = items.size
}
























