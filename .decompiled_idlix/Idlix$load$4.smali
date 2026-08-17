package com.idlix;
final class Idlix$load$4 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2 {
    final synthetic java.util.List $actors;
    final synthetic String $backdrop;
    final synthetic com.idlix.DetailResponse $data;
    final synthetic String $poster;
    final synthetic Object $rating;
    final synthetic java.util.List $recommendations;
    final synthetic java.util.List $tags;
    final synthetic String $trailer;
    final synthetic Integer $year;
    private synthetic Object L$0;
    int label;

    Idlix$load$4(String p2, String p3, Integer p4, com.idlix.DetailResponse p5, java.util.List p6, Object p7, java.util.List p8, String p9, java.util.List p10, kotlin.coroutines.Continuation p11)
    {
        this.$poster = p2;
        this.$backdrop = p3;
        this.$year = p4;
        this.$data = p5;
        this.$tags = p6;
        this.$rating = p7;
        this.$actors = p8;
        this.$trailer = p9;
        this.$recommendations = p10;
        super(2, p11);
        return;
    }

    public final kotlin.coroutines.Continuation create(Object p13, kotlin.coroutines.Continuation p14)
    {
        kotlin.coroutines.Continuation v11_0 = new com.idlix.Idlix$load$4;
        v11_0(this.$poster, this.$backdrop, this.$year, this.$data, this.$tags, this.$rating, this.$actors, this.$trailer, this.$recommendations, p14);
        v11_0.L$0 = p13;
        return ((kotlin.coroutines.Continuation) v11_0);
    }

    public final Object invoke(com.lagradost.cloudstream3.TvSeriesLoadResponse p3, kotlin.coroutines.Continuation p4)
    {
        return ((com.idlix.Idlix$load$4) this.create(p3, p4)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    public bridge synthetic Object invoke(Object p2, Object p3)
    {
        return this.invoke(((com.lagradost.cloudstream3.TvSeriesLoadResponse) p2), ((kotlin.coroutines.Continuation) p3));
    }

    public final Object invokeSuspend(Object p14)
    {
        kotlin.Unit v0_2;
        kotlin.Unit v0_0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                com.lagradost.cloudstream3.LoadResponse v3_1;
                kotlin.ResultKt.throwOnFailure(p14);
                kotlin.Unit v1_6 = ((com.lagradost.cloudstream3.TvSeriesLoadResponse) this.L$0);
                v1_6.setPosterUrl(this.$poster);
                v1_6.setBackgroundPosterUrl(this.$backdrop);
                v1_6.setYear(this.$year);
                v1_6.setPlot(this.$data.getOverview());
                v1_6.setTags(this.$tags);
                com.lagradost.cloudstream3.LoadResponse v3_0 = this.$rating;
                if (v3_0 == null) {
                    v3_1 = 0;
                } else {
                    v3_1 = v3_0.toString();
                }
                v1_6.setScore(com.lagradost.cloudstream3.Score.Companion.from10(v3_1));
                com.lagradost.cloudstream3.LoadResponse.Companion.addActorsOnly(((com.lagradost.cloudstream3.LoadResponse) v1_6), this.$actors);
                com.lagradost.cloudstream3.LoadResponse v6_1 = ((com.lagradost.cloudstream3.LoadResponse) v1_6);
                String v7 = this.$trailer;
                this.L$0 = v1_6;
                this.label = 1;
                if (com.lagradost.cloudstream3.LoadResponse$Companion.addTrailer$default(com.lagradost.cloudstream3.LoadResponse.Companion, v6_1, v7, 0, 0, ((kotlin.coroutines.Continuation) this), 6, 0) != v0_0) {
                    v0_2 = v1_6;
                } else {
                    return v0_0;
                }
            case 1:
                v0_2 = ((com.lagradost.cloudstream3.TvSeriesLoadResponse) this.L$0);
                kotlin.ResultKt.throwOnFailure(p14);
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        com.lagradost.cloudstream3.LoadResponse.Companion.addTMDbId(((com.lagradost.cloudstream3.LoadResponse) v0_2), this.$data.getTmdbId());
        com.lagradost.cloudstream3.LoadResponse.Companion.addImdbId(((com.lagradost.cloudstream3.LoadResponse) v0_2), this.$data.getImdbId());
        v0_2.setRecommendations(this.$recommendations);
        return kotlin.Unit.INSTANCE;
    }
}
