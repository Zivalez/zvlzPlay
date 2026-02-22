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
version = 4

cloudstream {
    language = "id"
    description = "Nonton & Download Anime Streaming Sub Indo"
    authors = listOf("Zivalez", "Asm0d3usX")
    status = 1
    tvTypes = listOf(
        "AnimeMovie",
        "OVA",
        "Anime",
    )
    iconUrl = "https://cloudstream.zvlz.my.id/icon/samehadaku.avif"
}
