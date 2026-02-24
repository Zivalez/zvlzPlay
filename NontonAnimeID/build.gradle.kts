// use an integer for version numbers
version = 23

cloudstream {
    language = "id"
    authors = listOf("Hexated", "TeKuma25", "zvlzPlay")
    
    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // Set to Ok since we are porting it
    tvTypes = listOf(
        "AnimeMovie",
        "Anime",
        "OVA",
    )

    iconUrl = "https://s11.nontonanimeid.boats/wp-content/uploads/2021/01/nontonanimeid-1.png" // Updated to a valid icon or favicons service
}
