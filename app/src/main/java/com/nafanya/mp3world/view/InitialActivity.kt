package com.nafanya.mp3world.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.downloader.PRDownloader
import com.nafanya.mp3world.R
import com.nafanya.mp3world.model.dependencies.PlayerApplication
import com.nafanya.mp3world.viewmodel.InitialViewModel
import java.util.Timer
import java.util.TimerTask

class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Log.d("TIME", "created")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TIME", "resumed")
        PlayerApplication.application = application
        checkPermissions()
    }

    // part of onCreate
    private fun checkPermissions() {
        val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissionRead) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permissionRead), 0) // triggers onPermissionResult
                return
            }
        }
        val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            if (checkSelfPermission(permissionWrite) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permissionWrite), 0)
                return
            }
        }
        val viewModel = ViewModelProvider(this)[InitialViewModel::class.java]
        viewModel.initializeLists()
        PRDownloader.initialize(applicationContext)
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    val intent = Intent(this@InitialActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },
            startDelay
        )
    }

    companion object {
        private const val startDelay = 1500L
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermissions()
    }
}
