package com.yenaly.cqupttoolbox.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.yenaly.cqupttoolbox.logic.Repository
import com.yenaly.cqupttoolbox.logic.model.LoginModel

/**
 * Fake view model. Use the simplest way to eradicate sticky event.
 *
 * @ProjectName : CQUPTToolbox
 * @Author : Yenaly Liew
 * @Time : 2022/03/29 029 20:32
 * @Description : Description...
 */
class SportsSingleViewModel {

    private val repository = Repository()

    private val _loginSmartSportsLiveData = MutableLiveData<LoginModel>()
    val loginSmartSportsLiveData = _loginSmartSportsLiveData.switchMap {
        repository.loginSmartSports(it.username, it.password, it.captcha)
    }

    fun loginSmartSports(username: String, password: String, captcha: String) {
        _loginSmartSportsLiveData.value = LoginModel(username, password, captcha)
    }

    private val _getSportsYearTermLiveData = MutableLiveData<Any?>()
    val getSportsYearTermLiveData = _getSportsYearTermLiveData.switchMap {
        repository.getSportsYearTerm()
    }

    fun getSportsYearTerm() {
        _getSportsYearTermLiveData.value = _getSportsYearTermLiveData.value
    }
}