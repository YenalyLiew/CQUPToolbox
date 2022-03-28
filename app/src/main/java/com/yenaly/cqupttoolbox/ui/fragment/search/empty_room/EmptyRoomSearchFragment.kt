package com.yenaly.cqupttoolbox.ui.fragment.search.empty_room

import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jaredrummler.materialspinner.MaterialSpinner
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.FragmentEmptyRoomSearchBinding
import com.yenaly.cqupttoolbox.ui.fragment.search.empty_room.building.Building
import com.yenaly.cqupttoolbox.ui.viewmodel.MainViewModel
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast
import org.jsoup.Jsoup

/**
 * A fragment for searching empty rooms.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/18 018 19:45
 * @Description : Description...
 */

class EmptyRoomSearchFragment : Fragment(), MaterialSpinner.OnItemSelectedListener<String> {

    private var _binding: FragmentEmptyRoomSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private val schoolWeek = List(19) { (it + 1).toString() }
    private val week = listOf("一", "二", "三", "四", "五", "六", "日")
    private val weekMap =
        mapOf(
            "一" to 1, "二" to 2, "三" to 3,
            "四" to 4, "五" to 5, "六" to 6, "日" to 7
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmptyRoomSearchBinding.inflate(inflater, container, false)
        binding.firstWeek.setItems(schoolWeek)
        binding.endWeek.setItems(schoolWeek)
        binding.week.setItems(week)
        if (!binding.fab.isShown) binding.fab.show()
        binding.emptyRoomViewPager.apply {
            adapter = EmptyRoomViewPagerAdapter(
                requireActivity(),
                viewModel.roomsList
            )
            isUserInputEnabled = true
            offscreenPageLimit = 2
        }

        binding.fab.setImageResource(R.drawable.ic_search)

        TabLayoutMediator(binding.emptyRoomTabLayout, binding.emptyRoomViewPager) { tab, position ->
            when (position) {
                Building.TWO.page -> tab.setText(R.string.building_two)
                Building.THREE.page -> tab.setText(R.string.building_three)
                Building.FOUR.page -> tab.setText(R.string.building_four)
                Building.FIVE.page -> tab.setText(R.string.building_five)
                Building.EIGHT.page -> tab.setText(R.string.building_eight)
            }
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstWeek.setOnItemSelectedListener(this)
        binding.endWeek.setOnItemSelectedListener(this)
        binding.week.setOnItemSelectedListener(this)

        binding.emptyRoomTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.viewPagerCurrentPage = tab?.position ?: 0
                if (!binding.fab.isShown) binding.fab.show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        binding.fab.setOnClickListener {
            if (viewModel.classesCheckBoxSet.isNotEmpty()) {
                it.isEnabled = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    it.performHapticFeedback(
                        HapticFeedbackConstants.CONFIRM,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    )
                } else {
                    it.performHapticFeedback(
                        HapticFeedbackConstants.VIRTUAL_KEY,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    )
                }
                getEmptyRoom(
                    viewModel.firstWeekRoomSearch,
                    viewModel.endWeekRoomSearch,
                    viewModel.weekRoomSearch,
                    viewModel.classesCheckBoxSet
                )
            } else "请选择课程时间段".showShortToast()
        }

        binding.oneToTwo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.classesCheckBoxSet.add(2)
            else viewModel.classesCheckBoxSet.remove(2)
        }
        binding.threeToFour.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.classesCheckBoxSet.add(4)
            else viewModel.classesCheckBoxSet.remove(4)
        }
        binding.fiveToSix.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.classesCheckBoxSet.add(6)
            else viewModel.classesCheckBoxSet.remove(6)
        }
        binding.sevenToEight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.classesCheckBoxSet.add(8)
            else viewModel.classesCheckBoxSet.remove(8)
        }
        binding.nineToTen.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.classesCheckBoxSet.add(10)
            else viewModel.classesCheckBoxSet.remove(10)
        }
        binding.elevenToTwelve.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.classesCheckBoxSet.add(12)
            else viewModel.classesCheckBoxSet.remove(12)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemSelected(view: MaterialSpinner?, position: Int, id: Long, item: String) {

        when (view?.id) {
            R.id.first_week -> viewModel.firstWeekRoomSearch = position + 1
            R.id.end_week -> viewModel.endWeekRoomSearch = position + 1
            R.id.week -> viewModel.weekRoomSearch = weekMap[item]!!
        }

        binding.fab.isEnabled = (viewModel.firstWeekRoomSearch <= viewModel.endWeekRoomSearch)

        if (viewModel.firstWeekRoomSearch > viewModel.endWeekRoomSearch) {
            "首周大于末周，请重新选择".showShortToast()
        }
    }

    private fun getEmptyRoom(start: Int, end: Int, week: Int, timeSet: HashSet<Int>) {
        viewModel.getEmptyRoomLiveData(start, end, week, timeSet)
            .observe(viewLifecycleOwner) { result ->
                val string = result.getOrNull()
                if (string != null) {
                    if (string.isNotBlank()) {
                        val doc = Jsoup.parse(string)
                        val eachRoomsLine = doc.select("table[class=pTable]").select("tr")
                        viewModel.roomsList.clear()
                        for (eachRoomLine in eachRoomsLine) {
                            for (eachRoom in eachRoomLine.select("a")) {
                                viewModel.roomsList.add(eachRoom.text().trim())
                            }
                        }
                    } else {
                        viewModel.roomsList.clear()
                        "数据获取失败，请检查内网状态再试".showShortToast()
                    }
                    binding.emptyRoomViewPager.adapter = EmptyRoomViewPagerAdapter(
                        requireActivity(),
                        viewModel.roomsList
                    )
                    binding.emptyRoomViewPager.setCurrentItem(
                        viewModel.viewPagerCurrentPage,
                        false
                    )
                    binding.fab.isEnabled = true
                } else {
                    result.exceptionOrNull()?.printStackTrace()
                    "数据获取失败，请检查内网状态再试".showShortToast()
                    binding.fab.isEnabled = true
                }
            }
    }
}