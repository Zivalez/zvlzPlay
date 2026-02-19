package com.nontondrama

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class NontonDramaPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(NontonDrama())
        registerExtractorAPI(Emturbovid())
        registerExtractorAPI(Hownetwork())
    }
}
