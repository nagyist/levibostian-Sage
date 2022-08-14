import Foundation
import Combine
import shared
import KMPNativeCoroutinesAsync
import UIKit
import Photos

@MainActor
class PhotosViewModel: ObservableObject {
    @Published var deviceImages = [Photo]()
    @Published var needPermissionToAccessLocalPhotos = false
    @Published var hasAskedLocalPhotosPermission = false
    @Published var localPhotosPermissionCtaButtonText = ""
    @Published var localPhotosPermissionCtaDescriptionText = ""
    
    private let localPhotoStore: iOSLocalPhotoStore
    private let permissionUtil: PermissionUtilType
    private let samplePhotosUtil: SamplePhotosUtil
    
    private var fetchLocalPhotoTask: Task<(), Never>? = nil
    private var checkPermissionsTask: Task<(), Never>? = nil
    
    init(localPhotoStore: iOSLocalPhotoStore = DiGraph.shared.iOSLocalPhotoStore,
         permissionUtil: PermissionUtilType = DiGraph.shared.permissionUtil,
         samplePhotosUtil: SamplePhotosUtil = DiGraph.shared.samplePhotosUtil) {
        self.localPhotoStore = localPhotoStore
        self.permissionUtil = permissionUtil
        self.samplePhotosUtil = samplePhotosUtil
        
        self.checkPermissionStatus()
    }
    
    deinit {
        fetchLocalPhotoTask?.cancel()
        checkPermissionsTask?.cancel()
    }
    
    func loadThumbnail(for asset: PHAsset, width: Double, height: Double, onComplete: @escaping (PHAsset, UIImage?) -> Void) {
        localPhotoStore.loadThumbnail(asset: asset, width: width, height: height) { pair in
            onComplete(pair.first!, pair.second)
        }
    }
    
    func askForPermissionLocalPhotos() {
        if (hasAskedLocalPhotosPermission) {
            permissionUtil.openAppSettings()
        } else {
            checkPermissionsTask = Task {
                PHPhotoLibrary.requestAuthorization(for: .readWrite, handler: { status in
                    DispatchQueue.main.async {
                        self.needPermissionToAccessLocalPhotos = status != .authorized || status != .limited
                    }
                })
                
                //            permissionUtil.askForPermission(permission: .accessPhotos) { bool in
                //                self.needPermissionToAccessLocalPhotos = bool.boolValue
                //            }
            }
        }
    }

    func fetchLocalPhotos() {
        fetchLocalPhotoTask = Task {
            do {
                let result = try await localPhotoStore.fetchLocalPhotos()
                
                if let imageAssets = (result as? GetDevicePhotosResult.Success)?.contents {
                    self.deviceImages = imageAssets
                }
            } catch {
                print("failed with eror \(error)")
            }
        }
    }
    
    func fetchSamplePhotos() {
        self.deviceImages = samplePhotosUtil.getSamplePhotos(randomOrder: true)
    }
    
    func checkPermissionStatus() {
        self.needPermissionToAccessLocalPhotos = !permissionUtil.doesHavePermission(permission: .accessPhotos)
        
        self.hasAskedLocalPhotosPermission = permissionUtil.hasAskedForPermission(permission: .accessPhotos)
        
        if (needPermissionToAccessLocalPhotos) {
            localPhotosPermissionCtaButtonText = "Ask Permission"
            localPhotosPermissionCtaDescriptionText = "Want to see photos that you have taken?"
            
            if (hasAskedLocalPhotosPermission) {
                localPhotosPermissionCtaButtonText = "Open Settings"
                localPhotosPermissionCtaDescriptionText = "To see photos that you have taken, Open Settings then change Photos permission."
            }
        }
    }
    
}
