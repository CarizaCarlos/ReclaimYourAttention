package com.reclaimyourattention.ui.ToolsScreens

import android.content.ContentResolver
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import com.reclaimyourattention.logic.tools.AppInfo
import com.reclaimyourattention.logic.tools.getInstalledApps
import kotlinx.coroutines.flow.flow


class AppBlockViewModel(
    private val context: Context
) : ViewModel() {
    private val _blockedPackages = mutableStateOf(emptySet<String>())
    val blockedPackages: State<Set<String>> = _blockedPackages

    val installedApps: Flow<List<AppInfo>> = flow {
        emit(getInstalledApps(context))
    }

    fun toggleBlock(packageName: String) {
        _blockedPackages.value = _blockedPackages.value.toMutableSet().apply {
            if (contains(packageName)) remove(packageName) else add(packageName)
        }
    }


}