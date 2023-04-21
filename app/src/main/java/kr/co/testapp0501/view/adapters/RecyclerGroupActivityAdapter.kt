package kr.co.testapp0501.view.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerGroupItemBinding
import kr.co.testapp0501.model.RecyclerGroupData

class RecyclerGroupActivityAdapter constructor(private val context: Context, private var items: MutableList<RecyclerGroupData>): RecyclerView.Adapter<RecyclerGroupActivityAdapter.VH>(){

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerGroupItemBinding = RecyclerGroupItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)
        var itemView: View = layoutInflater.inflate(R.layout.recycler_group_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        //Glide.with(context).load(items[position].imgGroup).into(holder.binding.imgGroup)
        Glide.with(context).load(R.drawable.bg_edit_input).into(holder.binding.imgGroup)
        holder.binding.tvGroupName.text = items[position].tvGroupName
        //holder.binding.tvGroupAdmin.text = items[position].tvGroupAdmin
    }

    override fun getItemCount(): Int = items.size
}