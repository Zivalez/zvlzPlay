package com.kingv2

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class KingV2Plugin : Plugin() {
    override fun load(context: android.content.Context) {
        registerMainAPI(KingV2())
    }
}
