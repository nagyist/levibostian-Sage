//
//  GalleryGridCellView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct GalleryGridCellView: View {
    @StateObject var vm : GalleryGridCellViewModel
    let photoAsset: Photo
    let imageWidth: Double
    
    init(asset: Photo, imageWidth: Double) {
        self.photoAsset = asset
        self.imageWidth = imageWidth
        _vm = StateObject(wrappedValue: GalleryGridCellViewModel(asset: asset, imageWidth: imageWidth))
    }
    
    var body: some View {
        if let localImageView = vm.image {
            localImageView
                .centerCropped(width: imageWidth)
        }
        if let remoteImage = vm.remoteImage {
            remoteImage
                .frame(width: imageWidth, height: imageWidth)
        }
    }
}

@MainActor
class GalleryGridCellViewModel: ObservableObject{
    @Published var image : Image?
    @Published var remoteImage: RemoteImageView?
    var photoAsset: Photo
    let imageWidth: Double
    
    private let localPhotoStore: iOSLocalPhotoStore
    
    init(asset: Photo, imageWidth: Double, localPhotoStore: iOSLocalPhotoStore = DiGraph.shared.iOSLocalPhotoStore) {
        self.photoAsset = asset
        self.imageWidth = imageWidth
        self.localPhotoStore = localPhotoStore

        self.requestImage(targetSize: CGSize(width: imageWidth, height: imageWidth))
    }
    
    func setAsset(_ asset: Photo, targetSize: CGSize) {
        self.photoAsset = asset
        self.requestImage(targetSize: targetSize)
    }
    
    func requestImage(targetSize: CGSize) {
        if let localPhotoAsset = self.photoAsset.localPhotoAsset {
            // the multiplying the image width for target size is a hack to make the thumbnails look better. I found that the higher the resolution, the better they look. This value has been tested to be the lowest multiplier while not losing quality.
            localPhotoStore.loadThumbnail(asset: localPhotoAsset, width: targetSize.width * 4, height: targetSize.height * 4) { pair in
                withAnimation(Animation.easeInOut (duration:0.15)) {
                    self.remoteImage = nil
                    self.image = Image(uiImage: pair.second!)
                }
            }
        }
        if self.photoAsset.isRemote {
            self.image = nil
            self.remoteImage = RemoteImageView(asset: photoAsset, targetSize: targetSize)
        }
    }
}
