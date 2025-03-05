package com.reclaimyourattention.ui.ToolsScreens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reclaimyourattention.ReclaimYourAttention.Companion.appContext

class AppBlockViewModelFactory(
    private val context: Context,
    private val initialBlockedPackages: Set<String> // Nuevo parámetro
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppBlockViewModel::class.java)) {
            return AppBlockViewModel(initialBlockedPackages) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}