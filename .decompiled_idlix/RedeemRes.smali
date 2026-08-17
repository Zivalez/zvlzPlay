package com.idlix;
public final class RedeemRes {
    private final String claim;
    private final long durationSec;
    private final String kind;
    private final long maxHeight;
    private final String redeemUrl;
    private final String title;
    private final String videoId;
    private final String viewerTier;

    public RedeemRes(String p1, String p2, String p3, String p4, String p5, long p6, String p8, long p9)
    {
        this.kind = p1;
        this.claim = p2;
        this.redeemUrl = p3;
        this.videoId = p4;
        this.title = p5;
        this.durationSec = p6;
        this.viewerTier = p8;
        this.maxHeight = p9;
        return;
    }

    public static synthetic com.idlix.RedeemRes copy$default(com.idlix.RedeemRes p12, String p13, String p14, String p15, String p16, String p17, long p18, String p20, long p21, int p23, Object p24)
    {
        String v2_1;
        if ((p23 & 1) == 0) {
            v2_1 = p13;
        } else {
            v2_1 = p12.kind;
        }
        String v3_1;
        if ((p23 & 2) == 0) {
            v3_1 = p14;
        } else {
            v3_1 = p12.claim;
        }
        String v4_1;
        if ((p23 & 4) == 0) {
            v4_1 = p15;
        } else {
            v4_1 = p12.redeemUrl;
        }
        String v5_1;
        if ((p23 & 8) == 0) {
            v5_1 = p16;
        } else {
            v5_1 = p12.videoId;
        }
        String v6_1;
        if ((p23 & 16) == 0) {
            v6_1 = p17;
        } else {
            v6_1 = p12.title;
        }
        long v7_1;
        if ((p23 & 32) == 0) {
            v7_1 = p18;
        } else {
            v7_1 = p12.durationSec;
        }
        String v9_1;
        if ((p23 & 64) == 0) {
            v9_1 = p20;
        } else {
            v9_1 = p12.viewerTier;
        }
        long v10;
        if ((p23 & 128) == 0) {
            v10 = p21;
        } else {
            v10 = p12.maxHeight;
        }
        return p12.copy(v2_1, v3_1, v4_1, v5_1, v6_1, v7_1, v9_1, v10);
    }

    public final String component1()
    {
        return this.kind;
    }

    public final String component2()
    {
        return this.claim;
    }

    public final String component3()
    {
        return this.redeemUrl;
    }

    public final String component4()
    {
        return this.videoId;
    }

    public final String component5()
    {
        return this.title;
    }

    public final long component6()
    {
        return this.durationSec;
    }

    public final String component7()
    {
        return this.viewerTier;
    }

    public final long component8()
    {
        return this.maxHeight;
    }

    public final com.idlix.RedeemRes copy(String p13, String p14, String p15, String p16, String p17, long p18, String p20, long p21)
    {
        com.idlix.RedeemRes v11 = new com.idlix.RedeemRes;
        v11(p13, p14, p15, p16, p17, p18, p20, p21);
        return v11;
    }

    public boolean equals(Object p8)
    {
        if (this != p8) {
            if ((p8 instanceof com.idlix.RedeemRes)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.kind, ((com.idlix.RedeemRes) p8).kind)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.claim, ((com.idlix.RedeemRes) p8).claim)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.redeemUrl, ((com.idlix.RedeemRes) p8).redeemUrl)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.videoId, ((com.idlix.RedeemRes) p8).videoId)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.title, ((com.idlix.RedeemRes) p8).title)) {
                                    if (this.durationSec == ((com.idlix.RedeemRes) p8).durationSec) {
                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.viewerTier, ((com.idlix.RedeemRes) p8).viewerTier)) {
                                            if (this.maxHeight == ((com.idlix.RedeemRes) p8).maxHeight) {
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
                return 0;
            }
        } else {
            return 1;
        }
    }

    public final String getClaim()
    {
        return this.claim;
    }

    public final long getDurationSec()
    {
        return this.durationSec;
    }

    public final String getKind()
    {
        return this.kind;
    }

    public final long getMaxHeight()
    {
        return this.maxHeight;
    }

    public final String getRedeemUrl()
    {
        return this.redeemUrl;
    }

    public final String getTitle()
    {
        return this.title;
    }

    public final String getVideoId()
    {
        return this.videoId;
    }

    public final String getViewerTier()
    {
        return this.viewerTier;
    }

    public int hashCode()
    {
        return ((((((((((((((this.kind.hashCode() * 31) + this.claim.hashCode()) * 31) + this.redeemUrl.hashCode()) * 31) + this.videoId.hashCode()) * 31) + this.title.hashCode()) * 31) + Long.hashCode(this.durationSec)) * 31) + this.viewerTier.hashCode()) * 31) + Long.hashCode(this.maxHeight));
    }

    public String toString()
    {
        return new StringBuilder().append("RedeemRes(kind=").append(this.kind).append(", claim=").append(this.claim).append(", redeemUrl=").append(this.redeemUrl).append(", videoId=").append(this.videoId).append(", title=").append(this.title).append(", durationSec=").append(this.durationSec).append(", viewerTier=").append(this.viewerTier).append(", maxHeight=").append(this.maxHeight).append(41).toString();
    }
}
