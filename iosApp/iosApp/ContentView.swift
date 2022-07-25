import SwiftUI
import Combine
import shared

struct ContentView: View {
    @StateObject var viewModel = FoldersViewModel(repository: DiGraph.shared.filesRepository)

    var body: some View {
            VStack {
                TabView {
                    PeopleListView(viewModel: viewModel)
                        .tabItem {
                            Label("People", systemImage: "person")
                        }
                }.onAppear {
                    viewModel.updateFolderContentsFromRemote(path: "/Photos")
                }.fullScreenCover(isPresented: $viewModel.needsAuthorization) {
                    DropboxLoginViewControllerPresentable()
                }
            }
    }
}

struct PeopleListView: View {
    @ObservedObject var viewModel: FoldersViewModel
    
    var body: some View {
        NavigationView {
            List(viewModel.folders, id: \.name) { person in
                PersonView(viewModel: viewModel, folder: person)
            }
        }
    }
}

struct PersonView: View {
    var viewModel: FoldersViewModel
    var folder: Folder
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(folder.name).font(.headline)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
