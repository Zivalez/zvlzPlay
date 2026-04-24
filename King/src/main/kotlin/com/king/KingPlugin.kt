package com.king

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class KingPlugin : Plugin() {
    override fun load(context: android.content.Context) {
        registerMainAPI(King())
    }
}
