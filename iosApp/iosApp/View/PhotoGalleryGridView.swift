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

struct PhotoGalleryGridView: View {
    @StateObject var viewModel = PhotosViewModel()
    @State var gridColumns = [GridItem(), GridItem(), GridItem()]
    private let gridItemSpacing: Double = 10
    @State private var isShowingHostingServiceAuthFlow = false
    @StateObject private var authFlowResult = HostingServiceAuthFlow.Result()
    
    var body: some View {
        GeometryReader { geoReader in
            NavigationView {
                VStack {
                    ScrollView {
                        LazyVGrid(columns: gridColumns, spacing: gridItemSpacing) {
                            ForEach(viewModel.deviceImages) { photo in
                                NavigationLink(destination: SinglePhotoView(photo: photo)) {
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
                    
                    if (viewModel.needPermissionToAccessLocalPhotos) {
                        NavigationLink(
                            destination:
                                HostingServiceAuthFlow(scopesToAskFor: [HostingServicePermissionScope.readFiles])
                                .environmentObject(authFlowResult),
                            isActive: $authFlowResult.isDoneWithAuthFlow) {
                            CTAButtonView(buttonText: viewModel.localPhotosPermissionCtaButtonText, descriptionText: viewModel.localPhotosPermissionCtaDescriptionText) {
                                    authFlowResult.isDoneWithAuthFlow = true
                            }
                        }
                    }
                }
            }
        }
    }
}

struct DevicePhotosView_Previews: PreviewProvider {
    static var previews: some View {
        Text("foo")
    }
}
