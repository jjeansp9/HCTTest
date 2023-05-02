package kr.co.avad.android.humancaretree.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * Base 로 사용할 Fragment
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    lateinit var viewDataBinding: T

    /**
     * layout xml
     */
    abstract val layoutResourceId : Int

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return viewDataBinding.root
    }
}