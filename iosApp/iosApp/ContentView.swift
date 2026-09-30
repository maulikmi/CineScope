import Shared
import SwiftUI

// MARK: - Swift ViewModel wrapping KMP StateHolder
@MainActor
class MovieViewModel: ObservableObject {

    @Published private(set) var movies: [Movie] = []
    @Published private(set) var isLoading = false
    @Published private(set) var isRefreshing = false
    @Published private(set) var isLoadingMore = false
    @Published private(set) var error: String?
    @Published private(set) var paginationError: String?

    private let stateHolder: MovieStateHolder

    init(stateHolder: MovieStateHolder = IosDependenciesKt.getMovieStateHolder()) {
        self.stateHolder = stateHolder
    }

    func loadMovies() {
        stateHolder.loadPopularMovies()
    }

    func refreshMovies() {
        stateHolder.refreshMovies()
    }

    func loadNextPage() {
        stateHolder.loadNextPage()
    }

    func retryLoadNextPage() {
        stateHolder.retryLoadNextPage()
    }

    func observeState() async {
        for await state in stateHolder.uiState {
            movies = state.movies
            isLoading = state.isLoading
            isRefreshing = state.isRefreshing
            isLoadingMore = state.isLoadingMore
            error = state.error
            paginationError = state.paginationError
        }
    }
}


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
