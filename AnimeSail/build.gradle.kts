dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

version = 1

cloudstream {
    language = "id"
    authors = listOf("Asm0d3usX", "Zivalez")
    description = "Streaming Anime Subtitle Indonesia dari AnimeSail"
    isCrossPlatform = true
    status = 1
    tvTypes = listOf(
        "AnimeMovie",
        "Anime",
        "OVA"
    )
    iconUrl = "https://aghanim.xyz/wp-content/themes/animesail/assets/images/ico.png"
}
