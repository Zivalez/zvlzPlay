package com.winbu

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class WinbuPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(Winbu())
    }
}
