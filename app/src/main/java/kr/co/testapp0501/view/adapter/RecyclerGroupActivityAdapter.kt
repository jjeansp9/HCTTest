package kr.co.testapp0501.view.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.roundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.RecyclerGroupItemBinding
import kr.co.testapp0501.model.recycler.RecyclerGroupData
import java.io.File

class RecyclerGroupActivityAdapter constructor(private val context: Context, private var items: MutableList<RecyclerGroupData>): RecyclerView.Adapter<RecyclerGroupActivityAdapter.VH>(){

    inner class VH constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: RecyclerGroupItemBinding = RecyclerGroupItemBinding.bind(itemView)
    }

    interface OnItemClickListener {
        fun groupClick(v: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    private lateinit var itemClickListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater:LayoutInflater = LayoutInflater.from(context)
        val itemView: View = layoutInflater.inflate(R.layout.recycler_group_item, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.binding.groupRoot.setOnClickListener { itemClickListener.groupClick(holder.binding.groupRoot, position) } // 그룹목록 클릭이벤트

        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(20))

        Log.i("adapterUrl", items[position].imgGroup)

        if (items[position].imgGroup == "") Glide.with(context).load(R.drawable.img_group_general).apply(requestOptions).into(holder.binding.imgGroup) // 그룹목록 이미지
        else Glide.with(context).load(items[position].imgGroup).apply(requestOptions).into(holder.binding.imgGroup) // 그룹목록 이미지

        holder.binding.tvGroupName.text = items[position].tvGroupName // 그룹 이름
        holder.binding.tvGroupAdmin.text = items[position].tvGroupAdmin // 그룹 관리자
        Log.i("swipes position", position.toString())

        // 권한에 따라 관리자 뱃지 숨기기/보여주기
        if (items[position].memberAuthLevel == 1){
            holder.binding.tvGroupAdmin.visibility = View.VISIBLE
            holder.binding.tvGroupAdmin.setBackgroundResource(R.drawable.bg_group_admin)
        }else{
            holder.binding.tvGroupAdmin.visibility = View.GONE
            holder.binding.tvGroupAdmin.background = null
        }

        // 마지막에 생성된 그룹목록은 [+] box의 형태로 생성
        if(items.lastIndex == position){
            Log.i("swipe lastIndex", position.toString())
            holder.binding.imgGroup.setBackgroundResource(R.drawable.bt_group_plusbox)
            holder.binding.tvGroupAdmin.text = ""
            holder.binding.tvGroupAdmin.background = null
        }else{
            holder.binding.imgGroup.setBackgroundResource(R.drawable.bg_edit)
        }
    }

    override fun getItemCount(): Int = items.size
}