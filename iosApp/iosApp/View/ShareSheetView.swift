//
//  ShareSheetView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/20/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import UIKit

struct ShareSheetView: UIViewControllerRepresentable {
    
    let imageToShare: UIImage
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<ShareSheetView>) -> UIActivityViewController {
        let controller = UIActivityViewController(activityItems: [imageToShare],
                                                  applicationActivities: nil)
        
        return controller
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: UIViewControllerRepresentableContext<ShareSheetView>) {}
}
