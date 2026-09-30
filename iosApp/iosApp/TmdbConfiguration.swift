//
//  TmdbConfiguration.swift
//  iosApp
//
//  Created by Mindinventory on 25/09/26.
//

import Foundation

enum TmdbConfiguration {

    static var token: String {
        guard let token = Bundle.main.object(
            forInfoDictionaryKey: "TMDB_TOKEN"
        ) as? String,
        !token.isEmpty else {
            fatalError("TMDB_TOKEN is missing")
        }

        return token
    }
}
