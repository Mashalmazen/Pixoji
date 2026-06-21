package com.mashalmazen.pixoji.data.permission

import android.Manifest
import android.os.Build

object PermissionManager {
    fun getRequiredPermission(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    fun getWritePermission(): String = Manifest.permission.WRITE_EXTERNAL_STORAGE
}
