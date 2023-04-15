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

    implementation(project(":domain"))
}