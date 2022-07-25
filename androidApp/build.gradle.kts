plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
    // kotlin("kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "earth.levi.sage.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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

    val lifecycle_version = "2.6.0-alpha01"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    // annotationProcessor("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")
}