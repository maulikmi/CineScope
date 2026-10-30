//
// Created by Mindinventory on 30/09/26.
//

import Foundation
import Shared
import SwiftUI

struct SearchView: View {

    @StateObject private var viewModel = SearchViewModel()

    var body: some View {
        NavigationStack {
            VStack(spacing: 12) {

                searchField
                    .padding(.horizontal, 16)
                    .padding(.top, 8)

                Group {
                    if viewModel.isLoading && viewModel.movies.isEmpty {
                        loadingView

                    } else if let error = viewModel.error,
                              viewModel.movies.isEmpty {
                        errorView(error)

                    } else if viewModel.hasSearched &&
                                viewModel.movies.isEmpty {
                        emptyResultsView

                    } else if !viewModel.movies.isEmpty {
                        resultsView

                    } else {
                        initialView
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            .navigationTitle("Search Movies")
            .navigationBarTitleDisplayMode(.large)
        }
        .task(id: viewModel.query) {
            let currentQuery = viewModel.query

            do {
                try await Task.sleep(
                    nanoseconds: 400_000_000
                )
            } catch {
                return
            }

            guard !Task.isCancelled else { return }

            viewModel.search(currentQuery)
        }
    }

    private var searchField: some View {
        HStack(spacing: 10) {
            Image(systemName: "magnifyingglass")
                .foregroundStyle(.secondary)

            TextField(
                "Search by movie title",
                text: $viewModel.query
            )
            .autocorrectionDisabled()

            if !viewModel.query.isEmpty {
                Button {
                    viewModel.query = ""
                } label: {
                    Image(systemName: "xmark.circle.fill")
                        .foregroundStyle(.secondary)
                }
            }
        }
        .padding(12)
        .background(
            Color(uiColor: .secondarySystemBackground)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    private var loadingView: some View {
        ProgressView("Searching movies...")
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private var initialView: some View {
        ContentUnavailableView(
            "Search Movies",
            systemImage: "magnifyingglass",
            description: Text("Search for your favourite movies")
        )
    }

    private var emptyResultsView: some View {
        ContentUnavailableView(
            "No Movies Found",
            systemImage: "film",
            description: Text("Try searching with another title")
        )
    }

    private func errorView(_ message: String) -> some View {
        ContentUnavailableView {
            Label("Something went wrong", systemImage: "exclamationmark.triangle")
        } description: {
            Text(message)
        } actions: {
            Button("Retry") {
                viewModel.retry()
            }
            .buttonStyle(.borderedProminent)
        }
    }

    private var resultsView: some View {
        List {
            ForEach(viewModel.movies, id: \.id) { movie in
                NavigationLink {
                    MovieDetailsView(movieId: movie.id)
                } label: {
                    MovieRowView(movie: movie)
                }
                .onAppear {
                    if movie.id == viewModel.movies.last?.id {
                        viewModel.loadNextPage()
                    }
                }
            }

            if viewModel.isLoadingMore {
                HStack {
                    Spacer()

                    ProgressView()

                    Spacer()
                }
                .padding(.vertical, 16)
                .listRowSeparator(.hidden)
            }
        }
        .listStyle(.plain)
    }
}
