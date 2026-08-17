package com.idlix;
public final class IdlixProviderSession {
    public static final com.idlix.IdlixProviderSession INSTANCE;
    private static final String clientId;
    private static final java.util.concurrent.atomic.AtomicBoolean started;

    static IdlixProviderSession()
    {
        com.idlix.IdlixProviderSession.INSTANCE = new com.idlix.IdlixProviderSession();
        com.idlix.IdlixProviderSession.clientId = java.util.UUID.randomUUID().toString();
        com.idlix.IdlixProviderSession.started = new java.util.concurrent.atomic.AtomicBoolean(0);
        return;
    }

    private IdlixProviderSession()
    {
        return;
    }

    public final String getClientId()
    {
        return com.idlix.IdlixProviderSession.clientId;
    }

    public final void start(String p8)
    {
        if (com.idlix.IdlixProviderSession.started.compareAndSet(0, 1)) {
            kotlinx.coroutines.BuildersKt.launch$default(((kotlinx.coroutines.CoroutineScope) kotlinx.coroutines.GlobalScope.INSTANCE), 0, 0, ((kotlin.jvm.functions.Function2) new com.idlix.IdlixProviderSession$start$1(0)), 3, 0);
            return;
        } else {
            return;
        }
    }
}
