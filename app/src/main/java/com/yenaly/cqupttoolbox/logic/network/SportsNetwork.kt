package com.yenaly.cqupttoolbox.logic.network

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.gildor.coroutines.okhttp.await
import java.util.concurrent.TimeUnit

/**
 * A network singleton of sports-related.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/22 022 10:02
 * @Description : Description...
 */
object SportsNetwork {

    private const val TIME_OUT_SECOND = 5L

    // http
    const val SMART_SPORTS_SCHEME =
        "http://"
    const val SMART_SPORTS_BASE =
        "202.202.43.198"
    private const val SPORTS_CHECK_RECORD =
        "face\\SportsCheckRecord"
    private const val CAMERA_RECORD =
        "face\\CameraRecord"
    private const val SPORTS_STUDENT_RESUME =
        "sunSportReport\\StudentResume"
    private const val CAMERA_RECORD_LIST =
        "face\\CameraRecord\\list"
    private const val SPORTS_CHECK_RECORD_LIST =
        "face\\SportsCheckRecord\\list"
    private const val SPORTS_STUDENT_RESUME_MOBILE_LIST =
        "sunSportReport\\StudentResume\\listByStudentForMobile"

    @Deprecated("This could not be used. I do not know why.")
    private const val SPORTS_STUDENT_RESUME_LIST =
        "sunSportReport\\StudentResume\\list"

    private val okHttpClient =
        OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    Log.d("Cookie_load_req_sports", Cookies.smartSportsCookiesList.toString())
                    return Cookies.smartSportsCookiesList
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                }
            })
            .connectTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
            .build()

    fun cancelTag(tag: Any?) {
        if (tag == null) return
        for (call in okHttpClient.dispatcher.queuedCalls()) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
        for (call in okHttpClient.dispatcher.runningCalls()) {
            if (tag == call.request().tag()) {
                call.cancel()
            }
        }
    }

    suspend fun getSportsYearTerm() = getSportsYearTermCall().await()

    suspend fun getSportsCameraRecord(yearTerm: String, page: Int) =
        getSportsCameraRecordCall(yearTerm, page).await()

    suspend fun getSportsCheckRecord(yearTerm: String, page: Int) =
        getSportsCheckRecordCall(yearTerm, page).await()

    suspend fun getStudentSportsResume(yearTerm: String) =
        getStudentSportsResumeCall(yearTerm).await()

    private fun getSportsYearTermCall(): Call {
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(SMART_SPORTS_BASE)
            .addPathSegments(SPORTS_STUDENT_RESUME)
            .build()
        val headers = Headers.Builder()
            .add(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36"
            )
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .headers(headers)
            .get()
            .tag("get_sports_year_term")
            .build()
        return okHttpClient.newCall(request)
    }

    private fun getSportsCameraRecordCall(yearTerm: String, page: Int): Call {
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(SMART_SPORTS_BASE)
            .addPathSegments(CAMERA_RECORD_LIST)
            .build()
        val headers = Headers.Builder()
            .add(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36"
            )
            .build()
        val formBody = FormBody.Builder()
            .add("yearTerm", yearTerm)
            .add("cameraChannelName", "")
            .add("placeCode", "")
            .add("admClass", "")
            .add("personName", "")
            .add("personId", "")
            .add("studentNo", "")
            .add("cameraFunctionType", "")
            .add("params[beginRecordTime]", "2015-01-01")
            .add("params[endRecordTime]", "2055-01-01")
            .add("pageSize", "10")
            .add("pageNum", page.toString())
            .add("orderByColumn", "recordTime")
            .add("isAsc", "desc")
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .headers(headers)
            .post(formBody)
            .tag("get_sports_camera_record_call")
            .build()
        return okHttpClient.newCall(request)
    }

    private fun getSportsCheckRecordCall(yearTerm: String, page: Int): Call {
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(SMART_SPORTS_BASE)
            .addPathSegments(SPORTS_CHECK_RECORD_LIST)
            .build()
        val headers = Headers.Builder()
            .add(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36"
            )
            .build()
        val formBody = FormBody.Builder()
            .add("yearTerm", yearTerm)
            .add("sportItem", "")
            .add("sportFieldCode", "")
            .add("isValid", "")
            .add("examIsValid", "")
            .add("extIsValid", "")
            .add("appealStatus", "")
            .add("appealAtt", "")
            .add("params[SportAppealCountCnd]", "")
            .add("params[SportAppealCount]", "")
            .add("params[sportDate]", "")
            .add("weekly", "")
            .add("pageSize", "20")
            .add("pageNum", page.toString())
            .add("orderByColumn", "sportDate")
            .add("isAsc", "desc")
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .headers(headers)
            .post(formBody)
            .tag("get_sports_check_record_call")
            .build()
        return okHttpClient.newCall(request)
    }

    private fun getStudentSportsResumeCall(yearTerm: String): Call {
        val httpUrl = HttpUrl.Builder()
            .scheme("http")
            .host(SMART_SPORTS_BASE)
            .addPathSegments(SPORTS_STUDENT_RESUME_MOBILE_LIST)
            .build()
        val headers = Headers.Builder()
            .add(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Mobile Safari/537.36"
            )
            .build()
        val requestsMap = "{\"yearTerm\":\"$yearTerm\"}"
        val requestBody = requestsMap.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(httpUrl)
            .headers(headers)
            .post(requestBody)
            .tag("get_student_sports_resume_call")
            .build()
        return okHttpClient.newCall(request)
    }
}