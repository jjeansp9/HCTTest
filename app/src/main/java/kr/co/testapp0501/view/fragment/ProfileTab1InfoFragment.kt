package kr.co.testapp0501.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.testapp0501.R
import kr.co.testapp0501.base.BaseFragment
import kr.co.testapp0501.databinding.FragmentProfileTab1InfoBinding
import kr.co.testapp0501.viewmodel.ProfileViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileTab1InfoFragment(private val jwtToken: String, private val memberSeq: Int) : BaseFragment<FragmentProfileTab1InfoBinding>(R.layout.fragment_profile_tab1_info) {
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ProfileTab1InfoFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentProfileTab1InfoBinding.inflate(inflater, container, false)
        viewDataBinding.vmProfile = ProfileViewModel()
        viewDataBinding.lifecycleOwner = this
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        loadProfileInfo(jwtToken, memberSeq)
    }

    private fun loadProfileInfo(jwtToken: String, memberSeq: Int){
        viewDataBinding.vmProfile?.requestMemberInfo(jwtToken, memberSeq)
    }

}
















