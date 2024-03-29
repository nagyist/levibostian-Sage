package earth.levi.sage.di

import com.russhwolf.settings.AppleSettings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import earth.levi.sage.db.SageDatabase
import earth.levi.sage.service.DropboxHostingService
import earth.levi.sage.service.HostingService
import earth.levi.sage.service.iOSHostingService
import earth.levi.sage.store.KeyValueStore
import earth.levi.sage.store.LocalPhotosStore
import earth.levi.sage.store.LocalPhotosStoreImp
import earth.levi.sage.store.iOSLocalPhotoStore
import earth.levi.sage.util.Logger
import earth.levi.sage.util.LoggerImpl
import earth.levi.sage.util.PermissionUtil
import platform.Foundation.NSUserDefaults

actual val DiGraph.keyValueStore: KeyValueStore
    get() = AppleSettings(NSUserDefaults())

actual val DiGraph.logger: Logger
    get() = LoggerImpl()

actual val DiGraph.sqlDriver: SqlDriver
    get() = NativeSqliteDriver(SageDatabase.Schema, "sage.db")

actual val DiGraph.hostingService: HostingService
    get() =  iosHostingService

val DiGraph.iosHostingService: iOSHostingService
    get() = DropboxHostingService(logger)

actual val DiGraph.localPhotoStore: LocalPhotosStore
    get() = LocalPhotosStoreImp()

val DiGraph.iOSLocalPhotoStore: iOSLocalPhotoStore
    get() = LocalPhotosStoreImp()

actual val DiGraph.permissionUtil: PermissionUtil
    get() = PermissionUtil()