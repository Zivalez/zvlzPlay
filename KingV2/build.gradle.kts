plugins {
    id("com.android.library") apply false
    kotlin("multiplatform") apply false
}

cloudstream {
    language = "id"
    authors = listOf("dit")
    description = "KingV2 provider for KINGBOKEP (alternate host)"
    // status: 1 = stable, 0 = experimental
    status = 1
    tvTypes = listOf("NSFW")
}
