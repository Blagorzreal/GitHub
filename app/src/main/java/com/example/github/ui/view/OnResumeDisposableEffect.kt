package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.github.util.log.AppLogger

@Composable
fun OnResumeDisposableEffect(
    owner: LifecycleOwner,
    onResume: () -> Unit,
    onDispose: (() -> Unit)? = null) {

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                AppLogger.log("OnResumeDisposableEffect", "ON_RESUME")
                onResume()
            }
        }

        owner.lifecycle.addObserver(observer)

        onDispose {
            AppLogger.log("OnResumeDisposableEffect", "Dispose")
            owner.lifecycle.removeObserver(observer)
            onDispose?.invoke()
        }
    }
}