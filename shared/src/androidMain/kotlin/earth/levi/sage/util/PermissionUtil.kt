package earth.levi.sage.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import earth.levi.sage.type.RuntimePermission

interface PermissionUtilType {
    fun doesHavePermission(context: Context, permission: RuntimePermission): Boolean
}

actual class PermissionUtil: PermissionUtilType {

    override fun doesHavePermission(context: Context, permission: RuntimePermission): Boolean {
        val manifestPermission = when (permission) {
            RuntimePermission.ACCESS_PHOTOS -> Manifest.permission.READ_EXTERNAL_STORAGE
        }

        return ContextCompat.checkSelfPermission(context, manifestPermission) == PackageManager.PERMISSION_GRANTED
    }
}