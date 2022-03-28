package com.yenaly.cqupttoolbox.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yenaly.cqupttoolbox.CTApplication
import com.yenaly.cqupttoolbox.R
import com.yenaly.cqupttoolbox.utils.FileUtils
import com.yenaly.cqupttoolbox.utils.ToastUtils.showShortToast

/**
 * An activity for settings.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/18 018 10:50
 * @Description : Description...
 */
class SettingsActivity : RootActivity() {

    private lateinit var toolbar: Toolbar

    companion object {
        class SettingsFragment : PreferenceFragmentCompat() {
            override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
                setPreferencesFromResource(R.xml.root_preferences, rootKey)

                val clearOtherCache: Preference? = findPreference("clear_other_cache")
                val aboutSoftware: Preference? = findPreference("about_software")

                var otherCacheByte: Long
                var clearOtherCacheText: String

                otherCacheByte = FileUtils.getFolderSizeByte(CTApplication.context.cacheDir)
                clearOtherCacheText = if (otherCacheByte != 0L) {
                    getString(R.string.current_other_cache_size, otherCacheByte / 1024.0 / 1024.0)
                } else getString(R.string.no_other_cache)
                clearOtherCache?.summary = clearOtherCacheText

                clearOtherCache?.setOnPreferenceClickListener {
                    if (otherCacheByte != 0L) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("†超天酱的提醒†")
                            .setMessage("若清理则会清除如摄像头采集记录加载的照片等的缓存")
                            .setCancelable(true)
                            .setPositiveButton("就要删") { _, _ ->
                                CTApplication.context.cacheDir.deleteRecursively()
                                otherCacheByte =
                                    FileUtils.getFolderSizeByte(CTApplication.context.cacheDir)
                                clearOtherCacheText = if (otherCacheByte != 0L) {
                                    getString(
                                        R.string.current_other_cache_size,
                                        otherCacheByte / 1024.0 / 1024.0
                                    )
                                } else getString(R.string.no_other_cache)
                                clearOtherCache.summary = clearOtherCacheText
                                "🥺🥺🥺".showShortToast()
                            }
                            .setNegativeButton("算了吧", null)
                            .show()
                    }

                    true
                }

                aboutSoftware?.setOnPreferenceClickListener {
                    true
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        toolbar = findViewById(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.settings)
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}