package earth.levi.sage.type

data class Photo private constructor(
    val localPhotoAsset: LoadedDevicePhotoAsset? = null,
    val remotePhotoUrl: String? = null
) {

    companion object {
        fun local(photoAsset: LoadedDevicePhotoAsset): Photo = Photo(localPhotoAsset = photoAsset)
        fun remote(url: String): Photo = Photo(remotePhotoUrl = url)
    }

    val isLocal: Boolean
        get() = localPhotoAsset != null

    val isRemote: Boolean
        get() = remotePhotoUrl != null
}