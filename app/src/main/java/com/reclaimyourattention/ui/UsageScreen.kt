package com.reclaimyourattention.ui

import android.app.AppOpsManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reclaimyourattention.R
import com.reclaimyourattention.ui.theme.ReclaimYourAttentionTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.platform.LocalContext
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.material3.AlertDialog
import java.util.Calendar
import java.util.concurrent.TimeUnit
import coil.compose.rememberAsyncImagePainter
import android.provider.Settings

import kotlin.random.Random

// Verifica si el permiso está concedido
fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}

// Lanza la pantalla de configuración para que el usuario active el permiso
fun requestUsageStatsPermission(context: Context) {
    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
}

@Composable
fun PermissionRequestDialog(onGrant: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* No permitir cerrar */ },
        title = { Text("Permiso requerido") },
        text = { Text("Ve a Ajustes > Acceso a uso > Activa el permiso para esta app") },
        confirmButton = {
            Button(onClick = onGrant) {
                Text("Abrir ajustes")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageScreen(navController: NavController? = null) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(hasUsageStatsPermission(context)) }

    if (!hasPermission) {
        PermissionRequestDialog {
            requestUsageStatsPermission(context)
            hasPermission = hasUsageStatsPermission(context) // Actualiza el estado
        }
        return
    }

    var selectedTimeFrame by remember { mutableStateOf(TimeFrame.DAILY) }

    val usageStats = remember { mutableStateListOf<AppUsageInfo>() }

    // Cargar datos cuando cambia el timeframe
//    LaunchedEffect(selectedTimeFrame) {
//        val stats = getAppUsageStats(context, selectedTimeFrame)
//        usageStats.clear()
//        usageStats.addAll(stats)
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas de Uso") },
                actions = {
                    TimeFrameSelector(selectedTimeFrame) {
                        selectedTimeFrame = it
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Gráfica de pastel
            UsagePieChart(
                data = usageStats.filter { it.percentage >= 5 },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // Lista de apps
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(usageStats.filter { it.percentage >= 5 }.sortedByDescending { it.percentage }) { app ->
                    AppUsageListItem(app = app)
                }
            }
        }
    }
}

// Selector de periodo de tiempo
@Composable
private fun TimeFrameSelector(
    selected: TimeFrame,
    onSelectionChanged: (TimeFrame) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        TimeFrame.values().forEach { timeframe ->
            Row(
                modifier = Modifier
                    .clickable { onSelectionChanged(timeframe) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selected == timeframe,
                    onClick = { onSelectionChanged(timeframe) }
                )
                Text(
                    text = timeframe.displayName,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

// Gráfico de pastel personalizado
@Composable
private fun UsagePieChart(
    data: List<AppUsageInfo>,
    modifier: Modifier = Modifier
) {
    val total = data.sumOf { it.percentage }.toFloat()
    var startAngle = 0f

    Canvas(modifier = modifier) {
        data.forEach { app ->
            val sweepAngle = (app.percentage / total) * 360f
            drawArc(
                color = app.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(size.width, size.height)
            )
            startAngle += sweepAngle
        }
    }
}

// Item de la lista de apps
@Composable
private fun AppUsageListItem(app: AppUsageInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(app.icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(app.appName, style = MaterialTheme.typography.bodyLarge)
            Text(
                "${app.percentage}% • ${app.usageTime.toFormattedTime()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(app.percentage / 100f)
                    .background(app.color)
            )
        }
    }
}

// Data classes y helpers
enum class TimeFrame(val displayName: String) {
    DAILY("Diario"),
    WEEKLY("Semanal")
}

data class AppUsageInfo(
    val packageName: String,
    val appName: String,
    val icon: Any,
    val usageTime: Long, // en milisegundos
    val percentage: Int,
    val color: Color
)

@Composable
private fun getAppUsageStats(context: Context, timeframe: TimeFrame): List<AppUsageInfo> {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance()
    val now = System.currentTimeMillis()

    val interval = when (timeframe) {
        TimeFrame.DAILY -> {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            UsageStatsManager.INTERVAL_DAILY
        }
        TimeFrame.WEEKLY -> {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            UsageStatsManager.INTERVAL_WEEKLY
        }
    }

    val stats = usageStatsManager.queryUsageStats(
        interval,
        calendar.timeInMillis,
        now
    )

    val totalTime = stats.sumOf { it.totalTimeInForeground }
    if (totalTime == 0L) return emptyList()

    return stats
        .filter { it.totalTimeInForeground > 0 }
        .map {
            AppUsageInfo(
                packageName = it.packageName,
                appName = getAppName(context, it.packageName),
                icon = getAppIcon(context, it.packageName),
                usageTime = it.totalTimeInForeground,
                percentage = ((it.totalTimeInForeground.toDouble() / totalTime) * 100).toInt(),
                color = Color(android.graphics.Color.HSVToColor(floatArrayOf(
                    Random.nextFloat() * 360,
                    0.6f,
                    0.8f
                ))
                ))
        }
}

private fun getAppName(context: Context, packageName: String): String {
    return try {
        val pm = context.packageManager
        pm.getApplicationInfo(packageName, 0).loadLabel(pm).toString()
    } catch (e: Exception) {
        packageName
    }
}

@Composable
private fun getAppIcon(context: Context, packageName: String): Painter {
    return rememberAsyncImagePainter(
        model = context.packageManager.getApplicationIcon(packageName)
    )
}

private fun Long.toFormattedTime(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    return "${hours}h ${minutes}m"
}