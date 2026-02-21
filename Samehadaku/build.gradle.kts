plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.samehadaku"
    
    defaultConfig {
        minSdk = 21
        compileSdkVersion(35)
        targetSdk = 35
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.json:json:20240303")
}
version = 3

cloudstream {
    language = "id"
    description = "Anime Sub Indo"
    authors = listOf("Zivalez", "Asm0d3usX")
    status = 1
    tvTypes = listOf(
        "AnimeMovie",
        "OVA",
        "Anime",
    )
    iconUrl = "https://v1.samehadaku.how/wp-content/uploads/2020/04/cropped-download-1-32x32.jpg"
}
