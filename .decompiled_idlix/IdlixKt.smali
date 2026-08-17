package com.idlix;
public final class IdlixKt {

    public static final com.lagradost.cloudstream3.SearchQuality getSearchQuality(String p8)
    {
        if (p8 != null) {
            String v2_0 = java.text.Normalizer.normalize(((CharSequence) p8), java.text.Normalizer$Form.NFKC).toLowerCase(java.util.Locale.ROOT);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(v2_0, "toLowerCase(...)");
            java.util.List v3_2 = new kotlin.Pair[13];
            v3_2[0] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(4k|ds4k|uhd|2160p)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.FourK);
            v3_2[1] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(hdts|hdcam|hdtc)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.HdCam);
            v3_2[2] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(camrip|cam[- ]?rip)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.CamRip);
            v3_2[3] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(cam)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.Cam);
            v3_2[4] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(web[- ]?dl|webrip|webdl)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.WebRip);
            v3_2[5] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(bluray|bdrip|blu[- ]?ray)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.BlueRay);
            v3_2[6] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(1440p|qhd)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.BlueRay);
            v3_2[7] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(1080p|fullhd)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.HD);
            v3_2[8] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(720p)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.SD);
            v3_2[9] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(hdrip|hdtv)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.HD);
            v3_2[10] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(dvd)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.DVD);
            v3_2[11] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(hq)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.HQ);
            v3_2[12] = kotlin.TuplesKt.to(new kotlin.text.Regex("\\b(rip)\\b", kotlin.text.RegexOption.IGNORE_CASE), com.lagradost.cloudstream3.SearchQuality.CamRip);
            java.util.Iterator v4_13 = kotlin.collections.CollectionsKt.listOf(v3_2).iterator();
            while (v4_13.hasNext()) {
                com.lagradost.cloudstream3.SearchQuality v5_15 = ((kotlin.Pair) v4_13.next());
                if (((kotlin.text.Regex) v5_15.component1()).containsMatchIn(((CharSequence) v2_0))) {
                    return ((com.lagradost.cloudstream3.SearchQuality) v5_15.component2());
                }
            }
            return 0;
        } else {
            return 0;
        }
    }
}
