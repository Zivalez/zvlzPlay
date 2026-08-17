package com.idlix;
public final class Episode {
    private final String airDate;
    private final Integer episodeNumber;
    private final String id;
    private final String name;
    private final String overview;
    private final Integer runtime;
    private final String stillPath;
    private final Object voteAverage;

    public Episode()
    {
        this(0, 0, 0, 0, 0, 0, 0, 0, 255, 0);
        return;
    }

    public Episode(String p1, Integer p2, String p3, String p4, String p5, String p6, Integer p7, Object p8)
    {
        this.id = p1;
        this.episodeNumber = p2;
        this.name = p3;
        this.overview = p4;
        this.stillPath = p5;
        this.airDate = p6;
        this.runtime = p7;
        this.voteAverage = p8;
        return;
    }

    public synthetic Episode(String p10, Integer p11, String p12, String p13, String p14, String p15, Integer p16, Object p17, int p18, kotlin.jvm.internal.DefaultConstructorMarker p19)
    {
        String v1_1;
        Object v2 = 0;
        if ((p18 & 1) == 0) {
            v1_1 = p10;
        } else {
            v1_1 = 0;
        }
        Integer v3_1;
        if ((p18 & 2) == 0) {
            v3_1 = p11;
        } else {
            v3_1 = 0;
        }
        String v4_1;
        if ((p18 & 4) == 0) {
            v4_1 = p12;
        } else {
            v4_1 = 0;
        }
        String v5_1;
        if ((p18 & 8) == 0) {
            v5_1 = p13;
        } else {
            v5_1 = 0;
        }
        String v6_1;
        if ((p18 & 16) == 0) {
            v6_1 = p14;
        } else {
            v6_1 = 0;
        }
        String v7_1;
        if ((p18 & 32) == 0) {
            v7_1 = p15;
        } else {
            v7_1 = 0;
        }
        Integer v8_1;
        if ((p18 & 64) == 0) {
            v8_1 = p16;
        } else {
            v8_1 = 0;
        }
        if ((p18 & 128) == 0) {
            v2 = p17;
        }
        this(v1_1, v3_1, v4_1, v5_1, v6_1, v7_1, v8_1, v2);
        return;
    }

    public static synthetic com.idlix.Episode copy$default(com.idlix.Episode p9, String p10, Integer p11, String p12, String p13, String p14, String p15, Integer p16, Object p17, int p18, Object p19)
    {
        String v2_1;
        if ((p18 & 1) == 0) {
            v2_1 = p10;
        } else {
            v2_1 = p9.id;
        }
        Integer v3_1;
        if ((p18 & 2) == 0) {
            v3_1 = p11;
        } else {
            v3_1 = p9.episodeNumber;
        }
        String v4_1;
        if ((p18 & 4) == 0) {
            v4_1 = p12;
        } else {
            v4_1 = p9.name;
        }
        String v5_1;
        if ((p18 & 8) == 0) {
            v5_1 = p13;
        } else {
            v5_1 = p9.overview;
        }
        String v6_1;
        if ((p18 & 16) == 0) {
            v6_1 = p14;
        } else {
            v6_1 = p9.stillPath;
        }
        String v7_1;
        if ((p18 & 32) == 0) {
            v7_1 = p15;
        } else {
            v7_1 = p9.airDate;
        }
        Integer v8_1;
        if ((p18 & 64) == 0) {
            v8_1 = p16;
        } else {
            v8_1 = p9.runtime;
        }
        Object v1_2;
        if ((p18 & 128) == 0) {
            v1_2 = p17;
        } else {
            v1_2 = p9.voteAverage;
        }
        return p9.copy(v2_1, v3_1, v4_1, v5_1, v6_1, v7_1, v8_1, v1_2);
    }

    public final String component1()
    {
        return this.id;
    }

    public final Integer component2()
    {
        return this.episodeNumber;
    }

    public final String component3()
    {
        return this.name;
    }

    public final String component4()
    {
        return this.overview;
    }

    public final String component5()
    {
        return this.stillPath;
    }

    public final String component6()
    {
        return this.airDate;
    }

    public final Integer component7()
    {
        return this.runtime;
    }

    public final Object component8()
    {
        return this.voteAverage;
    }

    public final com.idlix.Episode copy(String p11, Integer p12, String p13, String p14, String p15, String p16, Integer p17, Object p18)
    {
        com.idlix.Episode v9 = new com.idlix.Episode;
        v9(p11, p12, p13, p14, p15, p16, p17, p18);
        return v9;
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Episode)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.Episode) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.episodeNumber, ((com.idlix.Episode) p6).episodeNumber)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.name, ((com.idlix.Episode) p6).name)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.overview, ((com.idlix.Episode) p6).overview)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.stillPath, ((com.idlix.Episode) p6).stillPath)) {
                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.airDate, ((com.idlix.Episode) p6).airDate)) {
                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.runtime, ((com.idlix.Episode) p6).runtime)) {
                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.voteAverage, ((com.idlix.Episode) p6).voteAverage)) {
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

    public final String getAirDate()
    {
        return this.airDate;
    }

    public final Integer getEpisodeNumber()
    {
        return this.episodeNumber;
    }

    public final String getId()
    {
        return this.id;
    }

    public final String getName()
    {
        return this.name;
    }

    public final String getOverview()
    {
        return this.overview;
    }

    public final Integer getRuntime()
    {
        return this.runtime;
    }

    public final String getStillPath()
    {
        return this.stillPath;
    }

    public final Object getVoteAverage()
    {
        return this.voteAverage;
    }

    public int hashCode()
    {
        int v0_7;
        int v1_0 = 0;
        if (this.id != null) {
            v0_7 = this.id.hashCode();
        } else {
            v0_7 = 0;
        }
        Object v3_1;
        int v2_7 = (v0_7 * 31);
        if (this.episodeNumber != null) {
            v3_1 = this.episodeNumber.hashCode();
        } else {
            v3_1 = 0;
        }
        Object v3_4;
        int v0_1 = ((v2_7 + v3_1) * 31);
        if (this.name != null) {
            v3_4 = this.name.hashCode();
        } else {
            v3_4 = 0;
        }
        Object v3_7;
        int v2_1 = ((v0_1 + v3_4) * 31);
        if (this.overview != null) {
            v3_7 = this.overview.hashCode();
        } else {
            v3_7 = 0;
        }
        Object v3_10;
        int v0_4 = ((v2_1 + v3_7) * 31);
        if (this.stillPath != null) {
            v3_10 = this.stillPath.hashCode();
        } else {
            v3_10 = 0;
        }
        Object v3_13;
        int v2_3 = ((v0_4 + v3_10) * 31);
        if (this.airDate != null) {
            v3_13 = this.airDate.hashCode();
        } else {
            v3_13 = 0;
        }
        Object v3_16;
        int v0_6 = ((v2_3 + v3_13) * 31);
        if (this.runtime != null) {
            v3_16 = this.runtime.hashCode();
        } else {
            v3_16 = 0;
        }
        int v2_5 = ((v0_6 + v3_16) * 31);
        if (this.voteAverage != null) {
            v1_0 = this.voteAverage.hashCode();
        }
        return (v2_5 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Episode(id=").append(this.id).append(", episodeNumber=").append(this.episodeNumber).append(", name=").append(this.name).append(", overview=").append(this.overview).append(", stillPath=").append(this.stillPath).append(", airDate=").append(this.airDate).append(", runtime=").append(this.runtime).append(", voteAverage=").append(this.voteAverage).append(41).toString();
    }
}
