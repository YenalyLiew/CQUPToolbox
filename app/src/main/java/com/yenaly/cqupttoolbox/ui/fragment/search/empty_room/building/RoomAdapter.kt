package com.yenaly.cqupttoolbox.ui.fragment.search.empty_room.building

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.logic.model.FloorWithRoom

/**
 * A adapter for each room recycler view.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/19 019 21:30
 * @Description : Description...
 */
class RoomAdapter(
    floor: String,
    floorWithRoomList: ArrayList<FloorWithRoom>
) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    private val eachFloorWithRoomList = ArrayList<String>()

    init {
        for (floorWithRoom in floorWithRoomList) {
            if (floorWithRoom.floor == floor) {
                eachFloorWithRoomList.add(floorWithRoom.room)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emptyFloorRoom: TextView = view.findViewById(R.id.empty_floor_room)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_building_floor_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eachFloorWithRoom = eachFloorWithRoomList[position]
        holder.emptyFloorRoom.text = eachFloorWithRoom
    }

    override fun getItemCount(): Int {
        return eachFloorWithRoomList.size
    }
}