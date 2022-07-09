package com.example.github.util

import android.content.Context
import android.widget.Toast

class CommonHelper private constructor() {
    companion object {
        fun showToast(context: Context, message: String, toastLength: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(context, message, toastLength).show()

        fun missingVMParameterException(tag: String, parameter: String)
            = Exception("$tag: Unable to get $parameter")

        fun throwDatabaseModuleException(daoModule: String) = throwModuleException("$daoModule database")

        fun throwApiModuleException(apiModule: String) = throwModuleException("$apiModule api")

        private fun throwModuleException(module: String) = Exception("Unable to get $module")
    }
}