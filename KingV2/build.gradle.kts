plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.kingv2"

    defaultConfig {
        minSdk = 21
        compileSdk = 35
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.json:json:20240303")
}

version = 1

cloudstream {
    language = "id"
    authors = listOf("Zivalez")
    description = "KingV2 provider for KingBokep (alternate host)"
    // status: 1 = stable, 0 = experimental
    status = 1
    tvTypes = listOf("NSFW")
    // Use site favicon as provider icon
    iconUrl = "https://185.169.252.47/wp-content/uploads/2025/06/favicon.ico"
}
