package com.nontonanimeid

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context

@CloudstreamPlugin
class NontonanimeidPlugin : Plugin() {
    override fun load(context: Context) {
        registerMainAPI(Nontonanimeid())
        registerExtractorAPI(KotakAnimeid())
        registerExtractorAPI(KotakAnimeidS1())
    }
}
