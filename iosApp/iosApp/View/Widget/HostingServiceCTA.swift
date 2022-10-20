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
 1. Dynamically changes based on call to actions in the app for using a cloud hosting service.
 2. 
 */
struct HostingServiceCTA: View {
    struct CTADetails {
        let unauthorizedButtonText: String
        let unauthorizedDescription: String
    }
    
    let ctaDetails: CTADetails
    
    @Binding var syncStatus: FilesRepositorySyncResult
    @EnvironmentObject var authFlowResult: HostingServiceAuthFlow.Result
    
    var body: some View {
        if (syncStatus.unauthorized) {
            NavigationLink(
                destination:
                    HostingServiceAuthFlow(scopesToAskFor: [HostingServicePermissionScope.readFiles])
                    .environmentObject(authFlowResult),
                isActive: $authFlowResult.isDoneWithAuthFlow) {
                    CTAButtonView(buttonText: self.ctaDetails.unauthorizedButtonText, descriptionText: self.ctaDetails.unauthorizedDescription) {
                        authFlowResult.isDoneWithAuthFlow = true
                }
            }
        }
    }
}
