package com.reclaimyourattention.logic.tools

import android.content.Context
import android.graphics.drawable.Drawable

fun getInstalledApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        return pm.getInstalledPackages(0)
            .filter { it.applicationInfo!!.enabled } // Filtra apps deshabilitadas
            .map {
                AppInfo(
                    name = it.applicationInfo!!.loadLabel(pm).toString(),
                    packageName = it.packageName,

                )
            }
            .sortedBy { it.name }
    }

    data class AppInfo(
        val name: String,
        val packageName: String,

    )
