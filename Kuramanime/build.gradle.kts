dependencies {
//    implementation(kotlin("stdlib"))
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
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}
// use an integer for version numbers
version = 2


cloudstream {
    language = "id"
    description = "Nonton Anime, Donghua, Movie Sub Indo Terlengkap & Terbaru"
    authors = listOf("TeKuma25", "Zivalez")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 3
    tvTypes = listOf(
        "AnimeMovie",
        "Anime",
        "Donghua",
        "OVA",
    )

    iconUrl = "https://v15.kuramanime.tel/assets/img/logo-text.png"
}
