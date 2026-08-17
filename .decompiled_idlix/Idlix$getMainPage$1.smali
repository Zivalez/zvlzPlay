package com.idlix;
final class Idlix$getMainPage$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    Object L$0;
    Object L$1;
    int label;
    synthetic Object result;
    final synthetic com.idlix.Idlix this$0;

    Idlix$getMainPage$1(com.idlix.Idlix p1, kotlin.coroutines.Continuation p2)
    {
        this.this$0 = p1;
        super(p2);
        return;
    }

    public final Object invokeSuspend(Object p5)
    {
        this.result = p5;
        this.label = (this.label | -2147483648);
        return this.this$0.getMainPage(0, 0, ((kotlin.coroutines.Continuation) this));
    }
}
