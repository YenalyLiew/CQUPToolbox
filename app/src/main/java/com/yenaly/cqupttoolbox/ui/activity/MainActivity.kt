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
import com.yenaly.cqupttoolbox.utils.DisplayUtils.getStatusBarHeight
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
                .setTitle("?????????????????????")
                .setMessage("?????????????????????????????????????????????????????????????????????")
                .setPositiveButton("?????????") { _, _ ->
                    punchInEveryDay()
                    showLoadingDialog(loadingText = "???????????????")
                    viewModel.punchInEveryDay(
                        username = viewModel.userCode!!,
                        password = viewModel.userPassword!!,
                        openId = "${(1000000..2000000).random()}",
                        locationBig = "??????,?????????,?????????,?????????",
                        locationSmall = "??????????????????????????????96-5???",
                        latitude = 29.5647,
                        longitude = 106.55073,
                        currentLocation = "?????????,?????????,?????????",
                        currentDetailedLocation = "??????????????????"
                    )
                }
                .setNegativeButton("?????????", null)
                .show()
            false
        }
        binding.navView.menu.findItem(R.id.click_out).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setCancelable(true)
                .setTitle("????????????????????????")
                .setMessage("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????")
                .setPositiveButton("???") { _, _ ->
                    goOutSchool()
                    showLoadingDialog(loadingText = "???????????????")
                    viewModel.goOutSchool(
                        username = viewModel.userCode!!,
                        password = viewModel.userPassword!!,
                        openId = "${(1000000..2000000).random()}",
                        why = "??????",
                        where = "????????????????????????"
                    )
                }
                .setNegativeButton("?????????", null)
                .show()
            false
        }
        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setCancelable(true)
                .setTitle("????????????????????????")
                .setMessage(if (viewModel.currentUserName != null) "${viewModel.currentUserName}???????????????????????????????" else "?????????????????????????")
                .setPositiveButton("?????????") { _, _ -> backToLoginActivity() }
                .setNegativeButton("?????????", null)
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
            "?????????????????????????".showShortToast()
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
                    "??????????????????????????????????????????????????????We????????????".showShortToast()
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
                    .setTitle("??????????????????")
                    .setMessage(
                        "???????????????[$whichKind]???\n" +
                                "?????????????????????????????????\n" +
                                "??????????????????We????????????"
                    )
                    .setPositiveButton("?????????????", null)
                    .show()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                hideLoadingDialog()
                result.exceptionOrNull()?.message?.showShortToast()
            }
        }
    }
}