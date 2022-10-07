plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "earth.levi.sage"
        minSdk = 21
        targetSdk = 33
        versionCode = 1665181864
        versionName = "1.0.0-alpha.1"
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file("androidApp/keystores/uploadkey.jks")
            storePassword = System.getenv("ANDROID_SIGNING_KEY_PASSWORD")
            keyAlias = System.getenv("ANDROID_SIGNING_ALIAS")
            keyPassword = System.getenv("ANDROID_SIGNING_KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures {
        // enable View Binding
        viewBinding = true
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.fragment:fragment-ktx:1.4.1")

    implementation("com.squareup.picasso:picasso:2.8")

    val lifecycle_version = "2.6.0-alpha01"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // annotationProcessor("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
}
