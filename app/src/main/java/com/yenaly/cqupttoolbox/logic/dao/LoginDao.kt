package com.yenaly.cqupttoolbox.logic.dao

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yenaly.cqupttoolbox.CTApplication
import okhttp3.Cookie

/**
 * Login-related Database.
 *
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/02/18 018 10:53
 * @Description : Description...
 */
object LoginDao {

    fun saveLoginCodeCheckBox(isChecked: Boolean) {
        sharedPreferences().edit {
            putBoolean("save_login_code_check_box", isChecked)
        }
    }

    fun getLoginCodeCheckBox(): Boolean {
        return sharedPreferences().getBoolean("save_login_code_check_box", false)
    }

    fun saveLoginPasswordCheckBox(isChecked: Boolean) {
        sharedPreferences().edit {
            putBoolean("save_login_password_check_box", isChecked)
        }
    }

    fun getLoginPasswordCheckBox(): Boolean {
        return sharedPreferences().getBoolean("save_login_password_check_box", false)
    }

    fun saveLoginCode(code: String) {
        sharedPreferences().edit {
            putString("login_code", code)
        }
    }

    fun getLoginCode(): String {
        return sharedPreferences().getString("login_code", "") ?: ""
    }

    fun saveLoginPassword(password: String) {
        sharedPreferences().edit {
            putString("login_password", password)
        }
    }

    fun getLoginPassword(): String {
        return sharedPreferences().getString("login_password", "") ?: ""
    }

    fun saveLoginCookie(cookiesList: ArrayList<Cookie>) {
        sharedPreferences().edit {
            val cookies = Gson().toJson(cookiesList)
            putString("login_cookies", cookies)
        }
    }

    fun getLoginCookie(): ArrayList<Cookie>? {
        val cookies = sharedPreferences().getString("login_cookies", "") ?: ""
        return Gson().fromJson(cookies, object : TypeToken<ArrayList<Cookie>>() {}.type)
    }

    fun saveLoginNameAndId(name: String, id: String) {
        sharedPreferences().edit {
            putString("login_name", name)
            putString("login_id", id)
        }
    }

    fun getLoginName(): String {
        return sharedPreferences().getString("login_name", "") ?: ""
    }

    fun getLoginId(): String {
        return sharedPreferences().getString("login_id", "") ?: ""
    }

    fun saveNeedAutoLogin(autoLogin: Boolean) {
        sharedPreferences().edit {
            putBoolean("auto_login", autoLogin)
        }
    }

    fun getNeedAutoLogin(): Boolean {
        return sharedPreferences().getBoolean("auto_login", false)
    }

    fun saveSmartSportsCookies(cookiesList: ArrayList<Cookie>) {
        sharedPreferences().edit {
            val cookies = Gson().toJson(cookiesList)
            putString("smart_sports_cookies", cookies)
        }
    }

    fun getSmartSportsCookies(): ArrayList<Cookie>? {
        val cookies = sharedPreferences().getString("smart_sports_cookies", "") ?: ""
        return Gson().fromJson(cookies, object : TypeToken<ArrayList<Cookie>>() {}.type)
    }

    fun deleteSaveLoginInfo(idIsChecked: Boolean, passwordIsChecked: Boolean) {
        sharedPreferences().edit {
            if (!idIsChecked) clear()
            if (!passwordIsChecked && idIsChecked) {
                remove("login_password")
                remove("login_id")
                remove("login_name")
                remove("login_cookies")
                remove("smart_sports_cookies")
            }
        }
    }

    fun deleteLoginCookies(saveIsChecked: Boolean) {
        sharedPreferences().edit {
            if (!saveIsChecked) {
                remove("login_cookies")
                remove("smart_sports_cookies")
            }
        }
    }

    fun deleteSmartSportsCookies() {
        sharedPreferences().edit {
            remove("smart_sports_cookies")
        }
    }

    private fun sharedPreferences(): SharedPreferences {
        return CTApplication.context
            .getSharedPreferences("cqupt_toolbox", Context.MODE_PRIVATE)
    }
}