package com.idlix;
public final class Res {
    private final String gateToken;
    private final long serverNow;
    private final long unlockAt;

    public Res(String p1, long p2, long p4)
    {
        this.gateToken = p1;
        this.serverNow = p2;
        this.unlockAt = p4;
        return;
    }

    public static synthetic com.idlix.Res copy$default(com.idlix.Res p2, String p3, long p4, long p6, int p8, Object p9)
    {
        if ((p8 & 1) != 0) {
            p3 = p2.gateToken;
        }
        if ((p8 & 2) != 0) {
            p4 = p2.serverNow;
        }
        if ((p8 & 4) != 0) {
            p6 = p2.unlockAt;
        }
        return p2.copy(p3, p4, p6);
    }

    public final String component1()
    {
        return this.gateToken;
    }

    public final long component2()
    {
        return this.serverNow;
    }

    public final long component3()
    {
        return this.unlockAt;
    }

    public final com.idlix.Res copy(String p8, long p9, long p11)
    {
        com.idlix.Res v6 = new com.idlix.Res;
        v6(p8, p9, p11);
        return v6;
    }

    public boolean equals(Object p8)
    {
        if (this != p8) {
            if ((p8 instanceof com.idlix.Res)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.gateToken, ((com.idlix.Res) p8).gateToken)) {
                    if (this.serverNow == ((com.idlix.Res) p8).serverNow) {
                        if (this.unlockAt == ((com.idlix.Res) p8).unlockAt) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public final String getGateToken()
    {
        return this.gateToken;
    }

    public final long getServerNow()
    {
        return this.serverNow;
    }

    public final long getUnlockAt()
    {
        return this.unlockAt;
    }

    public int hashCode()
    {
        return ((((this.gateToken.hashCode() * 31) + Long.hashCode(this.serverNow)) * 31) + Long.hashCode(this.unlockAt));
    }

    public String toString()
    {
        return new StringBuilder().append("Res(gateToken=").append(this.gateToken).append(", serverNow=").append(this.serverNow).append(", unlockAt=").append(this.unlockAt).append(41).toString();
    }
}
