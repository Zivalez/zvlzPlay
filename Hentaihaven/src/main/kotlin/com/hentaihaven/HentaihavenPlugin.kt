package com.hentaihaven

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class HentaihavenPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(Hentaihaven())
    }
}
