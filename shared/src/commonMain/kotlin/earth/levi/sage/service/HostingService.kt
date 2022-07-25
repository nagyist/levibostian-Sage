package earth.levi.sage.service

import earth.levi.sage.kotlin_inline.Result
import earth.levi.sage.type.result.GetFolderContentsResult

/**
 * Defines actions for a hosting service for photos such as Dropbox, Samba share, etc.
 */
interface HostingService {
    suspend fun getFolderContents(path: String): Result<GetFolderContentsResult>
}