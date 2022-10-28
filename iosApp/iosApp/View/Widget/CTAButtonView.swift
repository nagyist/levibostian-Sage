//
//  CTAButtonView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct CTAButtonView: View {
    let buttonText: String
    let descriptionText: String
    let onCtaPressed: () -> Void
    
    init(buttonText: String, descriptionText: String, onCtaPressed: @escaping () -> Void) {
        self.buttonText = buttonText
        self.descriptionText = descriptionText
        self.onCtaPressed = onCtaPressed
    }

    var body: some View {
        HStack {
            Text(self.descriptionText)
            RoundedCornerButton(title: self.buttonText) {
                self.onCtaPressed()
            }
        }
    }
}
