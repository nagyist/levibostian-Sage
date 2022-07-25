import SwiftUI
import shared

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
	var body: some Scene {
		WindowGroup {
            ContentView()
                .onOpenURL { url in
                    let handledByDropbox = DiGraph.shared.iosHostingService.handleUrlOpened(url: url)
                    
                    if !handledByDropbox {
                        // we handle it ourselves.
                    }
                }
        }
	}
}
