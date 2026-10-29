//
// Created by Maulik Togadiya on 25/10/26.
//


import Foundation
import SwiftUI
import Shared

@MainActor
final class MovieDetailsViewModel: ObservableObject {

    @Published private(set) var movie: MovieDetails?
    @Published private(set) var isLoading = false
    @Published private(set) var error: String?

    private let stateHolder: MovieDetailsStateHolder
    private var requestedMovieID: Int32?

    init(
        stateHolder: MovieDetailsStateHolder =
        IosDependenciesKt.getMovieDetailsStateHolder()
    ) {
        self.stateHolder = stateHolder
    }

    func loadMovieDetails(movieID: Int32) {
        guard requestedMovieID != movieID else { return }

        requestedMovieID = movieID
        stateHolder.loadMovieDetails(movieID: movieID)
    }

    func retryMovieDetails(movieID: Int32) {
        requestedMovieID = movieID
        stateHolder.loadMovieDetails(movieID: movieID)
    }

    func observeState() async {
        for await state in stateHolder.uiState {
            guard !Task.isCancelled else { return }

            movie = state.movie
            isLoading = state.isLoading
            error = state.error
        }
    }

    func clear() {
        stateHolder.clear()
    }
}
