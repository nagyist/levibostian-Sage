@file:JvmName("DiGraphAndroid") // compiler complains when there is a file named DiGraph in shared module and DiGraph in Android app.
package earth.levi.sage.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.russhwolf.settings.AndroidSettings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import earth.levi.sage.db.SageDatabase
import earth.levi.sage.service.AndroidHostingService
import earth.levi.sage.service.DropboxHostingService
import earth.levi.sage.service.HostingService
import earth.levi.sage.store.KeyValueStore
import earth.levi.sage.store.LocalPhotosStore
import earth.levi.sage.store.LocalPhotosStoreImp
import earth.levi.sage.util.Logger
import earth.levi.sage.util.LoggerImpl

/**
 * To avoid the di graph holding a strong reference to Context (lint says this error prone to do),
 * this singleton will be called when the application is loaded and will take what it needs from
 * the Context.
 */
object ContextDependents {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var sqlDriver: AndroidSqliteDriver
    lateinit var contentResolver: ContentResolver

    fun initialize(context: Context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sqlDriver = AndroidSqliteDriver(SageDatabase.Schema, context, "sage.db")
        contentResolver = context.contentResolver
    }
}

actual val DiGraph.keyValueStore: KeyValueStore
    get() = AndroidSettings(ContextDependents.sharedPreferences)

actual val DiGraph.logger: Logger
    get() = LoggerImpl()

actual val DiGraph.sqlDriver: SqlDriver
    get() = ContextDependents.sqlDriver

actual val DiGraph.hostingService: HostingService
    get() = androidHostingService

val DiGraph.androidHostingService: AndroidHostingService
    get() = DropboxHostingService(keyValueStore, logger)

actual val DiGraph.localPhotoStore: LocalPhotosStore
    get() = LocalPhotosStoreImp(contentResolver, logger)

val DiGraph.contentResolver: ContentResolver
    get() = ContextDependents.contentResolver