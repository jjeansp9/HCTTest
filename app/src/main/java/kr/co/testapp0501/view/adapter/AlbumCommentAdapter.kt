package kr.co.testapp0501.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.testapp0501.databinding.LayoutAlbumCommentItemBinding
import kr.co.testapp0501.model.album.AlbumCommentItemModel

class AlbumCommentAdapter(
    private val onAlbumCommentItemClick: (String) -> Unit
    ) : ListAdapter<AlbumCommentItemModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    inner class AlbumCommentViewHolder(private val binding: LayoutAlbumCommentItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(albumCommentItem: AlbumCommentItemModel, onAlbumCommentItemClick: (String) -> Unit){
            binding.item = albumCommentItem
            //binding.root.setOnClickListener{onAlbumCommentItemClick(albumCommentItem)}
            binding.btnCommentReply.setOnClickListener{
                onAlbumCommentItemClick(albumCommentItem.replyClick)
                binding.layoutAlbumCommentReply.visibility = View.VISIBLE
            }
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumCommentItemBinding.inflate(inflater, parent, false)
        return AlbumCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AlbumCommentViewHolder)?.bind(getItem(position) as? AlbumCommentItemModel ?: return, onAlbumCommentItemClick)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlbumCommentItemModel>(){
            override fun areItemsTheSame(
                oldItem: AlbumCommentItemModel,
                newItem: AlbumCommentItemModel
            ): Boolean {
                return oldItem.tvCommentDate == newItem.tvCommentDate
            }

            override fun areContentsTheSame(
                oldItem: AlbumCommentItemModel,
                newItem: AlbumCommentItemModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}