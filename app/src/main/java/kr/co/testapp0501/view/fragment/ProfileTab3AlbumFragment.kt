package kr.co.testapp0501.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import kr.co.testapp0501.databinding.FragmentProfileTab3AlbumBinding
import kr.co.testapp0501.model.recycler.RecyclerTab3AlbumData
import kr.co.testapp0501.view.adapter.RecyclerTab3AlbumAdapter

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileTab3AlbumFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val binding: FragmentProfileTab3AlbumBinding by lazy { FragmentProfileTab3AlbumBinding.inflate(layoutInflater) }
    private val setViewType = 1 // [프로필 3번째 탭], [메인화면 앨범 새글] 구분하기 위한 변수

    private val items = mutableListOf<RecyclerTab3AlbumData>()
    private val adapter by lazy { context?.let { RecyclerTab3AlbumAdapter(it, items, setViewType) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileAlbumRecycler.adapter = adapter
        binding.profileAlbumRecycler.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))

        for (i in 0 .. 10){
            items.add(RecyclerTab3AlbumData("까망이 근황이에요 ㅎㅎ", "2023.02.18", 0, ""))
        }
    }
}