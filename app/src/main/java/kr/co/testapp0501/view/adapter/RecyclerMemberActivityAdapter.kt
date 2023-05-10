package kr.co.testapp0501.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerMemberItemBinding
import kr.co.testapp0501.model.recycler.RecyclerMemberData

class RecyclerMemberActivityAdapter constructor(private val context: Context, private var items: MutableList<RecyclerMemberData>): RecyclerView.Adapter<RecyclerMemberActivityAdapter.VH>(){
    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerMemberItemBinding = RecyclerMemberItemBinding.bind(itemView)
    }

    // (1) 리스너 인터페이스
    interface OnItemClickListener {
        fun itemClick(v: View, position: Int)
        fun acceptClick(v: View, position: Int)
        fun cancelClick(v: View, position: Int)
    }
    // (2) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (3) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.recycler_member_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        Glide.with(context).load(R.drawable.bg_edit).into(holder.binding.imgMember) // 멤버 프로필사진
        //Glide.with(context).load(R.drawable.btn_next_selector).into(holder.binding.btnMemberNext) // 멤버 [>] 버튼

        holder.binding.tvName.text = items[position].tvName // 멤버 이름
        holder.binding.tvBirth.text = items[position].tvBirth // 멤버 생일
        //holder.binding.btnMatching.text = items[position].btnMatching // 타입

        when (items[position].memberAuthLevel) {
            10 -> {
                holder.binding.tvBirth.visibility = View.VISIBLE // 생일

                holder.binding.btnMemberAccpet.visibility = View.VISIBLE // 수락 버튼
                holder.binding.btnMemberCancel.visibility = View.VISIBLE // 거절 버튼

                holder.binding.btnMemberAccpet.setOnClickListener{itemClickListener.acceptClick(holder.binding.btnMemberAccpet, position)} // 수락
                holder.binding.btnMemberCancel.setOnClickListener{itemClickListener.cancelClick(holder.binding.btnMemberCancel, position)} // 거절
            }
            1 -> {
                //holder.binding.btnMemberNext.visibility = View.VISIBLE // 멤버 NEXT 버튼
                holder.binding.imgMember.visibility = View.VISIBLE // 멤버 프로필사진
                holder.binding.tvAdmin.visibility = View.VISIBLE // 관리자 칭호
                holder.binding.itemRoot.setOnClickListener{itemClickListener.itemClick(holder.binding.itemRoot, position)} // 멤버 리스트를 클릭했을 때
            }
//            "A" -> {
//                //holder.binding.btnMemberNext.visibility = View.VISIBLE // 멤버 NEXT 버튼
//                holder.binding.imgMember.visibility = View.VISIBLE // 멤버 프로필사진
//                holder.binding.itemRoot.setOnClickListener{itemClickListener.itemClick(holder.binding.itemRoot, position)} // 멤버 리스트를 클릭했을 때
//            }
        }
    }

    override fun getItemCount(): Int = items.size
}