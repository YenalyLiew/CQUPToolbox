package com.yenaly.cqupttoolbox.ui.fragment.sports.check_record

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.FragmentSportsCheckRecordBinding
import com.yenaly.cqupttoolbox.logic.network.Cookies
import com.yenaly.cqupttoolbox.ui.activity.MainActivity
import com.yenaly.cqupttoolbox.ui.fragment.sports.SportsRootFragment
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast
import kotlinx.coroutines.launch

/**
 * A fragment for sports check record.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/03 003 20:55
 * @Description : Description...
 */

class SportsCheckRecordFragment : SportsRootFragment() {

    private var _binding: FragmentSportsCheckRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportsCheckRecordBinding.inflate(inflater, container, false)

        binding.fab.setImageResource(R.drawable.ic_tothetop)
        if (Cookies.smartSportsCookiesList.isNotEmpty() && viewModel.yearTerm.isNotEmpty()) {
            binding.yearTerm.setItems(viewModel.yearTerm)
        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        binding.sportsRv.layoutManager = layoutManager
        val pagingAdapter = CheckRecordPagingAdapter(this)
        binding.sportsRv.adapter = pagingAdapter

        if (Cookies.smartSportsCookiesList.isEmpty()) {
            (requireActivity() as MainActivity).showLoadingDialog(
                loadingText = "正在帮你登录智慧体育，等会",
                dialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT
            )
            selfViewModel.loginSmartSports(viewModel.userCode!!, viewModel.userPassword!!, "")
        } else {
            if (viewModel.yearTerm.isEmpty()) {
                selfViewModel.getSportsYearTerm()
            } else {
                lifecycleScope.launch {
                    viewModel.getSportsCheckRecordPaging(
                        yearTerm = viewModel.sportsCheckRecordYearTerm
                    ) { pages ->
                        viewModel.sportsCheckRecordTotalPage = pages
                    }.collect { pagingData ->
                        pagingAdapter.submitData(pagingData)
                    }
                }
            }
        }

        binding.yearTerm.setOnItemSelectedListener { _, _, _, item ->
            viewModel.sportsCheckRecordCurrentPage = 1
            viewModel.sportsCheckRecordYearTerm = item as String
            lifecycleScope.launch {
                viewModel.getSportsCheckRecordPaging(
                    yearTerm = viewModel.sportsCheckRecordYearTerm
                ) { pages ->
                    viewModel.sportsCheckRecordTotalPage = pages
                }.collect { pagingData ->
                    pagingAdapter.submitData(pagingData)
                    layoutManager.scrollToPosition(0)
                }
            }
        }

        binding.fab.setOnClickListener {
            it.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            binding.sportsRv.smoothScrollToPosition(0)
        }

        binding.sportsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (binding.fab.isShown) binding.fab.hide()
                } else if (dy < 0) {
                    if (!binding.fab.isShown) binding.fab.show()
                } else binding.fab.show()
            }
        })

        super.loginSmartSports(snackBarView = binding.coordinatorLayout, fab = binding.fab)

        super.getSportsYearTerm(
            yearTermSpinner = binding.yearTerm,
            snackBarView = binding.coordinatorLayout
        ) {
            lifecycleScope.launch {
                viewModel.getSportsCheckRecordPaging(
                    yearTerm = viewModel.sportsCheckRecordYearTerm
                ) { pages ->
                    viewModel.sportsCheckRecordTotalPage = pages
                }.collect { pagingData ->
                    pagingAdapter.submitData(pagingData)
                }
            }
        }

        pagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                }
                is LoadState.Loading -> {
                }
                is LoadState.Error -> {
                    "呃呃呃...体育信息获取失败".showShortToast()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}