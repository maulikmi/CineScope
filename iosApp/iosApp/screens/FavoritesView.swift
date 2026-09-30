//
// Created by Mindinventory on 30/09/26.
//

import Foundation
import SwiftUI

struct FavoritesView: View {
    var body: some View {
        NavigationStack {
            VStack {
                Text("Favorites")
                    .font(.largeTitle)
                    .fontWeight(.semibold)
            }
            .navigationTitle("Favorites")
        }
    }
}