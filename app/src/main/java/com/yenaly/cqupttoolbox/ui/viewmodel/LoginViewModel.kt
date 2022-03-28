package com.yenaly.cqupttoolbox.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.yenaly.cqupttoolbox.logic.Repository
import com.yenaly.cqupttoolbox.logic.dao.LoginDao
import com.yenaly.cqupttoolbox.logic.model.WeLoginRequestModel
import com.yenaly.cqupttoolbox.logic.model.WePunchModel
import okhttp3.Cookie

/**
 * A view model for login page.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/16 016 13:31
 * @Description : Description...
 */

class LoginViewModel : ViewModel() {

    private val repository: Repository = Repository()

    // used for LoginActivity

    private val _loginViaWeLiveData = MutableLiveData<WeLoginRequestModel>()
    val loginViaWeLiveData =
        _loginViaWeLiveData.switchMap {
            repository.loginViaWeCqupt(it.openId, it.username, it.password)
        }

    fun loginViaWeCqupt(username: String, password: String, openId: String) {
        _loginViaWeLiveData.value = WeLoginRequestModel(username, password, openId)
    }

    private val weCquptPunchMutableLiveData = MutableLiveData<WePunchModel>()
    val weCquptPunchLiveData = weCquptPunchMutableLiveData.switchMap {
        repository.punchInEveryDay(
            it.username,
            it.password,
            it.openId,
            it.locationBig,
            it.locationSmall,
            it.latitude,
            it.longitude,
            it.currentLocation,
            it.currentDetailedLocation
        )
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
    ) {
        weCquptPunchMutableLiveData.value = WePunchModel(
            username,
            password,
            openId,
            locationBig,
            locationSmall,
            latitude,
            longitude,
            currentLocation,
            currentDetailedLocation
        )
    }

    fun saveLoginCode(id: String) = LoginDao.saveLoginCode(id)

    fun saveLoginCodeCheckBox(isChecked: Boolean) = LoginDao.saveLoginCodeCheckBox(isChecked)

    fun getLoginCode() = LoginDao.getLoginCode()

    fun saveLoginPassword(password: String) = LoginDao.saveLoginPassword(password)

    fun saveLoginPasswordCheckBox(isChecked: Boolean) =
        LoginDao.saveLoginPasswordCheckBox(isChecked)

    fun getLoginPassword() = LoginDao.getLoginPassword()

    fun saveLoginCookies(cookiesList: ArrayList<Cookie>) = LoginDao.saveLoginCookie(cookiesList)

    fun getLoginCookies() = LoginDao.getLoginCookie()

    fun saveLoginNameAndId(name: String, id: String) = LoginDao.saveLoginNameAndId(name, id)

    fun getLoginName() = LoginDao.getLoginName()

    fun getLoginId() = LoginDao.getLoginId()

    fun saveNeedAutoLogin(autoLogin: Boolean) = LoginDao.saveNeedAutoLogin(autoLogin)

    fun getNeedAutoLogin() = LoginDao.getNeedAutoLogin()

    fun deleteSaveLoginInfo(idIsChecked: Boolean, passwordIsChecked: Boolean) =
        LoginDao.deleteSaveLoginInfo(idIsChecked, passwordIsChecked)

    fun deleteLoginCookies(saveIsChecked: Boolean) = LoginDao.deleteLoginCookies(saveIsChecked)

    fun getLoginCodeCheckBox() = LoginDao.getLoginCodeCheckBox()

    fun getLoginPasswordCheckBox() = LoginDao.getLoginPasswordCheckBox()
}