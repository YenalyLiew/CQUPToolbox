package com.yenaly.cqupttoolbox.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.ActivityMainBinding
import com.yenaly.cqupttoolbox.logic.network.Cookies
import com.yenaly.cqupttoolbox.ui.viewmodel.MainViewModel
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast
import de.hdodenhof.circleimageview.CircleImageView

/**
 * An activity for main page.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/16 016 11:06
 * @Description : Description...
 */
class MainActivity : RootActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerStuPic: CircleImageView
    private lateinit var headerStuId: TextView
    private lateinit var headerStuName: TextView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var goOutEndTime: String? = null
    private var exitTime = 0L
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (
            viewModel.userCode == null ||
            viewModel.userPassword == null ||
            viewModel.currentUserId == null ||
            viewModel.currentUserName == null
        ) {
            viewModel.userCode =
                intent.getStringExtra("user_code") ?: viewModel.getLoginCode()
            viewModel.userPassword =
                intent.getStringExtra("user_password") ?: viewModel.getLoginPassword()
            viewModel.currentUserId =
                intent.getStringExtra("user_id") ?: viewModel.getLoginId()
            viewModel.currentUserName =
                intent.getStringExtra("user_name") ?: viewModel.getLoginName()
        }

        setSupportActionBar(binding.appBarAbility.toolbar)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_ability) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_personal_info,
                R.id.nav_empty_room_search,
                R.id.nav_student_sports_resume,
                R.id.nav_sports_check_record,
                R.id.nav_sports_camera_record
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.menu.findItem(R.id.settings).setOnMenuItemClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            false
        }
        binding.navView.menu.findItem(R.id.click_punch).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setCancelable(true)
                .setTitle("你真的没事吗？")
                .setMessage("一键打卡前务必确定你是绿码人员，处于低风险地区")
                .setPositiveButton("还真是") { _, _ ->
                    punchInEveryDay()
                    showLoadingDialog(loadingText = "正在尝试中")
                    viewModel.punchInEveryDay(
                        username = viewModel.userCode!!,
                        password = viewModel.userPassword!!,
                        openId = "${(1000000..2000000).random()}",
                        locationBig = "中国,重庆市,重庆市,渝中区",
                        locationSmall = "重庆市渝中区人民支路96-5号",
                        latitude = 29.5647,
                        longitude = 106.55073,
                        currentLocation = "重庆市,重庆市,南岸区",
                        currentDetailedLocation = "重庆邮电大学"
                    )
                }
                .setNegativeButton("算了吧", null)
                .show()
            false
        }
        binding.navView.menu.findItem(R.id.click_out).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setCancelable(true)
                .setTitle("你确定要申请吗？")
                .setMessage("确保当前不是审批制。并在相应时间段内回来，忘记时间概不负责。时间段一会会有提示。")
                .setPositiveButton("好") { _, _ ->
                    goOutSchool()
                    showLoadingDialog(loadingText = "正在尝试中")
                    viewModel.goOutSchool(
                        username = viewModel.userCode!!,
                        password = viewModel.userPassword!!,
                        openId = "${(1000000..2000000).random()}",
                        why = "有事",
                        where = "重庆邮电大学附近"
                    ) { endTime ->
                        goOutEndTime = endTime
                    }
                }
                .setNegativeButton("算了吧", null)
                .show()
            false
        }
        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setCancelable(true)
                .setTitle("†超天酱的提醒†")
                .setMessage(if (viewModel.currentUserName != null) "${viewModel.currentUserName}酱，你确定要登出吗🥺" else "你确定要登出吗🥺")
                .setPositiveButton("还真是") { _, _ -> backToLoginActivity() }
                .setNegativeButton("算了吧", null)
                .show()
            false
        }

        val headerView = binding.navView.getHeaderView(0)
        headerView.setPadding(0, getStatusBarHeight(), 0, 0)
        headerStuPic = headerView.findViewById(R.id.stu_pic)
        headerStuId = headerView.findViewById(R.id.stu_id)
        headerStuName = headerView.findViewById(R.id.stu_name)

        Glide.with(this).load(R.mipmap.ic_launcher).into(headerStuPic)
        headerStuName.text = viewModel.currentUserName
        headerStuId.text = viewModel.currentUserId
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val currentFragmentId = navController.currentDestination?.id
        if (currentFragmentId != null && currentFragmentId == R.id.nav_personal_info) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    exit()
                    return false
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            "你真的要退出吗🥺".showShortToast()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_ability)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun backToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        Cookies.jwzxCookiesList.clear()
        Cookies.smartSportsCookiesList.clear()
        viewModel.saveNeedAutoLogin(false)
        startActivity(intent)
        this.finish()
    }

    private fun punchInEveryDay() {
        viewModel.weCquptPunchLiveData.observe(this) { result ->
            val isSuccess = result.getOrNull()
            if (isSuccess != null) {
                if (isSuccess) {
                    "按道理说打卡应该成功了，不放心可以去We重邮看看".showShortToast()
                    hideLoadingDialog()
                }
            } else {
                result.exceptionOrNull()?.printStackTrace()
                hideLoadingDialog()
                result.exceptionOrNull()?.message?.showShortToast()
            }
        }
    }

    private fun goOutSchool() {
        viewModel.goOutSchoolLiveData.observe(this) { result ->
            val whichKind = result.getOrNull()
            if (whichKind != null) {
                hideLoadingDialog()
                MaterialAlertDialogBuilder(this)
                    .setCancelable(true)
                    .setTitle("申请成功喽！")
                    .setMessage(
                        "已成功申请[$whichKind]！\n" +
                                "入校截至时间：${goOutEndTime ?: "加载失败"}，\n" +
                                "不放心可以去We重邮看看"
                    )
                    .setPositiveButton("明白了😋", null)
                    .show()
                goOutEndTime = null
            } else {
                result.exceptionOrNull()?.printStackTrace()
                hideLoadingDialog()
                result.exceptionOrNull()?.message?.showShortToast()
            }
        }
    }
}