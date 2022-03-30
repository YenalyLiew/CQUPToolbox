package com.yenaly.cqupttoolbox.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.yenaly.cqupttoolbox.logic.Repository
import com.yenaly.cqupttoolbox.logic.dao.LoginDao
import com.yenaly.cqupttoolbox.logic.model.*
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

    var sportsCameraRecordYearTerm = "20212"
    var sportsCameraRecordCurrentPage = 1
    var sportsCameraRecordTotalPage = 1
    val sportsCameraRecordList = ArrayList<SportsCameraRecordModel.Rows>()
    private val _sportsCameraRecordLiveData = MutableLiveData<YearTermAndPage>()
    val sportsCameraRecordLiveData = _sportsCameraRecordLiveData.switchMap {
        repository.getSportsCameraRecord(it.yearTerm, it.page)
    }

    fun getSportsCameraRecord(yearTerm: String, page: Int) {
        _sportsCameraRecordLiveData.value = YearTermAndPage(yearTerm, page)
    }

    // used for SportCheckRecordFragment

    var sportsCheckRecordYearTerm = "20212"
    var sportsCheckRecordCurrentPage = 1
    var sportsCheckRecordTotalPage = 1
    val sportsCheckRecordList = ArrayList<SportsCheckRecordModel.Rows>()
    private val _sportsCheckRecordLiveData = MutableLiveData<YearTermAndPage>()
    val sportsCheckRecordLiveData = _sportsCheckRecordLiveData.switchMap {
        repository.getSportsCheckRecord(it.yearTerm, it.page)
    }

    fun getSportsCheckRecord(yearTerm: String, page: Int) {
        _sportsCheckRecordLiveData.value = YearTermAndPage(yearTerm, page)
    }

    // used for StudentSportsResumeFragment

    var studentSportsResumeYearTerm = "20212"
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
}