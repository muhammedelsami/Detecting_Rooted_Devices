package com.muhammed.detectingrooteddevices

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

/**
 * Created by Muhammed Elşami on 11.10.2024.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkForRootAndBlock()
    }

    private fun checkForRootAndBlock() {
        if (checkForRootFiles() || canExecuteSuCommand()) {
            // Root detected, show warning to user and close application
            AlertDialog.Builder(this)
                .setTitle("Security Warning")
                .setMessage("The app cannot run on a rooted device.")
                .setCancelable(false)
                .setPositiveButton("Ok") { _, _ -> finish() }  // Application is closing
                .show()
        }
    }

    // Check for root files
    private fun checkForRootFiles(): Boolean {
        val rootFiles = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        return rootFiles.any { File(it).exists() }
    }

    // Check for su command
    private fun canExecuteSuCommand(): Boolean {
        return try {
            Runtime.getRuntime().exec("su").inputStream.read() != -1
        } catch (e: Exception) {
            false
        }
    }
}