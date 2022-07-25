import java.io.FileInputStream
import java.util.*
import kotlin.Throwable

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("com.rickclephas.kmp.nativecoroutines") version "0.12.6"
    kotlin("plugin.serialization") version "1.7.10"
}

version = "1.0"

android {
    compileSdk = 32

    defaultConfig {
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0-RC")

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
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
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