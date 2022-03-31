package com.nafanya.mp3world.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.downloader.PRDownloader
import com.nafanya.mp3world.R
import com.nafanya.mp3world.viewmodel.InitialViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.Timer
import java.util.TimerTask

@DelicateCoroutinesApi
class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.splash_screen)
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissionWrite) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permissionWrite), 0)
                return
            }
        }
        val viewModel = ViewModelProvider(this)[InitialViewModel::class.java]
        viewModel.initializeLists()
        PRDownloader.initialize(applicationContext)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(this@InitialActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }, 1000L)
    }

    companion object {
        private lateinit var activity: InitialActivity

        fun finish() {
            activity.finish()
        }
    }

    override fun finish() {
        super.finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
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
