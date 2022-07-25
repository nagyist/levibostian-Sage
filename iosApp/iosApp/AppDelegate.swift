//
//  AppDelegate.swift
//  iosApp
//
//  Created by Levi Bostian on 7/24/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        DiGraph.shared.iosHostingService.initialize()
        
        return true
    }
    

    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        let handledByDropbox = DiGraph.shared.iosHostingService.handleUrlOpened(url: url)
        
        if !handledByDropbox {
            // we handle it ourselves.
        }
        
        return handledByDropbox
    }
}
