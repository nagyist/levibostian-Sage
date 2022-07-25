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

class DropboxLoginViewController: UIViewController {
    
    private var shownDropboxLogin = false
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
                
        if (shownDropboxLogin) {
            self.dismiss(animated: false)
            return
        }
            
        shownDropboxLogin = true
        DiGraph.shared.iosHostingService.login(application: UIApplication.shared, viewController: self)
    }
}

struct DropboxLoginViewControllerPresentable: UIViewControllerRepresentable {
    typealias UIViewControllerType = DropboxLoginViewController
    
    func makeUIViewController(context: Context) -> DropboxLoginViewController {
        DropboxLoginViewController()
    }
    
    func updateUIViewController(_ uiViewController: DropboxLoginViewController, context: Context) {}
}
