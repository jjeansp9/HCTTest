package kr.co.testapp0501.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.co.testapp0501.R

class CommonUtil {
    companion object{
        fun setToolbar(activity: AppCompatActivity, title: String){
            val toolbar = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            activity.setSupportActionBar(toolbar)

            val tv = activity.findViewById<TextView>(R.id.tv_toolber_title) // 타이틀 뷰
            //activity.findViewById<ImageView>(R.id.btn_notice).visibility = View.VISIBLE // 알림 아이콘

            tv.text = title
            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }


    }
}