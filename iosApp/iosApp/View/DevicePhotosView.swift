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
    private let gridItemSpacing: Double = 10
    
    var body: some View {
        VStack {
                GeometryReader { geoReader in
                    NavigationView {
                        ScrollView {
                            LazyVGrid(columns: gridColumns, spacing: gridItemSpacing) {
                                    ForEach(viewModel.deviceImages) { photo in                                        
                                        NavigationLink(destination: PhotoView(photo: photo)) {
                                            PhotoImageView(photo: photo, width: (Double(geoReader.size.width) / Double(gridColumns.count)) - gridItemSpacing / 2)
                                        }
                                    }
                                }
                        }.onAppear {
                            if (viewModel.needPermissionToAccessLocalPhotos) {
                                viewModel.fetchSamplePhotos()
                            } else {
                                viewModel.fetchLocalPhotos()
                            }
                        }.navigationBarTitle("Gallery", displayMode: .large)
                    }
                }
                
                if (viewModel.needPermissionToAccessLocalPhotos) {
                    CTAButtonView(buttonText: viewModel.localPhotosPermissionCtaButtonText, descriptionText: viewModel.localPhotosPermissionCtaDescriptionText) {
                        viewModel.askForPermissionLocalPhotos()
                    }
                }
        }
    }
}

