package earth.levi.sage.util

import earth.levi.sage.type.RuntimePermission
import platform.Foundation.NSURL
import platform.Photos.*
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

interface PermissionUtilType {
    fun openAppSettings()
    fun hasAskedForPermission(permission: RuntimePermission): Boolean
    fun doesHavePermission(permission: RuntimePermission): Boolean
    fun askForPermission(permission: RuntimePermission, callback: (Boolean) -> Unit)
}

actual class PermissionUtil: PermissionUtilType {

    override fun openAppSettings() {
        val settingsUrl = NSURL(string = UIApplicationOpenSettingsURLString)

        if (UIApplication.sharedApplication.canOpenURL(settingsUrl)) {
            UIApplication.sharedApplication.openURL(settingsUrl)
        }
    }

    override fun hasAskedForPermission(permission: RuntimePermission): Boolean {
        return when(permission) {
            RuntimePermission.ACCESS_PHOTOS -> PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelReadWrite) != PHAuthorizationStatusNotDetermined
        }
    }

    override fun askForPermission(permission: RuntimePermission, callback: (Boolean) -> Unit) {
        when (permission) {
            RuntimePermission.ACCESS_PHOTOS -> {
                throw IllegalStateException("don't call this code")

                // This code below throws a KMM error about sharing code between threads. the new memory model is coming out very soon and should fix all of those errors from happening. So, I will just avoid calling this code for now and instead have the code exist in Swift.
                PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelReadWrite) { status ->
                    val hasPermission = status == PHAuthorizationStatusAuthorized

                    callback(hasPermission)
                }
            }
        }
    }

    override fun doesHavePermission(permission: RuntimePermission): Boolean {
        return when (permission) {
            RuntimePermission.ACCESS_PHOTOS -> {
                val status = PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelReadWrite)

                status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited
            }
        }
    }

}