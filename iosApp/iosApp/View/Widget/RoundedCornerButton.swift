//
//  RoundedCornerButton.swift
//  iosApp
//
//  Created by Levi Bostian on 10/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct RoundedCornerButton: View {
    let title: String
    let action: () -> Void
    
    var body: some View {
        Button(action: {
            self.action()
        }) {
            Text(self.title)
                .foregroundColor(.white)
        }
        .roundCorners()
        .setBackgroundColor(.accentColor)
    }
}
