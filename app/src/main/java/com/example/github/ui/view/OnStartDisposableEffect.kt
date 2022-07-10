package com.example.github.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.github.util.log.AppLogger

private const val tag = "OnStartDisposableEffect"

@Composable
fun OnStartDisposableEffect(
    owner: LifecycleOwner,
    onStart: () -> Unit,
    onDispose: (() -> Unit)? = null) {

    DisposableEffect(owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                AppLogger.log(tag, "ON_START")
                onStart()
            }
        }

        owner.lifecycle.addObserver(observer)

        onDispose {
            AppLogger.log(tag, "Dispose")
            owner.lifecycle.removeObserver(observer)
            onDispose?.invoke()
        }
    }
}