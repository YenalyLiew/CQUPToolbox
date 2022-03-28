package com.yenaly.cqupttoolbox.ui.fragment.search.empty_room.building

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.logic.model.FloorWithRoom

/**
 * An adapter for each building recycler view.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/19 019 21:19
 * @Description : Description...
 */
class BuildingAdapter(private val fragment: Fragment, rooms: ArrayList<String>) :
    RecyclerView.Adapter<BuildingAdapter.ViewHolder>() {

    private val floorWithRoomList = ArrayList<FloorWithRoom>()
    private val floorList = ArrayList<String>()
    private val floorMap = mapOf(
        "1" to "一楼",
        "2" to "二楼",
        "3" to "三楼",
        "4" to "四楼",
        "5" to "五楼",
        "6" to "六楼",
        "7" to "七楼"
    )

    init {
        for (room in rooms) {
            val floor = room.substring(1, 2)
            floorWithRoomList.add(FloorWithRoom(floor, room))
            if (floorList.size > 0) {
                if (floorList[floorList.size - 1] != floor)
                    floorList.add(floor)
            } else floorList.add(floor)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emptyFloor: TextView = view.findViewById(R.id.empty_floor)
        val emptyFloorRoomRv: RecyclerView = view.findViewById(R.id.empty_floor_room_rv)

        init {
            val layoutManager = GridLayoutManager(fragment.activity, 3)
            emptyFloorRoomRv.layoutManager = layoutManager
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_building_floor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.emptyFloor.text = floorMap[floorList[position]]
        holder.emptyFloorRoomRv.adapter = RoomAdapter(floorList[position], floorWithRoomList)
    }

    override fun getItemCount(): Int {
        return floorList.size
    }

}