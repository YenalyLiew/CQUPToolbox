package com.yenaly.cqupttoolbox.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Utils for SnackBar.
 *
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/04/04 004 21:53
 * @Description : Description...
 */
object SnackBarUtils {
    /**
     * show short snack bar with action.
     */
    fun String.showShortActionSnackBar(view: View, text: String, action: (View) -> Unit) {
        Snackbar.make(view, this, Snackbar.LENGTH_SHORT)
            .setAction(text) { action(it) }
            .show()
    }

    /**
     * show long snack bar with action.
     */
    fun String.showLongActionSnackBar(view: View, text: String, action: (View) -> Unit) {
        Snackbar.make(view, this, Snackbar.LENGTH_LONG)
            .setAction(text) { action(it) }
            .show()
    }
}