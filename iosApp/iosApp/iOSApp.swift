import SwiftUI
import Shared

@main
struct iOSApp: App {

    init(){
        print(TmdbConfiguration.token)
        KoinInitializerKt.doInitKoin(tmdbToken: TmdbConfiguration.token)

    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
