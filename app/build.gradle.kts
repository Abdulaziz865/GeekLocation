plugins {
    id("com.android.application")
    kotlin("android")
    //Kotlin Kapt
    kotlin("kapt")
}

android {
    namespace = "com.example.geeklocation"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.geeklocation"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //Core
    implementation("androidx.core:core-ktx:1.9.0")
    //App Compat
    implementation("androidx.appcompat:appcompat:1.6.1")
    //UI Components
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}