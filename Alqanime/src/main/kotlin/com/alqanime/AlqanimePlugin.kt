package com.alqanime

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class AlqanimePlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(Alqanime())
    }
}
