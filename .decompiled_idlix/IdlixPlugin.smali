package com.idlix;
public final class IdlixPlugin extends com.lagradost.cloudstream3.plugins.BasePlugin {

    public IdlixPlugin()
    {
        return;
    }

    public void load()
    {
        com.idlix.ProviderSessionKt.pingAnalytics("Idlix");
        this.registerMainAPI(((com.lagradost.cloudstream3.MainAPI) new com.idlix.Idlix()));
        this.registerExtractorAPI(((com.lagradost.cloudstream3.utils.ExtractorApi) new com.idlix.Jeniusplay()));
        this.registerExtractorAPI(((com.lagradost.cloudstream3.utils.ExtractorApi) new com.idlix.Majorplay()));
        return;
    }
}
