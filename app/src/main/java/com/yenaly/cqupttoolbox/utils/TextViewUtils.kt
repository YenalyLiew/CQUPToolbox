package com.yenaly.cqupttoolbox.utils

import android.text.TextUtils
import android.widget.TextView

/**
 * Utils for TextView.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/08 008 21:20
 * @Description : Description...
 */

object TextViewUtils {

    /**
     * make TextView's text marquee.
     *
     * @param textView TextView.
     * @param marqueeRepeatLimit marquee repeat times. Default value is -1 (unlimited repeat).
     */
    fun makeTextMarquee(textView: TextView, marqueeRepeatLimit: Int = -1) {
        textView.isFocusable = true
        textView.isFocusableInTouchMode = true
        textView.isSelected = true
        textView.isSingleLine = true
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.marqueeRepeatLimit = marqueeRepeatLimit
    }

}