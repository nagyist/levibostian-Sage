//
//  ColorExtension.swift
//  iosApp
//
//  Created by Levi Bostian on 8/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension Color {
    static var random: Color {
        let colors: [Color] = [
            .purple,
            .blue,
            .red,
            .green,
            .orange,
            .pink,
            .yellow
        ]
        
        return colors.randomElement()!
    }
}
