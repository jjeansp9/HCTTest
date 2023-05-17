package kr.co.testapp0501.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.testapp0501.databinding.LayoutAlbumUploadFooterBinding
import kr.co.testapp0501.model.album.AlbumUploadPhotoModel

class AlbumUploadAdapter(
    private val onAlbumPhotoClick : () -> Unit,
    private val onAlbumFooterClick : () -> Unit
) : ListAdapter<AlbumUploadPhotoModel, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {
    inner class AlbumFooterViewHolder(private val binding : LayoutAlbumUploadFooterBinding) : RecyclerView.ViewHolder(binding.albumUploadRoot){
        fun bind(onAlbumFooterClick: () -> Unit){
            binding.albumUploadRoot.setOnClickListener{onAlbumFooterClick()}
            binding.executePendingBindings()
        }
    }
//    inner class AlbumPhotoViewHolder(private val binding : Layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutAlbumUploadFooterBinding.inflate(inflater, parent, false)
        return AlbumFooterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? AlbumFooterViewHolder)?.bind(onAlbumFooterClick)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlbumUploadPhotoModel>(){
            override fun areItemsTheSame(oldItem: AlbumUploadPhotoModel, newItem: AlbumUploadPhotoModel): Boolean {
                return oldItem.photo == newItem.photo
            }

            override fun areContentsTheSame(oldItem: AlbumUploadPhotoModel, newItem: AlbumUploadPhotoModel): Boolean {
                return oldItem.photo == newItem.photo
            }

        }
    }
}