package com.idlix;
final class Majorplay$getUrl$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    synthetic Object result;
    final synthetic com.idlix.Majorplay this$0;

    Majorplay$getUrl$1(com.idlix.Majorplay p1, kotlin.coroutines.Continuation p2)
    {
        this.this$0 = p1;
        super(p2);
        return;
    }

    public final Object invokeSuspend(Object p8)
    {
        this.result = p8;
        this.label = (this.label | -2147483648);
        return this.this$0.getUrl(0, 0, 0, 0, ((kotlin.coroutines.Continuation) this));
    }
}
