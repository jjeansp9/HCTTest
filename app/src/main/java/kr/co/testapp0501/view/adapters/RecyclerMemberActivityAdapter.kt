package kr.co.testapp0501.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerGroupItemBinding
import kr.co.testapp0501.databinding.RecyclerMemberItemBinding
import kr.co.testapp0501.model.recycler.RecyclerMemberData

class RecyclerMemberActivityAdapter constructor(private val context: Context, private var items: MutableList<RecyclerMemberData>): RecyclerView.Adapter<RecyclerMemberActivityAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerMemberItemBinding = RecyclerMemberItemBinding.bind(itemView)
    }

    // (1) 리스너 인터페이스
    interface OnItemClickListener {
        fun memberClick(v: View, position: Int)
    }
    // (2) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (3) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        var itemView: View = layoutInflater.inflate(R.layout.recycler_member_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.binding.itemRoot.setOnClickListener{itemClickListener.memberClick(holder.binding.itemRoot, position)} // 멤버 리스트를 클릭했을 때

        Glide.with(context).load(R.drawable.bg_edit_input).into(holder.binding.imgMember) // 멤버 프로필사진
        holder.binding.tvName.text = items[position].tvName // 멤버 이름
    }

    override fun getItemCount(): Int = items.size
}