//
//  HostingServiceScopesListView.swift
//  iosApp
//
//  Created by Levi Bostian on 10/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

/**
 This screen is meant to list all of the scopes that are available with the ability to enable and disable them. you choose what permissions the app gets. Well, not exactly list the scopes but list all of the fetures that each permission uses.
 Read files will enable feature to view all files in app.
 Write files will enable organizing into albums.
 Share files will enable sharing photos with people.
 
 We know it's scope, but we instead present it as features.
 
 What is the purpose?
 1. we want to give you the feeling that you are in control.
 2. we want to not annoy you. I was at first going to have you login X number of times where each time you wanted to use a feature we would login again for adding that scope. But I figured that could be annoying to login X number of times when some people would rather just enable all of the fetures they want to use and not have to login.
 
 
 This is a feature that doesn't need to exist when the app launches. We can build it later. So, for now, it's being skipped.
 */
struct HostingServiceScopesListView: View {
        
    @State private var isReadPermissionChecked = true
    @State private var isWritePermissionChecked = true
    @State private var isSharePermissionChecked = true
    
    let onLeaveForLoginAction: () -> Void
    
    var body: some View {
        VStack {
//            Text("here, we are describing permissions for transparency reasons.")
//            Text("enable scopes that you want")
//
//            List {
//                ListItem(isChecked: $isReadPermissionChecked, title: "Read files", description: "Read dem files")
//                ListItem(isChecked: $isWritePermissionChecked, title: "Write files", description: "Write dem files")
//                ListItem(isChecked: $isSharePermissionChecked, title: "Share", description: "Share dem files")
//            }
//
//            Button("Login with Dropbox") {
//                var scopesToAskFor: [HostingServicePermissionScope] = []
//                if (isReadPermissionChecked) { scopesToAskFor.append(HostingServicePermissionScope.readFiles) }
//                if (isWritePermissionChecked) { scopesToAskFor.append(HostingServicePermissionScope.writeFiles) }
//                if (isSharePermissionChecked) { scopesToAskFor.append(HostingServicePermissionScope.shareFiles) }
//
//                DiGraph.shared.iosHostingService.authorize(scopes: scopesToAskFor, application: UIApplication.shared, dropboxLoginViewController: DropboxLoginViewController())
//            }
        }.onAppear {
            // for now, skip this screen and just leave.
            let scopesToAskFor: [HostingServicePermissionScope] = [.readFiles, .writeFiles, .shareFiles]
            DiGraph.shared.iosHostingService.authorize(scopes: scopesToAskFor, application: UIApplication.shared, dropboxLoginViewController: DropboxLoginViewController())
            
            onLeaveForLoginAction()
        }
    }
    
    struct ListItem: View {
        @Binding var isChecked: Bool
        
        let title: String
        let description: String
        
        var body: some View {
            HStack {
                Toggle(isOn: $isChecked) {}.labelsHidden()
                VStack(alignment: .leading) {
                    Text(title).font(.title2).padding(.bottom, 2)
                    Text(description).removeEllipsis()
                }
            }
            .padding(.vertical, 10)
        }
    }
}

struct HostingServiceScopesListView_Previews: PreviewProvider {
    static var previews: some View {
        HostingServiceScopesListView(onLeaveForLoginAction: {})
    }
}
