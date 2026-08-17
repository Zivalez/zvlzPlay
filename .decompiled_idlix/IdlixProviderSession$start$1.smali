package com.idlix;
final class IdlixProviderSession$start$1 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2 {
    long J$0;
    int label;

    IdlixProviderSession$start$1(kotlin.coroutines.Continuation p2)
    {
        super(2, p2);
        return;
    }

    public final kotlin.coroutines.Continuation create(Object p2, kotlin.coroutines.Continuation p3)
    {
        return ((kotlin.coroutines.Continuation) new com.idlix.IdlixProviderSession$start$1(p3));
    }

    public bridge synthetic Object invoke(Object p2, Object p3)
    {
        return this.invoke(((kotlinx.coroutines.CoroutineScope) p2), ((kotlin.coroutines.Continuation) p3));
    }

    public final Object invoke(kotlinx.coroutines.CoroutineScope p3, kotlin.coroutines.Continuation p4)
    {
        return ((com.idlix.IdlixProviderSession$start$1) this.create(p3, p4)).invokeSuspend(kotlin.Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object p8)
    {
        com.idlix.IdlixProviderSession$start$1 v3;
        long v1_0;
        String v0_0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(p8);
                v1_0 = 0;
                v3 = this;
                break;
            case 1:
                v1_0 = this.J$0;
                kotlin.ResultKt.throwOnFailure(p8);
                v3 = this;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        do {
            v1_0++;
            Object v4_2 = ((kotlin.coroutines.Continuation) v3);
            v3.J$0 = v1_0;
            v3.label = 1;
        } while(kotlinx.coroutines.DelayKt.delay(600000, v4_2) != v0_0);
        return v0_0;
    }
}
