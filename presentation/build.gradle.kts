plugins {
    Plugins.apply {
        id(androidLibrary)
        id(kotlinLibrary)
        id(hilt)
        kotlin(kapt)
    }
}

android {
    namespace = "com.example.presentation"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Config.jvmTarget
    }
    buildFeatures.viewBinding = true

}

dependencies {

    Dependencies.UiComponents.apply {
        // Core
        implementation(core)
        // AppCompat
        implementation(appCompat)
        // Material Design
        implementation(material)
        // UI Components
        implementation(constraint)
    }

    Dependencies.Hilt.apply {
        //Hilt
        implementation(hilt)
        kapt(hiltCompiler)
    }

    Dependencies.Firebase.apply {
        implementation(platform(bom))
        implementation(authKtx)
        implementation(playServicesAuth)
        implementation(measurementApi)
    }

    Dependencies.ViewBinding.apply {
        // ViewBindingPropertyDelegate
        implementation(viewBinding)
    }

    Dependencies.Navigation.apply {
        // Navigation
        implementation(navigationFragment)
        implementation(navigation)
    }

    Dependencies.Map.apply {
        //Map
        implementation(map)
    }

    implementation("com.google.android.gms:play-services-location:21.0.1")

    Dependencies.Lifecycles.apply {
        // Lifecycles
        implementation(lifecycles)
        implementation(legasySupport)
        implementation(lifecycleViewModel)
    }

    implementation(project(":domain"))
}