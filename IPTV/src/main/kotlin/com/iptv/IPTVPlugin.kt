package com.iptv

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class IPTVPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(IPTV())
    }
}
