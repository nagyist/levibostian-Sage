import Foundation
import Combine
import shared

class CloudPhotosViewModel: ObservableObject {
    @Published var folders = [Folder]()
    @Published var needsAuthorization: Bool = false
    
    private var pollFoldersTask: Task<(), Never>? = nil
    private var updateFolderContentsTask: Task<(), Never>? = nil
    private var syncTask: Task<(), Never>? = nil
    
    private let repository: FilesRepository
    
    init(repository: FilesRepository = DiGraph.shared.filesRepository) {
        self.repository = repository

        sync()
    }
    
    func updateFolderContentsFromRemote(path: String) {
        updateFolderContentsTask?.cancel()
        
        updateFolderContentsTask = Task {
            let result = await convertToSwiftResult(block: {
                try await repository.updateFolderContentsFromRemote(path: path)
            })
                
            if case let .success(data) = result, case is GetFolderContentsResult.Unauthorized = data {
                needsAuthorization = true
            }
        }
    }
    
    func cancel() {
        updateFolderContentsTask?.cancel()
    }
    
    func convertToSwiftResult<T: Any>(result: shared.Result<T>) -> Swift.Result<T, ErrorWithMessage> {
        if let data = result.getOrNull() {
            return Swift.Result.success(data)
        }
        
        let error = result.exceptionOrNull()!
        return Swift.Result.failure(ErrorWithMessage(message: error.message ?? ""))
    }
    
    func convertToSwiftResult<T: Any>(block: () async throws -> shared.Result<T>) async -> Swift.Result<T, ErrorWithMessage> {
        do {
            let result = try await block()
            
            return convertToSwiftResult(result: result)
        } catch {
            return Swift.Result.failure(ErrorWithMessage(message: error.localizedDescription))
        }
    }
    
}
