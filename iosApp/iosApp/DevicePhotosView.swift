//
//  DevicePhotosView.swift
//  iosApp
//
//  Created by Levi Bostian on 8/8/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct DevicePhotosView: View {
    @StateObject var viewModel = FoldersViewModel()
    
    var body: some View {
        Text("device photos go here")
    }
}
