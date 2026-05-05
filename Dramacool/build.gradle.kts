dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

version = 1

cloudstream {
    language = "en"
    description = "Asian Drama, Movies and Shows English Sub"
    authors = listOf("Zivalez")
    status = 3
    tvTypes = listOf(
        "AsianDrama",
        "TvSeries",
        "Movie",
    )
    iconUrl = "https://asianctv.net/favicon.ico"
}
