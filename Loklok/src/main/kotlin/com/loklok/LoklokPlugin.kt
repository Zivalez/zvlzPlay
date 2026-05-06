package com.loklok

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class LoklokPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(Loklok())
    }
}
