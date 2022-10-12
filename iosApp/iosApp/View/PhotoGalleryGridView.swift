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

// TODO: trying to create a swift ViewModel that creates @Published from
// shared ViewModel in common.
// ultimately, I am trying to crete a sync() functdion that kicks off an async task
// that publishes status updates to a Flow about the status of the sync and that
// Flow gets parsed into these published below.
// then, I'll be able to use the properties to populate views in the UI.
//
// the UI screen when it loads up is meant to call sync() and if any issues or whatever
// display UI views.
//
// gallery screen opens up, needs to perform a sync() with dropbox. or at least try even if
// never logged in before. we can use that status as an opportunity to ask to login forf first time.
// so, we need to call sync() on a swift viewmodel which then gives us Published so we can
// dynamically alter a view for us.
// inm teh view model, sync() triggers a common viewmodel code to do the sync on all platforms.
// it publishes a Flow because the status of sync can change a lot.
extension PhotoGalleryGridView {
    class ViewModel: ObservableObject {
        @Published private(set) var needsHostingServiceLogin = false
        @Published private(set) var needsSetRootFolderHostingService = false
    //        @Published private(set) var syncStatus
        
        private var syncTask: Task<(), Never>? = nil
        
//        private let filesViewModel: FilesV
        
        func sync() {
            syncTask?.cancel()
            
            syncTask = Task {
//                do {
//                    try await
//                }
            }
        }
        
    }
}

struct DevicePhotosView_Previews: PreviewProvider {
    static var previews: some View {
        Text("foo")
    }
}
