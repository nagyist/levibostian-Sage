//
//  PhotoView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/19/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Photos

struct SinglePhotoView: View {
    
    let photo: Photo
    
    @State private var shouldShareImage = false
    @StateObject private var viewModel = ViewModel()
    
    var body: some View {
        GeometryReader { geo in
            PhotoImageView(photo: photo, width: geo.size.width, height: geo.size.height)
        }.sheet(isPresented: $shouldShareImage) {
            ShareSheetView(imageToShare: viewModel.image!)
        }.toolbar {
            ToolbarItem(placement: .primaryAction) {
                if viewModel.image == nil {
                    ProgressView()
                        .progressViewStyle(.circular)
                } else {
                    Button(action: {
                        self.shouldShareImage = true
                    }) {
                        Label("Share", systemImage: "square.and.arrow.up")
                    }
                }
            }
        }.onAppear {
            viewModel.startFetchingImage(photo: self.photo)
        }
    }
}

extension SinglePhotoView {
    class ViewModel: ObservableObject {
        @Published private(set) var image: UIImage? = nil
        
        private let localPhotoStore: iOSLocalPhotoStore
        
        init(localPhotoStore: iOSLocalPhotoStore = DiGraph.shared.iOSLocalPhotoStore) {
            self.localPhotoStore = localPhotoStore
        }
        
        func startFetchingImage(photo: Photo) {
            // At this time, you can only share local images. I am not sure how we want to do
            // remote images at this time. download remote image to device then share that?
            // share dropbox share link URL? something else? 
            if let localPhotoAsset = photo.localPhotoAsset {
                localPhotoStore.loadOriginalSize(asset: localPhotoAsset) { pair in
                    self.image = pair.second
                }
            }
        }
    }
}

