package com.reclaimyourattention.ui.ToolsScreens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppBlockViewModelFactory(
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppBlockViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AppBlockViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }