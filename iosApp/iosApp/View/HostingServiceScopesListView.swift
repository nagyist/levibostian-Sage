//
//  HostingServiceScopesListView.swift
//  iosApp
//
//  Created by Levi Bostian on 10/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct HostingServiceScopesListView: View {
    
    let scopesToAskFor: [HostingServicePermissionScope]
    
    @EnvironmentObject var isDoneWithAuthFlow: HostingServiceAuthFlow.Result
    
    var body: some View {
        VStack {
            Text("here, we are describing permissions for transparency reasons.")
            
            List(scopesToAskFor, id: \.self) { scope in
                Text("Here is a scope: \(scope)")
            }
            
            Button("Login with Dropbox") {
                DiGraph.shared.iosHostingService.authorize(scopes: self.scopesToAskFor, application: UIApplication.shared, dropboxLoginViewController: DropboxLoginViewController())
            }
        }
    }
}

struct HostingServiceScopesListView_Previews: PreviewProvider {
    static var previews: some View {
        HostingServiceScopesListView(scopesToAskFor: [HostingServicePermissionScope.readFiles])
    }
}
