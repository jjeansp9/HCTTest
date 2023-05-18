package kr.co.testapp0501.common.util

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kr.co.testapp0501.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField
import java.util.*

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
//                if (activity.localClassName == "view.activity.AlbumActivity"){
//                    firstMenu.setOnClickListener{
//                        activity.startActivity(Intent(activity, cls))
//                    }
//                }
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

        // 이미지 파일 절대경로를 반환하는 메소드
        fun absolutelyPath(path: Uri?, context : Context): String {
            val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
            val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
            val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            c?.moveToFirst()

            val result = c?.getString(index!!)
            c?.close()

            return result!!
        }

        // 시간 형식을 변환하는 메소드
        fun convertDateTimeString(dateTimeString: String): String {
            val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val outputFormatter = DateTimeFormatterBuilder()
                .appendPattern("yyyy년 M월 d일 ")
                .appendPattern("a hh:mm")
                .toFormatter(Locale.KOREA)
            val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)

            val adjustedDateTime = dateTime.minusHours(3)

            return adjustedDateTime.format(outputFormatter)
        }
    }
}



























