//
//  ErrorWithMessage.swift
//  iosApp
//
//  Created by Levi Bostian on 7/23/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation

class ErrorWithMessage: Error {
    
    private let message: String
    
    init(message: String) {
        self.message = message
    }
    
    var localizedDescription: String {
        return self.message
    }
}
