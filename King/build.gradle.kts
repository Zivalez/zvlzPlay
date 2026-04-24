// use an integer for version numbers
version = 1

cloudstream {
    language = "id"

    description = "KingBokep provider"
    authors = listOf("Zivalez")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1
    tvTypes = listOf(
        "NSFW",
    )

    iconUrl = "https://cdn.kingbokep.video/favicon-32x32.png"
}
