import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.annotations.CloudstreamPlugin

@CloudstreamPlugin
class KingV2Plugin : com.lagradost.cloudstream3.Plugin() {
    override fun loadPlugin() {
        registerMainAPI(KingV2())
    }
}
