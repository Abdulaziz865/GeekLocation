plugins {
    Plugins.apply {
        id(androidLibrary)
        id(kotlinLibrary)
        id(hilt)
        kotlin(kapt)
    }
}

android {
    namespace = "com.example.data"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Config.jvmTarget
    }
}

dependencies {

    Dependencies.UiComponents.apply {
        // Core
        implementation(core)
        // AppCompat
        implementation(appCompat)
        // Material Design
        implementation(material)
    }

    Dependencies.Hilt.apply {
        //Hilt
        implementation(hilt)
        kapt(hiltCompiler)
    }

    //Location Services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    Dependencies.Firebase.apply {
        implementation(platform(bom))
        implementation(authKtx)
        implementation(playServicesAuth)
        implementation(measurementApi)
    }

    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.6.0")

    implementation(project(":domain"))
}