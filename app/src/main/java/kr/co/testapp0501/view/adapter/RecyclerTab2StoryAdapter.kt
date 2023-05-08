package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerProfileTab2ItemBinding
import kr.co.testapp0501.model.recycler.RecyclerTab2StoryItem

class RecyclerTab2StoryAdapter constructor(private val context: Context, private val items: MutableList<RecyclerTab2StoryItem>): RecyclerView.Adapter<RecyclerTab2StoryAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerProfileTab2ItemBinding = RecyclerProfileTab2ItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        var itemView: View = layoutInflater.inflate(R.layout.recycler_profile_tab2_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.tvStoryDate.text = items[position].tvStoryDate // 내 이야기 날짜
        holder.binding.tvStory.text = items[position].tvStory // 내 이야기 글
        Glide.with(context).load(items[position].imgStory).into(holder.binding.imgStory) // 내 이야기 이미지

    }

    override fun getItemCount(): Int = items.size


}