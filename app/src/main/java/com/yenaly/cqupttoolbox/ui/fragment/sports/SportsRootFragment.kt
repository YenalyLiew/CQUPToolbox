package com.yenaly.cqupttoolbox.ui.fragment.sports

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jaredrummler.materialspinner.MaterialSpinner
import com.yenaly.cqupttoolbox.ui.activity.MainActivity
import com.yenaly.cqupttoolbox.ui.viewmodel.MainViewModel
import com.yenaly.cqupttoolbox.ui.viewmodel.SportsSingleViewModel
import com.yenaly.cqupttoolbox.utils.SnackBarUtils.showShortActionSnackBar
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/04/05 005 19:45
 * @Description : Description...
 */
abstract class SportsRootFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()
    val selfViewModel = SportsSingleViewModel()  // Deliberately

    fun loginSmartSports(snackBarView: View, fab: FloatingActionButton) {
        selfViewModel.loginSmartSportsLiveData.observe(viewLifecycleOwner) { result ->
            val isSuccess = result.getOrNull()
            if (isSuccess != null) {
                if (isSuccess) {
                    if (viewModel.yearTerm.isEmpty()) {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        selfViewModel.getSportsYearTerm()
                    }
                }
            } else {
                result.exceptionOrNull()?.printStackTrace()
                (requireActivity() as MainActivity).hideLoadingDialog()
                val errorMessage = result.exceptionOrNull()?.message
                errorMessage?.let {
                    when {
                        "系统炸了" in it -> {
                            it.showShortActionSnackBar(snackBarView, "强制加载！") {
                                selfViewModel.getSportsYearTerm()
                            }
                        }
                        else -> {
                            it.showShortActionSnackBar(snackBarView, "重试") {
                                (requireActivity() as MainActivity).showLoadingDialog(
                                    loadingText = "正在帮你登录智慧体育，等会",
                                    dialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                selfViewModel.loginSmartSports(
                                    viewModel.userCode!!,
                                    viewModel.userPassword!!,
                                    ""
                                )
                            }
                        }
                    }
                }
            }
            fab.isEnabled = true
        }
    }

    fun getSportsYearTerm(
        yearTermSpinner: MaterialSpinner,
        snackBarView: View,
        nextAction: () -> Unit
    ) {
        selfViewModel.getSportsYearTermLiveData.observe(viewLifecycleOwner) { result ->
            val yearTerms = result.getOrNull()
            if (yearTerms != null) {
                Log.d("get_sports_year_term", yearTerms.toString())
                if (viewModel.yearTerm.isEmpty()) {
                    for (yearTerm in yearTerms) {
                        viewModel.yearTerm.add(yearTerm.text())
                    }
                    viewModel.studentSportsResumeYearTerm = viewModel.yearTerm[0]
                    nextAction()
                }
                yearTermSpinner.setItems(viewModel.yearTerm)
                Log.d("yearTerm", viewModel.yearTerm.toString())
            } else {
                result.exceptionOrNull()?.printStackTrace()
                val errorMessage = result.exceptionOrNull()?.message
                errorMessage?.let {
                    when {
                        "已经失效" in it -> {
                            it.showShortActionSnackBar(snackBarView, "重新登录") {
                                selfViewModel.loginSmartSports(
                                    viewModel.userCode!!,
                                    viewModel.userPassword!!,
                                    ""
                                )
                            }
                        }
                        else -> {
                            "获取学期列表失败了捏，稍后再试一下吧".showShortToast()
                        }
                    }
                }
            }
        }
    }
}