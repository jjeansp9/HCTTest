package kr.co.testapp0501.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Base 로 사용할 Fragment
 */
abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int
) : Fragment() {
    lateinit var viewDataBinding: T


    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return viewDataBinding.root
    }
}