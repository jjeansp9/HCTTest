package kr.co.testapp0501.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.testapp0501.databinding.LayoutAlbumUploadFooterBinding

class AlbumUploadAdapter(
    private val onAlbumFooterClick : () -> Unit
) : ListAdapter<String, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {
    inner class AlbumUploadViewHolder(private val binding : LayoutAlbumUploadFooterBinding) : RecyclerView.ViewHolder(binding.albumUploadRoot){
        fun bind(onAlbumFooterClick: () -> Unit){
            binding.albumUploadRoot.setOnClickListener{onAlbumFooterClick()}
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumUploadFooterBinding.inflate(inflater, parent, false)
        return AlbumUploadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AlbumUploadViewHolder)?.bind(onAlbumFooterClick)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}