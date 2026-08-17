package com.idlix;
public final class Iframe {
    private final String code;
    private final Long expiresAt;
    private final java.util.List subtitles;
    private final String url;
    private final String videoId;

    public Iframe()
    {
        this(0, 0, 0, 0, 0, 31, 0);
        return;
    }

    public Iframe(String p1, String p2, Long p3, java.util.List p4, String p5)
    {
        this.code = p1;
        this.url = p2;
        this.expiresAt = p3;
        this.subtitles = p4;
        this.videoId = p5;
        return;
    }

    public synthetic Iframe(String p5, String p6, Long p7, java.util.List p8, String p9, int p10, kotlin.jvm.internal.DefaultConstructorMarker p11)
    {
        String v11_1;
        if ((p10 & 1) == 0) {
            v11_1 = p5;
        } else {
            v11_1 = 0;
        }
        String v1;
        if ((p10 & 2) == 0) {
            v1 = p6;
        } else {
            v1 = 0;
        }
        Long v2;
        if ((p10 & 4) == 0) {
            v2 = p7;
        } else {
            v2 = 0;
        }
        java.util.List v3;
        if ((p10 & 8) == 0) {
            v3 = p8;
        } else {
            v3 = kotlin.collections.CollectionsKt.emptyList();
        }
        String v10_1;
        if ((p10 & 16) == 0) {
            v10_1 = p9;
        } else {
            v10_1 = 0;
        }
        this(v11_1, v1, v2, v3, v10_1);
        return;
    }

    public static synthetic com.idlix.Iframe copy$default(com.idlix.Iframe p3, String p4, String p5, Long p6, java.util.List p7, String p8, int p9, Object p10)
    {
        if ((p9 & 1) != 0) {
            p4 = p3.code;
        }
        if ((p9 & 2) != 0) {
            p5 = p3.url;
        }
        if ((p9 & 4) != 0) {
            p6 = p3.expiresAt;
        }
        if ((p9 & 8) != 0) {
            p7 = p3.subtitles;
        }
        if ((p9 & 16) != 0) {
            p8 = p3.videoId;
        }
        return p3.copy(p4, p5, p6, p7, p8);
    }

    public final String component1()
    {
        return this.code;
    }

    public final String component2()
    {
        return this.url;
    }

    public final Long component3()
    {
        return this.expiresAt;
    }

    public final java.util.List component4()
    {
        return this.subtitles;
    }

    public final String component5()
    {
        return this.videoId;
    }

    public final com.idlix.Iframe copy(String p8, String p9, Long p10, java.util.List p11, String p12)
    {
        com.idlix.Iframe v6 = new com.idlix.Iframe;
        v6(p8, p9, p10, p11, p12);
        return v6;
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Iframe)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.code, ((com.idlix.Iframe) p6).code)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.url, ((com.idlix.Iframe) p6).url)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.expiresAt, ((com.idlix.Iframe) p6).expiresAt)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.subtitles, ((com.idlix.Iframe) p6).subtitles)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.videoId, ((com.idlix.Iframe) p6).videoId)) {
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
            return 1;
        }
    }

    public final String getCode()
    {
        return this.code;
    }

    public final Long getExpiresAt()
    {
        return this.expiresAt;
    }

    public final java.util.List getSubtitles()
    {
        return this.subtitles;
    }

    public final String getUrl()
    {
        return this.url;
    }

    public final String getVideoId()
    {
        return this.videoId;
    }

    public int hashCode()
    {
        int v0_6;
        int v1_0 = 0;
        if (this.code != null) {
            v0_6 = this.code.hashCode();
        } else {
            v0_6 = 0;
        }
        String v3_1;
        int v2_3 = (v0_6 * 31);
        if (this.url != null) {
            v3_1 = this.url.hashCode();
        } else {
            v3_1 = 0;
        }
        String v3_4;
        int v0_1 = ((v2_3 + v3_1) * 31);
        if (this.expiresAt != null) {
            v3_4 = this.expiresAt.hashCode();
        } else {
            v3_4 = 0;
        }
        int v0_3 = ((((v0_1 + v3_4) * 31) + this.subtitles.hashCode()) * 31);
        if (this.videoId != null) {
            v1_0 = this.videoId.hashCode();
        }
        return (v0_3 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Iframe(code=").append(this.code).append(", url=").append(this.url).append(", expiresAt=").append(this.expiresAt).append(", subtitles=").append(this.subtitles).append(", videoId=").append(this.videoId).append(41).toString();
    }
}
