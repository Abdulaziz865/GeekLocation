object Dependencies {

    object UiComponents {
        const val core = "androidx.core:core-ktx:1.9.0"
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"
        const val material = "com.google.android.material:material:1.8.0"
        const val constraint = "androidx.constraintlayout:constraintlayout:2.1.4"
    }

    object Hilt {
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val gsonConvertor = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    }

    object OkHttpClient {
        const val bomHttp = "com.squareup.okhttp3:okhttp-bom:${Versions.bomHttp}"
        const val okHttp = "com.squareup.okhttp3:okhttp"
        const val httpInterceptor = "com.squareup.okhttp3:logging-interceptor"
    }

    object Coroutines {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    }

    object Inject {
        const val inject = "javax.inject:javax.inject:${Versions.inject}"
    }

    object ViewBinding {
        const val viewBinding = "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBinding}"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Lifecycles {
        const val lifecycles = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
        const val runtimeLifecycles = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val legasySupport = "androidx.legacy:legacy-support-v4:${Versions.legasySupport}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}    "
    }

    object Navigation {
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val navigation = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    }

    object Firebase{
        const val auth = "com.google.firebase:firebase-auth:${Versions.fireAuth}"
        const val authKtx = "com.google.firebase:firebase-auth-ktx:${Versions.fireAuthKtx}"
        const val playServicesAuth = "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}"
        const val bom = "com.google.firebase:firebase-bom:${Versions.fireBom}"
        const val measurementApi = "com.google.android.gms:play-services-measurement-api:${Versions.measurementApi}"
    }

    object Map {
        const val map = "com.google.android.gms:play-services-maps:${Versions.map}"
    }
}