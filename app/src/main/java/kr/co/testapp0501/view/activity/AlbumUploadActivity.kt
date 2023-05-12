package kr.co.testapp0501.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import kr.co.testapp0501.R
import kr.co.testapp0501.common.CommonUtil

class AlbumUploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_upload)

        setToolbar()
    }

    private fun setToolbar(){
        CommonUtil.setToolbar(
            this,
            javaClass,
            "게시물 작성",
            0,
            0,
            false,
            false
        )
    }

    // 뒤로가기 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}