//
//  EventBus.swift
//  iosApp
//
//  Created by Levi Bostian on 10/28/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

enum SageNotifiction: String {
    case cloudLoginStatusUpdate
}

extension NotificationCenter {
    func post(_ sageNotification: SageNotifiction, object: Any? = nil) {
        self.post(name: NSNotification.Name(sageNotification.rawValue), object: nil)
    }
    
    func addObserver(_ observer: Any, selector: Selector, notification: SageNotifiction) {
        self.addObserver(observer, selector: selector, name: NSNotification.Name(notification.rawValue), object: nil)
    }
}
