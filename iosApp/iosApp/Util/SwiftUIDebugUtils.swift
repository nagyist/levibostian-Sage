//
//  SwiftUIDebugUtils.swift
//  iosApp
//
//  Created by Levi Bostian on 8/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

// some handy functions to help debug SwiftUI views.
// Inspiration from: https://www.swiftbysundell.com/articles/building-swiftui-debugging-utilities/
extension View {
    func xray() -> some View {
        return border(Color.random, width: 1)
    }
}
