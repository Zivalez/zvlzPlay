package recloudstream

import com.lagradost.cloudstream3.plugins.BasePlugin
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin

@CloudstreamPlugin
class TwitchPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(Twitch())
        registerExtractorAPI(Twitch.TwitchExtractor())
    }
}
