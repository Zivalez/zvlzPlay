dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.json:json:20240303")
}

version = 3

cloudstream {
    language = "id"
    description = "Streaming Anime Sub Indo"
    authors = listOf("Zivalez")
    isCrossPlatform = true
    status = 1
    tvTypes = listOf(
        "AnimeMovie",
        "Anime",
        "OVA",
    )
    iconUrl = "https://cloudstream.zvlz.my.id/icon/otakudesu.avif"
}
