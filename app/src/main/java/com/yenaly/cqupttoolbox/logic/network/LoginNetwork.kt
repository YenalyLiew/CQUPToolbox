package com.yenaly.cqupttoolbox.logic.network

import android.os.Build
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import ru.gildor.coroutines.okhttp.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * A network singleton of login-related.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/22 022 10:01
 * @Description : Description...
 */
object LoginNetwork {

    private const val CQUPT_LOGIN_SCHEME =
        "https"
    private const val WE_CQUPT_SCHEME =
        "https"

    private const val WE_CQUPT_BASE =
        "we.cqupt.edu.cn"
    private const val WE_CQUPT_LOGIN =
        "api\\users\\bind.php"

    private const val CQUPT_LOGIN_BASE =
        "ids.cqupt.edu.cn"
    private const val CQUPT_AUTHSERVER =
        "authserver"
    private const val CQUPT_LOGIN =
        "login"

    private const val SPORTS_REDIRECT_SITE =
        "http://202.202.43.198/CASLogin"

    suspend fun loginWeCqupt(
        openId: String,
        username: String,
        password: String
    ) = LoginNetwork.loginWeCquptCall(openId, username, password).await()

    suspend fun getExecutionValueToSmartSports() =
        getExecutionValueToSmartSportsCall().await()

    suspend fun postUsernameAndPasswordToSmartSports(
        username: String,
        password: String,
        captcha: String,
        execution: String
    ) = postUsernameAndPasswordToSmartSportsCall(username, password, captcha, execution).await()

    private fun loginWeCquptCall(
        openId: String,
        username: String,
        password: String
    ): Call {
        val okHttpClient = OkHttpClient()
        val httpUrl = HttpUrl.Builder()
            .scheme(WE_CQUPT_SCHEME)
            .host(WE_CQUPT_BASE)
            .addPathSegments(WE_CQUPT_LOGIN)
            .build()
        val timestamp = (System.currentTimeMillis() / 1000.0).toInt().toString()
        val formMap = mapOf(
            "openid" to openId,
            "yktid" to username,
            "passwd" to password,
            "timestamp" to timestamp
        )
        Log.d("we_login_form_map", formMap.toString())
        val formJson = Gson().toJson(formMap)
        val byteFormJson = formJson.toByteArray()
        val encodedByteFormJson = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byteFormJson)
        } else {
            String(android.util.Base64.encode(byteFormJson, android.util.Base64.DEFAULT))
        }
        Log.d("encodedByteFormJson", encodedByteFormJson)
        val formBody = FormBody.Builder()
            .add("key", encodedByteFormJson)
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .post(formBody)
            .build()
        return okHttpClient.newCall(request)
    }

    private fun getExecutionValue(redirectSite: String, cookieList: ArrayList<Cookie>): Call {
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                private val cookieStore = HashMap<String, List<Cookie>>()
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore[url.host] = cookies
                    cookieList.clear()
                    cookieList.addAll(cookies)
                    Log.d("CookieJarSave_exe", cookieList.toString())
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies = cookieStore[url.host]
                    return cookies ?: cookieList
                }
            })
            .build()
        val httpUrl = HttpUrl.Builder()
            .scheme(CQUPT_LOGIN_SCHEME)
            .host(CQUPT_LOGIN_BASE)
            .addPathSegment(CQUPT_AUTHSERVER)
            .addPathSegment(CQUPT_LOGIN)
            .addQueryParameter("service", redirectSite)
            .build()
        val request = Request.Builder()
            .url(httpUrl)
            .get()
            .build()
        Log.d("get_execution_value", "$redirectSite has been executed.")
        return okHttpClient.newCall(request)
    }

    private fun getExecutionValueToSmartSportsCall() =
        getExecutionValue(SPORTS_REDIRECT_SITE, Cookies.smartSportsCookiesList)

    private fun postUsernameAndPassword(
        username: String,
        password: String,
        captcha: String,
        execution: String,
        cookieList: ArrayList<Cookie>
    ): Call {
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                private val cookieStore = HashMap<String, List<Cookie>>()
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    cookieStore[url.host] = cookies
                    cookieList.clear()
                    cookieList.addAll(cookies)
                    Log.d("CookieJarSave_login", cookieList.toString())
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    Log.d("CookieBeforePostURL", url.host)
                    Log.d("CookieBeforePost", cookieStore[url.host].toString())
                    return cookieStore[url.host] ?: cookieList
                }
            })
            .build()

        val httpUrl = HttpUrl.Builder()
            .scheme(CQUPT_LOGIN_SCHEME)
            .host(CQUPT_LOGIN_BASE)
            .addPathSegment(CQUPT_AUTHSERVER)
            .addPathSegment(CQUPT_LOGIN)
            .build()

        val headers = Headers.Builder()
            .add(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36"
            )
            .build()

        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .add("captcha", captcha)
            .add("_eventId", "submit")
            .add("cllt", "userNameLogin")
            .add("dllt", "generalLogin")
            .add("lt", "")
            .add("execution", execution)
            .build()

        val request = Request.Builder()
            .url(httpUrl)
            .headers(headers)
            .post(formBody)
            .build()

        return okHttpClient.newCall(request)
    }

    private fun postUsernameAndPasswordToSmartSportsCall(
        username: String,
        password: String,
        captcha: String,
        execution: String
    ) = postUsernameAndPassword(
        username,
        password,
        captcha,
        execution,
        Cookies.smartSportsCookiesList
    )
}