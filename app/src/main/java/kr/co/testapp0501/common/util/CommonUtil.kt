package kr.co.testapp0501.common.util

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kr.co.testapp0501.R

class CommonUtil {
    companion object{
        fun setToolbar(
            activity: AppCompatActivity,
            cls: Class<*>,
            title: String,
            drawableId1: Int,
            drawableId2: Int,
            firstMenuOn: Boolean,
            secondMenuOn: Boolean
        ){
            val toolbar = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            activity.setSupportActionBar(toolbar)

            val tv = activity.findViewById<TextView>(R.id.tv_toolber_title) // 타이틀 뷰
            tv.visibility = View.VISIBLE
            tv.text = title

            if (firstMenuOn){ // 인자로 받은 값이 false면 이 코드는 실행 안함
                val firstMenu = activity.findViewById<ImageView>(R.id.btn_menu_first) // 메뉴아이콘 1
                firstMenu.setImageResource(drawableId1)
                firstMenu.visibility = View.VISIBLE

                // 현재 액티비티, 화면이동할 액티비티값을 파라미터로 받아서 화면이동
                if (activity.localClassName == "view.activity.AlbumActivity"){
                    firstMenu.setOnClickListener{
                        activity.startActivity(Intent(activity, cls))
                    }
                }
            }
            Log.i("activityqqq", activity.localClassName)
            if (secondMenuOn){ // 인자로 받은 값이 false면 이 코드는 실행 안함
                val secondMenu = activity.findViewById<ImageView>(R.id.btn_menu_second) // 메뉴아이콘 2
                secondMenu.setImageResource(drawableId2)
                secondMenu.visibility = View.VISIBLE
            }


            // 그룹화면인 경우 뒤로가기버튼 없애기
            if (drawableId1 == R.drawable.ic_group_setting){
                activity.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            } else {
                activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }

    }
}