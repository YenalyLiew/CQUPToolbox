package com.yenaly.cqupttoolbox.logic.network

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import ru.gildor.coroutines.okhttp.await
import java.text.SimpleDateFormat
import java.util.*

/**
 * A network singleton of WeCQUPT-related.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/28 028 20:04
 * @Description : Description...
 */
object WeNetwork {

    private const val WE_CQUPT_SCHEME =
        "https"

    private const val WE_CQUPT_BASE =
        "we.cqupt.edu.cn"
    private const val GET_WEEK_TIME =
        "api\\get_week_time.php"
    private const val PUNCH_IN_EVERY_DAY =
        "api\\mrdk\\post_mrdk_info.php"
    private const val GET_GO_OUT_INFO =
        "api\\lxsp_new\\get_lxsp_modules.php"
    private const val POST_GO_OUT_INFO =
        "api\\lxsp_new\\post_lxsp_spxx.php"

    private val r = listOf(
        "s9ZS",
        "jQkB",
        "RuQM",
        "O0_L",
        "Buxf",
        "LepV",
        "Ec6w",
        "zPLD",
        "eZry",
        "QjBF",
        "XPB0",
        "zlTr",
        "YDr2",
        "Mfdu",
        "HSoi",
        "frhT",
        "GOdB",
        "AEN0",
        "zX0T",
        "wJg1",
        "fCmn",
        "SM3z",
        "2U5I",
        "LI3u",
        "3rAY",
        "aoa4",
        "Jf9u",
        "M69T",
        "XCea",
        "63gc",
        "6_Kf"
    )

    private val u = listOf(
        "89KC",
        "pzTS",
        "wgte",
        "29_3",
        "GpdG",
        "FDYl",
        "vsE9",
        "SPJk",
        "_buC",
        "GPHN",
        "OKax",
        "Kk4",
        "hYxa",
        "1BC5",
        "oBk",
        "JgUW",
        "0CPR",
        "jlEh",
        "gBGg",
        "frS6",
        "4ads",
        "Iwfk",
        "TCgR",
        "wbjP"
    )

    suspend fun getWeekTime() = getWeekTimeCall().await()

    suspend fun punchInEveryDay(
        name: String,
        id: String,
        gender: String,
        openId: String,
        locationBig: String,
        locationSmall: String,
        latitude: Double,
        longitude: Double,
        currentLocation: String,
        currentDetailedLocation: String
    ) = punchInEveryDayCall(
        name,
        id,
        gender,
        openId,
        locationBig,
        locationSmall,
        latitude.toString(),
        longitude.toString(),
        currentLocation,
        currentDetailedLocation
    ).await()

    suspend fun getGoOutSchoolInfo(
        id: String,
        openId: String
    ) = getGoOutSchoolInfoCall(id, openId).await()

    suspend fun goOutSchool(
        name: String,
        id: String,
        college: String,
        openId: String,
        why: String,
        whichKind: String,
        where: String,
        getEndTime: (String) -> Unit
    ) = goOutSchoolCall(name, id, college, openId, why, whichKind, where, getEndTime).await()

    private fun getWeekTimeCall(): Call {
        val okHttpClient = OkHttpClient()
        val httpUrl = HttpUrl.Builder()
            .scheme(WE_CQUPT_SCHEME)
            .host(WE_CQUPT_BASE)
            .addPathSegments(GET_WEEK_TIME)
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .get()
            .build()
        return okHttpClient.newCall(request)
    }

    private fun punchInEveryDayCall(
        name: String,
        id: String,
        gender: String,
        openId: String,
        locationBig: String,
        locationSmall: String,
        latitude: String,
        longitude: String,
        currentLocation: String,
        currentDetailedLocation: String
    ): Call {
        val okHttpClient = OkHttpClient()
        val httpUrl = HttpUrl.Builder()
            .scheme(WE_CQUPT_SCHEME)
            .host(WE_CQUPT_BASE)
            .addPathSegments(PUNCH_IN_EVERY_DAY)
            .build()
        val timestamp = (System.currentTimeMillis() / 1000.0).toInt().toString()
        val date = Calendar.getInstance(TimeZone.getTimeZone("CTT"))
        val day = date.get(Calendar.DAY_OF_MONTH)
        val hour = date.get(Calendar.HOUR_OF_DAY)
        val key = r[day - 1] + u[hour]
        Log.d("punch_related", "day:$day, hour:$hour, key:$key")
        val formMap = mapOf(
            "name" to name,
            "xh" to id,
            "xb" to gender,
            "openid" to openId,
            "locationBig" to locationBig,
            "locationSmall" to locationSmall,
            "latitude" to latitude,
            "longitude" to longitude,
            "szdq" to currentLocation,
            "xxdz" to currentDetailedLocation,
            "ywjcqzbl" to "低风险",
            "ywjchblj" to "无",
            "xjzdywqzbl" to "无",
            "twsfzc" to "是",
            "ywytdzz" to "无",
            "jkmresult" to "绿色",
            "beizhu" to "无",
            "mrdkkey" to key,
            "timestamp" to timestamp
        )
        Log.d("punch_form_map", formMap.toString())
        val formJson = Gson().toJson(formMap)
        val byteFormJson = formJson.toByteArray()
        val encodedByteFormJson = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteFormJson)
        } else {
            String(android.util.Base64.encode(byteFormJson, android.util.Base64.DEFAULT))
        }
        val formBody = FormBody.Builder()
            .add("key", encodedByteFormJson)
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .post(formBody)
            .build()
        return okHttpClient.newCall(request)
    }

    private fun getGoOutSchoolInfoCall(
        id: String,
        openId: String
    ): Call {
        val okHttpClient = OkHttpClient()
        val httpUrl = HttpUrl.Builder()
            .scheme(WE_CQUPT_SCHEME)
            .host(WE_CQUPT_BASE)
            .addPathSegments(GET_GO_OUT_INFO)
            .build()
        val currentTime = System.currentTimeMillis()
        val timestamp = (currentTime / 1000.0).toInt().toString()
        val formMap = mapOf(
            "xh" to id,
            "openid" to openId,
            "timestamp" to timestamp
        )
        val formJson = Gson().toJson(formMap)
        val byteFormJson = formJson.toByteArray()
        val encodedByteFormJson = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteFormJson)
        } else {
            String(android.util.Base64.encode(byteFormJson, android.util.Base64.DEFAULT))
        }
        val formBody = FormBody.Builder()
            .add("key", encodedByteFormJson)
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .post(formBody)
            .build()
        return okHttpClient.newCall(request)
    }

    private fun goOutSchoolCall(
        name: String,
        id: String,
        college: String,
        openId: String,
        why: String,
        whichKind: String,
        where: String,
        getEndTime: (String) -> Unit
    ): Call {
        val okHttpClient = OkHttpClient()
        val httpUrl = HttpUrl.Builder()
            .scheme(WE_CQUPT_SCHEME)
            .host(WE_CQUPT_BASE)
            .addPathSegments(POST_GO_OUT_INFO)
            .build()
        val currentTime = System.currentTimeMillis()
        val timestamp = (currentTime / 1000.0).toInt().toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val startTime = dateFormat.format(Date(currentTime))
        val endHour = whichKind.filter { it.isDigit() || it == '.' }.toFloat()
        Log.d("endHour", endHour.toString())
        val endTime = dateFormat.format(Date(currentTime + (endHour * 60 * 60 * 1000).toLong()))
        getEndTime(endTime)
        val grade = if (id.first().isLetter()) id.substring(1, 5) else id.substring(0, 4)
        val formMap = mapOf(
            "xh" to id,
            "name" to name,
            "xy" to college,
            "nj" to grade,
            "openid" to openId,
            "wcmdd" to "重庆市,重庆市,南岸区",
            "qjsy" to why,
            "wcxxdd" to where,
            "sfly" to "请选择",
            "wcrq" to startTime,
            "qjlx" to whichKind,
            "yjfxsj" to endTime,
            "beizhu" to "",
            "timestamp" to timestamp
        )
        val formJson = Gson().toJson(formMap)
        val byteFormJson = formJson.toByteArray()
        val encodedByteFormJson = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteFormJson)
        } else {
            String(android.util.Base64.encode(byteFormJson, android.util.Base64.DEFAULT))
        }
        val formBody = FormBody.Builder()
            .add("key", encodedByteFormJson)
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .post(formBody)
            .build()
        return okHttpClient.newCall(request)
    }
}