package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // 등록된 프로필이 없을 때
        if (items[position].albumProfileImg == "") Glide.with(context).load(R.drawable.img_profile).into(holder.binding.imgAlbumProfile)
        // 등록된 프로필이 있을 때
        else Glide.with(context).load(items[position].albumProfileImg).into(holder.binding.imgAlbumProfile)

        // 등록된게 없을 때 [업로드 사진]
        if (items[position].albumUploadPicture == "") Glide.with(context).load(R.drawable.img_group_general).into(holder.binding.imgAlbumUploadPicture)
        // 등록된게 있을 때 [업로드 사진]
        else Glide.with(context).load(items[position].albumUploadPicture).into(holder.binding.imgAlbumUploadPicture)
    }

    override fun getItemCount(): Int = items.size
}