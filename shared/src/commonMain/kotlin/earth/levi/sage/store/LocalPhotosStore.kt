package earth.levi.sage.store

import earth.levi.sage.type.LocalPhoto

interface LocalPhotosStore {
    suspend fun fetchLocalPhotos(): List<LocalPhoto>
}

expect class LocalPhotosStoreImp: LocalPhotosStore