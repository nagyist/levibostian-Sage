package earth.levi.sage.store

import earth.levi.sage.type.LocalPhoto
import earth.levi.sage.type.Photo

interface LocalPhotosStore {
    suspend fun fetchLocalPhotos(): List<Photo>
}