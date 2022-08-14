package earth.levi.sage.store

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import earth.levi.sage.type.LocalPhoto
import earth.levi.sage.type.Photo
import earth.levi.sage.util.Logger

// https://developer.android.com/training/data-storage/shared/media
// https://github.com/android/storage-samples/tree/main/MediaStore
class LocalPhotosStoreImp(
    private val contentResolver: ContentResolver,
    private val logger: Logger): LocalPhotosStore {

    override suspend fun fetchLocalPhotos(): List<Photo> {
        val selectColumns = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA // this is the path
        )
        val whereClause = null
        val whereArgs = null
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val allLocalPhotos = mutableListOf<Photo>()

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            selectColumns,
            whereClause,
            whereArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            logger.debug("Found ${cursor.count} images locally")

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(pathColumn)

                // How you get a file path when using `EXTERNAL_CONTENT_URI`.
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                allLocalPhotos.add(Photo.local(LocalPhoto(id = id.toString(), localPath = path)))
            }
        }

        return allLocalPhotos
    }

}