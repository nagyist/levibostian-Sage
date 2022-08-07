package earth.levi.sage.store

import earth.levi.sage.type.LocalPhoto

actual class LocalPhotosStoreImp: LocalPhotosStore {

    override suspend fun fetchLocalPhotos(): List<LocalPhoto> {
        return emptyList()
    }

}