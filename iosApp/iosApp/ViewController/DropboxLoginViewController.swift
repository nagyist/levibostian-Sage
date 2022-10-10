//
//  DropboxLoginViewController.swift
//  iosApp
//
//  Created by Levi Bostian on 7/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared
import SwiftUI

// You only want to use this ViewController when you want to show the Login UI for Dropbox login.
//
// How to use:
// - Populate the ViewController with properties such as scopes you want to ask for authorization
// - Call the Dropbox SDK which will display this ViewController.
class DropboxLoginViewController: UIViewController {
    
//    var scopesToRequest: [HostingServicePermissionScope] = []
    
    private var shownDropboxLogin = false
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
                
        if (shownDropboxLogin) {
            self.dismiss(animated: false)
            return
        }
            
        shownDropboxLogin = true
//        DiGraph.shared.iosHostingService.authorize(scopes: self.scopesToRequest)
    }
}

//struct DropboxLoginViewControllerPresentable: UIViewControllerRepresentable {
//    typealias UIViewControllerType = DropboxLoginViewController
//
//    func makeUIViewController(context: Context) -> DropboxLoginViewController {
//        DropboxLoginViewController()
//    }
//
//    func updateUIViewController(_ uiViewController: DropboxLoginViewController, context: Context) {}
//}
