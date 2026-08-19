plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.gomunime"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.json:json:20240303")
}

version = 2

cloudstream {
    language = "id"
    description = "Streaming anime subtitle Indonesia"
    authors = listOf("Zivalez")
    status = 1
    tvTypes = listOf(
        "Anime",
        "AnimeMovie",
        "OVA",
    )
    iconUrl = "https://cloudstream.zvlz.my.id/icon/gomunime.png"
}
