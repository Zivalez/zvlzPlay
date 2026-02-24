package com.nontonanimeid

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class NontonAnimeIDPlugin: Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list directly.
        registerMainAPI(NontonAnimeID())
        registerExtractorAPI(Nontonanimeid())
        registerExtractorAPI(EmbedKotakAnimeid())
        registerExtractorAPI(KotakAnimeidCom())
        registerExtractorAPI(Gdplayer())
        registerExtractorAPI(Kotaksb())
    }
}
