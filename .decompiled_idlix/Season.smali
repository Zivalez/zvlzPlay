package com.idlix;
public final class Season {
    private final java.util.List episodes;
    private final String id;
    private final String name;
    private final String posterPath;
    private final Integer seasonNumber;

    public Season()
    {
        this(0, 0, 0, 0, 0, 31, 0);
        return;
    }

    public Season(String p1, Integer p2, String p3, String p4, java.util.List p5)
    {
        this.id = p1;
        this.seasonNumber = p2;
        this.name = p3;
        this.posterPath = p4;
        this.episodes = p5;
        return;
    }

    public synthetic Season(String p5, Integer p6, String p7, String p8, java.util.List p9, int p10, kotlin.jvm.internal.DefaultConstructorMarker p11)
    {
        String v11_1;
        if ((p10 & 1) == 0) {
            v11_1 = p5;
        } else {
            v11_1 = 0;
        }
        Integer v1;
        if ((p10 & 2) == 0) {
            v1 = p6;
        } else {
            v1 = 0;
        }
        String v2;
        if ((p10 & 4) == 0) {
            v2 = p7;
        } else {
            v2 = 0;
        }
        String v3;
        if ((p10 & 8) == 0) {
            v3 = p8;
        } else {
            v3 = 0;
        }
        java.util.List v10_1;
        if ((p10 & 16) == 0) {
            v10_1 = p9;
        } else {
            v10_1 = 0;
        }
        this(v11_1, v1, v2, v3, v10_1);
        return;
    }

    public static synthetic com.idlix.Season copy$default(com.idlix.Season p3, String p4, Integer p5, String p6, String p7, java.util.List p8, int p9, Object p10)
    {
        if ((p9 & 1) != 0) {
            p4 = p3.id;
        }
        if ((p9 & 2) != 0) {
            p5 = p3.seasonNumber;
        }
        if ((p9 & 4) != 0) {
            p6 = p3.name;
        }
        if ((p9 & 8) != 0) {
            p7 = p3.posterPath;
        }
        if ((p9 & 16) != 0) {
            p8 = p3.episodes;
        }
        return p3.copy(p4, p5, p6, p7, p8);
    }

    public final String component1()
    {
        return this.id;
    }

    public final Integer component2()
    {
        return this.seasonNumber;
    }

    public final String component3()
    {
        return this.name;
    }

    public final String component4()
    {
        return this.posterPath;
    }

    public final java.util.List component5()
    {
        return this.episodes;
    }

    public final com.idlix.Season copy(String p8, Integer p9, String p10, String p11, java.util.List p12)
    {
        com.idlix.Season v6 = new com.idlix.Season;
        v6(p8, p9, p10, p11, p12);
        return v6;
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Season)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.Season) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.seasonNumber, ((com.idlix.Season) p6).seasonNumber)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.name, ((com.idlix.Season) p6).name)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.posterPath, ((com.idlix.Season) p6).posterPath)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.episodes, ((com.idlix.Season) p6).episodes)) {
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

    public final java.util.List getEpisodes()
    {
        return this.episodes;
    }

    public final String getId()
    {
        return this.id;
    }

    public final String getName()
    {
        return this.name;
    }

    public final String getPosterPath()
    {
        return this.posterPath;
    }

    public final Integer getSeasonNumber()
    {
        return this.seasonNumber;
    }

    public int hashCode()
    {
        int v0_6;
        int v1_0 = 0;
        if (this.id != null) {
            v0_6 = this.id.hashCode();
        } else {
            v0_6 = 0;
        }
        java.util.List v3_1;
        int v2_3 = (v0_6 * 31);
        if (this.seasonNumber != null) {
            v3_1 = this.seasonNumber.hashCode();
        } else {
            v3_1 = 0;
        }
        java.util.List v3_4;
        int v0_1 = ((v2_3 + v3_1) * 31);
        if (this.name != null) {
            v3_4 = this.name.hashCode();
        } else {
            v3_4 = 0;
        }
        java.util.List v3_7;
        int v2_1 = ((v0_1 + v3_4) * 31);
        if (this.posterPath != null) {
            v3_7 = this.posterPath.hashCode();
        } else {
            v3_7 = 0;
        }
        int v0_4 = ((v2_1 + v3_7) * 31);
        if (this.episodes != null) {
            v1_0 = this.episodes.hashCode();
        }
        return (v0_4 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Season(id=").append(this.id).append(", seasonNumber=").append(this.seasonNumber).append(", name=").append(this.name).append(", posterPath=").append(this.posterPath).append(", episodes=").append(this.episodes).append(41).toString();
    }
}
