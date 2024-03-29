plugins {
    Plugins.apply {
        id(application)
        kotlin(android)
        id(hilt)
        kotlin(kapt)
        id(googleServicesPlugin)
    }
}

android {
    namespace = Config.applicationId
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.compileSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

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
        jvmTarget = Config.jvmTarget
    }
    buildFeatures.viewBinding = true
}

dependencies {

    Dependencies.UiComponents.apply {
        // AppCompat
        implementation(appCompat)
        // Material Design
        implementation(material)
        // UI Components
        implementation(constraint)
    }

    Dependencies.ViewBinding.apply {
        // ViewBindingPropertyDelegate
        implementation(viewBinding)
    }

    Dependencies.Hilt.apply {
        //Hilt
        implementation(hilt)
        kapt(hiltCompiler)
    }

    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))
}