package com.yenaly.cqupttoolbox.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.databinding.ActivityLoginBinding
import com.yenaly.cqupttoolbox.ui.viewmodel.LoginViewModel
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast
import kotlin.concurrent.thread

/**
 * An activity for login.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/02/16 016 11:06
 * @Description : Description...
 */
class LoginActivity : RootActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (
            viewModel.getLoginCode().isNotEmpty() &&
            viewModel.getLoginPassword().isNotEmpty() &&
            viewModel.getLoginId().isNotEmpty() &&
            viewModel.getLoginName().isNotEmpty() &&
            viewModel.getNeedAutoLogin()
        ) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rememberIdCheckBox.isChecked = viewModel.getLoginCodeCheckBox()
        binding.rememberPasswordCheckBox.isChecked = viewModel.getLoginPasswordCheckBox()
        binding.loginCode.setText(viewModel.getLoginCode())
        binding.loginPassword.setText(viewModel.getLoginPassword())

        binding.rememberIdCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked)
                binding.rememberPasswordCheckBox.isChecked = false
        }
        binding.rememberPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rememberIdCheckBox.isChecked = true
            } else {
                viewModel.saveLoginPasswordCheckBox(isChecked)
                viewModel.deleteSaveLoginInfo(true, isChecked)
            }
        }

        binding.loginButton.setOnClickListener(this)
        binding.clickPunch.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        val loginId = binding.loginCode.text.toString()
        val loginPassword = binding.loginPassword.text.toString()
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        binding.loginCode.clearFocus()
        binding.loginPassword.clearFocus()

        when (v?.id) {
            R.id.login_button -> {
                if (loginId.isNotBlank() && loginPassword.isNotBlank()) {
                    loginViaWeCqupt()
                    showLoadingDialog(loadingText = "正在登录中")
                    viewModel.loginViaWeCqupt(
                        username = loginId,
                        password = loginPassword,
                        openId = "${(1000000..2000000).random()}"
                    )
                } else {
                    "先得输入个认证码和密码吧".showShortToast()
                }
            }
            R.id.click_punch -> {
                if (loginId.isNotBlank() && loginPassword.isNotBlank()) {
                    val view = LayoutInflater.from(this).inflate(R.layout.dialog_punch, null)
                    MaterialAlertDialogBuilder(this)
                        .setView(view)
                        .setPositiveButton("确认") { _, _ ->
                            val openId =
                                view.findViewById<EditText>(R.id.input_open_id).text.toString()
                            punchInEveryDay()
                            showLoadingDialog(loadingText = "正在尝试中")
                            viewModel.punchInEveryDay(
                                username = loginId,
                                password = loginPassword,
                                openId = openId.ifBlank { "${(1000000..2000000).random()}" },
                                locationBig = "中国,重庆市,重庆市,渝中区",
                                locationSmall = "重庆市渝中区人民支路96-5号",
                                latitude = 29.5647,
                                longitude = 106.55073,
                                currentLocation = "重庆市,重庆市,南岸区",
                                currentDetailedLocation = "重庆邮电大学"
                            )
                        }
                        .setNegativeButton("不会整") { _, _ ->
                            "去问问身边的计算机大爹们吧🤗".showShortToast()
                        }
                        .show()
                } else {
                    "先得输入个认证码和密码吧".showShortToast()
                }
            }
        }
    }

    private fun saveLoginInfo(code: String, password: String, name: String, id: String) {
        thread {
            viewModel.deleteSaveLoginInfo(
                binding.rememberIdCheckBox.isChecked,
                binding.rememberPasswordCheckBox.isChecked
            )
            viewModel.deleteLoginCookies(binding.rememberPasswordCheckBox.isChecked)
            if (binding.rememberIdCheckBox.isChecked) {
                viewModel.saveLoginCodeCheckBox(binding.rememberIdCheckBox.isChecked)
                viewModel.saveLoginCode(code)
            }
            if (binding.rememberPasswordCheckBox.isChecked && binding.rememberIdCheckBox.isChecked) {
                viewModel.saveLoginPasswordCheckBox(binding.rememberPasswordCheckBox.isChecked)
                viewModel.saveLoginPassword(password)
                viewModel.saveLoginNameAndId(name, id)
            }
        }
    }

    private fun loginViaWeCqupt() {
        viewModel.loginViaWeLiveData.observe(this) { result ->
            val information = result.getOrNull()
            if (information != null) {
                val loginCode = binding.loginCode.text.toString()
                val loginPassword = binding.loginPassword.text.toString()
                val loginName = information.name
                val loginId = information.id
                saveLoginInfo(loginCode, loginPassword, loginName, loginId)
                viewModel.saveNeedAutoLogin(true)
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("user_code", loginCode)
                    putExtra("user_password", loginPassword)
                    putExtra("user_name", loginName)
                    putExtra("user_id", loginId)
                }
                hideLoadingDialog()
                startActivity(intent)
                this.finish()
            } else {
                result.exceptionOrNull()?.printStackTrace()
                hideLoadingDialog()
                result.exceptionOrNull()?.message?.showShortToast()
            }
        }
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
}