package com.nafanya.mp3world.features.foregroundService

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import androidx.core.content.ContextCompat
import com.nafanya.player.PlayerInteractor
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Object that monitors [PlayerInteractor.isPlayerInitialised] and starts [ForegroundService]
 * when necessary.
 */
@Singleton
class ServiceMonitor @Inject constructor(
    private val context: Context,
    private val playerInteractor: PlayerInteractor
) {
    private val starterScope = CoroutineScope(Job())

    @Suppress("Deprecation")
    fun startMonitoring() {
        starterScope.launch {
            playerInteractor.isPlayerInitialised.collect { isInit ->
                if (isInit) {
                    val activityManager =
                        context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                    val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
                    val isServiceRunning = runningServices
                        ?.find { it.service.className == ForegroundService::class.java.name }
                        ?.foreground == true
                    if (!isServiceRunning) {
                        val intent =
                            Intent(context.applicationContext, ForegroundService::class.java)
                        ContextCompat.startForegroundService(context.applicationContext, intent)
                    }
                }
            }
        }
    }
}
