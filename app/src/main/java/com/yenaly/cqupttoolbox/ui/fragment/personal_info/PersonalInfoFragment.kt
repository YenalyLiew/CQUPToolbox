package com.yenaly.cqupttoolbox.ui.fragment.personal_info

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.FragmentPersonalInfoBinding
import com.yenaly.cqupttoolbox.ui.viewmodel.MainViewModel
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast

/**
 * A fragment for personal information.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/20 020 22:27
 * @Description : Description...
 */

class PersonalInfoFragment : Fragment() {

    private var _binding: FragmentPersonalInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)

        binding.fab.setImageResource(R.drawable.ic_refresh)
        binding.fab.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getWeekTime()

        viewModel.weekTimeLiveData.observe(viewLifecycleOwner) { result ->
            val weekTime = result.getOrNull()
            if (weekTime != null) {
                viewModel.weekTime = weekTime
                val weekText = "第 ${viewModel.weekTime!!.week} 周"
                val weekDayText = "星期${viewModel.weekTime!!.day}"
                binding.name.text = viewModel.currentUserName
                binding.termTextView.text = viewModel.weekTime!!.term
                binding.weekTextView.text = weekText
                binding.weekdayTextView.text = weekDayText
                if (binding.fab.isShown) binding.fab.hide()
            } else {
                val nothing = "..."
                binding.name.text = nothing
                binding.termTextView.text = nothing
                binding.weekTextView.text = nothing
                binding.weekdayTextView.text = nothing
                binding.fab.show()
                result.exceptionOrNull()?.printStackTrace()
                "数据获取失败，请检查网络再试".showShortToast()
            }
        }

        binding.fab.setOnClickListener {
            it.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            viewModel.getWeekTime()
            binding.fab.hide()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}