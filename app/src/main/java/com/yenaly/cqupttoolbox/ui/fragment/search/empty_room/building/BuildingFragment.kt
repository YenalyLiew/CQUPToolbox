package com.yenaly.cqupttoolbox.ui.fragment.search.empty_room.building

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yenaly.cqupttoolbox.R

/**
 * A fragment for each building.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/24 024 12:42
 * @Description : Description...
 */
class BuildingFragment : Fragment() {

    private lateinit var buildingRv: RecyclerView
    private var emptyRoomList: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_building, container, false)
        buildingRv = view.findViewById(R.id.building_rv)
        emptyRoomList = arguments?.getStringArrayList("empty_rooms_list")
        Log.d("empty_room_list", emptyRoomList.toString())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        buildingRv.layoutManager = layoutManager
        buildingRv.adapter = BuildingAdapter(this, emptyRoomList ?: ArrayList())
        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        buildingRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (fab.isShown) fab.hide()
                } else if (dy < 0) {
                    if (!fab.isShown) fab.show()
                } else fab.show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(rooms: ArrayList<String>): Fragment {
            val fragment = BuildingFragment()
            val args = Bundle()
            args.putStringArrayList("empty_rooms_list", rooms)
            fragment.arguments = args
            return fragment
        }
    }
}