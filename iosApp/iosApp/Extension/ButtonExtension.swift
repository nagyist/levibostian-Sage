//
//  ButtonExtension.swift
//  iosApp
//
//  Created by Levi Bostian on 8/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension View {
    func roundCorners(radius: CGFloat = 8) -> some View {
        return self
            .cornerRadius(radius)
            .padding(EdgeInsets(
                top: 12,
                leading: 12,
                bottom: 12,
                trailing: 12)
            )
    }
    
    func setBackgroundColor(_ color: Color) -> some View {
        return background(AnyView(Capsule().fill(color)))
    }
}
