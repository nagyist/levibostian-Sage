package earth.levi.sage.type.result

import earth.levi.sage.type.FolderContents

sealed class GetFolderContentsResult {
    object Unauthorized: GetFolderContentsResult()
    data class Success(val contents: FolderContents, val fullSyncCompleted: Boolean): GetFolderContentsResult()
}