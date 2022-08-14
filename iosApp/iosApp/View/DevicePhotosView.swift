//
//  DevicePhotosView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/8/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Photos

struct DevicePhotosView: View {
    @StateObject var viewModel = PhotosViewModel()
    @State var gridColumns = [GridItem(), GridItem(), GridItem()]
    
    var body: some View {
        VStack {
            GeometryReader { geoReader in
                ScrollView {
                    LazyVGrid(columns: gridColumns) {
                        ForEach(viewModel.deviceImages) { asset in
                            GalleryGridCellView(asset: asset, imageWidth: geoReader.size.width / CGFloat(gridColumns.count))
                        }
                    }
                }
            }
            
            if (viewModel.needPermissionToAccessLocalPhotos) {
                CTAButtonView(buttonText: viewModel.localPhotosPermissionCtaButtonText, descriptionText: viewModel.localPhotosPermissionCtaDescriptionText) {
                    viewModel.askForPermissionLocalPhotos()
                }
            }
        }.onAppear {
            if (viewModel.needPermissionToAccessLocalPhotos) {
                viewModel.fetchSamplePhotos()
            } else {
                viewModel.fetchLocalPhotos()
            }
        }
    }
}

