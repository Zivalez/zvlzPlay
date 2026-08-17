package com.idlix;
public final class Jeniusplay$ResponseSource {
    private final boolean hls;
    private final String securedLink;
    private final String videoSource;

    public Jeniusplay$ResponseSource(boolean p1, String p2, String p3)
    {
        this.hls = p1;
        this.videoSource = p2;
        this.securedLink = p3;
        return;
    }

    public static synthetic com.idlix.Jeniusplay$ResponseSource copy$default(com.idlix.Jeniusplay$ResponseSource p0, boolean p1, String p2, String p3, int p4, Object p5)
    {
        if ((p4 & 1) != 0) {
            p1 = p0.hls;
        }
        if ((p4 & 2) != 0) {
            p2 = p0.videoSource;
        }
        if ((p4 & 4) != 0) {
            p3 = p0.securedLink;
        }
        return p0.copy(p1, p2, p3);
    }

    public final boolean component1()
    {
        return this.hls;
    }

    public final String component2()
    {
        return this.videoSource;
    }

    public final String component3()
    {
        return this.securedLink;
    }

    public final com.idlix.Jeniusplay$ResponseSource copy(boolean p2, String p3, String p4)
    {
        return new com.idlix.Jeniusplay$ResponseSource(p2, p3, p4);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Jeniusplay$ResponseSource)) {
                if (this.hls == ((com.idlix.Jeniusplay$ResponseSource) p6).hls) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.videoSource, ((com.idlix.Jeniusplay$ResponseSource) p6).videoSource)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.securedLink, ((com.idlix.Jeniusplay$ResponseSource) p6).securedLink)) {
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

    public final boolean getHls()
    {
        return this.hls;
    }

    public final String getSecuredLink()
    {
        return this.securedLink;
    }

    public final String getVideoSource()
    {
        return this.videoSource;
    }

    public int hashCode()
    {
        int v2_1;
        int v0_3 = (((Boolean.hashCode(this.hls) * 31) + this.videoSource.hashCode()) * 31);
        if (this.securedLink != null) {
            v2_1 = this.securedLink.hashCode();
        } else {
            v2_1 = 0;
        }
        return (v0_3 + v2_1);
    }

    public String toString()
    {
        return new StringBuilder().append("ResponseSource(hls=").append(this.hls).append(", videoSource=").append(this.videoSource).append(", securedLink=").append(this.securedLink).append(41).toString();
    }
}
