package com.yenaly.cqupttoolbox.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.yenaly.cqupttoolbox.logic.dao.LoginDao
import com.yenaly.cqupttoolbox.logic.model.*
import com.yenaly.cqupttoolbox.logic.network.*
import kotlinx.coroutines.Dispatchers
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException

/**
 * A repository of logic layer.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/22 022 11:36
 * @Description : Description...
 */
class Repository {

    // LoginNetwork

    fun loginViaWeCqupt(
        openId: String,
        username: String,
        password: String
    ) = liveData(Dispatchers.IO) {
        val result = try {
            val loginWeCqupt = LoginNetwork.loginWeCqupt(openId, username, password)
            val loginString = loginWeCqupt.body!!.string()
            Log.d("login_we_string", loginString)
            val loginInformation = Gson().fromJson(loginString, WeLoginResponseModel::class.java)
            if (loginInformation.message == "ok") {
                Result.success(loginInformation.data)
            } else {
                Result.failure(RuntimeException("密码有误，重新输入试试"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    // SearchNetwork

    fun getEmptyRoom(
        start: Int,
        end: Int,
        week: Int,
        timeSet: HashSet<Int>
    ) = liveData(Dispatchers.IO) {
        val result = try {
            val response = SearchNetwork.getEmptyRoom(start, end, week, timeSet)
            val string = response.body!!.string()
            Result.success(string)
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    // SportsNetwork

    fun loginSmartSports(
        username: String,
        password: String,
        captcha: String
    ) = liveData(Dispatchers.IO) {
        val result = try {
            val getExecutionValue = LoginNetwork.getExecutionValueToSmartSports()
            val docExecutionValue = Jsoup.parse(getExecutionValue.body!!.string())
            val executionValue = docExecutionValue.getElementById("execution")?.attr("value")
            if (executionValue != null) {
                val postIdAndPwd = LoginNetwork.postUsernameAndPasswordToSmartSports(
                    username,
                    password,
                    captcha,
                    executionValue
                )
                val docPost = Jsoup.parse(postIdAndPwd.body!!.string())
                val statement = docPost.getElementById("yearTerm")
                Log.d("doc_login", docPost.toString())
                if (statement != null) {
                    LoginDao.saveSmartSportsCookies(Cookies.smartSportsCookiesList)
                    Result.success(true)
                } else {
                    if (docPost.getElementById("showErrorTip") == null) {
                        Result.failure(RuntimeException("密码可能不正确，未知错误"))
                    } else {
                        Result.failure(RuntimeException("别忘了先连接内网，再重新试试吧"))
                    }
                }
            } else {
                Result.failure(RuntimeException("Cannot get the jwzx execution value."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun getSportsYearTerm() = liveData(Dispatchers.IO) {
        val result = try {
            val response = SportsNetwork.getSportsYearTerm()
            val string = response.body!!.string()
            val doc = Jsoup.parse(string)
            if (doc.getElementById("showErrorTip") == null) {
                val yearTerms = doc.select("select[id=yearTerm]").first()?.select("option")
                Result.success(yearTerms)
            } else {
                LoginDao.deleteSmartSportsCookies()
                Cookies.smartSportsCookiesList.clear()
                Result.failure(RuntimeException("Cookie已经失效了..."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun getSportsCameraRecord(yearTerm: String, page: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = SportsNetwork.getSportsCameraRecord(yearTerm, page)
            val string = response.body!!.string()
            Log.d("sportCameraRecord", string)
            val information = Gson().fromJson(string, SportsCameraRecordModel::class.java)
            if (information.code == "0") {
                Result.success(information)
            } else {
                Result.failure(RuntimeException("sports camera info status is ${information.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun getSportsCheckRecord(yearTerm: String, page: Int) = liveData(Dispatchers.IO) {
        val result = try {
            val response = SportsNetwork.getSportsCheckRecord(yearTerm, page)
            val string = response.body!!.string()
            val information = Gson().fromJson(string, SportsCheckRecordModel::class.java)
            if (information.code == "0") {
                Result.success(information)
            } else {
                Result.failure(RuntimeException("sport check info status is ${information.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun getStudentSportsResume(yearTerm: String) = liveData(Dispatchers.IO) {
        val result = try {
            val response = SportsNetwork.getStudentSportsResume(yearTerm)
            val stringBefore = response.body!!.string()
            val string = stringBefore.substringAfter('[').substringBeforeLast(']')
            Log.d("resume", string)
            val information = Gson().fromJson(string, StudentSportsResumeMobileModel::class.java)
            if (information != null) {
                Result.success(information)
            } else {
                Result.failure(RuntimeException("获取智慧体育信息失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    // WeNetwork

    fun getWeekTime() = liveData(Dispatchers.IO) {
        val result = try {
            val response = WeNetwork.getWeekTime()
            val string = response.body!!.string()
            val information = Gson().fromJson(string, WeekTimeModel::class.java)
            if (information.message == "ok") {
                Result.success(information.data)
            } else {
                Result.failure(RuntimeException("WeCQUPT status is ${information.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun punchInEveryDay(
        username: String,
        password: String,
        openId: String,
        locationBig: String,
        locationSmall: String,
        latitude: Double,
        longitude: Double,
        currentLocation: String,
        currentDetailedLocation: String
    ) = liveData(Dispatchers.IO) {
        val result = try {
            val loginWeCqupt = LoginNetwork.loginWeCqupt(openId, username, password)
            val loginString = loginWeCqupt.body!!.string()
            Log.d("login_we_string", loginString)
            val loginInformation = Gson().fromJson(loginString, WeLoginResponseModel::class.java)
            if (loginInformation.message == "ok") {
                val punch = WeNetwork.punchInEveryDay(
                    name = loginInformation.data.name,
                    id = loginInformation.data.id,
                    gender = loginInformation.data.gender,
                    openId,
                    locationBig,
                    locationSmall,
                    latitude,
                    longitude,
                    currentLocation,
                    currentDetailedLocation
                )
                val punchString = punch.body!!.string()
                Log.d("punch_string", punchString)
                val punchInformation =
                    Gson().fromJson(punchString, WeLoginResponseModel::class.java)
                if (punchInformation.message == "OK") {
                    Result.success(true)
                } else {
                    Result.failure(RuntimeException("打卡失败"))
                }
            } else {
                Result.failure(RuntimeException("bind is failure."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(RuntimeException("可能不自己抓包OpenID会出现点问题，你还是自己抓包后填写一下吧，一劳永逸"))
        }
        emit(result)
    }
}