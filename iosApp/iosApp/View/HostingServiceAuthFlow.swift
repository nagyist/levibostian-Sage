//
//  HostingServiceAuthFlow.swift
//  iosApp
//
//  Created by Levi Bostian on 10/9/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

/**
 View that houses all of the steps to update authentication for a hosting service.
 
 Pass in scopes that you need, the hosting service you want to connect to and View walks user through the rest.
 */
struct HostingServiceAuthFlow: View {
    
    @State var callToActionButtonPressed = false
    @Environment(\.presentationMode) private var dismissAuthFlowModal
    
    var body: some View {
        NavigationView { // only exists or toolbar will not be visible
            VStack {
                if (callToActionButtonPressed) {
                    HostingServiceScopesListView {
                        dismissAuthFlowModal.wrappedValue.dismiss()
                    }
                } else {
                    VStack {
                        Text("Unlock more features").font(.title)
                            
                            List {
                                ListItem(title: "Backup", icon: "icloud.and.arrow.up", description: "If you ever lose your device, you might lose your photos and videos.")
                                ListItem(title: "Share", icon: "square.and.arrow.up", description: "Share higher quality (higher resolution) photos/videos or limit the length of time someone gets access to photos/video.")
                                ListItem(title: "Organize", icon: "photo.on.rectangle.angled", description: "Organize all of your photos/video no matter what device they are on.")
                                ListItem(title: "Private", icon: "lock", description: "Instead of storing your photos/videos in the cloud of privacy invasive company, store them somewhere that you control.")
                            }
                            
                            Text("To unlock these features, login to your Dropbox account.")
                        
                            RoundedCornerButton(title: "Let's unlock these features!") {
                                callToActionButtonPressed = true
                            }
                    }
                }
            }.toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: {
                        dismissAuthFlowModal.wrappedValue.dismiss()
                    }) {
                        Image(systemName: "xmark.circle.fill")
                    }
                }
            }
            .navigationBarTitleDisplayMode(.inline) // this is a hack to make the navigation bar smaller in size. it was too high taking up too much space on the screen.
        }
    }
    
    struct ListItem: View {
        let title: String
        let icon: String
        let description: String
        
        var body: some View {
            VStack(alignment: .leading) {
                Label(title, systemImage: icon).font(.title2).padding(.bottom, 2)
                Text(description).padding(.leading, 40).removeEllipsis()
            }.padding(.vertical, 10)
        }
    }
}

struct HostingServiceAuthFlow_Previews: PreviewProvider {
    static var previews: some View {
        HostingServiceAuthFlow()
    }
}
