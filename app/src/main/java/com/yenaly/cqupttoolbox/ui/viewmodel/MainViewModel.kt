package com.yenaly.cqupttoolbox.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.yenaly.cqupttoolbox.logic.Repository
import com.yenaly.cqupttoolbox.logic.dao.LoginDao
import com.yenaly.cqupttoolbox.logic.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.Cookie

/**
 * A view model for main page.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/19 019 09:49
 * @Description : Description...
 */
class MainViewModel : ViewModel() {

    private val repository: Repository = Repository()

    // used for MainActivity

    var currentUserName: String? = null
    var currentUserId: String? = null
    var userCode: String? = null
    var userPassword: String? = null

    var weekTime: WeekTimeModel.Data? = null

    fun getLoginCode() = LoginDao.getLoginCode()

    fun getLoginPassword() = LoginDao.getLoginPassword()

    fun getLoginId() = LoginDao.getLoginId()

    fun getLoginName() = LoginDao.getLoginName()

    fun saveNeedAutoLogin(autoLogin: Boolean) = LoginDao.saveNeedAutoLogin(autoLogin)

    fun getNeedAutoLogin() = LoginDao.getNeedAutoLogin()

    fun deleteLoginCookies(saveIsChecked: Boolean) = LoginDao.deleteLoginCookies(saveIsChecked)

    private val _weekTimeLiveData = MutableLiveData<Any?>()
    val weekTimeLiveData = _weekTimeLiveData.switchMap {
        repository.getWeekTime()
    }

    fun getWeekTime() {
        _weekTimeLiveData.value = _weekTimeLiveData.value
    }

    private val _weCquptPunchLiveData = MutableLiveData<WePunchModel>()
    val weCquptPunchLiveData = _weCquptPunchLiveData.switchMap {
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
        _weCquptPunchLiveData.value = WePunchModel(
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

    private val _goOutSchoolLiveData = MutableLiveData<GoOutSchoolVariables>()
    val goOutSchoolLiveData = _goOutSchoolLiveData.switchMap {
        repository.goOutSchool(
            it.username,
            it.password,
            it.openId,
            it.why,
            it.where
        )
    }

    fun goOutSchool(
        username: String,
        password: String,
        openId: String,
        why: String,
        where: String
    ) {
        _goOutSchoolLiveData.value = GoOutSchoolVariables(
            username, password, openId, why, where
        )
    }


    // used for EmptyRoomSearchFragment

    var firstWeekRoomSearch = 1
    var endWeekRoomSearch = 1
    var weekRoomSearch = 1
    var viewPagerCurrentPage = 0
    val classesCheckBoxSet = HashSet<Int>()
    val roomsList = ArrayList<String>()

    fun getEmptyRoomLiveData(start: Int, end: Int, week: Int, timeSet: HashSet<Int>) =
        repository.getEmptyRoom(start, end, week, timeSet)

    // used for SportsCameraRecordFragment

    val yearTerm = ArrayList<String>()

    var sportsCameraRecordYearTerm = ""
    var sportsCameraRecordCurrentPage = 1
    var sportsCameraRecordTotalPage = 1

    fun getSportsCameraRecordPaging(
        yearTerm: String,
        getPages: (Int) -> Unit
    ): Flow<PagingData<SportsCameraPagingModel>> =
        repository.getSportsCameraRecordPaging(yearTerm, getPages)
            .map { pagingData -> pagingData.map(SportsCameraPagingModel::InfoItem) }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) return@insertSeparators null
                    if (before == null) {
                        return@insertSeparators SportsCameraPagingModel.SeparatorItem(after.date)
                    }
                    if (before.date == after.date) {
                        null
                    } else {
                        SportsCameraPagingModel.SeparatorItem(after.date)
                    }
                }
            }
            .cachedIn(viewModelScope)

    // used for SportCheckRecordFragment

    var sportsCheckRecordYearTerm = ""
    var sportsCheckRecordCurrentPage = 1
    var sportsCheckRecordTotalPage = 1

    fun getSportsCheckRecordPaging(
        yearTerm: String,
        getPages: (Int) -> Unit
    ) = repository.getSportsCheckRecordPaging(yearTerm, getPages).cachedIn(viewModelScope)

    // used for StudentSportsResumeFragment

    var studentSportsResumeYearTerm = ""
    private val _studentSportsResumeLiveData = MutableLiveData<String>()
    val studentSportsResumeLiveData = _studentSportsResumeLiveData.switchMap { yearTerm ->
        repository.getStudentSportsResume(yearTerm)
    }

    fun getStudentSportsResume(yearTerm: String) {
        _studentSportsResumeLiveData.value = yearTerm
    }

    fun saveSmartSportsCookies(cookiesList: ArrayList<Cookie>) =
        LoginDao.saveSmartSportsCookies(cookiesList)

    fun getSmartSportsCookies() = LoginDao.getSmartSportsCookies()

    // Data class

    data class GoOutSchoolVariables(
        val username: String,
        val password: String,
        val openId: String,
        val why: String,
        val where: String
    )
}