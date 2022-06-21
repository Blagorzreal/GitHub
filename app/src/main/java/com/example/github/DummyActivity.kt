package com.example.github

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.github.util.log.AppLogger

// XXX: The Android Studio IDE shows a warning if the class name contains the word "Splash" itself
// It can be workaround with @SuppressLint("CustomSplashScreen").
// https://developer.android.com/guide/topics/ui/splash-screen/migrate#best-practices
class DummyActivity: ComponentActivity() {
    companion object {
        private const val TAG = "Splash"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppLogger.log(TAG, "Create")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            handleSplashEnding()
        else
            installSplashScreen().setOnExitAnimationListener { handleSplashEnding() }
    }

    private fun handleSplashEnding() {
        AppLogger.log(TAG, "Ended")

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}