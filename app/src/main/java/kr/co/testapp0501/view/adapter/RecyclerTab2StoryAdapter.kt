package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerProfileTab2ItemBinding
import kr.co.testapp0501.model.recycler.RecyclerTab2StoryData

class RecyclerTab2StoryAdapter constructor(private val context: Context, private val items: MutableList<RecyclerTab2StoryData>): RecyclerView.Adapter<RecyclerTab2StoryAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerProfileTab2ItemBinding = RecyclerProfileTab2ItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.recycler_profile_tab2_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.tvStoryDate.text = items[position].tvStoryDate // 내 이야기 날짜
        holder.binding.tvStory.text = items[position].tvStory // 내 이야기 글

        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(20))
        if (items[position].imgStory == "") Glide.with(context).load(R.drawable.img_story).apply(requestOptions).into(holder.binding.imgStory) // 이야기 이미지
        else Glide.with(context).load(items[position].imgStory).apply(requestOptions).into(holder.binding.imgStory) // 이야기 이미지

    }

    override fun getItemCount(): Int = items.size


}