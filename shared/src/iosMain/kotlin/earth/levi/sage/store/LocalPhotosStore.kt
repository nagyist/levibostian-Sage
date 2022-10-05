package earth.levi.sage.store

import earth.levi.sage.type.LoadedDevicePhotoAsset
import earth.levi.sage.type.Photo
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSSortDescriptor
import platform.Photos.*
import platform.UIKit.UIImage

interface iOSLocalPhotoStore: LocalPhotosStore {
    fun loadThumbnail(asset: LoadedDevicePhotoAsset, width: Double, height: Double, onComplete: (Pair<PHAsset, UIImage?>) -> Unit)
    fun loadOriginalSize(asset: LoadedDevicePhotoAsset, onComplete: (Pair<PHAsset, UIImage?>) -> Unit)
}

class LocalPhotosStoreImp: iOSLocalPhotoStore {

    private val imageManager = PHImageManager()

    override suspend fun fetchLocalPhotos(): List<Photo> {
        val allPhotosOptions = PHFetchOptions()
        allPhotosOptions.sortDescriptors = listOf(NSSortDescriptor(key = "creationDate", ascending = true))
        val allPhotosFetchResult = PHAsset.fetchAssetsWithOptions(allPhotosOptions)
        val allPhotos = mutableListOf<Photo>()

        for (i in 0.toULong() until allPhotosFetchResult.count) {
            val phAsset = allPhotosFetchResult.objectAtIndex(i) as PHAsset
            allPhotos.add(Photo.local(phAsset))
        }

        return allPhotos
    }

    override fun loadThumbnail(asset: LoadedDevicePhotoAsset, width: Double, height: Double, onComplete: (Pair<PHAsset, UIImage?>) -> Unit) {
        imageManager.requestImageForAsset(asset, CGSizeMake(width = width, height = height), PHImageContentModeAspectFit, options = null) { image, _ ->
            onComplete(Pair(asset, image))
        }
    }

    override fun loadOriginalSize(asset: LoadedDevicePhotoAsset, onComplete: (Pair<PHAsset, UIImage?>) -> Unit) {
        loadThumbnail(asset, PHImageManagerMaximumSize.width, PHImageManagerMaximumSize.height, onComplete)
    }

}