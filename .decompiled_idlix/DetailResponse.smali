package com.idlix;
public final class DetailResponse {
    private final String backdropPath;
    private final java.util.List backdrops;
    private final java.util.List cast;
    private final String country;
    private final String director;
    private final String firstAirDate;
    private final com.idlix.Season firstSeason;
    private final java.util.List genres;
    private final String id;
    private final String imdbId;
    private final Boolean isPublished;
    private final String logoPath;
    private final String originalLanguage;
    private final String overview;
    private final Object popularity;
    private final String posterPath;
    private final String quality;
    private final String releaseDate;
    private final Integer runtime;
    private final java.util.List seasons;
    private final String slug;
    private final String status;
    private final String tagline;
    private final String title;
    private final String tmdbId;
    private final String trailerUrl;
    private final Object viewCount;
    private final Object voteAverage;

    public DetailResponse()
    {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 268435455, 0);
        return;
    }

    public DetailResponse(String p17, String p18, String p19, String p20, String p21, String p22, String p23, String p24, String p25, String p26, java.util.List p27, String p28, String p29, Integer p30, Object p31, Object p32, String p33, String p34, String p35, String p36, String p37, String p38, java.util.List p39, java.util.List p40, java.util.List p41, com.idlix.Season p42, Object p43, Boolean p44)
    {
        this.id = p17;
        this.title = p18;
        this.slug = p19;
        this.imdbId = p20;
        this.tmdbId = p21;
        this.overview = p22;
        this.tagline = p23;
        this.posterPath = p24;
        this.backdropPath = p25;
        this.logoPath = p26;
        this.backdrops = p27;
        this.releaseDate = p28;
        this.firstAirDate = p29;
        this.runtime = p30;
        this.voteAverage = p31;
        this.popularity = p32;
        this.originalLanguage = p33;
        this.country = p34;
        this.status = p35;
        this.trailerUrl = p36;
        this.quality = p37;
        this.director = p38;
        this.genres = p39;
        this.cast = p40;
        this.seasons = p41;
        this.firstSeason = p42;
        this.viewCount = p43;
        this.isPublished = p44;
        return;
    }

    public synthetic DetailResponse(String p30, String p31, String p32, String p33, String p34, String p35, String p36, String p37, String p38, String p39, java.util.List p40, String p41, String p42, Integer p43, Object p44, Object p45, String p46, String p47, String p48, String p49, String p50, String p51, java.util.List p52, java.util.List p53, java.util.List p54, com.idlix.Season p55, Object p56, Boolean p57, int p58, kotlin.jvm.internal.DefaultConstructorMarker p59)
    {
        String v1_1;
        if ((p58 & 1) == 0) {
            v1_1 = p30;
        } else {
            v1_1 = 0;
        }
        String v3_1;
        if ((p58 & 2) == 0) {
            v3_1 = p31;
        } else {
            v3_1 = 0;
        }
        String v4_0;
        if ((p58 & 4) == 0) {
            v4_0 = p32;
        } else {
            v4_0 = 0;
        }
        String v5_1;
        if ((p58 & 8) == 0) {
            v5_1 = p33;
        } else {
            v5_1 = 0;
        }
        String v6_1;
        if ((p58 & 16) == 0) {
            v6_1 = p34;
        } else {
            v6_1 = 0;
        }
        String v7_1;
        if ((p58 & 32) == 0) {
            v7_1 = p35;
        } else {
            v7_1 = 0;
        }
        String v8_1;
        if ((p58 & 64) == 0) {
            v8_1 = p36;
        } else {
            v8_1 = 0;
        }
        String v9_1;
        if ((p58 & 128) == 0) {
            v9_1 = p37;
        } else {
            v9_1 = 0;
        }
        String v10_1;
        if ((p58 & 256) == 0) {
            v10_1 = p38;
        } else {
            v10_1 = 0;
        }
        String v11_1;
        if ((p58 & 512) == 0) {
            v11_1 = p39;
        } else {
            v11_1 = 0;
        }
        java.util.List v12_1;
        if ((p58 & 1024) == 0) {
            v12_1 = p40;
        } else {
            v12_1 = 0;
        }
        String v13_1;
        if ((p58 & 2048) == 0) {
            v13_1 = p41;
        } else {
            v13_1 = 0;
        }
        String v14_1;
        if ((p58 & 4096) == 0) {
            v14_1 = p42;
        } else {
            v14_1 = 0;
        }
        Integer v15_1;
        if ((p58 & 8192) == 0) {
            v15_1 = p43;
        } else {
            v15_1 = 0;
        }
        Object v2_1;
        if ((p58 & 16384) == 0) {
            v2_1 = p44;
        } else {
            v2_1 = 0;
        }
        Object v16_2;
        if ((p58 & 32768) == 0) {
            v16_2 = p45;
        } else {
            v16_2 = 0;
        }
        String v17_2;
        if ((p58 & 65536) == 0) {
            v17_2 = p46;
        } else {
            v17_2 = 0;
        }
        String v18_2;
        if ((p58 & 131072) == 0) {
            v18_2 = p47;
        } else {
            v18_2 = 0;
        }
        String v19_2;
        if ((p58 & 262144) == 0) {
            v19_2 = p48;
        } else {
            v19_2 = 0;
        }
        String v20_2;
        if ((p58 & 524288) == 0) {
            v20_2 = p49;
        } else {
            v20_2 = 0;
        }
        String v21_2;
        if ((p58 & 1048576) == 0) {
            v21_2 = p50;
        } else {
            v21_2 = 0;
        }
        String v22_2;
        if ((p58 & 2097152) == 0) {
            v22_2 = p51;
        } else {
            v22_2 = 0;
        }
        java.util.List v23_0;
        if ((p58 & 4194304) == 0) {
            v23_0 = p52;
        } else {
            v23_0 = 0;
        }
        java.util.List v24_2;
        if ((p58 & 8388608) == 0) {
            v24_2 = p53;
        } else {
            v24_2 = 0;
        }
        java.util.List v25_2;
        if ((p58 & 16777216) == 0) {
            v25_2 = p54;
        } else {
            v25_2 = 0;
        }
        com.idlix.Season v26_2;
        if ((p58 & 33554432) == 0) {
            v26_2 = p55;
        } else {
            v26_2 = 0;
        }
        Object v27_2;
        if ((p58 & 67108864) == 0) {
            v27_2 = p56;
        } else {
            v27_2 = 0;
        }
        Boolean v0_2;
        if ((p58 & 134217728) == 0) {
            v0_2 = p57;
        } else {
            v0_2 = 0;
        }
        this(v1_1, v3_1, v4_0, v5_1, v6_1, v7_1, v8_1, v9_1, v10_1, v11_1, v12_1, v13_1, v14_1, v15_1, v2_1, v16_2, v17_2, v18_2, v19_2, v20_2, v21_2, v22_2, v23_0, v24_2, v25_2, v26_2, v27_2, v0_2);
        return;
    }

    public static synthetic com.idlix.DetailResponse copy$default(com.idlix.DetailResponse p17, String p18, String p19, String p20, String p21, String p22, String p23, String p24, String p25, String p26, String p27, java.util.List p28, String p29, String p30, Integer p31, Object p32, Object p33, String p34, String p35, String p36, String p37, String p38, String p39, java.util.List p40, java.util.List p41, java.util.List p42, com.idlix.Season p43, Object p44, Boolean p45, int p46, Object p47)
    {
        String v2_1;
        if ((p46 & 1) == 0) {
            v2_1 = p18;
        } else {
            v2_1 = p17.id;
        }
        String v3_1;
        if ((p46 & 2) == 0) {
            v3_1 = p19;
        } else {
            v3_1 = p17.title;
        }
        String v4_1;
        if ((p46 & 4) == 0) {
            v4_1 = p20;
        } else {
            v4_1 = p17.slug;
        }
        String v5_1;
        if ((p46 & 8) == 0) {
            v5_1 = p21;
        } else {
            v5_1 = p17.imdbId;
        }
        String v6_1;
        if ((p46 & 16) == 0) {
            v6_1 = p22;
        } else {
            v6_1 = p17.tmdbId;
        }
        String v7_1;
        if ((p46 & 32) == 0) {
            v7_1 = p23;
        } else {
            v7_1 = p17.overview;
        }
        String v8_1;
        if ((p46 & 64) == 0) {
            v8_1 = p24;
        } else {
            v8_1 = p17.tagline;
        }
        String v9_1;
        if ((p46 & 128) == 0) {
            v9_1 = p25;
        } else {
            v9_1 = p17.posterPath;
        }
        String v10_1;
        if ((p46 & 256) == 0) {
            v10_1 = p26;
        } else {
            v10_1 = p17.backdropPath;
        }
        String v11_1;
        if ((p46 & 512) == 0) {
            v11_1 = p27;
        } else {
            v11_1 = p17.logoPath;
        }
        java.util.List v12_1;
        if ((p46 & 1024) == 0) {
            v12_1 = p28;
        } else {
            v12_1 = p17.backdrops;
        }
        String v13_1;
        if ((p46 & 2048) == 0) {
            v13_1 = p29;
        } else {
            v13_1 = p17.releaseDate;
        }
        String v14_1;
        if ((p46 & 4096) == 0) {
            v14_1 = p30;
        } else {
            v14_1 = p17.firstAirDate;
        }
        Object v15_7;
        if ((p46 & 8192) == 0) {
            v15_7 = p31;
        } else {
            v15_7 = p17.runtime;
        }
        Object v15_9;
        if ((p46 & 16384) == 0) {
            v15_9 = p32;
        } else {
            v15_9 = p17.voteAverage;
        }
        Object v15_10;
        if ((p46 & 32768) == 0) {
            v15_10 = p33;
        } else {
            v15_10 = p17.popularity;
        }
        Object v15_11;
        if ((p46 & 65536) == 0) {
            v15_11 = p34;
        } else {
            v15_11 = p17.originalLanguage;
        }
        Object v15_12;
        if ((p46 & 131072) == 0) {
            v15_12 = p35;
        } else {
            v15_12 = p17.country;
        }
        Object v15_13;
        if ((p46 & 262144) == 0) {
            v15_13 = p36;
        } else {
            v15_13 = p17.status;
        }
        Object v15_14;
        if ((p46 & 524288) == 0) {
            v15_14 = p37;
        } else {
            v15_14 = p17.trailerUrl;
        }
        Object v15_15;
        if ((p46 & 1048576) == 0) {
            v15_15 = p38;
        } else {
            v15_15 = p17.quality;
        }
        Object v15_0;
        if ((p46 & 2097152) == 0) {
            v15_0 = p39;
        } else {
            v15_0 = p17.director;
        }
        Object v15_1;
        if ((p46 & 4194304) == 0) {
            v15_1 = p40;
        } else {
            v15_1 = p17.genres;
        }
        Object v15_2;
        if ((p46 & 8388608) == 0) {
            v15_2 = p41;
        } else {
            v15_2 = p17.cast;
        }
        Object v15_3;
        if ((p46 & 16777216) == 0) {
            v15_3 = p42;
        } else {
            v15_3 = p17.seasons;
        }
        Object v15_4;
        if ((p46 & 33554432) == 0) {
            v15_4 = p43;
        } else {
            v15_4 = p17.firstSeason;
        }
        Object v15_5;
        if ((p46 & 67108864) == 0) {
            v15_5 = p44;
        } else {
            v15_5 = p17.viewCount;
        }
        Boolean v1_2;
        if ((p46 & 134217728) == 0) {
            v1_2 = p45;
        } else {
            v1_2 = p17.isPublished;
        }
        return p17.copy(v2_1, v3_1, v4_1, v5_1, v6_1, v7_1, v8_1, v9_1, v10_1, v11_1, v12_1, v13_1, v14_1, v15_7, v15_9, v15_10, v15_11, v15_12, v15_13, v15_14, v15_15, v15_0, v15_1, v15_2, v15_3, v15_4, v15_5, v1_2);
    }

    public static synthetic void getBackdropPath$annotations()
    {
        return;
    }

    public static synthetic void getFirstAirDate$annotations()
    {
        return;
    }

    public static synthetic void getFirstSeason$annotations()
    {
        return;
    }

    public static synthetic void getImdbId$annotations()
    {
        return;
    }

    public static synthetic void getLogoPath$annotations()
    {
        return;
    }

    public static synthetic void getOriginalLanguage$annotations()
    {
        return;
    }

    public static synthetic void getPosterPath$annotations()
    {
        return;
    }

    public static synthetic void getReleaseDate$annotations()
    {
        return;
    }

    public static synthetic void getTmdbId$annotations()
    {
        return;
    }

    public static synthetic void getTrailerUrl$annotations()
    {
        return;
    }

    public static synthetic void getViewCount$annotations()
    {
        return;
    }

    public static synthetic void getVoteAverage$annotations()
    {
        return;
    }

    public static synthetic void isPublished$annotations()
    {
        return;
    }

    public final String component1()
    {
        return this.id;
    }

    public final String component10()
    {
        return this.logoPath;
    }

    public final java.util.List component11()
    {
        return this.backdrops;
    }

    public final String component12()
    {
        return this.releaseDate;
    }

    public final String component13()
    {
        return this.firstAirDate;
    }

    public final Integer component14()
    {
        return this.runtime;
    }

    public final Object component15()
    {
        return this.voteAverage;
    }

    public final Object component16()
    {
        return this.popularity;
    }

    public final String component17()
    {
        return this.originalLanguage;
    }

    public final String component18()
    {
        return this.country;
    }

    public final String component19()
    {
        return this.status;
    }

    public final String component2()
    {
        return this.title;
    }

    public final String component20()
    {
        return this.trailerUrl;
    }

    public final String component21()
    {
        return this.quality;
    }

    public final String component22()
    {
        return this.director;
    }

    public final java.util.List component23()
    {
        return this.genres;
    }

    public final java.util.List component24()
    {
        return this.cast;
    }

    public final java.util.List component25()
    {
        return this.seasons;
    }

    public final com.idlix.Season component26()
    {
        return this.firstSeason;
    }

    public final Object component27()
    {
        return this.viewCount;
    }

    public final Boolean component28()
    {
        return this.isPublished;
    }

    public final String component3()
    {
        return this.slug;
    }

    public final String component4()
    {
        return this.imdbId;
    }

    public final String component5()
    {
        return this.tmdbId;
    }

    public final String component6()
    {
        return this.overview;
    }

    public final String component7()
    {
        return this.tagline;
    }

    public final String component8()
    {
        return this.posterPath;
    }

    public final String component9()
    {
        return this.backdropPath;
    }

    public final com.idlix.DetailResponse copy(String p31, String p32, String p33, String p34, String p35, String p36, String p37, String p38, String p39, String p40, java.util.List p41, String p42, String p43, Integer p44, Object p45, Object p46, String p47, String p48, String p49, String p50, String p51, String p52, java.util.List p53, java.util.List p54, java.util.List p55, com.idlix.Season p56, Object p57, Boolean p58)
    {
        com.idlix.DetailResponse v29 = new com.idlix.DetailResponse;
        v29(p31, p32, p33, p34, p35, p36, p37, p38, p39, p40, p41, p42, p43, p44, p45, p46, p47, p48, p49, p50, p51, p52, p53, p54, p55, p56, p57, p58);
        return v29;
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.DetailResponse)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.DetailResponse) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.title, ((com.idlix.DetailResponse) p6).title)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.slug, ((com.idlix.DetailResponse) p6).slug)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.imdbId, ((com.idlix.DetailResponse) p6).imdbId)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.tmdbId, ((com.idlix.DetailResponse) p6).tmdbId)) {
                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.overview, ((com.idlix.DetailResponse) p6).overview)) {
                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.tagline, ((com.idlix.DetailResponse) p6).tagline)) {
                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.posterPath, ((com.idlix.DetailResponse) p6).posterPath)) {
                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.backdropPath, ((com.idlix.DetailResponse) p6).backdropPath)) {
                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.logoPath, ((com.idlix.DetailResponse) p6).logoPath)) {
                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.backdrops, ((com.idlix.DetailResponse) p6).backdrops)) {
                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.releaseDate, ((com.idlix.DetailResponse) p6).releaseDate)) {
                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.firstAirDate, ((com.idlix.DetailResponse) p6).firstAirDate)) {
                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.runtime, ((com.idlix.DetailResponse) p6).runtime)) {
                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.voteAverage, ((com.idlix.DetailResponse) p6).voteAverage)) {
                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.popularity, ((com.idlix.DetailResponse) p6).popularity)) {
                                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.originalLanguage, ((com.idlix.DetailResponse) p6).originalLanguage)) {
                                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.country, ((com.idlix.DetailResponse) p6).country)) {
                                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.status, ((com.idlix.DetailResponse) p6).status)) {
                                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.trailerUrl, ((com.idlix.DetailResponse) p6).trailerUrl)) {
                                                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.quality, ((com.idlix.DetailResponse) p6).quality)) {
                                                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.director, ((com.idlix.DetailResponse) p6).director)) {
                                                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.genres, ((com.idlix.DetailResponse) p6).genres)) {
                                                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.cast, ((com.idlix.DetailResponse) p6).cast)) {
                                                                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.seasons, ((com.idlix.DetailResponse) p6).seasons)) {
                                                                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.firstSeason, ((com.idlix.DetailResponse) p6).firstSeason)) {
                                                                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.viewCount, ((com.idlix.DetailResponse) p6).viewCount)) {
                                                                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.isPublished, ((com.idlix.DetailResponse) p6).isPublished)) {
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

    public final java.util.List getBackdrops()
    {
        return this.backdrops;
    }

    public final java.util.List getCast()
    {
        return this.cast;
    }

    public final String getCountry()
    {
        return this.country;
    }

    public final String getDirector()
    {
        return this.director;
    }

    public final String getFirstAirDate()
    {
        return this.firstAirDate;
    }

    public final com.idlix.Season getFirstSeason()
    {
        return this.firstSeason;
    }

    public final java.util.List getGenres()
    {
        return this.genres;
    }

    public final String getId()
    {
        return this.id;
    }

    public final String getImdbId()
    {
        return this.imdbId;
    }

    public final String getLogoPath()
    {
        return this.logoPath;
    }

    public final String getOriginalLanguage()
    {
        return this.originalLanguage;
    }

    public final String getOverview()
    {
        return this.overview;
    }

    public final Object getPopularity()
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

    public final Integer getRuntime()
    {
        return this.runtime;
    }

    public final java.util.List getSeasons()
    {
        return this.seasons;
    }

    public final String getSlug()
    {
        return this.slug;
    }

    public final String getStatus()
    {
        return this.status;
    }

    public final String getTagline()
    {
        return this.tagline;
    }

    public final String getTitle()
    {
        return this.title;
    }

    public final String getTmdbId()
    {
        return this.tmdbId;
    }

    public final String getTrailerUrl()
    {
        return this.trailerUrl;
    }

    public final Object getViewCount()
    {
        return this.viewCount;
    }

    public final Object getVoteAverage()
    {
        return this.voteAverage;
    }

    public int hashCode()
    {
        int v0_22;
        int v1_0 = 0;
        if (this.id != null) {
            v0_22 = this.id.hashCode();
        } else {
            v0_22 = 0;
        }
        Boolean v3_8;
        int v2_23 = (v0_22 * 31);
        if (this.title != null) {
            v3_8 = this.title.hashCode();
        } else {
            v3_8 = 0;
        }
        Boolean v3_41;
        int v0_7 = ((v2_23 + v3_8) * 31);
        if (this.slug != null) {
            v3_41 = this.slug.hashCode();
        } else {
            v3_41 = 0;
        }
        Boolean v3_51;
        int v2_17 = ((v0_7 + v3_41) * 31);
        if (this.imdbId != null) {
            v3_51 = this.imdbId.hashCode();
        } else {
            v3_51 = 0;
        }
        Boolean v3_54;
        int v0_19 = ((v2_17 + v3_51) * 31);
        if (this.tmdbId != null) {
            v3_54 = this.tmdbId.hashCode();
        } else {
            v3_54 = 0;
        }
        Boolean v3_57;
        int v2_19 = ((v0_19 + v3_54) * 31);
        if (this.overview != null) {
            v3_57 = this.overview.hashCode();
        } else {
            v3_57 = 0;
        }
        Boolean v3_60;
        int v0_21 = ((v2_19 + v3_57) * 31);
        if (this.tagline != null) {
            v3_60 = this.tagline.hashCode();
        } else {
            v3_60 = 0;
        }
        Boolean v3_63;
        int v2_21 = ((v0_21 + v3_60) * 31);
        if (this.posterPath != null) {
            v3_63 = this.posterPath.hashCode();
        } else {
            v3_63 = 0;
        }
        Boolean v3_66;
        int v0_24 = ((v2_21 + v3_63) * 31);
        if (this.backdropPath != null) {
            v3_66 = this.backdropPath.hashCode();
        } else {
            v3_66 = 0;
        }
        Boolean v3_69;
        int v2_24 = ((v0_24 + v3_66) * 31);
        if (this.logoPath != null) {
            v3_69 = this.logoPath.hashCode();
        } else {
            v3_69 = 0;
        }
        Boolean v3_73;
        int v0_26 = ((v2_24 + v3_69) * 31);
        if (this.backdrops != null) {
            v3_73 = this.backdrops.hashCode();
        } else {
            v3_73 = 0;
        }
        Boolean v3_76;
        int v2_26 = ((v0_26 + v3_73) * 31);
        if (this.releaseDate != null) {
            v3_76 = this.releaseDate.hashCode();
        } else {
            v3_76 = 0;
        }
        Boolean v3_1;
        int v0_28 = ((v2_26 + v3_76) * 31);
        if (this.firstAirDate != null) {
            v3_1 = this.firstAirDate.hashCode();
        } else {
            v3_1 = 0;
        }
        Boolean v3_4;
        int v2_0 = ((v0_28 + v3_1) * 31);
        if (this.runtime != null) {
            v3_4 = this.runtime.hashCode();
        } else {
            v3_4 = 0;
        }
        Boolean v3_7;
        int v0_2 = ((v2_0 + v3_4) * 31);
        if (this.voteAverage != null) {
            v3_7 = this.voteAverage.hashCode();
        } else {
            v3_7 = 0;
        }
        Boolean v3_11;
        int v2_2 = ((v0_2 + v3_7) * 31);
        if (this.popularity != null) {
            v3_11 = this.popularity.hashCode();
        } else {
            v3_11 = 0;
        }
        Boolean v3_14;
        int v0_4 = ((v2_2 + v3_11) * 31);
        if (this.originalLanguage != null) {
            v3_14 = this.originalLanguage.hashCode();
        } else {
            v3_14 = 0;
        }
        Boolean v3_17;
        int v2_4 = ((v0_4 + v3_14) * 31);
        if (this.country != null) {
            v3_17 = this.country.hashCode();
        } else {
            v3_17 = 0;
        }
        Boolean v3_20;
        int v0_6 = ((v2_4 + v3_17) * 31);
        if (this.status != null) {
            v3_20 = this.status.hashCode();
        } else {
            v3_20 = 0;
        }
        Boolean v3_23;
        int v2_7 = ((v0_6 + v3_20) * 31);
        if (this.trailerUrl != null) {
            v3_23 = this.trailerUrl.hashCode();
        } else {
            v3_23 = 0;
        }
        Boolean v3_27;
        int v0_9 = ((v2_7 + v3_23) * 31);
        if (this.quality != null) {
            v3_27 = this.quality.hashCode();
        } else {
            v3_27 = 0;
        }
        Boolean v3_30;
        int v2_9 = ((v0_9 + v3_27) * 31);
        if (this.director != null) {
            v3_30 = this.director.hashCode();
        } else {
            v3_30 = 0;
        }
        Boolean v3_34;
        int v0_11 = ((v2_9 + v3_30) * 31);
        if (this.genres != null) {
            v3_34 = this.genres.hashCode();
        } else {
            v3_34 = 0;
        }
        Boolean v3_37;
        int v2_11 = ((v0_11 + v3_34) * 31);
        if (this.cast != null) {
            v3_37 = this.cast.hashCode();
        } else {
            v3_37 = 0;
        }
        Boolean v3_40;
        int v0_13 = ((v2_11 + v3_37) * 31);
        if (this.seasons != null) {
            v3_40 = this.seasons.hashCode();
        } else {
            v3_40 = 0;
        }
        Boolean v3_44;
        int v2_13 = ((v0_13 + v3_40) * 31);
        if (this.firstSeason != null) {
            v3_44 = this.firstSeason.hashCode();
        } else {
            v3_44 = 0;
        }
        Boolean v3_47;
        int v0_15 = ((v2_13 + v3_44) * 31);
        if (this.viewCount != null) {
            v3_47 = this.viewCount.hashCode();
        } else {
            v3_47 = 0;
        }
        int v2_15 = ((v0_15 + v3_47) * 31);
        if (this.isPublished != null) {
            v1_0 = this.isPublished.hashCode();
        }
        return (v2_15 + v1_0);
    }

    public final Boolean isPublished()
    {
        return this.isPublished;
    }

    public String toString()
    {
        String v0_1 = new StringBuilder();
        v0_1.append("DetailResponse(id=").append(this.id).append(", title=").append(this.title).append(", slug=").append(this.slug).append(", imdbId=").append(this.imdbId).append(", tmdbId=").append(this.tmdbId).append(", overview=").append(this.overview).append(", tagline=").append(this.tagline).append(", posterPath=").append(this.posterPath).append(", backdropPath=").append(this.backdropPath).append(", logoPath=").append(this.logoPath).append(", backdrops=").append(this.backdrops).append(", releaseDate=");
        v0_1.append(this.releaseDate).append(", firstAirDate=").append(this.firstAirDate).append(", runtime=").append(this.runtime).append(", voteAverage=").append(this.voteAverage).append(", popularity=").append(this.popularity).append(", originalLanguage=").append(this.originalLanguage).append(", country=").append(this.country).append(", status=").append(this.status).append(", trailerUrl=").append(this.trailerUrl).append(", quality=").append(this.quality).append(", director=").append(this.director).append(", genres=").append(this.genres);
        v0_1.append(", cast=").append(this.cast).append(", seasons=").append(this.seasons).append(", firstSeason=").append(this.firstSeason).append(", viewCount=").append(this.viewCount).append(", isPublished=").append(this.isPublished).append(41);
        return v0_1.toString();
    }
}
