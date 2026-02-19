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

cloudstream {
    // metadata
    language = "id"
    description = "Nonton Film Streaming Movie Idlix Subtitle Indonesia Gratis Online"
    authors = listOf("Hexated", "TeKuma25", "Zivalez")
    status = 3 
    tvTypes = listOf("Movie", "TvSeries", "AsianDrama", "Anime")
    iconUrl = "https://tv12.idlixku.com/wp-content/uploads/2020/06/idlix.png"
}
