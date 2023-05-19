package kr.co.testapp0501.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.LayoutBoardDetailItemBinding
import kr.co.testapp0501.databinding.RecyclerAlbumItemBinding
import kr.co.testapp0501.model.album.AlbumModel
import kr.co.testapp0501.model.board.BoardDetailModel
import kr.co.testapp0501.model.network.ApiService

class BoardDetailAdapter constructor(private val context: Context, private val items: MutableList<BoardDetailModel>): RecyclerView.Adapter<BoardDetailAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: LayoutBoardDetailItemBinding = LayoutBoardDetailItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.layout_board_detail_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val imgUrl = "/board/album/"
        Log.i("Adapter", ApiService.FILE_SUFFIX_URL +imgUrl)

        Glide.with(context).load(ApiService.FILE_SUFFIX_URL +imgUrl+ items[position].imgAlbum).into(holder.binding.imgDetail)

    }

    override fun getItemCount(): Int = items.size
}