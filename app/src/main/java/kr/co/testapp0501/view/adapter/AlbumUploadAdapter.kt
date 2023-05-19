package kr.co.testapp0501.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.testapp0501.databinding.LayoutAlbumUploadFooterBinding
import kr.co.testapp0501.databinding.LayoutAlbumUploadItemBinding
import kr.co.testapp0501.model.album.AlbumUploadPhotoModel

class AlbumUploadAdapter(
    private val onAlbumPhotoClick : (AlbumUploadPhotoModel) -> Unit,
    private val onAlbumFooterClick : () -> Unit
) : ListAdapter<AlbumUploadPhotoModel, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {
    inner class AlbumPhotoViewHolder(private val binding: LayoutAlbumUploadItemBinding) :
        RecyclerView.ViewHolder(binding.albumUploadRoot) {
        fun bind(
            photoItem: AlbumUploadPhotoModel,
            onAlbumPhotoClick: (AlbumUploadPhotoModel) -> Unit
        ) {
            binding.item = photoItem
            binding.albumUploadRoot.setOnClickListener { onAlbumPhotoClick(photoItem) }
            binding.executePendingBindings()
        }
    }

    inner class AlbumFooterViewHolder(private val binding: LayoutAlbumUploadFooterBinding) :
        RecyclerView.ViewHolder(binding.albumFooterRoot) {
        fun bind(onAlbumFooterClick: () -> Unit) {
            binding.root.setOnClickListener { onAlbumFooterClick() }
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumPhotoViewHolder -> holder.bind(getItem(position) as AlbumUploadPhotoModel, onAlbumPhotoClick)
            is AlbumFooterViewHolder -> holder.bind(onAlbumFooterClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PHOTO -> {
                val binding = LayoutAlbumUploadItemBinding.inflate(inflater, parent, false)
                AlbumPhotoViewHolder(binding)
            }
            VIEW_TYPE_FOOTER -> {
                val binding = LayoutAlbumUploadFooterBinding.inflate(inflater, parent, false)
                AlbumFooterViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPE_FOOTER else VIEW_TYPE_PHOTO
    }

    companion object {
        private const val VIEW_TYPE_PHOTO = 1
        private const val VIEW_TYPE_FOOTER = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AlbumUploadPhotoModel>() {
            override fun areItemsTheSame(
                oldItem: AlbumUploadPhotoModel,
                newItem: AlbumUploadPhotoModel
            ): Boolean {
                return oldItem.photo == newItem.photo
            }

            override fun areContentsTheSame(
                oldItem: AlbumUploadPhotoModel,
                newItem: AlbumUploadPhotoModel
            ): Boolean {
                return oldItem.photo == newItem.photo
            }
        }
    }
}