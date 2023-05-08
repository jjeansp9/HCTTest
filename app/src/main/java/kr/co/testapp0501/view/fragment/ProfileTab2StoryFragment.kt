package kr.co.testapp0501.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import kr.co.testapp0501.R
import kr.co.testapp0501.databinding.FragmentProfileTab1InfoBinding
import kr.co.testapp0501.databinding.FragmentProfileTab2StoryBinding
import kr.co.testapp0501.model.recycler.RecyclerTab2StoryItem
import kr.co.testapp0501.view.adapter.RecyclerTab2StoryAdapter
import kr.co.testapp0501.viewmodel.ProfileViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileTab2StoryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileTab2StoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val binding: FragmentProfileTab2StoryBinding by lazy { FragmentProfileTab2StoryBinding.inflate(layoutInflater) }

    private val items = mutableListOf<RecyclerTab2StoryItem>()
    private val adapter by lazy { context?.let { RecyclerTab2StoryAdapter(it, items) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileStoryRecycler.adapter = adapter
        binding.profileStoryRecycler.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))

        for (i in 0 .. 10){
            items.add(RecyclerTab2StoryItem("1999.01.24",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                ""
            ))
        }
    }




}