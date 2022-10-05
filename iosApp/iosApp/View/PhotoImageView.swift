//
//  PhotoImageView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/20/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

// Just takes in a Photo asset and it will display it for you.
struct PhotoImageView: View {
    @StateObject var vm : PhotoImageViewViewModel
    let photo: Photo
    let width: Double
    let height: Double
    
    // make a square image
    init(photo: Photo, width: Double) {
        self.init(photo: photo, width: width, height: width)
    }
    
    // make a non-square image
    init(photo: Photo, width: Double, height: Double) {
        self.photo = photo
        self.width = width
        self.height = height
        
        _vm = StateObject(wrappedValue: PhotoImageViewViewModel(photo: photo, width: width, height: height))
    }
    
    var body: some View {
        if let localImageView = vm.image {
            localImageView
                .centerCropped(width: width)
        }
        if let remoteImage = vm.remoteImage {
            remoteImage
                .frame(width: width, height: height)
        }
    }
}

@MainActor
class PhotoImageViewViewModel: ObservableObject {
    @Published var image : Image?
    @Published var remoteImage: RemoteImageView?
    let photo: Photo
    let width: Double
    let heigtht: Double
    
    private let localPhotoStore: iOSLocalPhotoStore
    
    init(photo: Photo, width: Double, height: Double, localPhotoStore: iOSLocalPhotoStore = DiGraph.shared.iOSLocalPhotoStore) {
        self.photo = photo
        self.width = width
        self.heigtht = height
        self.localPhotoStore = localPhotoStore

        self.requestImage(targetSize: CGSize(width: width, height: height))
    }
    
    func requestImage(targetSize: CGSize) {
        if let localPhotoAsset = self.photo.localPhotoAsset {
            // the multiplying the image width for target size is a hack to make the thumbnails look better. I found that the higher the resolution, the better they look. This value has been tested to be the lowest multiplier while not losing quality.
            localPhotoStore.loadThumbnail(asset: localPhotoAsset, width: targetSize.width * 4, height: targetSize.height * 4) { pair in
                withAnimation(Animation.easeInOut (duration:0.15)) {
                    self.remoteImage = nil
                    self.image = Image(uiImage: pair.second!)
                }
            }
        }
        if self.photo.isRemote {
            self.image = nil
            self.remoteImage = RemoteImageView(photo: self.photo, targetSize: targetSize)
        }
    }
}
