import SwiftUI
import Combine
import shared

struct ContentView: View {
    @State private var selectedScreen = Screen.devicePhotos
    
    enum Screen: String, CaseIterable, Identifiable {
        case devicePhotos, cloudPhotos
        var id: Self { self }
    }

    var body: some View {
            VStack {
                Picker("What photos do you want to browse?", selection: $selectedScreen, content: {
                    Text("Device").tag(Screen.devicePhotos)
                    Text("Cloud").tag(Screen.cloudPhotos)
                })
                .pickerStyle(SegmentedPickerStyle())
                .padding(.horizontal, 10.0)
                
                if (selectedScreen == Screen.devicePhotos) {
                    DevicePhotosView()
                } else {
                    CloudPhotosView()
                }
            }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
