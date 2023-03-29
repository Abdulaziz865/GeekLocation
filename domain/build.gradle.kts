plugins {
    Plugins.apply {
        id(javaLibrary)
        id(kotlinJvm)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}