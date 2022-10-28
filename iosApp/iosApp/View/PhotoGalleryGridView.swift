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
import Combine

struct PhotoGalleryGridView: View {
//    @StateObject var viewModel = PhotosViewModel()
    @StateObject var newViewModel = ViewModel()
    
    @State var gridColumns = [GridItem(), GridItem(), GridItem()]
    private let gridItemSpacing: Double = 10
    @State private var isShowingHostingServiceAuthFlow = false
    
    var body: some View {
        GeometryReader { geoReader in
            NavigationView {
                VStack {
                    ScrollView {
//                        LazyVGrid(columns: gridColumns, spacing: gridItemSpacing) {
//                            ForEach(viewModel.deviceImages) { photo in
//                                NavigationLink(destination: SinglePhotoView(photo: photo)) {
//                                    PhotoImageView(photo: photo, width: (Double(geoReader.size.width) / Double(gridColumns.count)) - gridItemSpacing / 2)
//                                }
//                            }
//                        }
                    }.onAppear {
//                        if (viewModel.needPermissionToAccessLocalPhotos) {
//                            viewModel.fetchSamplePhotos()
//                        } else {
//                            viewModel.fetchLocalPhotos()
//                        }
                        
                        newViewModel.sync()
                    }.navigationBarTitle("Gallery", displayMode: .large)
                    
                    HostingServiceCTA(
                        ctaDetails: HostingServiceCTA.CTADetails(unauthorizedButtonText: "login!", unauthorizedDescription: "ooops you need to login"),
                        syncStatus: $newViewModel.syncStatus
                    )
                }
            }
        }
    }
}

extension PhotoGalleryGridView {
    class ViewModel: ObservableObject {
        @Published var syncStatus = FilesRepositorySyncResult.companion.none()
        
        private let filesViewModel: FilesViewModel
        
        private var cancellables = [AnyCancellable]()
        
        init(filesViewModel: FilesViewModel = DiGraph.shared.filesViewModel) {
            self.filesViewModel = filesViewModel
            
            startObservingSyncState()
        }
        
        private func startObservingSyncState() {
            createPublisher(for: filesViewModel.syncStateNative)
                .sinkMainReceive { value in
                    self.syncStatus = value
                }.store(in: &cancellables)
        }
        
        // called from UI when it's ready
        func sync() {
            filesViewModel.startSync()
        }
    }
}

struct DevicePhotosView_Previews: PreviewProvider {
    static var previews: some View {
        Text("foo")
    }
}
