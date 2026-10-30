//
// Created by Maulik Togadiya on 30/10/26.
//

import Foundation
import Combine
import Shared

@MainActor
final class SearchViewModel: ObservableObject {

    @Published var query: String = ""
    @Published var movies: [Movie] = []
    @Published var isLoading = false
    @Published var isLoadingMore = false
    @Published var error: String?
    @Published var hasSearched = false

    private let stateHolder: SearchStateHolder
    private var observationTask: Task<Void, Never>?

    init() {
        self.stateHolder = IosDependenciesKt.getSearchStateHolder()
        observeState()
    }

    private func observeState() {
        observationTask = Task {
            for await state in stateHolder.uiState {
                guard !Task.isCancelled else { return }

                self.query = state.query
                self.movies = state.movies
                self.isLoading = state.isLoading
                self.isLoadingMore = state.isLoadingMore
                self.error = state.error
                self.hasSearched = state.hasSearched
            }
        }
    }

    func search(_ query: String) {
        stateHolder.search(query: query, force: false)
    }

    func loadNextPage() {
        stateHolder.loadNextPage()
    }

    func retry() {
        stateHolder.retry()
    }

    deinit {
        observationTask?.cancel()
        stateHolder.clear()
    }
}