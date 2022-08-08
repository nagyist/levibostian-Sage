object Versions {
    const val sqlDelight = "1.5.3"
    const val androidJetpackNavigation = "2.5.1"
}

object Deps {
    // meant for gradle plugins
    object Gradle {
        const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
        const val androidJetpackNavigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidJetpackNavigation}"
    }

    object SqlDelight {
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        const val coroutineExtensions = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
        const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
        const val nativeDriverMacos = "com.squareup.sqldelight:native-driver-macosx64:${Versions.sqlDelight}"
        const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"
    }

    object AndroidJetpackNavigation {
        const val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.androidJetpackNavigation}"
        const val ui = "androidx.navigation:navigation-ui-ktx:${Versions.androidJetpackNavigation}"
        const val testingLib = "androidx.navigation:navigation-testing:${Versions.androidJetpackNavigation}"
        const val compose = "androidx.navigation:navigation-compose:${Versions.androidJetpackNavigation}"
    }
}