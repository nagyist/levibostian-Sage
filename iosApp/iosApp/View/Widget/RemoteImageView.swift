//
//  RemoteImageView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/11/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import NukeUI
import shared
import SwiftUI

// Displays an image from the Internet
// Wrapper around some other library so it can be swapped at a later time.
struct RemoteImageView: View {
    let photo: Photo
    let targetSize: CGSize
    
    var body: some View {
        if let remoteImageUrl = photo.remotePhotoUrl,
        let url = URL(string: remoteImageUrl) {
            let remoteImageView = LazyImage(url: url) { state in
                if let image = state.image {
                    image.resizingMode(.center)
                    
                    image
                } else if state.error != nil {
                    Color.galleryPlaceholder // Indicates an error
                } else {
                    Color.galleryPlaceholder // Acts as a placeholder
                }
            }
            
            remoteImageView.frame(width: targetSize.width, height: targetSize.height)
        }
    }
}
