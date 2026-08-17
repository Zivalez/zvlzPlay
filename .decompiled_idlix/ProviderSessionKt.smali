package com.idlix;
public final class ProviderSessionKt {

    public static final void pingAnalytics(String p1)
    {
        com.idlix.IdlixProviderSession.INSTANCE.start(p1);
        return;
    }
}
