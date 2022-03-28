package com.yenaly.cqupttoolbox.ui.fragment.sports.check_record

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.FragmentSportsCheckRecordBinding
import com.yenaly.cqupttoolbox.logic.network.Cookies
import com.yenaly.cqupttoolbox.ui.activity.MainActivity
import com.yenaly.cqupttoolbox.ui.viewmodel.MainViewModel
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast
import org.jsoup.Jsoup
import kotlin.math.ceil

/**
 * A fragment for sports check record.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/03 003 20:55
 * @Description : Description...
 */

class SportsCheckRecordFragment : Fragment() {

    private var _binding: FragmentSportsCheckRecordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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
        binding.totalPage.text = viewModel.sportsCheckRecordTotalPage.toString()
        binding.whichPage.setText(viewModel.sportsCheckRecordCurrentPage.toString())
        pageSelectButtonEnabled(viewModel.sportsCheckRecordCurrentPage)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.yearTerm.isEmpty()) {
            (requireActivity() as MainActivity).showLoadingDialog(
                loadingText = "正在帮你登录智慧体育，等会",
                dialogWidth = ViewGroup.LayoutParams.WRAP_CONTENT
            )
            viewModel.loginSmartSports(viewModel.userCode!!, viewModel.userPassword!!, "")
        } else {
            viewModel.getSportsCheckRecord(
                viewModel.sportsCheckRecordYearTerm,
                viewModel.sportsCheckRecordCurrentPage
            )
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.sportsRv.layoutManager = layoutManager
        val adapter = SportsCheckRecordAdapter(this, viewModel.sportsCheckRecordList)
        binding.sportsRv.adapter = adapter

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.whichPage.setOnEditorActionListener { v, _, _ ->
            if (v.text.toString().isNotBlank()) {
                if (v.text.toString().toInt() <= viewModel.sportsCheckRecordTotalPage &&
                    v.text.toString().toInt() != 0
                ) {
                    pageSelectButtonEnabled(v.text.toString().toInt())
                    imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    binding.whichPage.clearFocus()
                    viewModel.sportsCheckRecordCurrentPage = v.text.toString().toInt()
                    viewModel.getSportsCheckRecord(
                        viewModel.sportsCheckRecordYearTerm,
                        viewModel.sportsCheckRecordCurrentPage
                    )
                } else {
                    "呃呃呃...没那么多页".showShortToast()
                }
            }
            true
        }

        binding.previousPage.setOnClickListener {
            if (
                viewModel.sportsCheckRecordCurrentPage > 0 &&
                viewModel.sportsCheckRecordCurrentPage <= viewModel.sportsCheckRecordTotalPage
            ) {
                viewModel.sportsCheckRecordCurrentPage -= 1
                pageSelectButtonEnabled(viewModel.sportsCheckRecordCurrentPage)
                binding.whichPage.setText(viewModel.sportsCheckRecordCurrentPage.toString())
                viewModel.getSportsCheckRecord(
                    viewModel.sportsCheckRecordYearTerm,
                    viewModel.sportsCheckRecordCurrentPage
                )
            }
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            binding.whichPage.clearFocus()
        }

        binding.nextPage.setOnClickListener {
            if (
                viewModel.sportsCheckRecordCurrentPage > 0 &&
                viewModel.sportsCheckRecordCurrentPage <= viewModel.sportsCheckRecordTotalPage
            ) {
                viewModel.sportsCheckRecordCurrentPage += 1
                pageSelectButtonEnabled(viewModel.sportsCheckRecordCurrentPage)
                binding.whichPage.setText(viewModel.sportsCheckRecordCurrentPage.toString())
                viewModel.getSportsCheckRecord(
                    viewModel.sportsCheckRecordYearTerm,
                    viewModel.sportsCheckRecordCurrentPage
                )
            }
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            binding.whichPage.clearFocus()
        }

        binding.yearTerm.setOnItemSelectedListener { _, _, _, item ->
            viewModel.sportsCheckRecordCurrentPage = 1
            binding.whichPage.setText(viewModel.sportsCheckRecordCurrentPage.toString())
            viewModel.sportsCheckRecordYearTerm = item as String
            viewModel.getSportsCheckRecord(item, viewModel.sportsCheckRecordCurrentPage)
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

        viewModel.loginSmartSportsLiveData.observe(viewLifecycleOwner) { result ->
            val isSuccess = result.getOrNull()
            if (isSuccess != null) {
                if (isSuccess) {
                    if (viewModel.yearTerm.isEmpty()) {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        viewModel.getSportsYearTerm()
                    }
                }
            } else {
                result.exceptionOrNull()?.printStackTrace()
                (requireActivity() as MainActivity).hideLoadingDialog()
                result.exceptionOrNull()?.message?.showShortToast()
            }
        }

        viewModel.getSportsYearTermLiveData.observe(viewLifecycleOwner) { result ->
            val string = result.getOrNull()
            if (string != null) {
                Log.d("get_sports_year_term", string)
                val doc = Jsoup.parse(string)
                val yearTerms = doc.select("select[id=yearTerm]").first()?.select("option")
                if (yearTerms != null && viewModel.yearTerm.isEmpty()) {
                    for (yearTerm in yearTerms) {
                        viewModel.yearTerm.add(yearTerm.text())
                    }
                    viewModel.sportsCheckRecordYearTerm = viewModel.yearTerm[0]
                }
                binding.yearTerm.setItems(viewModel.yearTerm)
                viewModel.getSportsCheckRecord(
                    viewModel.sportsCheckRecordYearTerm,
                    viewModel.sportsCheckRecordCurrentPage
                )
                Log.d("yearTerm", viewModel.yearTerm.toString())
            } else {
                result.exceptionOrNull()?.printStackTrace()
                "获取学期列表失败了捏，稍后再试一下吧 (getSportsYearTerm)".showShortToast()
            }
        }

        viewModel.sportsCheckRecordLiveData.observe(viewLifecycleOwner) { result ->
            val sportsRecord = result.getOrNull()
            if (sportsRecord != null) {
                viewModel.sportsCheckRecordList.clear()
                viewModel.sportsCheckRecordList.addAll(sportsRecord.rows)
                if (viewModel.sportsCheckRecordTotalPage != ceil(sportsRecord.total / 20.0).toInt()) {
                    viewModel.sportsCheckRecordTotalPage = ceil(sportsRecord.total / 20.0).toInt()
                    binding.totalPage.text = viewModel.sportsCheckRecordTotalPage.toString()
                }
                pageSelectButtonEnabled(viewModel.sportsCheckRecordCurrentPage)
                adapter.notifyDataSetChanged()
                binding.sportsRv.smoothScrollToPosition(0)
            } else {
                result.exceptionOrNull()?.printStackTrace()
                "呃呃呃...体育信息获取失败".showShortToast()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun pageSelectButtonEnabled(page: Int) {
        when (page) {
            0 -> {
                binding.previousPage.isEnabled = false
                binding.nextPage.isEnabled = false
            }
            1 -> {
                if (viewModel.sportsCheckRecordTotalPage == 1) {
                    binding.previousPage.isEnabled = false
                    binding.nextPage.isEnabled = false
                } else {
                    binding.previousPage.isEnabled = false
                    binding.nextPage.isEnabled = true
                }
            }
            viewModel.sportsCheckRecordTotalPage -> {
                binding.previousPage.isEnabled = true
                binding.nextPage.isEnabled = false
            }
            else -> {
                binding.previousPage.isEnabled = true
                binding.nextPage.isEnabled = true
            }
        }
    }
}