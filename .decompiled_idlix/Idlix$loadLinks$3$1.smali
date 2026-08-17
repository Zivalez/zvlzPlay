package com.idlix;
final class Idlix$loadLinks$3$1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2 {
    private synthetic Object L$0;
    int label;
    final synthetic com.idlix.Idlix this$0;

    Idlix$loadLinks$3$1(com.idlix.Idlix p2, kotlin.coroutines.Continuation p3)
    {
        this.this$0 = p2;
        super(2, p3);
        return;
    }

    public final kotlin.coroutines.Continuation create(Object p3, kotlin.coroutines.Continuation p4)
    {
        kotlin.coroutines.Continuation v0_1 = new com.idlix.Idlix$loadLinks$3$1(this.this$0, p4);
        v0_1.L$0 = p3;
        return ((kotlin.coroutines.Continuation) v0_1);
    }

    public final Object invoke(com.lagradost.cloudstream3.utils.ExtractorLink p3, kotlin.coroutines.Continuation p4)
    {
        return ((com.idlix.Idlix$loadLinks$3$1) this.create(p3, p4)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    public bridge synthetic Object invoke(Object p2, Object p3)
    {
        return this.invoke(((com.lagradost.cloudstream3.utils.ExtractorLink) p2), ((kotlin.coroutines.Continuation) p3));
    }

    public final Object invokeSuspend(Object p6)
    {
        kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(p6);
                com.lagradost.cloudstream3.utils.ExtractorLink v0_2 = ((com.lagradost.cloudstream3.utils.ExtractorLink) this.L$0);
                v0_2.setReferer(new StringBuilder().append(this.this$0.getMainUrl()).append(47).toString());
                kotlin.Unit v1_4 = new kotlin.Pair[3];
                v1_4[0] = kotlin.TuplesKt.to("Referer", new StringBuilder().append(this.this$0.getMainUrl()).append(47).toString());
                v1_4[1] = kotlin.TuplesKt.to("Origin", this.this$0.getMainUrl());
                v1_4[2] = kotlin.TuplesKt.to("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                v0_2.setHeaders(kotlin.collections.MapsKt.mapOf(v1_4));
                return kotlin.Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
    }
}
