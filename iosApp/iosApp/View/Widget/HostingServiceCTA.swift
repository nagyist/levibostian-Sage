//
//  HostingServiceCTA.swift
//  iosApp
//
//  Created by Levi Bostian on 10/20/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

/**
 Duties of View:
 1. Dynamically shows/hides itself based on call to actions in the app for using a cloud hosting service.
 2. View's Call to Action changes based on what screen you're viewing. This makes context better for end user. Let's say you need
    to update scope for Dropbox to share a photo. The Call to Action to add this scope could be, "You cannot share this photo without updating your Dropbox login settings".
 */
struct HostingServiceCTA: View {
    struct CTADetails {
        let unauthorizedButtonText: String
        let unauthorizedDescription: String
    }
    
    let ctaDetails: CTADetails
    
    @Binding var syncStatus: FilesRepositorySyncResult
    @State var showAuthFlowModal = false
    
    var body: some View {
        if (syncStatus.unauthorized) {
            CTAButtonView(buttonText: self.ctaDetails.unauthorizedButtonText, descriptionText: self.ctaDetails.unauthorizedDescription) {
                showAuthFlowModal = true
            }
            .sheet(isPresented: $showAuthFlowModal) {
                HostingServiceAuthFlow().accentColor(Color("AccentColor"))
            }
        }
    }
}
