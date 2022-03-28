package com.yenaly.cqupttoolbox.utils

import com.yenaly.cqupttoolbox.CTApplication

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/28 028 10:24
 * @Description : Description...
 */
object DisplayUtils {
    /**
     * convert dip to px.
     *
     * @param dpValue dip value.
     * @return px value.
     */
    fun dip2px(dpValue: Float): Int {
        val scale = CTApplication.context.resources.displayMetrics.density
        return (dpValue * scale + 0.5F).toInt()
    }

    val Float.dp: Int
        get() = dip2px(this)

    val Int.dp: Int
        get() = dip2px(this.toFloat())

    /**
     * convert px to dip.
     *
     * @param pxValue px value.
     * @return dip value.
     */
    fun px2dip(pxValue: Float): Int {
        val scale = CTApplication.context.resources.displayMetrics.density
        return (pxValue / scale + 0.5F).toInt()
    }
}