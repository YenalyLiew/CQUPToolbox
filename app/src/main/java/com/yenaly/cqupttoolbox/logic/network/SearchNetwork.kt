package com.yenaly.cqupttoolbox.logic.network

import android.util.Log
import okhttp3.*
import ru.gildor.coroutines.okhttp.await
import java.util.concurrent.TimeUnit

/**
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/25 025 20:33
 * @Description : Description...
 */
object SearchNetwork {

    private const val TIME_OUT_SECOND = 3L

    // http
    private const val CQUPT_SITE_BASE =
        "jwzx.cqupt.edu.cn"
    private const val EMPTY_ROOM_SEARCH =
        "kebiao\\emptyRoomSearch.php"

    private val okHttpClient =
        OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    Log.d("Cookie_load_req_dox", Cookies.jwzxCookiesList.toString())
                    return Cookies.jwzxCookiesList
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                }
            })
            .connectTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
            .build()

    suspend fun getEmptyRoom(
        start: Int,
        end: Int,
        week: Int,
        timeSet: HashSet<Int>
    ) = getEmptyRoomCall(start, end, week, timeSet).await()

    private fun getEmptyRoomCall(
        start: Int,
        end: Int,
        week: Int,
        timeSet: HashSet<Int>
    ): Call {
        val httpUrl = HttpUrl.Builder().run {
            scheme("http")
            host(CQUPT_SITE_BASE)
            addPathSegments(EMPTY_ROOM_SEARCH)
            addQueryParameter("zcStart", start.toString())
            addQueryParameter("zcEnd", end.toString())
            addQueryParameter("xq", week.toString())
            for (time in timeSet)
                addQueryParameter("sd[]", time.toString())
            build()
        }

        val request = Request.Builder()
            .url(httpUrl)
            .get()
            .tag("get_empty_room")
            .build()

        return okHttpClient.newCall(request)
    }
}