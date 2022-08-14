//
//  ImageExtension.swift
//  iosApp
//
//  Created by Levi Bostian on 8/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension Image {
    func centerCropped(width: CGFloat, height: CGFloat? = nil) -> some View {
        let height = height ?? width
        
        return self
            .resizable()
            .scaledToFill()
            .frame(width: width, height: height)
            .clipped()
    }
}

