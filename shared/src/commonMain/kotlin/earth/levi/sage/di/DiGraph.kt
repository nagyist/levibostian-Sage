package earth.levi.sage.di

import com.squareup.sqldelight.db.SqlDriver
import earth.levi.sage.db.SageDatabase
import earth.levi.sage.repository.FilesRepository
import earth.levi.sage.repository.FilesRepositoryImpl
import earth.levi.sage.service.HostingService
import earth.levi.sage.store.KeyValueStore
import earth.levi.sage.store.LocalPhotosStore
import earth.levi.sage.util.Logger
import earth.levi.sage.util.PermissionUtil
import earth.levi.sage.util.SamplePhotosUtil
import earth.levi.sage.util.SamplePhotosUtilImp
import earth.levi.sage.viewmodel.FilesViewModel
import kotlin.math.log

object DiGraph

expect val DiGraph.keyValueStore: KeyValueStore

expect val DiGraph.hostingService: HostingService

expect val DiGraph.localPhotoStore: LocalPhotosStore

expect val DiGraph.logger: Logger

val DiGraph.database: SageDatabase
    get() = SageDatabase(sqlDriver)

// each platform needs to provide it's own sqlite driver
expect val DiGraph.sqlDriver: SqlDriver

val DiGraph.filesRepository: FilesRepository
    get() = FilesRepositoryImpl(database, hostingService)

expect val DiGraph.permissionUtil: PermissionUtil

val DiGraph.samplePhotosUtil: SamplePhotosUtil
    get() = SamplePhotosUtilImp()

val DiGraph.filesViewModel: FilesViewModel
    get() = FilesViewModel(filesRepository, logger)