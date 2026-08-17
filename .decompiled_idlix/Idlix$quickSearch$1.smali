package com.idlix;
final class Idlix$quickSearch$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int label;
    synthetic Object result;
    final synthetic com.idlix.Idlix this$0;

    Idlix$quickSearch$1(com.idlix.Idlix p1, kotlin.coroutines.Continuation p2)
    {
        this.this$0 = p1;
        super(p2);
        return;
    }

    public final Object invokeSuspend(Object p4)
    {
        this.result = p4;
        this.label = (this.label | -2147483648);
        return this.this$0.quickSearch(0, ((kotlin.coroutines.Continuation) this));
    }
}
