package com.idlix;
public final class SearchApiResult {
    private final String backdropPath;
    private final String contentType;
    private final String firstAirDate;
    private final java.util.List genres;
    private final String id;
    private final Long numberOfSeasons;
    private final String originalLanguage;
    private final String originalTitle;
    private final String overview;
    private final double popularity;
    private final String posterPath;
    private final String quality;
    private final String releaseDate;
    private final String slug;
    private final String title;
    private final long viewCount;
    private final double voteAverage;

    public SearchApiResult(String p17, String p18, String p19, String p20, String p21, java.util.List p22, String p23, double p24, long p26, double p28, String p30, String p31, String p32, String p33, Long p34, String p35, String p36)
    {
        this.id = p17;
        this.contentType = p18;
        this.title = p19;
        this.originalTitle = p20;
        this.overview = p21;
        this.genres = p22;
        this.originalLanguage = p23;
        this.voteAverage = p24;
        this.viewCount = p26;
        this.popularity = p28;
        this.posterPath = p30;
        this.backdropPath = p31;
        this.slug = p32;
        this.firstAirDate = p33;
        this.numberOfSeasons = p34;
        this.releaseDate = p35;
        this.quality = p36;
        return;
    }

    public static synthetic com.idlix.SearchApiResult copy$default(com.idlix.SearchApiResult p17, String p18, String p19, String p20, String p21, String p22, java.util.List p23, String p24, double p25, long p27, double p29, String p31, String p32, String p33, String p34, Long p35, String p36, String p37, int p38, Object p39)
    {
        String v2_1;
        if ((p38 & 1) == 0) {
            v2_1 = p18;
        } else {
            v2_1 = p17.id;
        }
        String v3_1;
        if ((p38 & 2) == 0) {
            v3_1 = p19;
        } else {
            v3_1 = p17.contentType;
        }
        String v4_1;
        if ((p38 & 4) == 0) {
            v4_1 = p20;
        } else {
            v4_1 = p17.title;
        }
        String v5_1;
        if ((p38 & 8) == 0) {
            v5_1 = p21;
        } else {
            v5_1 = p17.originalTitle;
        }
        String v6_1;
        if ((p38 & 16) == 0) {
            v6_1 = p22;
        } else {
            v6_1 = p17.overview;
        }
        java.util.List v7_1;
        if ((p38 & 32) == 0) {
            v7_1 = p23;
        } else {
            v7_1 = p17.genres;
        }
        String v8_1;
        if ((p38 & 64) == 0) {
            v8_1 = p24;
        } else {
            v8_1 = p17.originalLanguage;
        }
        double v9_1;
        if ((p38 & 128) == 0) {
            v9_1 = p25;
        } else {
            v9_1 = p17.voteAverage;
        }
        long v11_1;
        if ((p38 & 256) == 0) {
            v11_1 = p27;
        } else {
            v11_1 = p17.viewCount;
        }
        double v13_1;
        if ((p38 & 512) == 0) {
            v13_1 = p29;
        } else {
            v13_1 = p17.popularity;
        }
        String v15_1;
        if ((p38 & 1024) == 0) {
            v15_1 = p31;
        } else {
            v15_1 = p17.posterPath;
        }
        String v15_3;
        if ((p38 & 2048) == 0) {
            v15_3 = p32;
        } else {
            v15_3 = p17.backdropPath;
        }
        String v15_5;
        if ((p38 & 4096) == 0) {
            v15_5 = p33;
        } else {
            v15_5 = p17.slug;
        }
        String v15_7;
        if ((p38 & 8192) == 0) {
            v15_7 = p34;
        } else {
            v15_7 = p17.firstAirDate;
        }
        String v15_9;
        if ((p38 & 16384) == 0) {
            v15_9 = p35;
        } else {
            v15_9 = p17.numberOfSeasons;
        }
        String v15_10;
        if ((p38 & 32768) == 0) {
            v15_10 = p36;
        } else {
            v15_10 = p17.releaseDate;
        }
        String v1_2;
        if ((p38 & 65536) == 0) {
            v1_2 = p37;
        } else {
            v1_2 = p17.quality;
        }
        return p17.copy(v2_1, v3_1, v4_1, v5_1, v6_1, v7_1, v8_1, v9_1, v11_1, v13_1, v15_1, v15_3, v15_5, v15_7, v15_9, v15_10, v1_2);
    }

    public final String component1()
    {
        return this.id;
    }

    public final double component10()
    {
        return this.popularity;
    }

    public final String component11()
    {
        return this.posterPath;
    }

    public final String component12()
    {
        return this.backdropPath;
    }

    public final String component13()
    {
        return this.slug;
    }

    public final String component14()
    {
        return this.firstAirDate;
    }

    public final Long component15()
    {
        return this.numberOfSeasons;
    }

    public final String component16()
    {
        return this.releaseDate;
    }

    public final String component17()
    {
        return this.quality;
    }

    public final String component2()
    {
        return this.contentType;
    }

    public final String component3()
    {
        return this.title;
    }

    public final String component4()
    {
        return this.originalTitle;
    }

    public final String component5()
    {
        return this.overview;
    }

    public final java.util.List component6()
    {
        return this.genres;
    }

    public final String component7()
    {
        return this.originalLanguage;
    }

    public final double component8()
    {
        return this.voteAverage;
    }

    public final long component9()
    {
        return this.viewCount;
    }

    public final com.idlix.SearchApiResult copy(String p23, String p24, String p25, String p26, String p27, java.util.List p28, String p29, double p30, long p32, double p34, String p36, String p37, String p38, String p39, Long p40, String p41, String p42)
    {
        com.idlix.SearchApiResult v21 = new com.idlix.SearchApiResult;
        v21(p23, p24, p25, p26, p27, p28, p29, p30, p32, p34, p36, p37, p38, p39, p40, p41, p42);
        return v21;
    }

    public boolean equals(Object p8)
    {
        if (this != p8) {
            if ((p8 instanceof com.idlix.SearchApiResult)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.SearchApiResult) p8).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.contentType, ((com.idlix.SearchApiResult) p8).contentType)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.title, ((com.idlix.SearchApiResult) p8).title)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.originalTitle, ((com.idlix.SearchApiResult) p8).originalTitle)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.overview, ((com.idlix.SearchApiResult) p8).overview)) {
                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.genres, ((com.idlix.SearchApiResult) p8).genres)) {
                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.originalLanguage, ((com.idlix.SearchApiResult) p8).originalLanguage)) {
                                            if (Double.compare(this.voteAverage, ((com.idlix.SearchApiResult) p8).voteAverage) == 0) {
                                                if (this.viewCount == ((com.idlix.SearchApiResult) p8).viewCount) {
                                                    if (Double.compare(this.popularity, ((com.idlix.SearchApiResult) p8).popularity) == 0) {
                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.posterPath, ((com.idlix.SearchApiResult) p8).posterPath)) {
                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.backdropPath, ((com.idlix.SearchApiResult) p8).backdropPath)) {
                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.slug, ((com.idlix.SearchApiResult) p8).slug)) {
                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.firstAirDate, ((com.idlix.SearchApiResult) p8).firstAirDate)) {
                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.numberOfSeasons, ((com.idlix.SearchApiResult) p8).numberOfSeasons)) {
                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.releaseDate, ((com.idlix.SearchApiResult) p8).releaseDate)) {
                                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.quality, ((com.idlix.SearchApiResult) p8).quality)) {
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

    public final String getBackdropPath()
    {
        return this.backdropPath;
    }

    public final String getContentType()
    {
        return this.contentType;
    }

    public final String getFirstAirDate()
    {
        return this.firstAirDate;
    }

    public final java.util.List getGenres()
    {
        return this.genres;
    }

    public final String getId()
    {
        return this.id;
    }

    public final Long getNumberOfSeasons()
    {
        return this.numberOfSeasons;
    }

    public final String getOriginalLanguage()
    {
        return this.originalLanguage;
    }

    public final String getOriginalTitle()
    {
        return this.originalTitle;
    }

    public final String getOverview()
    {
        return this.overview;
    }

    public final double getPopularity()
    {
        return this.popularity;
    }

    public final String getPosterPath()
    {
        return this.posterPath;
    }

    public final String getQuality()
    {
        return this.quality;
    }

    public final String getReleaseDate()
    {
        return this.releaseDate;
    }

    public final String getSlug()
    {
        return this.slug;
    }

    public final String getTitle()
    {
        return this.title;
    }

    public final long getViewCount()
    {
        return this.viewCount;
    }

    public final double getVoteAverage()
    {
        return this.voteAverage;
    }

    public int hashCode()
    {
        String v2_25;
        int v1_11 = (((((((((((((((((((((((((this.id.hashCode() * 31) + this.contentType.hashCode()) * 31) + this.title.hashCode()) * 31) + this.originalTitle.hashCode()) * 31) + this.overview.hashCode()) * 31) + this.genres.hashCode()) * 31) + this.originalLanguage.hashCode()) * 31) + Double.hashCode(this.voteAverage)) * 31) + Long.hashCode(this.viewCount)) * 31) + Double.hashCode(this.popularity)) * 31) + this.posterPath.hashCode()) * 31) + this.backdropPath.hashCode()) * 31) + this.slug.hashCode()) * 31);
        int v3 = 0;
        if (this.firstAirDate != null) {
            v2_25 = this.firstAirDate.hashCode();
        } else {
            v2_25 = 0;
        }
        String v2_28;
        int v0_13 = ((v1_11 + v2_25) * 31);
        if (this.numberOfSeasons != null) {
            v2_28 = this.numberOfSeasons.hashCode();
        } else {
            v2_28 = 0;
        }
        String v2_31;
        int v1_14 = ((v0_13 + v2_28) * 31);
        if (this.releaseDate != null) {
            v2_31 = this.releaseDate.hashCode();
        } else {
            v2_31 = 0;
        }
        int v0_16 = ((v1_14 + v2_31) * 31);
        if (this.quality != null) {
            v3 = this.quality.hashCode();
        }
        return (v0_16 + v3);
    }

    public String toString()
    {
        String v0_1 = new StringBuilder();
        v0_1.append("SearchApiResult(id=").append(this.id).append(", contentType=").append(this.contentType).append(", title=").append(this.title).append(", originalTitle=").append(this.originalTitle).append(", overview=").append(this.overview).append(", genres=").append(this.genres).append(", originalLanguage=").append(this.originalLanguage).append(", voteAverage=").append(this.voteAverage).append(", viewCount=").append(this.viewCount).append(", popularity=").append(this.popularity).append(", posterPath=").append(this.posterPath).append(", backdropPath=");
        v0_1.append(this.backdropPath).append(", slug=").append(this.slug).append(", firstAirDate=").append(this.firstAirDate).append(", numberOfSeasons=").append(this.numberOfSeasons).append(", releaseDate=").append(this.releaseDate).append(", quality=").append(this.quality).append(41);
        return v0_1.toString();
    }
}
