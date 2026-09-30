//
// Created by Mindinventory on 30/09/26.
//

import Foundation
import SwiftUI

struct SearchView : View{
    var body : some View{
        NavigationStack{
            VStack {
                Text("Search")
                    .font(.largeTitle)
                    .fontWeight(.semibold)
            }
            .navigationTitle("Search")
        }
    }
}