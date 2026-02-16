package com.kuramanime

import android.content.Context
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class KuramanimePlugin : Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list
        // directly.
        registerMainAPI(Kuramanime())
        registerExtractorAPI(Nyomo())
        registerExtractorAPI(Streamhide())
        registerExtractorAPI(Kuramadrive())
        registerExtractorAPI(Lbx())
    }
}
