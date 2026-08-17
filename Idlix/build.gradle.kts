plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.idlix"
    compileSdkVersion(35)
    
     defaultConfig {
        minSdk = 21
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.Blatzar:NiceHttp:0.4.11")
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("com.faendir.rhino:rhino-android:1.6.0")
    implementation("me.xdrop:fuzzywuzzy:1.4.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("app.cash.quickjs:quickjs-android:0.9.2")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.json:json:20240303")
}
version = 12

cloudstream {
    // metadata
    language = "id"
    description = "Nonton Film & Series Streaming Idlix Subtitle Indonesia"
    authors = listOf("Hexated", "Phisher98", "Zivalez")
    status = 1
    tvTypes = listOf("Movies", "TvSeries", "AsianDrama", "Anime")
    iconUrl = "https://cloudstream.zvlz.my.id/icon/idlix.png"
}
