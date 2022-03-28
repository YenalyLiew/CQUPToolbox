package com.yenaly.cqupttoolbox

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/25 025 20:10
 * @Description : Description...
 */
class CTApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}