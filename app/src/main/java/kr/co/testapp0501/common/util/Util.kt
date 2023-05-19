package kr.co.testapp0501.common.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kr.co.testapp0501.R
import kr.co.testapp0501.model.network.ApiService

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
        Log.i("albumtest", imageUrl.toString())
    }
    @JvmStatic
    @BindingAdapter(value = ["albumImageUrl", "albumDefaultImage"], requireAll = false)
    fun albumLoadImage(view: ImageView, imageUrl: String?, defaultImage: Int?){
        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(20))
        imageUrl?.let {
            Glide.with(view)
                .load(imageUrl)
                .apply(requestOptions)
                .placeholder(defaultImage ?: R.drawable.bt_group_plusbox)
                .into(view)
            Log.i("Util1", imageUrl.toString())
        } ?: run {
            defaultImage?.let {
                view.setImageResource(it)
                Log.i("Util2", imageUrl.toString())
            } ?: run {
                view.setImageResource(R.drawable.bt_group_plusbox)
                Log.i("Util3", imageUrl.toString())
            }
        }
    }

    private val profileImgUrl = "/member/profile/"

    @JvmStatic
    @BindingAdapter(value = ["profileImageUrl", "profileDefaultImage"], requireAll = false)
    fun profileImage(view: ImageView, imageUrl: String?, defaultImage: Int?){
        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(20))
        imageUrl?.let {
            Glide.with(view)
                .load(ApiService.FILE_SUFFIX_URL + profileImgUrl + imageUrl)
                .apply(requestOptions)
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