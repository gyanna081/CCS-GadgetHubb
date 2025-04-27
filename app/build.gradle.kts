plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ccsgadgethub"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ccsgadgethub"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.core:core-ktx:1.7.0")

    // Jetpack Compose Libraries
    implementation(platform(libs.androidx.compose.bom))  // BOM for Compose versions
    implementation(libs.androidx.ui)                    // For UI components
    implementation(libs.androidx.ui.graphics)           // For graphics-related functionality
    implementation(libs.androidx.ui.tooling.preview)    // For preview functionality in Compose

    // Material3 for UI components
    implementation(libs.androidx.material3)

    // Navigation for Compose
    implementation(libs.androidx.navigation.compose)

    // Unit Testing
    testImplementation(libs.junit)

    // Android Test Libraries
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging Tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material Icons Extended
    implementation(libs.androidx.material.icons.extended)  // For extended Material icons (like Menu, Logout)

    // Coil for image loading
    implementation(libs.coil.compose)
}
