package com.yenaly.cqupttoolbox.ui.activity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.utils.DisplayUtils.dp

/**
 * The root activity.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/20 020 20:02
 * @Description : Description...
 */
abstract class RootActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog

    fun showLoadingDialog(
        loadingText: String = "正在加载中",
        cancelable: Boolean = false,
        dialogWidth: Int = 260.dp,
        dialogHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    ) {
        val loadingDialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        loadingDialogView.findViewById<TextView>(R.id.loading_text).text = loadingText
        loadingDialog = MaterialAlertDialogBuilder(this)
            .setCancelable(cancelable)
            .setView(loadingDialogView)
            .create()
        loadingDialog.show()
        Log.d("dialogWidthPx", dialogWidth.toString())
        Log.d("dialogHeightPx", dialogHeight.toString())
        loadingDialog.window?.setLayout(dialogWidth, dialogHeight)

    }

    fun hideLoadingDialog() {
        loadingDialog.hide()
    }

    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::loadingDialog.isInitialized) {
            loadingDialog.dismiss()
        }
    }
}