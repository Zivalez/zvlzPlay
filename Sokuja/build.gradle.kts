version = 1

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("org.json:json:20240303")
}

cloudstream {
    language = "id"
    description = "Anime Subtitle Indonesia"
    authors = listOf("Zivalez")

    status = 3
    tvTypes = listOf(
        "AnimeMovie",
        "Anime",
        "OVA",
    )

    iconUrl = "https://cloudstream.zvlz.my.id/icon/sokuja.png"
}