//
// Created by Mindinventory on 30/09/26.
//

import Foundation
import SwiftUI
import Shared



struct HomeView: View {
    @StateObject private var viewModel = MovieViewModel()

    var body: some View {
        NavigationStack {
            Group {
                if viewModel.isLoading && viewModel.movies.isEmpty {
                    ProgressView("Loading popular movies...")
                } else if let error = viewModel.error, viewModel.movies.isEmpty {
                    VStack(spacing: 16) {
                        Image(systemName: "exclamationmark.triangle")
                            .font(.largeTitle)
                            .foregroundColor(.red)
                        Text(error)
                            .multilineTextAlignment(.center)
                            .foregroundColor(.secondary)
                        Button("Retry") {
                            viewModel.retryInitialLoad()
                        }
                        .buttonStyle(.borderedProminent)
                    }
                    .padding()
                } else {

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

                        } else if let error = viewModel.paginationError {

                            VStack(spacing: 8) {
                                Text(error)
                                    .font(.subheadline)
                                    .foregroundStyle(.red)
                                    .multilineTextAlignment(.center)

                                Button("Retry") {
                                    viewModel.retryLoadNextPage()
                                }
                                .buttonStyle(.borderedProminent)
                            }
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 16)
                            .listRowSeparator(.hidden)
                        }
                    }
                    .listStyle(.plain)
                    .refreshable {
                        viewModel.refreshMovies()
                    }
                }
            }
            .navigationTitle("Popular Movies")
            .task {
                await viewModel.observeState()
            }
            .onAppear {
                if viewModel.movies.isEmpty {
                    viewModel.ensureLoaded()
                }
            }
        }
    }
}

struct MovieRowView: View {
    let movie: Movie

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            if let posterPath = movie.posterPath {
                let imageURL = TmdbImageUrlBuilder.shared.poster(path: posterPath, size: "w500")
                if let url = URL(string: imageURL ?? "") {
                    AsyncImage(url: url) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    } placeholder: {
                        Rectangle()
                            .fill(Color.gray.opacity(0.3))
                            .overlay {
                                ProgressView()
                            }
                    }
                    .frame(width: 70, height: 100)
                    .cornerRadius(8)
                    .clipped()
                }
            }


            VStack(alignment: .leading, spacing: 6) {
                Text(movie.title)
                    .font(.headline)
                    .lineLimit(2)

                if let overview = movie.overview, !overview.isEmpty {
                    Text(overview)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .lineLimit(3)
                }

                HStack {
                    Image(systemName: "star.fill")
                        .foregroundColor(.yellow)
                        .font(.caption)
                    Text(String(format: "%.1f", movie.rating))
                        .font(.caption)
                        .bold()
                }
                .padding(.top, 2)
            }
        }
        .padding(.vertical, 4)
    }
}
