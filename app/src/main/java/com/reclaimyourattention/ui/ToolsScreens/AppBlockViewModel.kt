package com.reclaimyourattention.ui.ToolsScreens

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AppBlockViewModel(
    initialBlockedPackages: Set<String>
) : ViewModel() {
    private val _blockedPackages = mutableStateOf(initialBlockedPackages)
    val blockedPackages: State<Set<String>> = _blockedPackages

    val installedApps: Flow<List<AppInfo>> = flow {
        emit(getInstalledApps())
    }

    private fun getInstalledApps(): List<AppInfo> {
        val pm = appContext.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return pm.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)
            .mapNotNull {
                try {
                    val appInfo = it.activityInfo.applicationInfo
                    AppInfo(
                        name = appInfo.loadLabel(pm).toString(),
                        packageName = appInfo.packageName,
                        isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    )
                } catch (e: Exception) {
                    null
                }
            }
            .filter { app ->
                val whitelist = listOf(
                    "com.google.android.youtube",
                    "com.android.chrome",
                    "com.google.android.apps.photos"
                )
                !app.isSystemApp || whitelist.contains(app.packageName)
            }
            .sortedBy { it.name.lowercase() }
    }

    fun toggleBlock(packageName: String) {
        _blockedPackages.value = _blockedPackages.value.toMutableSet().apply {
            if (contains(packageName)) remove(packageName) else add(packageName)
        }
    }
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val isSystemApp: Boolean,

    )