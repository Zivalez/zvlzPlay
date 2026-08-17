package com.idlix;
final class Idlix$load$1 extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    int I$0;
    Object L$0;
    Object L$1;
    Object L$10;
    Object L$11;
    Object L$12;
    Object L$13;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    synthetic Object result;
    final synthetic com.idlix.Idlix this$0;

    Idlix$load$1(com.idlix.Idlix p1, kotlin.coroutines.Continuation p2)
    {
        this.this$0 = p1;
        super(p2);
        return;
    }

    public final Object invokeSuspend(Object p4)
    {
        this.result = p4;
        this.label = (this.label | -2147483648);
        return this.this$0.load(0, ((kotlin.coroutines.Continuation) this));
    }
}
