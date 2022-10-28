//
//  TextExtensions.swift
//  iosApp
//
//  Created by Levi Bostian on 10/26/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension View {
    // Used primarily for Text to make sure the text doesn't get cut off. 
    func removeEllipsis() -> some View {
        return self.fixedSize(horizontal: false, vertical: true)
    }
}
