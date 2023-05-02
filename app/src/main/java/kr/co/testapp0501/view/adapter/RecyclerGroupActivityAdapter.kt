package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerGroupItemBinding
import kr.co.testapp0501.model.recycler.RecyclerGroupData

class RecyclerGroupActivityAdapter constructor(private val context: Context, private var items: MutableList<RecyclerGroupData>): RecyclerView.Adapter<RecyclerGroupActivityAdapter.VH>(){

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerGroupItemBinding = RecyclerGroupItemBinding.bind(itemView)
    }

    // (1) 리스너 인터페이스
    interface OnItemClickListener {
        fun groupClick(v: View, position: Int)
    }
    // (2) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (3) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)
        var itemView: View = layoutInflater.inflate(R.layout.recycler_group_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.binding.groupRoot.setOnClickListener { itemClickListener.groupClick(holder.binding.groupRoot, position) }

        //Glide.with(context).load(items[position].imgGroup).into(holder.binding.imgGroup)
        Glide.with(context).load(items[position].imgGroup).into(holder.binding.imgGroup)
        holder.binding.tvGroupName.text = items[position].tvGroupName
        //holder.binding.tvGroupAdmin.text = items[position].tvGroupAdmin

        if(items[position].imgGroup == "add"){
            holder.binding.imgGroup.setBackgroundResource(R.drawable.bt_group_plusbox)
            holder.binding.tvGroupAdmin.text = ""
            holder.binding.tvGroupAdmin.background = null
        }
    }

    override fun getItemCount(): Int = items.size
}