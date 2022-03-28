package com.yenaly.cqupttoolbox.ui.fragment.search.empty_room

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yenaly.cqupttoolbox.ui.fragment.search.empty_room.building.Building
import com.yenaly.cqupttoolbox.ui.fragment.search.empty_room.building.BuildingFragment

/**
 * A adapter for empty rooms' view pager.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/19 019 11:04
 * @Description : Description...
 */
class EmptyRoomViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    roomsList: List<String>
) : FragmentStateAdapter(fragmentActivity) {

    private val buildingTwo   = ArrayList<String>()
    private val buildingThree = ArrayList<String>()
    private val buildingFour  = ArrayList<String>()
    private val buildingFive  = ArrayList<String>()
    private val buildingEight = ArrayList<String>()

    init {
        for (room in roomsList) when {
            room.startsWith('2') -> buildingTwo.add(room)
            room.startsWith('3') -> buildingThree.add(room)
            room.startsWith('4') -> buildingFour.add(room)
            room.startsWith('5') -> buildingFive.add(room)
            room.startsWith('8') -> buildingEight.add(room)
        }
    }

    override fun getItemCount(): Int {
        return Building.values().size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Building.TWO.page   -> BuildingFragment.newInstance(buildingTwo)
            Building.THREE.page -> BuildingFragment.newInstance(buildingThree)
            Building.FOUR.page  -> BuildingFragment.newInstance(buildingFour)
            Building.FIVE.page  -> BuildingFragment.newInstance(buildingFive)
            Building.EIGHT.page -> BuildingFragment.newInstance(buildingEight)
            else                -> Fragment()
        }
    }
}