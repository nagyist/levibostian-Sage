//
//  HostingServiceAuthFlow.swift
//  iosApp
//
//  Created by Levi Bostian on 10/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HostingServiceAuthFlow: View {
    
    let scopesToAskFor: [HostingServicePermissionScope]
    @State var callToActionButtonPressed = false
    
    @EnvironmentObject var isDoneWithAuthFlow: Result
    
    var body: some View {
//        NavigationView {
            VStack {
                // First, show View that describes this cool feature and all the great hings you can do! backup, view photos, create albums, etc. CTA to login to Dropbox.
                // next screen, we show list of permissions we will ask for and why. CTA to
                
                Text("Cloud service! Neat feature!")
                Text("Here are the cool features and why you want to enable this feature.")
                
                NavigationLink(destination:
                                HostingServiceScopesListView(scopesToAskFor: self.scopesToAskFor).environmentObject(isDoneWithAuthFlow),
                               isActive: $callToActionButtonPressed) {
                    Button("Let's do it!") {
                        callToActionButtonPressed = true
                    }
                }
            }
//        }
    }
    
    class Result: ObservableObject {
        @Published var isDoneWithAuthFlow = false
    }
}

struct HostingServiceAuthFlow_Previews: PreviewProvider {
    static var previews: some View {
        HostingServiceAuthFlow(scopesToAskFor: [])
    }
}
