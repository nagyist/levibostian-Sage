import java.io.FileInputStream
import java.util.*
import kotlin.Throwable

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("com.rickclephas.kmp.nativecoroutines") version Versions.kmpNativeCoroutines
}

version = "1.0"

android {
    compileSdk = 33

    defaultConfig {
        // dropbox SDK requires inserting the app key in manifest file.
        // this is how we can insert dynamic value into manifest
        val secretProperties = getSecretProperties()
        val dropboxAppKey = secretProperties.getProperty("DROPBOX_APP_KEY")
        manifestPlaceholders["dropboxKey"] = dropboxAppKey
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

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            // Set to false to fix Xcode SwiftUI preview: https://github.com/cashapp/sqldelight/issues/2512#issuecomment-937699879
            // However, false made the dropbox objective-c SDK no longer compile. So, keeping at static to keep project compiling successfully.
            isStatic = true
        }

        /**
         * Shared kotlin module can only use objective-c pods, not pure swift. Kotlin at this time can only compile to and from (2-way support) with objective-c.
         *
         * Learn more
         * https://kotlinlang.org/docs/multiplatform-mobile-ios-dependencies.html
         * https://youtrack.jetbrains.com/issue/KT-49521/Support-direct-interoperability-with-Swift
         */
        pod("ObjectiveDropboxOfficial") {
            version = "~> 6.3.2"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.russhwolf:multiplatform-settings:0.9") // for key/value storage on KMP

                with(Deps.SqlDelight) {
                    implementation(runtime)
                    implementation(coroutineExtensions)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.dropbox.core:dropbox-core-sdk:5.2.0")
                implementation(Deps.SqlDelight.androidDriver)
                implementation(Deps.AndroidX.core) // for checking runtime permissions
                implementation(Deps.AndroidX.viewModel) // creating an adapter between shared ViewModels and an Android specific ViewModel
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(Deps.SqlDelight.nativeDriver)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    namespace = "earth.levi.sage"
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

sqldelight {
    database("SageDatabase") {
        packageName = "earth.levi.sage.db"
        sourceFolders = listOf("sqldelight")
    }
}

fun getSecretProperties(): Properties = Properties().apply {
    val secretPropertiesFile = file("./secret.properties")
    if (!secretPropertiesFile.exists()) throw Throwable("Forgot to include file ${secretPropertiesFile.absoluteFile} for building android app")

    load(FileInputStream(secretPropertiesFile))
}