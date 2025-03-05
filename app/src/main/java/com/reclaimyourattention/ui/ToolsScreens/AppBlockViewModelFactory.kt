package com.reclaimyourattention.ui.ToolsScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppBlockViewModelFactory(
    private val initialBlockedPackages: Set<String>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppBlockViewModel(initialBlockedPackages) as T
    }
}