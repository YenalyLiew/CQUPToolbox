package com.yenaly.cqupttoolbox.ui.fragment.sports.student_resume

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.FragmentStudentSportsResumeBinding
import com.yenaly.cqupttoolbox.logic.model.StudentSportsResumeMobileModel
import com.yenaly.cqupttoolbox.logic.network.Cookies
import com.yenaly.cqupttoolbox.ui.activity.MainActivity
import com.yenaly.cqupttoolbox.ui.fragment.sports.SportsRootFragment
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast

/**
 * A fragment for students' sports resume.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/05 005 15:34
 * @Description : Description...
 */

class StudentSportsResumeFragment : SportsRootFragment() {

    private var _binding: FragmentStudentSportsResumeBinding? = null
    private val binding get() = _binding!!

    private val validMap = mapOf("Y" to "有", "N" to "待定/无")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentSportsResumeBinding.inflate(inflater, container, false)

        binding.fab.setImageResource(R.drawable.ic_refresh)
        binding.scrollView.visibility = View.GONE
        if (Cookies.smartSportsCookiesList.isNotEmpty() && viewModel.yearTerm.isNotEmpty()) {
            binding.yearTerm.setItems(viewModel.yearTerm)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                viewModel.getStudentSportsResume(viewModel.studentSportsResumeYearTerm)
            }
        }

        binding.yearTerm.setOnItemSelectedListener { _, _, _, item ->
            viewModel.studentSportsResumeYearTerm = item as String
            viewModel.getStudentSportsResume(item)
        }

        binding.fab.setOnClickListener {
            it.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            if (viewModel.yearTerm.isEmpty()) {
                (requireActivity() as MainActivity).showLoadingDialog(
                    loadingText = "正在帮你登录智慧体育，等会",
                    dialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT
                )
                selfViewModel.loginSmartSports(viewModel.userCode!!, viewModel.userPassword!!, "")
            } else {
                viewModel.getStudentSportsResume(viewModel.studentSportsResumeYearTerm)
                if (viewModel.yearTerm.isNotEmpty()) {
                    binding.yearTerm.setItems(viewModel.yearTerm)
                } else selfViewModel.getSportsYearTerm()
            }
            binding.fab.isEnabled = false
        }

        super.loginSmartSports(snackBarView = binding.coordinatorLayout, fab = binding.fab)

        super.getSportsYearTerm(
            yearTermSpinner = binding.yearTerm,
            snackBarView = binding.coordinatorLayout
        ) {
            viewModel.getStudentSportsResume(viewModel.studentSportsResumeYearTerm)
        }

        viewModel.studentSportsResumeLiveData.observe(viewLifecycleOwner) { result ->
            val resume = result.getOrNull()
            if (resume != null) {
                binding.scrollView.visibility = View.VISIBLE
                loadSportsResume(resume)
            } else {
                result.exceptionOrNull()?.printStackTrace()
                binding.scrollView.visibility = View.GONE
                "呃呃呃...体育信息获取失败".showShortToast()
            }
            binding.fab.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @Suppress("DEPRECATION")
    private fun loadSportsResume(detail: StudentSportsResumeMobileModel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.termShouldSport.text = Html.fromHtml(
                getString(R.string.sports_total, detail.termShouldSportCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.termShouldRun.text = Html.fromHtml(
                getString(R.string.sports_run, detail.termShouldRunCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.termShouldOther.text = Html.fromHtml(
                getString(R.string.sports_other, detail.termShouldOtherCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.currentShouldSport.text = Html.fromHtml(
                getString(R.string.sports_total, detail.sportCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.currentShouldRun.text = Html.fromHtml(
                getString(R.string.include_in_exam, detail.examSportCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.currentExtra.text = Html.fromHtml(
                getString(R.string.include_in_other, detail.extSportCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.examRun.text = Html.fromHtml(
                getString(R.string.sports_run, detail.runCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.examRunTime.text = Html.fromHtml(
                getString(R.string.sports_run_time, detail.runDura),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.examRunMile.text = Html.fromHtml(
                getString(R.string.sports_run_mile, detail.runMile),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.examOther.text = Html.fromHtml(
                getString(R.string.sports_other, detail.otherCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.examOtherTime.text = Html.fromHtml(
                getString(R.string.sports_other_time, detail.otherDura),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.ifAbleExam.text = Html.fromHtml(
                getString(R.string.is_exam_valid, validMap[detail.isExam]),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.extraRun.text = Html.fromHtml(
                getString(R.string.sports_run, detail.extRunCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.extraRunTime.text = Html.fromHtml(
                getString(R.string.sports_run_time, detail.extRunDura),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.extraRunMile.text = Html.fromHtml(
                getString(R.string.sports_run_mile, detail.extRunMile),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.extraOther.text = Html.fromHtml(
                getString(R.string.sports_other, detail.extOtherCount),
                Html.FROM_HTML_MODE_LEGACY
            )
            binding.extraOtherTime.text = Html.fromHtml(
                getString(R.string.sports_other_time, detail.extOtherDura),
                Html.FROM_HTML_MODE_LEGACY
            )
        } else {
            binding.termShouldSport.text = Html.fromHtml(
                getString(R.string.sports_total, detail.termShouldSportCount)
            )
            binding.termShouldRun.text = Html.fromHtml(
                getString(R.string.sports_run, detail.termShouldRunCount)
            )
            binding.termShouldOther.text = Html.fromHtml(
                getString(R.string.sports_other, detail.termShouldOtherCount)
            )
            binding.currentShouldSport.text = Html.fromHtml(
                getString(R.string.sports_total, detail.sportCount)
            )
            binding.currentShouldRun.text = Html.fromHtml(
                getString(R.string.include_in_exam, detail.examSportCount)
            )
            binding.currentExtra.text = Html.fromHtml(
                getString(R.string.include_in_other, detail.extSportCount)
            )
            binding.examRun.text = Html.fromHtml(
                getString(R.string.sports_run, detail.runCount)
            )
            binding.examRunTime.text = Html.fromHtml(
                getString(R.string.sports_run_time, detail.runDura)
            )
            binding.examRunMile.text = Html.fromHtml(
                getString(R.string.sports_run_mile, detail.runMile)
            )
            binding.examOther.text = Html.fromHtml(
                getString(R.string.sports_other, detail.otherCount)
            )
            binding.examOtherTime.text = Html.fromHtml(
                getString(R.string.sports_other_time, detail.otherDura)
            )
            binding.ifAbleExam.text = Html.fromHtml(
                getString(R.string.is_exam_valid, detail.isExam)
            )
            binding.extraRun.text = Html.fromHtml(
                getString(R.string.sports_run, detail.extRunCount)
            )
            binding.extraRunTime.text = Html.fromHtml(
                getString(R.string.sports_run_time, detail.extRunDura)
            )
            binding.extraRunMile.text = Html.fromHtml(
                getString(R.string.sports_run_mile, detail.extRunMile)
            )
            binding.extraOther.text = Html.fromHtml(
                getString(R.string.sports_other, detail.extOtherCount)
            )
            binding.extraOtherTime.text = Html.fromHtml(
                getString(R.string.sports_other_time, detail.extOtherDura)
            )
        }
    }
}