package com.yenaly.cqupttoolbox.logic.network

import com.yenaly.cqupttoolbox.logic.dao.LoginDao
import okhttp3.Cookie

/**
 * ALl cookies are stored here.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/22 022 10:10
 * @Description : Description...
 */
object Cookies {

    val jwzxCookiesList: ArrayList<Cookie> = ArrayList()

    val smartSportsCookiesList: ArrayList<Cookie> = LoginDao.getSmartSportsCookies() ?: ArrayList()

}