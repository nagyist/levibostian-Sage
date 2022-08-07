package earth.levi.sage.type

import earth.levi.sage.db.Folder

interface FolderOrPhoto

val FolderOrPhoto.isPhoto: Boolean
    get() = this is Photo

val FolderOrPhoto.isFolder: Boolean
    get() = !isPhoto

val FolderOrPhoto.asPhoto: Photo
    get() = this as Photo

val FolderOrPhoto.asFolder: Folder
    get() = this as Folder

val FolderOrPhoto.asPhotoOrNull: Photo?
    get() = if (isPhoto) asPhoto else null