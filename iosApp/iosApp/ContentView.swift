import Shared
import SwiftUI


// MARK: - SwiftUI View
struct ContentView: View {
    var body: some View {
        TabView {
            HomeView()
                .tabItem {
                    Label(
                        "Home",
                        systemImage: "house.fill"
                    )
                }
            SearchView()
                .tabItem {
                    Label(
                        "Search",
                        systemImage: "magnifyingglass"
                    )
                }
            FavoritesView()
                .tabItem {
                    Label(
                        "Favorites",
                        systemImage: "heart.fill"
                    )
                }
        }
    }
}
