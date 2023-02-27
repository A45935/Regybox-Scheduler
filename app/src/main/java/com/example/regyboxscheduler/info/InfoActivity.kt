package com.example.regyboxscheduler.info

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class InfoActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoView(
                onBackRequest = { finish() },
                onSendEmailRequested = ::onOpenSendEmail,
                onUrlRequested = ::openUrl
            )
        }
    }

    private fun onOpenSendEmail(email: String) {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, "About the Battleship App")
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("RegyboxScheduler", "Failed to send email", e)
            Toast
                .makeText(
                    this,
                    "No suitable app",
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }

    private fun openUrl(url: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("RegyboxScheduler", "Failed to open URL", e)
            Toast
                .makeText(
                    this,
                    "No suitable app",
                    Toast.LENGTH_LONG
                )
                .show()
        }
    }
}