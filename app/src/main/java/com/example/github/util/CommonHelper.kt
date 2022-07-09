package com.example.github.util

import android.content.Context
import android.widget.Toast

class CommonHelper private constructor() {
    companion object {
        fun showToast(context: Context, message: String, toastLength: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(context, message, toastLength).show()
    }
}