plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt") // Habilita kapt para Room
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.gastosmicrowearos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gastosmicrowearos"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
}

dependencies {
    implementation("androidx.wear.compose:compose-material:1.3.0")

    // Jetpack Compose UI principal (incluye KeyboardOptions y KeyboardType)
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material:material-icons-extended:<versión>")


    // Room
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))



    // Jetpack Compose Foundation y UI extras
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.foundation)
    implementation(libs.activity.compose)

    // Material3 para Jetpack Compose
    implementation("androidx.compose.material3:material3:1.1.0")

    // Lifecycle ViewModel Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // Splash screen
    implementation(libs.core.splashscreen)

    // Tooling para preview y desarrollo
    implementation(libs.wear.tooling.preview)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Tests
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")


    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")




}