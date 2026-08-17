package com.idlix;
public final class ApiItem {
    private final String backdropPath;
    private final Integer commentCount;
    private final String contentType;
    private final String country;
    private final String createdAt;
    private final String firstAirDate;
    private final java.util.List genres;
    private final Boolean hasVideo;
    private final String id;
    private final Boolean isPublished;
    private final Integer numberOfEpisodes;
    private final Integer numberOfSeasons;
    private final String originalLanguage;
    private final Object popularity;
    private final String posterPath;
    private final String quality;
    private final String releaseDate;
    private final Integer runtime;
    private final String slug;
    private final String title;
    private final Object viewCount;
    private final String voteAverage;

    public ApiItem()
    {
        this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4194303, 0);
        return;
    }

    public ApiItem(String p17, String p18, String p19, String p20, String p21, String p22, String p23, String p24, Object p25, String p26, String p27, Integer p28, String p29, Integer p30, Integer p31, String p32, Integer p33, String p34, Object p35, java.util.List p36, Boolean p37, Boolean p38)
    {
        this.id = p17;
        this.title = p18;
        this.slug = p19;
        this.posterPath = p20;
        this.backdropPath = p21;
        this.releaseDate = p22;
        this.firstAirDate = p23;
        this.voteAverage = p24;
        this.viewCount = p25;
        this.quality = p26;
        this.country = p27;
        this.runtime = p28;
        this.createdAt = p29;
        this.numberOfSeasons = p30;
        this.numberOfEpisodes = p31;
        this.contentType = p32;
        this.commentCount = p33;
        this.originalLanguage = p34;
        this.popularity = p35;
        this.genres = p36;
        this.hasVideo = p37;
        this.isPublished = p38;
        return;
    }

    public synthetic ApiItem(String p24, String p25, String p26, String p27, String p28, String p29, String p30, String p31, Object p32, String p33, String p34, Integer p35, String p36, Integer p37, Integer p38, String p39, Integer p40, String p41, Object p42, java.util.List p43, Boolean p44, Boolean p45, int p46, kotlin.jvm.internal.DefaultConstructorMarker p47)
    {
        String v1_1;
        if ((p46 & 1) == 0) {
            v1_1 = p24;
        } else {
            v1_1 = 0;
        }
        String v3_1;
        if ((p46 & 2) == 0) {
            v3_1 = p25;
        } else {
            v3_1 = 0;
        }
        String v4_0;
        if ((p46 & 4) == 0) {
            v4_0 = p26;
        } else {
            v4_0 = 0;
        }
        String v5_1;
        if ((p46 & 8) == 0) {
            v5_1 = p27;
        } else {
            v5_1 = 0;
        }
        String v6_1;
        if ((p46 & 16) == 0) {
            v6_1 = p28;
        } else {
            v6_1 = 0;
        }
        String v7_1;
        if ((p46 & 32) == 0) {
            v7_1 = p29;
        } else {
            v7_1 = 0;
        }
        String v8_1;
        if ((p46 & 64) == 0) {
            v8_1 = p30;
        } else {
            v8_1 = 0;
        }
        String v9_1;
        if ((p46 & 128) == 0) {
            v9_1 = p31;
        } else {
            v9_1 = 0;
        }
        Object v10_1;
        if ((p46 & 256) == 0) {
            v10_1 = p32;
        } else {
            v10_1 = 0;
        }
        String v11_1;
        if ((p46 & 512) == 0) {
            v11_1 = p33;
        } else {
            v11_1 = 0;
        }
        String v12_1;
        if ((p46 & 1024) == 0) {
            v12_1 = p34;
        } else {
            v12_1 = 0;
        }
        Integer v13_1;
        if ((p46 & 2048) == 0) {
            v13_1 = p35;
        } else {
            v13_1 = 0;
        }
        String v14_1;
        if ((p46 & 4096) == 0) {
            v14_1 = p36;
        } else {
            v14_1 = 0;
        }
        Integer v15_1;
        if ((p46 & 8192) == 0) {
            v15_1 = p37;
        } else {
            v15_1 = 0;
        }
        Integer v2_1;
        if ((p46 & 16384) == 0) {
            v2_1 = p38;
        } else {
            v2_1 = 0;
        }
        String v16_2;
        if ((p46 & 32768) == 0) {
            v16_2 = p39;
        } else {
            v16_2 = 0;
        }
        Integer v17_2;
        if ((p46 & 65536) == 0) {
            v17_2 = p40;
        } else {
            v17_2 = 0;
        }
        String v18_2;
        if ((p46 & 131072) == 0) {
            v18_2 = p41;
        } else {
            v18_2 = 0;
        }
        Object v19_2;
        if ((p46 & 262144) == 0) {
            v19_2 = p42;
        } else {
            v19_2 = 0;
        }
        java.util.List v20_2;
        if ((p46 & 524288) == 0) {
            v20_2 = p43;
        } else {
            v20_2 = 0;
        }
        Boolean v21_2;
        if ((p46 & 1048576) == 0) {
            v21_2 = p44;
        } else {
            v21_2 = 0;
        }
        Boolean v0_2;
        if ((p46 & 2097152) == 0) {
            v0_2 = p45;
        } else {
            v0_2 = 0;
        }
        this(v1_1, v3_1, v4_0, v5_1, v6_1, v7_1, v8_1, v9_1, v10_1, v11_1, v12_1, v13_1, v14_1, v15_1, v2_1, v16_2, v17_2, v18_2, v19_2, v20_2, v21_2, v0_2);
        return;
    }

    public static synthetic com.idlix.ApiItem copy$default(com.idlix.ApiItem p17, String p18, String p19, String p20, String p21, String p22, String p23, String p24, String p25, Object p26, String p27, String p28, Integer p29, String p30, Integer p31, Integer p32, String p33, Integer p34, String p35, Object p36, java.util.List p37, Boolean p38, Boolean p39, int p40, Object p41)
    {
        String v2_1;
        if ((p40 & 1) == 0) {
            v2_1 = p18;
        } else {
            v2_1 = p17.id;
        }
        String v3_1;
        if ((p40 & 2) == 0) {
            v3_1 = p19;
        } else {
            v3_1 = p17.title;
        }
        String v4_1;
        if ((p40 & 4) == 0) {
            v4_1 = p20;
        } else {
            v4_1 = p17.slug;
        }
        String v5_1;
        if ((p40 & 8) == 0) {
            v5_1 = p21;
        } else {
            v5_1 = p17.posterPath;
        }
        String v6_1;
        if ((p40 & 16) == 0) {
            v6_1 = p22;
        } else {
            v6_1 = p17.backdropPath;
        }
        String v7_1;
        if ((p40 & 32) == 0) {
            v7_1 = p23;
        } else {
            v7_1 = p17.releaseDate;
        }
        String v8_1;
        if ((p40 & 64) == 0) {
            v8_1 = p24;
        } else {
            v8_1 = p17.firstAirDate;
        }
        String v9_1;
        if ((p40 & 128) == 0) {
            v9_1 = p25;
        } else {
            v9_1 = p17.voteAverage;
        }
        Object v10_1;
        if ((p40 & 256) == 0) {
            v10_1 = p26;
        } else {
            v10_1 = p17.viewCount;
        }
        String v11_1;
        if ((p40 & 512) == 0) {
            v11_1 = p27;
        } else {
            v11_1 = p17.quality;
        }
        String v12_1;
        if ((p40 & 1024) == 0) {
            v12_1 = p28;
        } else {
            v12_1 = p17.country;
        }
        Integer v13_1;
        if ((p40 & 2048) == 0) {
            v13_1 = p29;
        } else {
            v13_1 = p17.runtime;
        }
        String v14_1;
        if ((p40 & 4096) == 0) {
            v14_1 = p30;
        } else {
            v14_1 = p17.createdAt;
        }
        Boolean v15_1;
        if ((p40 & 8192) == 0) {
            v15_1 = p31;
        } else {
            v15_1 = p17.numberOfSeasons;
        }
        Boolean v15_3;
        if ((p40 & 16384) == 0) {
            v15_3 = p32;
        } else {
            v15_3 = p17.numberOfEpisodes;
        }
        Boolean v15_4;
        if ((p40 & 32768) == 0) {
            v15_4 = p33;
        } else {
            v15_4 = p17.contentType;
        }
        Boolean v15_5;
        if ((p40 & 65536) == 0) {
            v15_5 = p34;
        } else {
            v15_5 = p17.commentCount;
        }
        Boolean v15_6;
        if ((p40 & 131072) == 0) {
            v15_6 = p35;
        } else {
            v15_6 = p17.originalLanguage;
        }
        Boolean v15_7;
        if ((p40 & 262144) == 0) {
            v15_7 = p36;
        } else {
            v15_7 = p17.popularity;
        }
        Boolean v15_8;
        if ((p40 & 524288) == 0) {
            v15_8 = p37;
        } else {
            v15_8 = p17.genres;
        }
        Boolean v15_9;
        if ((p40 & 1048576) == 0) {
            v15_9 = p38;
        } else {
            v15_9 = p17.hasVideo;
        }
        Boolean v1_2;
        if ((p40 & 2097152) == 0) {
            v1_2 = p39;
        } else {
            v1_2 = p17.isPublished;
        }
        return p17.copy(v2_1, v3_1, v4_1, v5_1, v6_1, v7_1, v8_1, v9_1, v10_1, v11_1, v12_1, v13_1, v14_1, v15_1, v15_3, v15_4, v15_5, v15_6, v15_7, v15_8, v15_9, v1_2);
    }

    public final String component1()
    {
        return this.id;
    }

    public final String component10()
    {
        return this.quality;
    }

    public final String component11()
    {
        return this.country;
    }

    public final Integer component12()
    {
        return this.runtime;
    }

    public final String component13()
    {
        return this.createdAt;
    }

    public final Integer component14()
    {
        return this.numberOfSeasons;
    }

    public final Integer component15()
    {
        return this.numberOfEpisodes;
    }

    public final String component16()
    {
        return this.contentType;
    }

    public final Integer component17()
    {
        return this.commentCount;
    }

    public final String component18()
    {
        return this.originalLanguage;
    }

    public final Object component19()
    {
        return this.popularity;
    }

    public final String component2()
    {
        return this.title;
    }

    public final java.util.List component20()
    {
        return this.genres;
    }

    public final Boolean component21()
    {
        return this.hasVideo;
    }

    public final Boolean component22()
    {
        return this.isPublished;
    }

    public final String component3()
    {
        return this.slug;
    }

    public final String component4()
    {
        return this.posterPath;
    }

    public final String component5()
    {
        return this.backdropPath;
    }

    public final String component6()
    {
        return this.releaseDate;
    }

    public final String component7()
    {
        return this.firstAirDate;
    }

    public final String component8()
    {
        return this.voteAverage;
    }

    public final Object component9()
    {
        return this.viewCount;
    }

    public final com.idlix.ApiItem copy(String p25, String p26, String p27, String p28, String p29, String p30, String p31, String p32, Object p33, String p34, String p35, Integer p36, String p37, Integer p38, Integer p39, String p40, Integer p41, String p42, Object p43, java.util.List p44, Boolean p45, Boolean p46)
    {
        com.idlix.ApiItem v23 = new com.idlix.ApiItem;
        v23(p25, p26, p27, p28, p29, p30, p31, p32, p33, p34, p35, p36, p37, p38, p39, p40, p41, p42, p43, p44, p45, p46);
        return v23;
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.ApiItem)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.ApiItem) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.title, ((com.idlix.ApiItem) p6).title)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.slug, ((com.idlix.ApiItem) p6).slug)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.posterPath, ((com.idlix.ApiItem) p6).posterPath)) {
                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.backdropPath, ((com.idlix.ApiItem) p6).backdropPath)) {
                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.releaseDate, ((com.idlix.ApiItem) p6).releaseDate)) {
                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.firstAirDate, ((com.idlix.ApiItem) p6).firstAirDate)) {
                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.voteAverage, ((com.idlix.ApiItem) p6).voteAverage)) {
                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.viewCount, ((com.idlix.ApiItem) p6).viewCount)) {
                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.quality, ((com.idlix.ApiItem) p6).quality)) {
                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.country, ((com.idlix.ApiItem) p6).country)) {
                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.runtime, ((com.idlix.ApiItem) p6).runtime)) {
                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.createdAt, ((com.idlix.ApiItem) p6).createdAt)) {
                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.numberOfSeasons, ((com.idlix.ApiItem) p6).numberOfSeasons)) {
                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.numberOfEpisodes, ((com.idlix.ApiItem) p6).numberOfEpisodes)) {
                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.contentType, ((com.idlix.ApiItem) p6).contentType)) {
                                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.commentCount, ((com.idlix.ApiItem) p6).commentCount)) {
                                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.originalLanguage, ((com.idlix.ApiItem) p6).originalLanguage)) {
                                                                                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.popularity, ((com.idlix.ApiItem) p6).popularity)) {
                                                                                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.genres, ((com.idlix.ApiItem) p6).genres)) {
                                                                                                if (kotlin.jvm.internal.Intrinsics.areEqual(this.hasVideo, ((com.idlix.ApiItem) p6).hasVideo)) {
                                                                                                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.isPublished, ((com.idlix.ApiItem) p6).isPublished)) {
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
            return 1;
        }
    }

    public final String getBackdropPath()
    {
        return this.backdropPath;
    }

    public final Integer getCommentCount()
    {
        return this.commentCount;
    }

    public final String getContentType()
    {
        return this.contentType;
    }

    public final String getCountry()
    {
        return this.country;
    }

    public final String getCreatedAt()
    {
        return this.createdAt;
    }

    public final String getFirstAirDate()
    {
        return this.firstAirDate;
    }

    public final java.util.List getGenres()
    {
        return this.genres;
    }

    public final Boolean getHasVideo()
    {
        return this.hasVideo;
    }

    public final String getId()
    {
        return this.id;
    }

    public final Integer getNumberOfEpisodes()
    {
        return this.numberOfEpisodes;
    }

    public final Integer getNumberOfSeasons()
    {
        return this.numberOfSeasons;
    }

    public final String getOriginalLanguage()
    {
        return this.originalLanguage;
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

    public final String getSlug()
    {
        return this.slug;
    }

    public final String getTitle()
    {
        return this.title;
    }

    public final Object getViewCount()
    {
        return this.viewCount;
    }

    public final String getVoteAverage()
    {
        return this.voteAverage;
    }

    public int hashCode()
    {
        int v0_16;
        int v1_0 = 0;
        if (this.id != null) {
            v0_16 = this.id.hashCode();
        } else {
            v0_16 = 0;
        }
        Boolean v3_8;
        int v2_17 = (v0_16 * 31);
        if (this.title != null) {
            v3_8 = this.title.hashCode();
        } else {
            v3_8 = 0;
        }
        Boolean v3_30;
        int v0_7 = ((v2_17 + v3_8) * 31);
        if (this.slug != null) {
            v3_30 = this.slug.hashCode();
        } else {
            v3_30 = 0;
        }
        Boolean v3_33;
        int v2_11 = ((v0_7 + v3_30) * 31);
        if (this.posterPath != null) {
            v3_33 = this.posterPath.hashCode();
        } else {
            v3_33 = 0;
        }
        Boolean v3_36;
        int v0_13 = ((v2_11 + v3_33) * 31);
        if (this.backdropPath != null) {
            v3_36 = this.backdropPath.hashCode();
        } else {
            v3_36 = 0;
        }
        Boolean v3_39;
        int v2_13 = ((v0_13 + v3_36) * 31);
        if (this.releaseDate != null) {
            v3_39 = this.releaseDate.hashCode();
        } else {
            v3_39 = 0;
        }
        Boolean v3_42;
        int v0_15 = ((v2_13 + v3_39) * 31);
        if (this.firstAirDate != null) {
            v3_42 = this.firstAirDate.hashCode();
        } else {
            v3_42 = 0;
        }
        Boolean v3_45;
        int v2_15 = ((v0_15 + v3_42) * 31);
        if (this.voteAverage != null) {
            v3_45 = this.voteAverage.hashCode();
        } else {
            v3_45 = 0;
        }
        Boolean v3_48;
        int v0_18 = ((v2_15 + v3_45) * 31);
        if (this.viewCount != null) {
            v3_48 = this.viewCount.hashCode();
        } else {
            v3_48 = 0;
        }
        Boolean v3_51;
        int v2_18 = ((v0_18 + v3_48) * 31);
        if (this.quality != null) {
            v3_51 = this.quality.hashCode();
        } else {
            v3_51 = 0;
        }
        Boolean v3_55;
        int v0_20 = ((v2_18 + v3_51) * 31);
        if (this.country != null) {
            v3_55 = this.country.hashCode();
        } else {
            v3_55 = 0;
        }
        Boolean v3_58;
        int v2_20 = ((v0_20 + v3_55) * 31);
        if (this.runtime != null) {
            v3_58 = this.runtime.hashCode();
        } else {
            v3_58 = 0;
        }
        Boolean v3_1;
        int v0_22 = ((v2_20 + v3_58) * 31);
        if (this.createdAt != null) {
            v3_1 = this.createdAt.hashCode();
        } else {
            v3_1 = 0;
        }
        Boolean v3_4;
        int v2_0 = ((v0_22 + v3_1) * 31);
        if (this.numberOfSeasons != null) {
            v3_4 = this.numberOfSeasons.hashCode();
        } else {
            v3_4 = 0;
        }
        Boolean v3_7;
        int v0_2 = ((v2_0 + v3_4) * 31);
        if (this.numberOfEpisodes != null) {
            v3_7 = this.numberOfEpisodes.hashCode();
        } else {
            v3_7 = 0;
        }
        Boolean v3_11;
        int v2_2 = ((v0_2 + v3_7) * 31);
        if (this.contentType != null) {
            v3_11 = this.contentType.hashCode();
        } else {
            v3_11 = 0;
        }
        Boolean v3_14;
        int v0_4 = ((v2_2 + v3_11) * 31);
        if (this.commentCount != null) {
            v3_14 = this.commentCount.hashCode();
        } else {
            v3_14 = 0;
        }
        Boolean v3_17;
        int v2_4 = ((v0_4 + v3_14) * 31);
        if (this.originalLanguage != null) {
            v3_17 = this.originalLanguage.hashCode();
        } else {
            v3_17 = 0;
        }
        Boolean v3_20;
        int v0_6 = ((v2_4 + v3_17) * 31);
        if (this.popularity != null) {
            v3_20 = this.popularity.hashCode();
        } else {
            v3_20 = 0;
        }
        Boolean v3_23;
        int v2_7 = ((v0_6 + v3_20) * 31);
        if (this.genres != null) {
            v3_23 = this.genres.hashCode();
        } else {
            v3_23 = 0;
        }
        Boolean v3_27;
        int v0_9 = ((v2_7 + v3_23) * 31);
        if (this.hasVideo != null) {
            v3_27 = this.hasVideo.hashCode();
        } else {
            v3_27 = 0;
        }
        int v2_9 = ((v0_9 + v3_27) * 31);
        if (this.isPublished != null) {
            v1_0 = this.isPublished.hashCode();
        }
        return (v2_9 + v1_0);
    }

    public final Boolean isPublished()
    {
        return this.isPublished;
    }

    public String toString()
    {
        String v0_1 = new StringBuilder();
        v0_1.append("ApiItem(id=").append(this.id).append(", title=").append(this.title).append(", slug=").append(this.slug).append(", posterPath=").append(this.posterPath).append(", backdropPath=").append(this.backdropPath).append(", releaseDate=").append(this.releaseDate).append(", firstAirDate=").append(this.firstAirDate).append(", voteAverage=").append(this.voteAverage).append(", viewCount=").append(this.viewCount).append(", quality=").append(this.quality).append(", country=").append(this.country).append(", runtime=");
        v0_1.append(this.runtime).append(", createdAt=").append(this.createdAt).append(", numberOfSeasons=").append(this.numberOfSeasons).append(", numberOfEpisodes=").append(this.numberOfEpisodes).append(", contentType=").append(this.contentType).append(", commentCount=").append(this.commentCount).append(", originalLanguage=").append(this.originalLanguage).append(", popularity=").append(this.popularity).append(", genres=").append(this.genres).append(", hasVideo=").append(this.hasVideo).append(", isPublished=").append(this.isPublished).append(41);
        return v0_1.toString();
    }
}
