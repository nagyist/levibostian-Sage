package earth.levi.sage.type

/**
 * Represents a local photo found on the device. Object mostly used for importing into the database.
 */
data class LocalPhoto(
    val id: String, // whatever can make us identify a specific file in the file system as unique.
    val localPath: String
) {
    val path: String
        get() = localPath
}