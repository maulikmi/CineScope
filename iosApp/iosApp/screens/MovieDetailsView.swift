//
// Created by Maulik Togadiya on 25/10/26.
//

import Foundation
import SwiftUI
import Shared

struct MovieDetailsView: View {
    let movieId: Int32

    @StateObject private var viewModel = MovieDetailsViewModel()

    var body: some View {
        Group {
            if viewModel.isLoading && viewModel.movie == nil {
                VStack(spacing: 12) {
                    ProgressView()
                    Text("Loading movie...")
                        .foregroundStyle(.secondary)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)

            } else if let error = viewModel.error,
                      viewModel.movie == nil {
                VStack(spacing: 16) {
                    Text("⚠️")
                        .font(.system(size: 48))

                    Text(error)
                        .font(.body)
                        .multilineTextAlignment(.center)
                        .foregroundStyle(.secondary)

                    Button("Retry") {
                        viewModel.loadMovieDetails(movieID: movieId)
                    }
                    .buttonStyle(.borderedProminent)
                }
                .padding(24)
                .frame(maxWidth: .infinity, maxHeight: .infinity)

            } else if let movie = viewModel.movie {
                ScrollView {
                    VStack(alignment: .leading, spacing: 0) {
                        // Backdrop
                        if let backdropPath = movie.backdropPath,
                           let backdropURLString = TmdbImageUrlBuilder.shared.backdrop(path: backdropPath, size: "w780"),
                           let backdropURL = URL(string: backdropURLString) {
                            AsyncImage(url: backdropURL) { image in
                                image
                                    .resizable()
                                    .aspectRatio(contentMode: .fill)
                            } placeholder: {
                                Rectangle()
                                    .fill(Color.gray.opacity(0.2))
                                    .overlay {
                                        ProgressView()
                                    }
                            }
                            .frame(height: 220)
                            .clipped()
                        }

                        // Content
                        VStack(alignment: .leading, spacing: 20) {
                            // Poster + basic information
                            HStack(alignment: .top, spacing: 16) {
                                if let posterPath = movie.posterPath,
                                   let posterURLString = TmdbImageUrlBuilder.shared.poster(path: posterPath, size: "w500"),
                                   let posterURL = URL(string: posterURLString) {
                                    AsyncImage(url: posterURL) { image in
                                        image
                                            .resizable()
                                            .aspectRatio(contentMode: .fill)
                                    } placeholder: {
                                        Rectangle()
                                            .fill(Color.gray.opacity(0.2))
                                            .overlay {
                                                ProgressView()
                                            }
                                    }
                                    .frame(width: 120, height: 180)
                                    .cornerRadius(12)
                                    .clipped()
                                }

                                VStack(alignment: .leading, spacing: 6) {
                                    Text(movie.title)
                                        .font(.title3)
                                        .bold()

                                    if let tagline = movie.tagline,
                                       !tagline.trimmingCharacters(in: .whitespaces).isEmpty {
                                        Text("\"\(tagline)\"")
                                            .font(.subheadline)
                                            .italic()
                                            .foregroundStyle(.secondary)
                                    }

                                    HStack(spacing: 8) {
                                        if let releaseDate = movie.releaseDate,
                                           !releaseDate.trimmingCharacters(in: .whitespaces).isEmpty {
                                            Text(releaseDate)
                                                .font(.subheadline)
                                                .foregroundStyle(.secondary)
                                        }

                                        if let status = movie.status,
                                           !status.trimmingCharacters(in: .whitespaces).isEmpty {
                                            Text(status)
                                                .font(.caption2)
                                                .fontWeight(.bold)
                                                .foregroundStyle(.blue)
                                                .padding(.horizontal, 6)
                                                .padding(.vertical, 2)
                                                .background(Color.blue.opacity(0.15))
                                                .cornerRadius(4)
                                        }
                                    }

                                    HStack(spacing: 4) {
                                        Image(systemName: "star.fill")
                                            .font(.caption)
                                            .foregroundStyle(Color(red: 0xF5/255, green: 0xC5/255, blue: 0x18/255))

                                        Text(String(format: "%.1f", movie.rating))
                                            .font(.subheadline)
                                            .bold()

                                        Text("(\(movie.voteCount) votes)")
                                            .font(.caption)
                                            .foregroundStyle(.secondary)
                                    }

                                    if let runtime = movie.runtime {
                                        Text(RuntimeFormatter.shared.format(minutes: runtime))
                                            .font(.subheadline)
                                            .foregroundStyle(.secondary)
                                    }
                                }
                            }

                            // Genres
                            if !movie.genres.isEmpty {
                                ScrollView(.horizontal, showsIndicators: false) {
                                    HStack(spacing: 8) {
                                        ForEach(movie.genres, id: \.id) { genre in
                                            Text(genre.name)
                                                .font(.caption)
                                                .fontWeight(.medium)
                                                .padding(.horizontal, 12)
                                                .padding(.vertical, 6)
                                                .background(Color(.systemGray5))
                                                .clipShape(Capsule())
                                        }
                                    }
                                }
                            }

                            // Overview
                            VStack(alignment: .leading, spacing: 8) {
                                Text("Overview")
                                    .font(.headline)
                                    .bold()

                                let overviewText = movie.overview?.trimmingCharacters(in: .whitespacesAndNewlines)
                                Text((overviewText?.isEmpty == false) ? overviewText! : "No overview available.")
                                    .font(.body)
                                    .foregroundStyle(.secondary)
                            }

                            Divider()

                            // Key Information Grid
                            VStack(alignment: .leading, spacing: 12) {
                                Text("Key Information")
                                    .font(.headline)
                                    .bold()

                                HStack(spacing: 12) {
                                    InfoCardView(title: "Status", value: movie.status ?? "-")
                                    InfoCardView(title: "Language", value: movie.originalLanguage?.uppercased() ?? "-")
                                }

                                HStack(spacing: 12) {
                                    InfoCardView(title: "Budget", value: CurrencyFormatter.shared.format(amount: movie.budget))
                                    InfoCardView(title: "Revenue", value: CurrencyFormatter.shared.format(amount: movie.revenue))
                                }
                            }

                            // Production Companies
                            if !movie.productionCompanies.isEmpty {
                                Divider()

                                VStack(alignment: .leading, spacing: 12) {
                                    Text("Production Companies")
                                        .font(.headline)
                                        .bold()

                                    ScrollView(.horizontal, showsIndicators: false) {
                                        HStack(spacing: 12) {
                                            ForEach(movie.productionCompanies, id: \.id) { company in
                                                HStack(spacing: 8) {
                                                    if let logoPath = company.logoPath,
                                                       let logoURLString = TmdbImageUrlBuilder.shared.logo(path: logoPath, size: "w185"),
                                                       let logoURL = URL(string: logoURLString) {
                                                        AsyncImage(url: logoURL) { image in
                                                            image
                                                                .resizable()
                                                                .aspectRatio(contentMode: .fit)
                                                        } placeholder: {
                                                            EmptyView()
                                                        }
                                                        .frame(height: 24)
                                                    }

                                                    VStack(alignment: .leading, spacing: 2) {
                                                        Text(company.name)
                                                            .font(.subheadline)
                                                            .fontWeight(.medium)

                                                        if let country = company.originCountry, !country.isEmpty {
                                                            Text(country)
                                                                .font(.caption2)
                                                                .foregroundStyle(.secondary)
                                                        }
                                                    }
                                                }
                                                .padding(12)
                                                .background(Color(.systemGray6))
                                                .cornerRadius(12)
                                            }
                                        }
                                    }
                                }
                            }

                            // Production Countries
                            if !movie.productionCountries.isEmpty {
                                Divider()

                                VStack(alignment: .leading, spacing: 8) {
                                    Text("Production Countries")
                                        .font(.headline)
                                        .bold()

                                    ScrollView(.horizontal, showsIndicators: false) {
                                        HStack(spacing: 8) {
                                            ForEach(movie.productionCountries, id: \.iso31661) { country in
                                                Text(country.name)
                                                    .font(.footnote)
                                                    .padding(.horizontal, 10)
                                                    .padding(.vertical, 6)
                                                    .background(Color(.systemGray6))
                                                    .cornerRadius(8)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        .padding(16)
                    }
                }

            } else {
                VStack(spacing: 12) {
                    ProgressView()
                    Text("Loading movie...")
                        .foregroundStyle(.secondary)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
        }
        .navigationTitle("Movie Details")
        .navigationBarTitleDisplayMode(.inline)
        .task {
            let observationTask = Task {
                await viewModel.observeState()
            }

            viewModel.loadMovieDetails(movieID: movieId)

            await observationTask.value
        }.onDisappear(){
            viewModel.clear()
        }
    }
}

struct InfoCardView: View {
    let title: String
    let value: String

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.caption)
                .foregroundStyle(.secondary)
            Text(value)
                .font(.body)
                .bold()
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(12)
        .background(Color(.systemGray6))
        .cornerRadius(12)
    }
}
