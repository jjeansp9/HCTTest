package kr.co.avad.android.humancaretree.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Base 로 사용할 Activity
 */
abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int
    ) : AppCompatActivity() {

    lateinit var viewDataBinding : T



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
    }
}