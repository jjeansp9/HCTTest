package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerProfileTab3ItemBinding
import kr.co.testapp0501.model.recycler.RecyclerTab3AlbumData

class RecyclerTab3AlbumAdapter constructor(private val context: Context, private val items: MutableList<RecyclerTab3AlbumData>): RecyclerView.Adapter<RecyclerTab3AlbumAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerProfileTab3ItemBinding = RecyclerProfileTab3ItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        var itemView: View = layoutInflater.inflate(R.layout.recycler_profile_tab3_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.tvAlbumTitle.text = items[position].tvAlbumTitle
        holder.binding.tvAlbumDate.text = items[position].tvAlbumDate.toString()
        holder.binding.tvNumOfComments.text = items[position].tvNumOfComments.toString()
        Glide.with(context).load(items[position].imgAlbum).into(holder.binding.imgAlbum)
    }

    override fun getItemCount(): Int = items.size
}