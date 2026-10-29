//
// Created by Maulik Togadiya on 29/10/26.
//

import Foundation
import Shared


// MARK: - Swift ViewModel wrapping KMP StateHolder

@MainActor
final class MovieViewModel: ObservableObject {

    @Published private(set) var movies: [Movie] = []
    @Published private(set) var isLoading = false
    @Published private(set) var isRefreshing = false
    @Published private(set) var isLoadingMore = false
    @Published private(set) var error: String?
    @Published private(set) var paginationError: String?

    private let stateHolder: MovieStateHolder
    private var hasStartedInitialLoad = false

    init(
        stateHolder: MovieStateHolder =
        IosDependenciesKt.getMovieStateHolder()
    ) {
        self.stateHolder = stateHolder
    }

    func ensureLoaded() {
        guard !hasStartedInitialLoad else { return }
        hasStartedInitialLoad = true
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

    func retryInitialLoad() {
        hasStartedInitialLoad = true
        stateHolder.loadPopularMovies()
    }

    func observeState() async {
        for await state in stateHolder.uiState {
            guard !Task.isCancelled else { return }

            movies = state.movies
            isLoading = state.isLoading
            isRefreshing = state.isRefreshing
            isLoadingMore = state.isLoadingMore
            error = state.error
            paginationError = state.paginationError
        }
    }

    deinit {
        stateHolder.clear()
    }
}

