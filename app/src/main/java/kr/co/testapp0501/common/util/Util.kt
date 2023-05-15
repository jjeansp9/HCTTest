package kr.co.testapp0501.common.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kr.co.testapp0501.R

object Util {
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "defaultImage"], requireAll = false)
    fun loadImage(view: ImageView, imageUrl: String?, defaultImage: Int?){
        imageUrl?.let {
            Glide.with(view)
                .load(it)
                .placeholder(defaultImage ?: R.drawable.img_profile)
                .into(view)
        } ?: run {
            defaultImage?.let {
                view.setImageResource(it)
            } ?: run {
                view.setImageResource(R.drawable.img_profile)
            }
        }
    }
}