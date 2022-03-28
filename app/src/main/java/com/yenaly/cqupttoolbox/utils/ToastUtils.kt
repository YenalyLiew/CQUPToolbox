package com.yenaly.cqupttoolbox.utils

import android.app.Activity
import android.widget.Toast
import com.yenaly.cqupttoolbox.CTApplication

/**
 * Utils for Toast.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/08 008 22:08
 * @Description : Descriptions...
 */

object ToastUtils {

    fun String.showShortToast() {
        Toast.makeText(CTApplication.context, this, Toast.LENGTH_SHORT).show()
    }

    fun String.showLongToast() {
        Toast.makeText(CTApplication.context, this, Toast.LENGTH_LONG).show()
    }

    fun String.showShortToastOnUiThread(activity: Activity) {
        if (!activity.isFinishing)
            activity.runOnUiThread {
                Toast.makeText(CTApplication.context, this, Toast.LENGTH_SHORT).show()
            }
    }

    fun String.showLongToastOnUiThread(activity: Activity) {
        if (!activity.isFinishing)
            activity.runOnUiThread {
                Toast.makeText(CTApplication.context, this, Toast.LENGTH_LONG).show()
            }
    }
}