package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerProfileTab3ItemBinding
import kr.co.testapp0501.model.recycler.RecyclerTab3AlbumData

class RecyclerTab3AlbumAdapter constructor(private val context: Context, private val items: MutableList<RecyclerTab3AlbumData>, getViewType: Int): RecyclerView.Adapter<RecyclerTab3AlbumAdapter.VH>(){

    val type = getViewType

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvAlbumTitle: TextView by lazy { itemView.findViewById(R.id.tv_album_title) }
        val tvAlbumDate: TextView by lazy { itemView.findViewById(R.id.tv_album_date) }
        val tvNumOfComments: TextView by lazy { itemView.findViewById(R.id.tv_num_of_comments) }
        val imgAlbum: ImageView by lazy { itemView.findViewById(R.id.img_album) }
        val albumUpdateRoot: ConstraintLayout by lazy { itemView.findViewById(R.id.album_update_root) }
        val boardRoot: ConstraintLayout by lazy { itemView.findViewById(R.id.board_root) }
    }

    interface OnItemClickListener {
        fun rootClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        // type 이 0이면 R.layout.recycler_main_album_update_item 연결
        // type 이 1이면 recycler_profile_tab3_item 연결
        val itemView =
            if (type == 0) layoutInflater.inflate(R.layout.recycler_main_album_update_item, parent, false)
            else layoutInflater.inflate(R.layout.recycler_profile_tab3_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvAlbumTitle.text = items[position].tvAlbumTitle
        holder.tvAlbumDate.text = items[position].tvAlbumDate
        holder.tvNumOfComments.text = items[position].tvNumOfComments.toString()

        if (type == 0) holder.albumUpdateRoot.setOnClickListener{itemClickListener.rootClick(holder.albumUpdateRoot, position)} // 메인 하단 앨범 새글
        else if (type == 1) holder.boardRoot.setOnClickListener{itemClickListener.rootClick(holder.boardRoot, position)} // 프로필의 앨범, 자료실

        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(20))
        if (items[position].imgAlbum == "") Glide.with(context).load(R.drawable.img_story).apply(requestOptions).into(holder.imgAlbum) // 앨범글 이미지
        else Glide.with(context).load(items[position].imgAlbum).apply(requestOptions).into(holder.imgAlbum) // 앨범글 이미지
    }

    override fun getItemCount(): Int = items.size
}