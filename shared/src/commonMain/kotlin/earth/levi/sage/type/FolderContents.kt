package earth.levi.sage.type

import earth.levi.sage.db.File
import earth.levi.sage.db.Folder

data class FolderContents(
    val subfolders: List<Folder> = emptyList(),
    val files: List<File> = emptyList()
) {
    fun addFolderContents(newFolderContents: FolderContents): FolderContents {
        return this.addSubfolders(newFolderContents.subfolders).addFiles(newFolderContents.files)
    }

    fun addSubfolders(newSubfolders: List<Folder>): FolderContents {
        return this.copy(subfolders = this.subfolders.toMutableList().apply { addAll(newSubfolders) })
    }

    fun addFiles(newFiles: List<File>): FolderContents {
        return this.copy(files = this.files.toMutableList().apply { addAll(newFiles) })
    }
}