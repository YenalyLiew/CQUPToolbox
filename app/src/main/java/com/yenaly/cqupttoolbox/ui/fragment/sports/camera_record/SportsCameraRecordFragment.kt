package com.yenaly.cqupttoolbox.ui.fragment.sports.camera_record

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
import com.bumptech.glide.Glide
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.FragmentSportsCameraRecordBinding
import com.yenaly.cqupttoolbox.logic.network.Cookies
import com.yenaly.cqupttoolbox.ui.activity.MainActivity
import com.yenaly.cqupttoolbox.ui.fragment.sports.SportsRootFragment
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast
import kotlinx.coroutines.launch

/**
 * A fragment for sports camera record.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/21 021 22:14
 * @Description : Description...
 */

class SportsCameraRecordFragment : SportsRootFragment() {

    private var _binding: FragmentSportsCameraRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSportsCameraRecordBinding.inflate(inflater, container, false)

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
        val pagingAdapter = CameraRecordPagingAdapter(this)
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
                    viewModel.getSportsCameraRecordPaging(
                        yearTerm = viewModel.sportsCameraRecordYearTerm
                    ) { pages ->
                        viewModel.sportsCameraRecordTotalPage = pages
                    }.collect { pagingData ->
                        pagingAdapter.submitData(pagingData)
                    }
                }
            }
        }

        binding.yearTerm.setOnItemSelectedListener { _, _, _, item ->
            viewModel.sportsCameraRecordCurrentPage = 1
            viewModel.sportsCameraRecordYearTerm = item as String
            lifecycleScope.launch {
                viewModel.getSportsCameraRecordPaging(
                    yearTerm = viewModel.sportsCameraRecordYearTerm
                ) { pages ->
                    viewModel.sportsCameraRecordTotalPage = pages
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

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(this@SportsCameraRecordFragment).resumeRequests()
                } else {
                    Glide.with(this@SportsCameraRecordFragment).pauseRequests()
                }
            }
        })

        super.loginSmartSports(snackBarView = binding.coordinatorLayout, fab = binding.fab)

        super.getSportsYearTerm(
            yearTermSpinner = binding.yearTerm,
            snackBarView = binding.coordinatorLayout
        ) {
            lifecycleScope.launch {
                viewModel.getSportsCameraRecordPaging(
                    yearTerm = viewModel.sportsCameraRecordYearTerm
                ) { pages ->
                    viewModel.sportsCameraRecordTotalPage = pages
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