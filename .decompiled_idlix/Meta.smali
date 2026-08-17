package com.idlix;
public final class Meta {
    private final String country;
    private final String genre;
    private final String network;
    private final String sort;
    private final String year;

    public Meta()
    {
        this(0, 0, 0, 0, 0, 31, 0);
        return;
    }

    public Meta(String p1, String p2, String p3, String p4, String p5)
    {
        this.genre = p1;
        this.country = p2;
        this.year = p3;
        this.network = p4;
        this.sort = p5;
        return;
    }

    public synthetic Meta(String p5, String p6, String p7, String p8, String p9, int p10, kotlin.jvm.internal.DefaultConstructorMarker p11)
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
        String v10_1;
        if ((p10 & 16) == 0) {
            v10_1 = p9;
        } else {
            v10_1 = 0;
        }
        this(v11_1, v1, v2, v3, v10_1);
        return;
    }

    public static synthetic com.idlix.Meta copy$default(com.idlix.Meta p3, String p4, String p5, String p6, String p7, String p8, int p9, Object p10)
    {
        if ((p9 & 1) != 0) {
            p4 = p3.genre;
        }
        if ((p9 & 2) != 0) {
            p5 = p3.country;
        }
        if ((p9 & 4) != 0) {
            p6 = p3.year;
        }
        if ((p9 & 8) != 0) {
            p7 = p3.network;
        }
        if ((p9 & 16) != 0) {
            p8 = p3.sort;
        }
        return p3.copy(p4, p5, p6, p7, p8);
    }

    public final String component1()
    {
        return this.genre;
    }

    public final String component2()
    {
        return this.country;
    }

    public final String component3()
    {
        return this.year;
    }

    public final String component4()
    {
        return this.network;
    }

    public final String component5()
    {
        return this.sort;
    }

    public final com.idlix.Meta copy(String p8, String p9, String p10, String p11, String p12)
    {
        com.idlix.Meta v6 = new com.idlix.Meta;
        v6(p8, p9, p10, p11, p12);
        return v6;
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Meta)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.genre, ((com.idlix.Meta) p6).genre)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.country, ((com.idlix.Meta) p6).country)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.year, ((com.idlix.Meta) p6).year)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.network, ((com.idlix.Meta) p6).network)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.sort, ((com.idlix.Meta) p6).sort)) {
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

    public final String getCountry()
    {
        return this.country;
    }

    public final String getGenre()
    {
        return this.genre;
    }

    public final String getNetwork()
    {
        return this.network;
    }

    public final String getSort()
    {
        return this.sort;
    }

    public final String getYear()
    {
        return this.year;
    }

    public int hashCode()
    {
        int v0_6;
        int v1_0 = 0;
        if (this.genre != null) {
            v0_6 = this.genre.hashCode();
        } else {
            v0_6 = 0;
        }
        String v3_1;
        int v2_3 = (v0_6 * 31);
        if (this.country != null) {
            v3_1 = this.country.hashCode();
        } else {
            v3_1 = 0;
        }
        String v3_4;
        int v0_1 = ((v2_3 + v3_1) * 31);
        if (this.year != null) {
            v3_4 = this.year.hashCode();
        } else {
            v3_4 = 0;
        }
        String v3_7;
        int v2_1 = ((v0_1 + v3_4) * 31);
        if (this.network != null) {
            v3_7 = this.network.hashCode();
        } else {
            v3_7 = 0;
        }
        int v0_4 = ((v2_1 + v3_7) * 31);
        if (this.sort != null) {
            v1_0 = this.sort.hashCode();
        }
        return (v0_4 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Meta(genre=").append(this.genre).append(", country=").append(this.country).append(", year=").append(this.year).append(", network=").append(this.network).append(", sort=").append(this.sort).append(41).toString();
    }
}
