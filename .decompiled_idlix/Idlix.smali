package com.idlix;
public final class Idlix extends com.lagradost.cloudstream3.MainAPI {
    private final boolean hasDownloadSupport;
    private final boolean hasMainPage;
    private String lang;
    private final java.util.List mainPage;
    private String mainUrl;
    private String name;
    private final java.util.Set supportedTypes;

    public static synthetic kotlin.Unit $r8$lambda$9zutLBdhMXlhZ8mCAXEU1R_6Znw(com.idlix.Episode p0, int p1, com.lagradost.cloudstream3.Episode p2)
    {
        return com.idlix.Idlix.load$lambda$25$lambda$24$lambda$23(p0, p1, p2);
    }

    public static synthetic kotlin.Unit $r8$lambda$P-iKxWdLEEpabQZ5Prat4Ya96d4(com.idlix.Episode p0, com.idlix.DetailResponse p1, com.lagradost.cloudstream3.Episode p2)
    {
        return com.idlix.Idlix.load$lambda$21$lambda$20(p0, p1, p2);
    }

    public static synthetic kotlin.Unit $r8$lambda$T86_EO4GO5Gn4WcCkxp4FvqXzPE(String p0, com.idlix.ApiItem p1, com.lagradost.cloudstream3.TvSeriesSearchResponse p2)
    {
        return com.idlix.Idlix.load$lambda$18$lambda$17(p0, p1, p2);
    }

    public static synthetic kotlin.Unit $r8$lambda$jXfCnZlo-Rz5bFAKC-vlZKQM_-s(String p0, Integer p1, com.idlix.SearchApiResult p2, double p3, com.lagradost.cloudstream3.MovieSearchResponse p5)
    {
        return com.idlix.Idlix.search$lambda$9$lambda$6(p0, p1, p2, p3, p5);
    }

    public static synthetic kotlin.Unit $r8$lambda$p65ziEtxJy8ZW1oQgNQvLm-cGZQ(String p0, com.idlix.ApiItem p1, com.lagradost.cloudstream3.TvSeriesSearchResponse p2)
    {
        return com.idlix.Idlix.getMainPage$lambda$3$lambda$2(p0, p1, p2);
    }

    public static synthetic kotlin.Unit $r8$lambda$ry5qnGbBDbJa3FAu0c-m3zYLfag(String p0, com.idlix.ApiItem p1, com.lagradost.cloudstream3.MovieSearchResponse p2)
    {
        return com.idlix.Idlix.load$lambda$18$lambda$16(p0, p1, p2);
    }

    public static synthetic kotlin.Unit $r8$lambda$uS2mE6fUfAA0UGyfqfl-nHgQvzQ(String p0, Integer p1, double p2, com.lagradost.cloudstream3.TvSeriesSearchResponse p4)
    {
        return com.idlix.Idlix.search$lambda$9$lambda$8(p0, p1, p2, p4);
    }

    public static synthetic kotlin.Unit $r8$lambda$xiU2oXgf8BThEnZRGbpYQmB2d6k(String p0, com.idlix.ApiItem p1, com.lagradost.cloudstream3.MovieSearchResponse p2)
    {
        return com.idlix.Idlix.getMainPage$lambda$3$lambda$1(p0, p1, p2);
    }

    public Idlix()
    {
        this.mainUrl = "https://z2.idlixku.com";
        this.name = "Idlix";
        this.hasMainPage = 1;
        this.lang = "id";
        this.hasDownloadSupport = 1;
        kotlin.Pair[] v2_0 = new com.lagradost.cloudstream3.TvType[4];
        v2_0[0] = com.lagradost.cloudstream3.TvType.Movie;
        v2_0[1] = com.lagradost.cloudstream3.TvType.TvSeries;
        v2_0[2] = com.lagradost.cloudstream3.TvType.Anime;
        v2_0[3] = com.lagradost.cloudstream3.TvType.AsianDrama;
        this.supportedTypes = kotlin.collections.SetsKt.setOf(v2_0);
        kotlin.Pair[] v2_3 = new kotlin.Pair[14];
        v2_3[0] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&year=2026").toString(), "Terbaru");
        v2_3[1] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/movies?page=%d&limit=36&sort=createdAt").toString(), "Movie");
        v2_3[2] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/series?page=%d&limit=36&sort=createdAt").toString(), "TV Series");
        v2_3[3] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&genre=action").toString(), "Action");
        v2_3[4] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&genre=adventure").toString(), "Adventure");
        v2_3[5] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&genre=animation").toString(), "Animation");
        v2_3[6] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&genre=comedy").toString(), "Comedy");
        v2_3[7] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&genre=horror").toString(), "Horror");
        v2_3[8] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&genre=romance").toString(), "Romance");
        v2_3[9] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&network=netflix").toString(), "Netflix");
        v2_3[10] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&network=hbo").toString(), "HBO");
        v2_3[11] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&network=prime-video").toString(), "Prime Video");
        v2_3[12] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&network=disney-plus").toString(), "Disney+");
        v2_3[13] = kotlin.TuplesKt.to(new StringBuilder().append(this.getMainUrl()).append("/api/browse?page=%d&limit=36&sort=latest&network=apple-tv-plus").toString(), "Apple TV+");
        this.mainPage = com.lagradost.cloudstream3.MainAPIKt.mainPageOf(v2_3);
        return;
    }

    private static final kotlin.Unit getMainPage$lambda$3$lambda$1(String p4, com.idlix.ApiItem p5, com.lagradost.cloudstream3.MovieSearchResponse p6)
    {
        p6.setPosterUrl(p4);
        kotlin.Unit v0_3 = p5.getReleaseDate();
        Integer v1_0 = 0;
        if (v0_3 != null) {
            kotlin.Unit v0_6 = kotlin.text.StringsKt.substringBefore$default(v0_3, "-", 0, 2, 0);
            if (v0_6 != null) {
                v1_0 = kotlin.text.StringsKt.toIntOrNull(v0_6);
            }
        }
        p6.setYear(v1_0);
        p6.setQuality(com.idlix.IdlixKt.getSearchQuality(p5.getQuality()));
        p6.setScore(com.lagradost.cloudstream3.Score.Companion.from10(p5.getVoteAverage()));
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit getMainPage$lambda$3$lambda$2(String p4, com.idlix.ApiItem p5, com.lagradost.cloudstream3.TvSeriesSearchResponse p6)
    {
        p6.setPosterUrl(p4);
        kotlin.Unit v0_2 = p5.getReleaseDate();
        Integer v1_0 = 0;
        if (v0_2 != null) {
            kotlin.Unit v0_6 = kotlin.text.StringsKt.substringBefore$default(v0_2, "-", 0, 2, 0);
            if (v0_6 != null) {
                v1_0 = kotlin.text.StringsKt.toIntOrNull(v0_6);
            }
        }
        p6.setYear(v1_0);
        p6.setScore(com.lagradost.cloudstream3.Score.Companion.from10(p5.getVoteAverage()));
        p6.setQuality(com.idlix.IdlixKt.getSearchQuality(p5.getQuality()));
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit load$lambda$18$lambda$16(String p4, com.idlix.ApiItem p5, com.lagradost.cloudstream3.MovieSearchResponse p6)
    {
        p6.setPosterUrl(p4);
        kotlin.Unit v0_2 = p5.getReleaseDate();
        if (v0_2 == null) {
            v0_2 = p5.getFirstAirDate();
        }
        Integer v1 = 0;
        if (v0_2 == null) {
        } else {
            kotlin.Unit v0_0 = kotlin.text.StringsKt.substringBefore$default(v0_2, "-", 0, 2, 0);
            if (v0_0 == null) {
            } else {
                v1 = kotlin.text.StringsKt.toIntOrNull(v0_0);
            }
        }
        p6.setYear(v1);
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit load$lambda$18$lambda$17(String p4, com.idlix.ApiItem p5, com.lagradost.cloudstream3.TvSeriesSearchResponse p6)
    {
        p6.setPosterUrl(p4);
        kotlin.Unit v0_2 = p5.getReleaseDate();
        if (v0_2 == null) {
            v0_2 = p5.getFirstAirDate();
        }
        Integer v1 = 0;
        if (v0_2 == null) {
        } else {
            kotlin.Unit v0_0 = kotlin.text.StringsKt.substringBefore$default(v0_2, "-", 0, 2, 0);
            if (v0_0 == null) {
            } else {
                v1 = kotlin.text.StringsKt.toIntOrNull(v0_0);
            }
        }
        p6.setYear(v1);
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit load$lambda$21$lambda$20(com.idlix.Episode p4, com.idlix.DetailResponse p5, com.lagradost.cloudstream3.Episode p6)
    {
        int v1_1;
        p6.setName(p4.getName());
        p6.setSeason(p5.getFirstSeason().getSeasonNumber());
        p6.setEpisode(p4.getEpisodeNumber());
        p6.setDescription(p4.getOverview());
        p6.setRunTime(p4.getRuntime());
        int v1_0 = p4.getVoteAverage();
        String v2_0 = 0;
        if (v1_0 == 0) {
            v1_1 = 0;
        } else {
            v1_1 = v1_0.toString();
        }
        p6.setScore(com.lagradost.cloudstream3.Score.Companion.from10(v1_1));
        com.lagradost.cloudstream3.MainAPIKt.addDate$default(p6, p4.getAirDate(), 0, 2, 0);
        kotlin.Unit v0_6 = p4.getStillPath();
        if (v0_6 != null) {
            v2_0 = new StringBuilder().append("https://image.tmdb.org/t/p/w300").append(v0_6).toString();
        }
        p6.setPosterUrl(v2_0);
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit load$lambda$25$lambda$24$lambda$23(com.idlix.Episode p4, int p5, com.lagradost.cloudstream3.Episode p6)
    {
        int v1_1;
        p6.setName(p4.getName());
        p6.setSeason(Integer.valueOf(p5));
        p6.setEpisode(p4.getEpisodeNumber());
        p6.setDescription(p4.getOverview());
        p6.setRunTime(p4.getRuntime());
        int v1_0 = p4.getVoteAverage();
        String v2_0 = 0;
        if (v1_0 == 0) {
            v1_1 = 0;
        } else {
            v1_1 = v1_0.toString();
        }
        p6.setScore(com.lagradost.cloudstream3.Score.Companion.from10(v1_1));
        com.lagradost.cloudstream3.MainAPIKt.addDate$default(p6, p4.getAirDate(), 0, 2, 0);
        kotlin.Unit v0_6 = p4.getStillPath();
        if (v0_6 != null) {
            v2_0 = new StringBuilder().append("https://image.tmdb.org/t/p/w300").append(v0_6).toString();
        }
        p6.setPosterUrl(v2_0);
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit search$lambda$9$lambda$6(String p5, Integer p6, com.idlix.SearchApiResult p7, double p8, com.lagradost.cloudstream3.MovieSearchResponse p10)
    {
        p10.setPosterUrl(p5);
        p10.setYear(p6);
        p10.setQuality(com.lagradost.cloudstream3.MainAPIKt.getQualityFromString(p7.getQuality()));
        p10.setScore(com.lagradost.cloudstream3.Score.Companion.from10(Double.valueOf(p8)));
        return kotlin.Unit.INSTANCE;
    }

    private static final kotlin.Unit search$lambda$9$lambda$8(String p5, Integer p6, double p7, com.lagradost.cloudstream3.TvSeriesSearchResponse p9)
    {
        p9.setPosterUrl(p5);
        p9.setYear(p6);
        p9.setScore(com.lagradost.cloudstream3.Score.Companion.from10(Double.valueOf(p7)));
        return kotlin.Unit.INSTANCE;
    }

    private final String toIdlixDetailApiUrl(String p7)
    {
        String v0_3 = kotlin.text.StringsKt.trim(((CharSequence) p7)).toString();
        if (!kotlin.text.StringsKt.isBlank(((CharSequence) v0_3))) {
            if ((!kotlin.text.StringsKt.contains(((CharSequence) v0_3), ((CharSequence) "/api/movies/"), 1)) && (!kotlin.text.StringsKt.contains(((CharSequence) v0_3), ((CharSequence) "/api/series/"), 1))) {
                String v1_7 = com.idlix.Idlix.toIdlixDetailApiUrl$slugAfter(v0_3, "/movie/");
                if (v1_7 == null) {
                    String v1_9 = com.idlix.Idlix.toIdlixDetailApiUrl$slugAfter(v0_3, "/series/");
                    if (v1_9 == null) {
                        return v0_3;
                    } else {
                        return new StringBuilder().append(this.getMainUrl()).append("/api/series/").append(v1_9).toString();
                    }
                } else {
                    return new StringBuilder().append(this.getMainUrl()).append("/api/movies/").append(v1_7).toString();
                }
            } else {
                return v0_3;
            }
        } else {
            return v0_3;
        }
    }

    private static final String toIdlixDetailApiUrl$slugAfter(String p5, String p6)
    {
        String v2 = 0;
        String v0_2 = kotlin.text.StringsKt.substringBefore$default(kotlin.text.StringsKt.substringBefore$default(kotlin.text.StringsKt.substringBefore$default(kotlin.text.StringsKt.substringAfter(p5, p6, ""), "?", 0, 2, 0), "#", 0, 2, 0), "/", 0, 2, 0);
        if (!kotlin.text.StringsKt.isBlank(((CharSequence) v0_2))) {
            v2 = v0_2;
        }
        return v2;
    }

    public boolean getHasDownloadSupport()
    {
        return this.hasDownloadSupport;
    }

    public boolean getHasMainPage()
    {
        return this.hasMainPage;
    }

    public String getLang()
    {
        return this.lang;
    }

    public Object getMainPage(int p34, com.lagradost.cloudstream3.MainPageRequest p35, kotlin.coroutines.Continuation p36)
    {
        com.lagradost.cloudstream3.HomePageResponse v0_2;
        if (!(p36 instanceof com.idlix.Idlix$getMainPage$1)) {
            v0_2 = new com.idlix.Idlix$getMainPage$1(this, p36);
        } else {
            v0_2 = ((com.idlix.Idlix$getMainPage$1) p36);
            if ((((com.idlix.Idlix$getMainPage$1) p36).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Idlix$getMainPage$1) p36).label = (((com.idlix.Idlix$getMainPage$1) p36).label - -2147483648);
            }
        }
        com.lagradost.cloudstream3.MainPageRequest v20;
        String v3_4;
        com.lagradost.cloudstream3.TvType v15_1 = v0_2;
        com.idlix.Idlix$$ExternalSyntheticLambda1 v14_5 = v15_1.result;
        com.lagradost.cloudstream3.HomePageResponse v0_5 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (v15_1.label) {
            case 0:
                String v3_6;
                kotlin.ResultKt.throwOnFailure(v14_5);
                if (!kotlin.text.StringsKt.contains$default(((CharSequence) p35.getData()), ((CharSequence) "%d"), 0, 2, 0)) {
                    v3_6 = p35.getData();
                } else {
                    v3_6 = String.format(p35.getData(), java.util.Arrays.copyOf(new Object[] {kotlin.coroutines.jvm.internal.Boxing.boxInt(p34)}), 1));
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(v3_6, "format(...)");
                }
                java.util.List v4_13 = v3_6;
                String v3_9 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                v15_1.L$0 = this;
                v15_1.L$1 = p35;
                v15_1.label = 1;
                v20 = p35;
                v14_5 = com.lagradost.nicehttp.Requests.get$default(v3_9, v4_13, 0, 0, 0, 0, 0, 0, 0, 10000, 0, 0, 0, v15_1, 3838, 0);
                if (v14_5 != v0_5) {
                    v3_4 = this;
                } else {
                    return v0_5;
                }
            case 1:
                com.lagradost.cloudstream3.HomePageResponse v0_8 = ((com.lagradost.cloudstream3.MainPageRequest) v15_1.L$1);
                v3_4 = ((com.idlix.Idlix) v15_1.L$0);
                kotlin.ResultKt.throwOnFailure(v14_5);
                v20 = v0_8;
                com.idlix.Idlix$getMainPage$1 v24 = v15_1;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        com.lagradost.cloudstream3.HomePageResponse v0_14 = ((com.lagradost.nicehttp.NiceResponse) v14_5);
        try {
            int v5_6 = v0_14.getParser();
            kotlin.jvm.internal.Intrinsics.checkNotNull(v5_6);
            String v12_0 = v5_6.parseSafe(v0_14.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.ApiResponse));
        } catch (com.lagradost.cloudstream3.HomePageResponse v0_1) {
            v0_1.printStackTrace();
            v12_0 = 0;
        }
        String v12_1 = ((com.idlix.ApiResponse) v12_0);
        if (v12_1 != null) {
            java.util.List v4_2 = ((Iterable) v12_1.getData());
            java.util.Collection v7_4 = ((java.util.Collection) new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault(v4_2, 10)));
            java.util.Iterator v9_0 = v4_2.iterator();
            while (v9_0.hasNext()) {
                com.lagradost.cloudstream3.SearchResponse v10_1 = ((com.idlix.ApiItem) v9_0.next());
                int v11_0 = v10_1.getTitle();
                if (v11_0 == 0) {
                    v11_0 = "UnKnown";
                }
                String v12_2;
                String v13_0 = v11_0;
                int v11_1 = v10_1.getPosterPath();
                if (v11_1 == 0) {
                    v12_2 = 0;
                } else {
                    v12_2 = new StringBuilder().append("https://image.tmdb.org/t/p/w342").append(v11_1).toString();
                }
                com.lagradost.cloudstream3.SearchResponse v10_3;
                int v11_2 = v12_2;
                if (!kotlin.jvm.internal.Intrinsics.areEqual(v10_1.getContentType(), "movie")) {
                    v10_3 = ((com.lagradost.cloudstream3.SearchResponse) com.lagradost.cloudstream3.MainAPIKt.newTvSeriesSearchResponse$default(((com.lagradost.cloudstream3.MainAPI) v3_4), v13_0, new StringBuilder().append(v3_4.getMainUrl()).append("/api/series/").append(v10_1.getSlug()).toString(), com.lagradost.cloudstream3.TvType.TvSeries, 0, new com.idlix.Idlix$$ExternalSyntheticLambda1(v11_2, v10_1), 8, 0));
                } else {
                    v10_3 = ((com.lagradost.cloudstream3.SearchResponse) com.lagradost.cloudstream3.MainAPIKt.newMovieSearchResponse$default(((com.lagradost.cloudstream3.MainAPI) v3_4), v13_0, new StringBuilder().append(v3_4.getMainUrl()).append("/api/movies/").append(v10_1.getSlug()).toString(), com.lagradost.cloudstream3.TvType.Movie, 0, new com.idlix.Idlix$$ExternalSyntheticLambda0(v11_2, v10_1), 8, 0));
                }
                v7_4.add(v10_3);
            }
            return com.lagradost.cloudstream3.MainAPIKt.newHomePageResponse$default(v20.getName(), ((java.util.List) v7_4), 0, 4, 0);
        } else {
            return com.lagradost.cloudstream3.MainAPIKt.newHomePageResponse$default(v20.getName(), kotlin.collections.CollectionsKt.emptyList(), 0, 4, 0);
        }
    }

    public java.util.List getMainPage()
    {
        return this.mainPage;
    }

    public String getMainUrl()
    {
        return this.mainUrl;
    }

    public String getName()
    {
        return this.name;
    }

    public java.util.Set getSupportedTypes()
    {
        return this.supportedTypes;
    }

    public Object load(String p54, kotlin.coroutines.Continuation p55)
    {
        Exception v0_13;
        long v1_0 = p55;
        if (!(p55 instanceof com.idlix.Idlix$load$1)) {
            v0_13 = new com.idlix.Idlix$load$1(this, p55);
        } else {
            v0_13 = ((com.idlix.Idlix$load$1) p55);
            if ((((com.idlix.Idlix$load$1) p55).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Idlix$load$1) p55).label = (((com.idlix.Idlix$load$1) p55).label - -2147483648);
            }
        }
        int v11_9;
        com.idlix.Cast v29_0;
        com.idlix.Cast v18_0;
        com.idlix.Cast v34_0;
        long v1_1;
        com.idlix.LoadData v28_0;
        int v15_1;
        int v54_2;
        int v4_5;
        int v8_0;
        long v1_2;
        int v6_12;
        int v31_0;
        Exception v35_1;
        int v21_3;
        int v54_4;
        int v9_0;
        com.idlix.Cast v18_3;
        int v8_2;
        com.idlix.Cast v18_4;
        int v9_1;
        com.idlix.Cast v29_1;
        long v12_0;
        Exception v0_119;
        int v10_0;
        String v22_2;
        int v3_9;
        int v2_2;
        int v10_1;
        int v17_1;
        int v26_0;
        long v12_2;
        int v32_0;
        com.idlix.LoadData v14_3;
        int v16_0;
        com.lagradost.cloudstream3.utils.AppUtils v23_0;
        com.lagradost.cloudstream3.utils.AppUtils v20_0;
        com.lagradost.cloudstream3.utils.AppUtils v55_2;
        int v2_7;
        int v3_0;
        int v16_2;
        int v3_1;
        int v6_1;
        com.lagradost.cloudstream3.utils.AppUtils v33_0;
        int v3_2;
        int v17_7;
        int v7_0;
        int v11_1;
        com.lagradost.cloudstream3.utils.AppUtils v23_4;
        int v30_0;
        Exception v0_22;
        Exception v0_1;
        int v30_1;
        long v19_0;
        int v5_19;
        com.idlix.Cast v13_0;
        int v24_0;
        int v21_0;
        int v3_6;
        com.idlix.Cast v13_1;
        int v4_0;
        long v19_2;
        int v7_5;
        int v5_0;
        int v24_1;
        int v32_1;
        int v4_2;
        int v5_2;
        int v6_9;
        int v25_0;
        int v15_0 = v0_13;
        com.idlix.LoadData v14_0 = v15_0.result;
        long v12_1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (v15_0.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(v14_0);
                int v4_8 = this.toIdlixDetailApiUrl(p54);
                int v3_8 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                int v6_10 = this.getMainUrl();
                v15_0.L$0 = this;
                v15_0.label = 1;
                v28_0 = "movie";
                v29_1 = "/api/series/";
                v22_2 = 1;
                v30_0 = "episode";
                int v31_1 = v12_1;
                v32_1 = "/api/movies/";
                v23_4 = v14_0;
                v54_4 = v15_0;
                v14_0 = com.lagradost.nicehttp.Requests.get$default(v3_8, v4_8, 0, v6_10, 0, 0, 0, 0, 0, 10000, 0, 0, 0, v54_4, 3834, 0);
                v6_9 = v31_1;
                if (v14_0 != v6_9) {
                    v3_6 = this;
                    Exception v0_42 = ((com.lagradost.nicehttp.NiceResponse) v14_0);
                    try {
                        int v5_9 = v0_42.getParser();
                        kotlin.jvm.internal.Intrinsics.checkNotNull(v5_9);
                        int v7_10 = v5_9.parseSafe(v0_42.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.DetailResponse));
                    } catch (Exception v0_43) {
                        v0_43.printStackTrace();
                        v7_10 = 0;
                    }
                    int v7_11 = ((com.idlix.DetailResponse) v7_10);
                    if (v7_11 == 0) {
                        throw new com.lagradost.cloudstream3.ErrorLoadingException("Invalid JSON");
                    } else {
                        int v15_4 = v7_11;
                        Exception v0_46 = v15_4.getTitle();
                        if (v0_46 == null) {
                            v0_46 = "Unknown";
                        }
                        int v7_12;
                        com.idlix.LoadData v14_8 = v0_46;
                        Exception v0_47 = v15_4.getPosterPath();
                        if (v0_47 == null) {
                            v7_12 = 0;
                        } else {
                            v7_12 = new StringBuilder().append("https://image.tmdb.org/t/p/w500").append(v0_47).toString();
                        }
                        int v7_14;
                        long v12_7 = v7_12;
                        Exception v0_48 = v15_4.getBackdropPath();
                        if (v0_48 == null) {
                            v7_14 = 0;
                        } else {
                            v7_14 = new StringBuilder().append("https://image.tmdb.org/t/p/original").append(v0_48).toString();
                        }
                        com.idlix.Cast v13_6 = v7_14;
                        Exception v0_49 = v15_4.getReleaseDate();
                        if (v0_49 == null) {
                            v0_49 = v15_4.getFirstAirDate();
                        }
                        int v7_20;
                        int v11_8;
                        if (v0_49 == null) {
                            v11_8 = 0;
                            v7_20 = v11_8;
                        } else {
                            v11_8 = 0;
                            Exception v0_50 = kotlin.text.StringsKt.substringBefore$default(v0_49, "-", 0, 2, 0);
                            if (v0_50 == null) {
                            } else {
                                v7_20 = kotlin.text.StringsKt.toIntOrNull(v0_50);
                            }
                        }
                        Exception v0_52;
                        int v10_4 = v7_20;
                        Exception v0_51 = v15_4.getGenres();
                        if (v0_51 == null) {
                            v0_52 = kotlin.collections.CollectionsKt.emptyList();
                        } else {
                            int v8_9 = ((java.util.Collection) new java.util.ArrayList());
                            int v17_11 = ((Iterable) v0_51).iterator();
                            while (v17_11.hasNext()) {
                                long v19_5 = ((com.idlix.Genre) v17_11.next()).getName();
                                if (v19_5 != 0) {
                                    v8_9.add(v19_5);
                                }
                            }
                            v0_52 = ((java.util.List) v8_9);
                        }
                        Exception v0_62;
                        int v11_10 = v0_52;
                        new StringBuilder().append("https://image.tmdb.org/t/p/original").append(v15_4.getLogoPath()).toString();
                        Exception v0_61 = v15_4.getCast();
                        if (v0_61 == null) {
                            v0_62 = kotlin.collections.CollectionsKt.emptyList();
                        } else {
                            Exception v0_63 = ((Iterable) v0_61);
                            int v7_23 = ((java.util.Collection) new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault(v0_63, 10)));
                            int v9_4 = v0_63.iterator();
                            while (v9_4.hasNext()) {
                                int v5_7;
                                int v16_4 = ((com.idlix.Cast) v9_4.next());
                                int v17_8 = v16_4.getName();
                                if (v17_8 != 0) {
                                    v5_7 = v17_8;
                                } else {
                                    v5_7 = "";
                                }
                                Exception v0_29;
                                int v17_9 = v16_4.getProfilePath();
                                if (v17_9 == 0) {
                                    v0_29 = 0;
                                } else {
                                    long v19 = 0;
                                    v0_29 = new StringBuilder().append("https://image.tmdb.org/t/p/w185").append(v17_9).toString();
                                }
                                v7_23.add(new com.lagradost.cloudstream3.Actor(v5_7, v0_29));
                                int v5 = 2;
                            }
                            v0_62 = ((java.util.List) v7_23);
                        }
                        int v4_12;
                        int v7_24;
                        int v5_16;
                        long v1_11 = v0_62;
                        int v9_5 = v15_4.getTrailerUrl();
                        int v8_12 = v15_4.getVoteAverage();
                        if (v15_4.getSeasons() == null) {
                            v7_24 = v29_1;
                            v5_16 = v32_1;
                            v4_12 = new StringBuilder().append(v3_6.getMainUrl()).append(v5_16).append(v15_4.getSlug()).append("/related").toString();
                        } else {
                            v7_24 = v29_1;
                            v4_12 = new StringBuilder().append(v3_6.getMainUrl()).append(v7_24).append(v15_4.getSlug()).append("/related").toString();
                            v5_16 = v32_1;
                        }
                        Exception v0_89;
                        if (v15_4.getSeasons() == null) {
                            v0_89 = new StringBuilder().append(v3_6.getMainUrl()).append("/movie/").append(v15_4.getSlug()).toString();
                        } else {
                            v0_89 = new StringBuilder().append(v3_6.getMainUrl()).append("/series/").append(v15_4.getSlug()).toString();
                        }
                        int v2_16 = v0_89;
                        try {
                            Exception v0_96 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                            int v17_12 = v3_6.getMainUrl();
                            com.idlix.Cast v29_2 = v7_24;
                            int v7_25 = v54_4;
                            try {
                                v7_25.L$0 = v3_6;
                                v7_25.L$1 = v15_4;
                                v7_25.L$2 = v14_8;
                                v7_25.L$3 = v12_7;
                                v7_25.L$4 = v13_6;
                                v7_25.L$5 = v10_4;
                                v7_25.L$6 = v11_10;
                                v7_25.L$7 = v1_11;
                                v7_25.L$8 = v9_5;
                                v7_25.L$9 = v8_12;
                                v7_25.L$10 = v2_16;
                                int v54_6 = v1_11;
                                try {
                                    v7_25.label = 2;
                                    v33_0 = v5_16;
                                    v21_3 = v7_25;
                                    v34_0 = v29_2;
                                    v24_1 = v8_12;
                                    int v25_3 = v9_5;
                                    int v26_5 = v10_4;
                                    int v27_3 = v11_10;
                                    long v1_18 = v12_7;
                                    com.idlix.Cast v29_5 = v13_6;
                                    v31_0 = v14_8;
                                    v32_0 = v15_4;
                                    v35_1 = v3_6;
                                    int v3_10 = v0_96;
                                    int v36_1 = v6_9;
                                    int v6_13 = v17_12;
                                    try {
                                        v14_0 = com.lagradost.nicehttp.Requests.get$default(v3_10, v4_12, 0, v6_13, 0, 0, 0, 0, 0, 0, 0, 0, 0, v21_3, 4090, 0);
                                        v10_1 = v36_1;
                                    } catch (Exception v0) {
                                        com.idlix.LoadData v14_6 = v28_0;
                                        v4_5 = v34_0;
                                        v10_1 = v36_1;
                                        v6_12 = v54_6;
                                        v19_2 = v1_18;
                                        v3_9 = v2_16;
                                        v5_19 = v25_3;
                                        v16_2 = v26_5;
                                        v7_0 = v27_3;
                                        v18_3 = v29_5;
                                        int v45_0 = kotlin.collections.CollectionsKt.emptyList();
                                        int v25_5 = v5_19;
                                        Exception v0_110 = v6_12;
                                        long v1_21 = v7_0;
                                        int v2_24 = v32_0;
                                        int v5_21 = v3_9;
                                        if (v2_24.getSeasons() == null) {
                                            int v4_16 = ((com.lagradost.cloudstream3.MainAPI) v35_1);
                                            int v9_9 = v2_24.getId();
                                            if (v9_9 == 0) {
                                                v9_9 = "";
                                            }
                                            int v7_28 = com.lagradost.cloudstream3.utils.AppUtils.INSTANCE.toJson(new com.idlix.LoadData(v9_9, v14_6));
                                            int v8_18 = new com.idlix.Idlix$load$5;
                                            v8_18(v19_2, v18_3, v16_2, v2_24, v1_21, v24_1, v0_110, v25_5, v45_0, 0);
                                            int v8_19 = ((kotlin.jvm.functions.Function2) v8_18);
                                            v11_9 = v21_3;
                                            v11_9.L$0 = 0;
                                            v11_9.L$1 = 0;
                                            v11_9.L$2 = 0;
                                            v11_9.L$3 = 0;
                                            v11_9.L$4 = 0;
                                            v11_9.L$5 = 0;
                                            v11_9.L$6 = 0;
                                            v11_9.L$7 = 0;
                                            v11_9.L$8 = 0;
                                            v11_9.L$9 = 0;
                                            v11_9.L$10 = 0;
                                            v11_9.label = 5;
                                            v14_0 = com.lagradost.cloudstream3.MainAPIKt.newMovieLoadResponse(v4_16, v31_0, v5_21, com.lagradost.cloudstream3.TvType.Movie, v7_28, v8_19, v11_9);
                                            if (v14_0 != v10_1) {
                                                int v15 = v11_9;
                                                v0_119 = ((com.lagradost.cloudstream3.LoadResponse) v14_0);
                                                return v0_119;
                                            } else {
                                                return v10_1;
                                            }
                                        } else {
                                            int v15_8;
                                            int v3_41 = ((java.util.List) new java.util.ArrayList());
                                            int v6_15 = v2_24.getFirstSeason();
                                            if (v6_15 == 0) {
                                                v15_8 = v30_0;
                                            } else {
                                                int v6_16 = v6_15.getEpisodes();
                                                if (v6_16 == 0) {
                                                } else {
                                                    int v8_20 = ((Iterable) v6_16).iterator();
                                                    while (v8_20.hasNext()) {
                                                        int v15_2;
                                                        int v6_8 = ((com.idlix.Episode) v8_20.next());
                                                        int v11_3 = ((com.lagradost.cloudstream3.MainAPI) v35_1);
                                                        com.idlix.Cast v13_2 = v6_8.getId();
                                                        if (v13_2 != null) {
                                                            v15_2 = v30_0;
                                                            v3_41.add(com.lagradost.cloudstream3.MainAPIKt.newEpisode(v11_3, com.lagradost.cloudstream3.utils.AppUtils.INSTANCE.toJson(new com.idlix.LoadData(v13_2, v15_2)), new com.idlix.Idlix$$ExternalSyntheticLambda4(v6_8, v2_24)));
                                                        } else {
                                                            v15_2 = v30_0;
                                                        }
                                                        v30_0 = v15_2;
                                                    }
                                                    v15_8 = v30_0;
                                                }
                                            }
                                            v54_2 = p55;
                                            v9_1 = v1_21;
                                            v17_7 = v2_24;
                                            v2_2 = v3_41;
                                            v12_2 = v5_21;
                                            v55_2 = 0;
                                            v6_1 = ((Iterable) v2_24.getSeasons()).iterator();
                                            v5_0 = v10_1;
                                            v30_1 = v15_8;
                                            v8_2 = v16_2;
                                            v1_2 = v18_3;
                                            v3_2 = v19_2;
                                            v13_1 = v21_3;
                                            v16_0 = v23_4;
                                            v7_5 = v24_1;
                                            v11_1 = v25_5;
                                            v15_1 = v31_0;
                                            v18_0 = v35_1;
                                            v14_3 = v45_0;
                                            v10_0 = v0_110;
                                            if (!v6_1.hasNext()) {
                                                int v17_15 = v2_2;
                                                int v2_26 = v12_2;
                                                Exception v0_124 = ((com.lagradost.cloudstream3.MainAPI) v18_0);
                                                com.idlix.Cast v18_13 = new com.idlix.Idlix$load$4;
                                                v18_13(v3_2, v1_2, v8_2, v17_7, v9_1, v7_5, v10_0, v11_1, v14_3, 0);
                                                com.idlix.Cast v18_14 = ((kotlin.jvm.functions.Function2) v18_13);
                                                v13_1.L$0 = 0;
                                                v13_1.L$1 = 0;
                                                v13_1.L$2 = 0;
                                                v13_1.L$3 = 0;
                                                v13_1.L$4 = 0;
                                                v13_1.L$5 = 0;
                                                v13_1.L$6 = 0;
                                                v13_1.L$7 = 0;
                                                v13_1.L$8 = 0;
                                                v13_1.L$9 = 0;
                                                v13_1.L$10 = 0;
                                                v13_1.L$11 = 0;
                                                v13_1.L$12 = 0;
                                                v13_1.L$13 = 0;
                                                v13_1.label = 4;
                                                String v22_5 = v13_1;
                                                v14_0 = com.lagradost.cloudstream3.MainAPIKt.newTvSeriesLoadResponse(v0_124, v15_1, v2_26, com.lagradost.cloudstream3.TvType.TvSeries, v17_15, v18_14, v22_5);
                                                if (v14_0 != v5_0) {
                                                    v15 = v22_5;
                                                    v0_119 = ((com.lagradost.cloudstream3.LoadResponse) v14_0);
                                                    return v0_119;
                                                } else {
                                                    return v5_0;
                                                }
                                            } else {
                                                int v6_0;
                                                int v2_1;
                                                com.lagradost.cloudstream3.utils.AppUtils v20_5;
                                                int v17_0;
                                                Exception v0_129 = ((com.idlix.Season) v6_1.next()).getSeasonNumber();
                                                if (v0_129 == null) {
                                                    v29_0 = v4_5;
                                                    v20_5 = v6_1;
                                                    v4_0 = v17_7;
                                                    v6_0 = v18_0;
                                                    v17_0 = v2_2;
                                                    v2_1 = v12_2;
                                                    v12_0 = v30_1;
                                                } else {
                                                    int v5_5;
                                                    com.idlix.Cast v34_1;
                                                    com.lagradost.cloudstream3.utils.AppUtils v20_6 = v0_129.intValue();
                                                    Exception v0_132 = v17_7.getFirstSeason();
                                                    int v21_7 = 0;
                                                    if (v0_132 == null) {
                                                        v34_1 = v5_0;
                                                        v5_5 = v20_6;
                                                    } else {
                                                        Exception v0_133 = v0_132.getSeasonNumber();
                                                        if (v0_133 != null) {
                                                            v34_1 = v5_0;
                                                            v5_5 = v20_6;
                                                            if (v5_5 == v0_133.intValue()) {
                                                                v21_7 = v22_2;
                                                            }
                                                        } else {
                                                            v34_1 = v5_0;
                                                            v5_5 = v20_6;
                                                        }
                                                    }
                                                    if (v21_7 != 0) {
                                                        v29_0 = v4_5;
                                                        v20_5 = v6_1;
                                                        v4_0 = v17_7;
                                                        v6_0 = v18_0;
                                                        v17_0 = v2_2;
                                                        v2_1 = v12_2;
                                                        v12_0 = v30_1;
                                                        v5_0 = v34_1;
                                                    } else {
                                                        com.lagradost.cloudstream3.utils.AppUtils v20_1 = v6_1;
                                                        Exception v0_10 = new StringBuilder().append(v18_0.getMainUrl()).append(v4_5).append(v17_7.getSlug()).append("/season/").append(v5_5).toString();
                                                        try {
                                                            Exception v35_0 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                                                            String v38_0 = v18_0.getMainUrl();
                                                            int v6_5 = v18_0;
                                                            try {
                                                                v13_1.L$0 = v6_5;
                                                                v29_0 = v4_5;
                                                                int v4_3 = v17_7;
                                                                try {
                                                                    v13_1.L$1 = v4_3;
                                                                    v13_1.L$2 = v15_1;
                                                                    v13_1.L$3 = v3_2;
                                                                    v13_1.L$4 = v1_2;
                                                                    v13_1.L$5 = v8_2;
                                                                    v13_1.L$6 = v9_1;
                                                                    v13_1.L$7 = v10_0;
                                                                    v13_1.L$8 = v11_1;
                                                                    v13_1.L$9 = v7_5;
                                                                    v13_1.L$10 = v12_2;
                                                                    v13_1.L$11 = v14_3;
                                                                    v13_1.L$12 = v2_2;
                                                                    int v17_4 = v2_2;
                                                                    int v2_3 = v20_1;
                                                                    try {
                                                                        v13_1.L$13 = v2_3;
                                                                        v13_1.I$0 = v5_5;
                                                                        com.lagradost.cloudstream3.utils.AppUtils v20_2 = v2_3;
                                                                        try {
                                                                            v13_1.label = 3;
                                                                            int v2_5 = com.lagradost.nicehttp.Requests.get$default(v35_0, v0_10, 0, v38_0, 0, 0, 0, 0, 0, 0, 0, 0, 0, v13_1, 4090, 0);
                                                                            com.idlix.Cast v18_7 = v5_5;
                                                                            v5_5 = v34_1;
                                                                        } catch (Exception v0) {
                                                                            v23_0 = v3_2;
                                                                            v25_0 = v4_3;
                                                                            v26_0 = v6_5;
                                                                            v21_0 = v8_2;
                                                                            v24_0 = v15_1;
                                                                            v8_0 = v17_4;
                                                                            v4_2 = 0;
                                                                            v6_1 = v20_2;
                                                                            v3_0 = v55_2;
                                                                            v20_0 = v9_1;
                                                                            v19_0 = v10_0;
                                                                            v15_0 = v13_1;
                                                                            v13_0 = v14_3;
                                                                            v14_0 = v16_0;
                                                                            v9_0 = v1_2;
                                                                            v16_0 = v12_2;
                                                                            v1_0 = v54_2;
                                                                            v12_1 = v34_1;
                                                                            v5_2 = v5_5;
                                                                            v18_4 = v11_1;
                                                                            v0_22 = v3_0;
                                                                            v2_2 = v8_0;
                                                                            v17_1 = v14_0;
                                                                            v11_1 = v18_4;
                                                                            v10_0 = v19_0;
                                                                            v3_1 = v23_0;
                                                                            v18_0 = v26_0;
                                                                            int v8_1 = v7_5;
                                                                            v14_3 = v13_0;
                                                                            v13_1 = v15_0;
                                                                            v15_1 = v24_0;
                                                                            int v7_1 = 0;
                                                                            int v26_1;
                                                                            com.lagradost.cloudstream3.utils.AppUtils v55_1;
                                                                            int v54_1;
                                                                            com.idlix.LoadData v28_1;
                                                                            if (v7_1 == 0) {
                                                                                v54_1 = v0_22;
                                                                                v55_1 = v1_0;
                                                                                v26_1 = v3_1;
                                                                                v28_1 = v12_1;
                                                                                v12_0 = v30_1;
                                                                            } else {
                                                                                int v7_2 = v7_1.getEpisodes();
                                                                                if (v7_2 == 0) {
                                                                                    v54_1 = v0_22;
                                                                                    v55_1 = v1_0;
                                                                                    v26_1 = v3_1;
                                                                                    v28_1 = v12_1;
                                                                                    v12_0 = v30_1;
                                                                                } else {
                                                                                    com.lagradost.cloudstream3.utils.AppUtils v23_3 = ((Iterable) v7_2).iterator();
                                                                                    while (v23_3.hasNext()) {
                                                                                        com.idlix.LoadData v28_2;
                                                                                        int v27_0;
                                                                                        long v12_3;
                                                                                        int v7_7 = ((com.idlix.Episode) v23_3.next());
                                                                                        int v54_3 = v0_22;
                                                                                        Exception v0_25 = ((com.lagradost.cloudstream3.MainAPI) v18_0);
                                                                                        com.lagradost.cloudstream3.utils.AppUtils v55_3 = v1_0;
                                                                                        int v26_2 = v3_1;
                                                                                        int v3_3 = v7_7.getId();
                                                                                        if (v3_3 != 0) {
                                                                                            v27_0 = v4_2;
                                                                                            v28_2 = v12_1;
                                                                                            v12_3 = v30_1;
                                                                                            v2_2.add(com.lagradost.cloudstream3.MainAPIKt.newEpisode(v0_25, com.lagradost.cloudstream3.utils.AppUtils.INSTANCE.toJson(new com.idlix.LoadData(v3_3, v12_3)), new com.idlix.Idlix$$ExternalSyntheticLambda5(v7_7, v5_2)));
                                                                                        } else {
                                                                                            v27_0 = v4_2;
                                                                                            v28_2 = v12_1;
                                                                                            v12_3 = v30_1;
                                                                                        }
                                                                                        v0_22 = v54_3;
                                                                                        v1_0 = v55_3;
                                                                                        v30_1 = v12_3;
                                                                                        v3_1 = v26_2;
                                                                                        v4_2 = v27_0;
                                                                                        v12_1 = v28_2;
                                                                                    }
                                                                                    v54_1 = v0_22;
                                                                                    v55_1 = v1_0;
                                                                                    v26_1 = v3_1;
                                                                                    v28_1 = v12_1;
                                                                                    v12_0 = v30_1;
                                                                                }
                                                                            }
                                                                            v1_1 = v54_1;
                                                                            v0_1 = v55_1;
                                                                            v7_5 = v8_1;
                                                                            v8_2 = v21_0;
                                                                            v4_0 = v25_0;
                                                                            v3_2 = v26_1;
                                                                            v5_0 = v28_1;
                                                                        }
                                                                        if (v2_5 != v5_5) {
                                                                            v23_0 = v3_2;
                                                                            v25_0 = v4_3;
                                                                            v26_0 = v6_5;
                                                                            v21_0 = v8_2;
                                                                            v24_0 = v15_1;
                                                                            v8_0 = v17_4;
                                                                            v4_2 = 0;
                                                                            v6_1 = v20_2;
                                                                            v3_0 = v55_2;
                                                                            v20_0 = v9_1;
                                                                            v19_0 = v10_0;
                                                                            v15_0 = v13_1;
                                                                            v13_0 = v14_3;
                                                                            v9_0 = v1_2;
                                                                            v14_0 = v2_5;
                                                                            v2_7 = v12_2;
                                                                            v1_0 = v54_2;
                                                                            v12_1 = v5_5;
                                                                            v5_2 = v18_7;
                                                                            v18_4 = v11_1;
                                                                            try {
                                                                                Exception v0_17 = ((com.lagradost.nicehttp.NiceResponse) v14_0);
                                                                                try {
                                                                                    int v11_0 = v0_17.getParser();
                                                                                    kotlin.jvm.internal.Intrinsics.checkNotNull(v11_0);
                                                                                    Exception v0_19 = v11_0.parseSafe(v0_17.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.SeasonWrapper));
                                                                                } catch (Exception v0_20) {
                                                                                    v0_20.printStackTrace();
                                                                                    v0_19 = 0;
                                                                                }
                                                                                Exception v0_23;
                                                                                Exception v0_21 = ((com.idlix.SeasonWrapper) v0_19);
                                                                                if (v0_21 == null) {
                                                                                    v0_23 = 0;
                                                                                } else {
                                                                                    v0_23 = v0_21.getSeason();
                                                                                }
                                                                                v14_3 = v13_0;
                                                                                v13_1 = v15_0;
                                                                                v17_1 = v16_0;
                                                                                v11_1 = v18_4;
                                                                                v10_0 = v19_0;
                                                                                v15_1 = v24_0;
                                                                                v18_0 = v26_0;
                                                                                v16_0 = v2_7;
                                                                                v2_2 = v8_0;
                                                                                v8_1 = v7_5;
                                                                                v7_1 = v0_23;
                                                                                v0_22 = v3_0;
                                                                                v3_1 = v23_0;
                                                                            } catch (Exception v0) {
                                                                                v14_0 = v16_0;
                                                                                v16_0 = v2_7;
                                                                            }
                                                                        } else {
                                                                            return v5_5;
                                                                        }
                                                                    } catch (Exception v0) {
                                                                        v20_2 = 3;
                                                                    }
                                                                } catch (Exception v0) {
                                                                    v23_0 = v3_2;
                                                                    v25_0 = v4_3;
                                                                    v26_0 = v6_5;
                                                                    v21_0 = v8_2;
                                                                    v24_0 = v15_1;
                                                                    v8_0 = v2_3;
                                                                    v4_2 = 0;
                                                                    v6_1 = v20_1;
                                                                    v3_0 = v55_2;
                                                                    v20_0 = v9_1;
                                                                    v19_0 = v10_0;
                                                                    v15_0 = v13_1;
                                                                    v13_0 = v14_3;
                                                                    v14_0 = v16_0;
                                                                    v9_0 = v1_2;
                                                                    v16_0 = v12_2;
                                                                    v1_0 = v54_2;
                                                                    v12_1 = v34_1;
                                                                    v5_2 = v5_5;
                                                                    v18_4 = v11_1;
                                                                }
                                                            } catch (Exception v0) {
                                                                v29_0 = v4_3;
                                                                v23_0 = v3_2;
                                                                v25_0 = v17_7;
                                                                v26_0 = v6_5;
                                                                v21_0 = v8_2;
                                                                v24_0 = v15_1;
                                                                v8_0 = v2_2;
                                                                v4_2 = 0;
                                                                v6_1 = v20_1;
                                                                v3_0 = v55_2;
                                                                v20_0 = v9_1;
                                                                v19_0 = v10_0;
                                                                v15_0 = v13_1;
                                                                v13_0 = v14_3;
                                                                v14_0 = v16_0;
                                                                v9_0 = v1_2;
                                                                v16_0 = v12_2;
                                                                v1_0 = v54_2;
                                                                v12_1 = v34_1;
                                                                v5_2 = v5_5;
                                                                v18_4 = v11_1;
                                                            }
                                                        } catch (Exception v0) {
                                                            v29_0 = v4_5;
                                                            v23_0 = v3_2;
                                                            v25_0 = v17_7;
                                                            v26_0 = v18_0;
                                                            v21_0 = v8_2;
                                                            v24_0 = v15_1;
                                                            v8_0 = v2_2;
                                                            v4_2 = 0;
                                                            v6_1 = v20_1;
                                                            v3_0 = v55_2;
                                                            v20_0 = v9_1;
                                                            v19_0 = v10_0;
                                                            v15_0 = v13_1;
                                                            v13_0 = v14_3;
                                                            v14_0 = v16_0;
                                                            v9_0 = v1_2;
                                                            v16_0 = v12_2;
                                                            v1_0 = v54_2;
                                                            v12_1 = v34_1;
                                                            v5_2 = v5_5;
                                                            v18_4 = v11_1;
                                                        }
                                                    }
                                                }
                                                v0_1 = v54_2;
                                                v18_0 = v6_0;
                                                v6_1 = v20_5;
                                                v20_0 = v9_1;
                                                v9_0 = v1_2;
                                                v1_1 = v55_2;
                                                int v52 = v16_0;
                                                v16_0 = v2_1;
                                                v2_2 = v17_0;
                                                v17_1 = v52;
                                            }
                                        }
                                    }
                                    if (v14_0 != v10_1) {
                                        v6_12 = v54_6;
                                        v19_2 = v1_18;
                                        v3_9 = v2_16;
                                        v4_5 = v24_1;
                                        v5_19 = v25_3;
                                        v16_2 = v26_5;
                                        v7_0 = v27_3;
                                        v18_3 = v29_5;
                                        try {
                                            Exception v0_101 = ((com.lagradost.nicehttp.NiceResponse) v14_0);
                                            try {
                                                int v2_18 = v0_101.getParser();
                                                kotlin.jvm.internal.Intrinsics.checkNotNull(v2_18);
                                                int v2_19 = v2_18.parseSafe(v0_101.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.ApiResponse));
                                            } catch (Exception v0_102) {
                                                v0_102.printStackTrace();
                                                v2_19 = 0;
                                            }
                                            int v17_14;
                                            Exception v0_105;
                                            int v2_20 = ((com.idlix.ApiResponse) v2_19);
                                            try {
                                                if (v2_20 == 0) {
                                                    v17_14 = v3_9;
                                                    v24_1 = v4_5;
                                                    v25_5 = v5_19;
                                                    v14_6 = v28_0;
                                                    v4_5 = v34_0;
                                                    v0_105 = kotlin.collections.CollectionsKt.emptyList();
                                                } else {
                                                    Exception v0_104 = v2_20.getData();
                                                    if (v0_104 == null) {
                                                    } else {
                                                        long v1_20 = 0;
                                                        int v2_23 = ((java.util.Collection) new java.util.ArrayList());
                                                        int v11_12 = ((Iterable) v0_104).iterator();
                                                        while (v11_12.hasNext()) {
                                                            int v15_6;
                                                            int v54_8;
                                                            Exception v0_112 = ((com.idlix.ApiItem) v11_12.next());
                                                            int v37_1 = v0_112.getTitle();
                                                            if (v37_1 != 0) {
                                                                long v1_22;
                                                                com.idlix.LoadData v14_11 = v0_112.getPosterPath();
                                                                if (v14_11 == null) {
                                                                    v54_8 = v1_20;
                                                                    v17_14 = v3_9;
                                                                    v1_22 = 0;
                                                                } else {
                                                                    v54_8 = v1_20;
                                                                    try {
                                                                        v17_14 = v3_9;
                                                                        try {
                                                                            v1_22 = new StringBuilder().append("https://image.tmdb.org/t/p/w342").append(v14_11).toString();
                                                                        } catch (Exception v0) {
                                                                            v24_1 = v4_5;
                                                                            v3_9 = v17_14;
                                                                            v14_6 = v28_0;
                                                                            v4_5 = v34_0;
                                                                        }
                                                                    } catch (Exception v0) {
                                                                        v24_1 = v4_5;
                                                                        v14_6 = v28_0;
                                                                        v4_5 = v34_0;
                                                                    }
                                                                }
                                                                v14_6 = v28_0;
                                                                String v38_1;
                                                                if (!kotlin.jvm.internal.Intrinsics.areEqual(v0_112.getContentType(), v14_6)) {
                                                                    v24_1 = v4_5;
                                                                    v15_6 = v33_0;
                                                                    v4_5 = v34_0;
                                                                    v25_5 = v5_19;
                                                                    v38_1 = new StringBuilder().append(v35_1.getMainUrl()).append(v4_5).append(v0_112.getSlug()).toString();
                                                                } else {
                                                                    try {
                                                                        v15_6 = v33_0;
                                                                        v24_1 = v4_5;
                                                                        try {
                                                                            v38_1 = new StringBuilder().append(v35_1.getMainUrl()).append(v15_6).append(v0_112.getSlug()).toString();
                                                                            v25_5 = v5_19;
                                                                            v4_5 = v34_0;
                                                                        } catch (Exception v0) {
                                                                            v3_9 = v17_14;
                                                                            v4_5 = v34_0;
                                                                        }
                                                                    } catch (Exception v0) {
                                                                        v24_1 = v4_5;
                                                                        v3_9 = v17_14;
                                                                        v4_5 = v34_0;
                                                                    }
                                                                }
                                                                if (!kotlin.jvm.internal.Intrinsics.areEqual(v0_112.getContentType(), v14_6)) {
                                                                    v3_9 = ((com.lagradost.cloudstream3.SearchResponse) com.lagradost.cloudstream3.MainAPIKt.newTvSeriesSearchResponse$default(((com.lagradost.cloudstream3.MainAPI) v35_1), v37_1, v38_1, com.lagradost.cloudstream3.TvType.TvSeries, 0, new com.idlix.Idlix$$ExternalSyntheticLambda3(v1_22, v0_112), 8, 0));
                                                                } else {
                                                                    v3_9 = ((com.lagradost.cloudstream3.SearchResponse) com.lagradost.cloudstream3.MainAPIKt.newMovieSearchResponse$default(((com.lagradost.cloudstream3.MainAPI) v35_1), v37_1, v38_1, com.lagradost.cloudstream3.TvType.Movie, 0, new com.idlix.Idlix$$ExternalSyntheticLambda2(v1_22, v0_112), 8, 0));
                                                                }
                                                            } else {
                                                                v54_8 = v1_20;
                                                                v17_14 = v3_9;
                                                                v24_1 = v4_5;
                                                                v25_5 = v5_19;
                                                                v14_6 = v28_0;
                                                                v15_6 = v33_0;
                                                                v4_5 = v34_0;
                                                                v3_9 = 0;
                                                            }
                                                            if (v3_9 != 0) {
                                                                boolean vtmp69 = v2_23.add(v3_9);
                                                            }
                                                            v1_20 = v54_8;
                                                            v34_0 = v4_5;
                                                            v28_0 = v14_6;
                                                            v33_0 = v15_6;
                                                            v3_9 = v17_14;
                                                            v4_5 = v24_1;
                                                            v5_19 = v25_5;
                                                        }
                                                        v17_14 = v3_9;
                                                        v24_1 = v4_5;
                                                        v25_5 = v5_19;
                                                        v14_6 = v28_0;
                                                        v4_5 = v34_0;
                                                        v0_105 = ((java.util.List) v2_23);
                                                    }
                                                }
                                            } catch (Exception v0) {
                                                v3_9 = v17_14;
                                                v5_19 = v25_5;
                                            }
                                            v45_0 = v0_105;
                                            v0_110 = v6_12;
                                            v1_21 = v7_0;
                                            v5_21 = v17_14;
                                            v2_24 = v32_0;
                                        } catch (Exception v0) {
                                            v24_1 = v4_5;
                                            v14_6 = v28_0;
                                            v4_5 = v34_0;
                                        }
                                    } else {
                                        return v10_1;
                                    }
                                } catch (Exception v0) {
                                    v35_1 = v3_10;
                                    v21_3 = 0;
                                    v24_1 = 0;
                                    v31_0 = 0;
                                    v32_0 = 0;
                                    v14_6 = v28_0;
                                    v4_5 = v29_5;
                                    v10_1 = v6_13;
                                    v6_12 = v54_6;
                                    v19_2 = 0;
                                    v3_9 = v2_16;
                                    v5_19 = 0;
                                    v16_2 = 0;
                                    v7_0 = 0;
                                    v18_3 = v13_6;
                                }
                            } catch (Exception v0) {
                                v35_1 = v3_6;
                                v21_3 = v7_25;
                                v24_1 = v8_12;
                                v31_0 = v14_8;
                                v32_0 = v15_4;
                                v14_6 = v28_0;
                                v4_5 = v29_2;
                                v10_1 = v6_9;
                                v6_12 = 2;
                                v19_2 = v12_7;
                                v3_9 = v2_16;
                                v5_19 = v9_5;
                                v16_2 = v10_4;
                                v7_0 = v11_10;
                                v18_3 = v13_6;
                            }
                        } catch (Exception v0) {
                            v21_3 = v54_4;
                            v35_1 = v3_6;
                            v4_5 = v7_25;
                            v24_1 = v8_12;
                            v31_0 = v14_8;
                            v32_0 = v15_4;
                            v14_6 = v28_0;
                            v10_1 = v6_9;
                            v6_12 = v1_11;
                            v19_2 = v12_7;
                            v3_9 = v2_16;
                            v5_19 = v9_5;
                            v16_2 = v10_4;
                            v7_0 = v11_10;
                            v18_3 = v13_6;
                        }
                    }
                } else {
                    return v6_9;
                }
            case 1:
                Exception v0_38 = ((com.idlix.Idlix) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v3_6 = v0_38;
                v28_0 = "movie";
                v29_1 = "/api/series/";
                v22_2 = 1;
                v30_0 = "episode";
                v6_9 = v12_1;
                v32_1 = "/api/movies/";
                v23_4 = v14_0;
                v54_4 = v15_0;
                break;
            case 2:
                v3_9 = ((String) v15_0.L$10);
                v4_5 = v15_0.L$9;
                v5_19 = ((String) v15_0.L$8);
                v6_12 = ((java.util.List) v15_0.L$7);
                v7_0 = ((java.util.List) v15_0.L$6);
                v16_2 = ((Integer) v15_0.L$5);
                v18_3 = ((String) v15_0.L$4);
                v19_2 = ((String) v15_0.L$3);
                int v21_2 = ((String) v15_0.L$2);
                String v22_1 = ((com.idlix.DetailResponse) v15_0.L$1);
                com.lagradost.cloudstream3.utils.AppUtils v23_2 = ((com.idlix.Idlix) v15_0.L$0);
                try {
                    kotlin.ResultKt.throwOnFailure(v14_0);
                    v28_0 = "movie";
                    v34_0 = "/api/series/";
                    v30_0 = "episode";
                    v33_0 = "/api/movies/";
                    v31_0 = v21_2;
                    v32_0 = v22_1;
                    v35_1 = v23_2;
                    v22_2 = 1;
                    v10_1 = v12_1;
                    v23_4 = v14_0;
                    v21_3 = v15_0;
                } catch (Exception v0) {
                    v24_1 = v4_5;
                    v4_5 = "/api/series/";
                    v30_0 = "episode";
                    v31_0 = v21_3;
                    v32_0 = 1;
                    v35_1 = v23_4;
                    v22_2 = v10_1;
                    v10_1 = v12_1;
                    v23_4 = v14_0;
                    v21_3 = v15_0;
                    v14_6 = "movie";
                }
                break;
            case 3:
                v3_0 = 0;
                v4_2 = 0;
                v5_2 = v15_0.I$0;
                v6_1 = ((java.util.Iterator) v15_0.L$13);
                v8_0 = ((java.util.List) v15_0.L$12);
                v13_0 = ((java.util.List) v15_0.L$11);
                int v16_14 = ((String) v15_0.L$10);
                v7_5 = v15_0.L$9;
                v18_4 = ((String) v15_0.L$8);
                v19_0 = ((java.util.List) v15_0.L$7);
                v20_0 = ((java.util.List) v15_0.L$6);
                v21_0 = ((Integer) v15_0.L$5);
                String v22_4 = ((String) v15_0.L$4);
                v23_0 = ((String) v15_0.L$3);
                v24_0 = ((String) v15_0.L$2);
                v25_0 = ((com.idlix.DetailResponse) v15_0.L$1);
                v26_0 = ((com.idlix.Idlix) v15_0.L$0);
                try {
                    kotlin.ResultKt.throwOnFailure(v14_0);
                    v29_0 = "/api/series/";
                    v30_1 = "episode";
                    v2_7 = v16_14;
                    v9_0 = v22_4;
                    v22_2 = 1;
                    v16_0 = v14_0;
                } catch (Exception v0) {
                    v29_0 = v9_0;
                    v30_1 = "episode";
                    v9_0 = 1;
                    v22_2 = 1;
                }
                break;
            case 4:
                kotlin.ResultKt.throwOnFailure(v14_0);
                break;
            case 5:
                kotlin.ResultKt.throwOnFailure(v14_0);
                v11_9 = v15_0;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        v54_2 = v0_1;
        v55_2 = v1_1;
        v1_2 = v9_0;
        v30_1 = v12_0;
        v12_2 = v16_0;
        v16_0 = v17_1;
        v9_1 = v20_0;
        v17_7 = v4_0;
        v4_5 = v29_0;
    }

    public Object loadLinks(String p51, boolean p52, kotlin.jvm.functions.Function1 p53, kotlin.jvm.functions.Function1 p54, kotlin.coroutines.Continuation p55)
    {
        int v0_3;
        if (!(p55 instanceof com.idlix.Idlix$loadLinks$1)) {
            v0_3 = new com.idlix.Idlix$loadLinks$1(this, p55);
        } else {
            v0_3 = ((com.idlix.Idlix$loadLinks$1) p55);
            if ((((com.idlix.Idlix$loadLinks$1) p55).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Idlix$loadLinks$1) p55).label = (((com.idlix.Idlix$loadLinks$1) p55).label - -2147483648);
            }
        }
        Object v1_1;
        String v7_14;
        Object v1_2;
        int v0_35;
        kotlin.reflect.KClass v3_11;
        long v16_4;
        kotlin.reflect.KClass v3_12;
        int v0_16;
        int v2_20;
        String v6_25;
        kotlin.reflect.KClass v21_0;
        com.lagradost.cloudstream3.utils.ExtractorLinkType v9_7;
        com.lagradost.cloudstream3.utils.ExtractorLinkType v9_8;
        com.lagradost.nicehttp.Requests v28_0;
        okhttp3.MediaType v4_1;
        kotlin.reflect.KClass v3_20;
        Object v1_12;
        String v6_9;
        int v13_0;
        java.util.Map v23;
        String v5_16;
        java.util.Map v30_1;
        String v6_11;
        int v8_5;
        int v2_8;
        Object v14_2;
        String v5_17;
        kotlin.reflect.KClass v3_2;
        okhttp3.MediaType v4_7;
        kotlin.reflect.KClass v24;
        okhttp3.MediaType v4_8;
        okhttp3.MediaType v4_9;
        String v29_0;
        kotlin.reflect.KClass v3_5;
        int v22;
        int v2_12;
        java.util.Map v33_1;
        String v11_0;
        okhttp3.MediaType v4_11;
        String v5_1;
        String v15_0 = v0_3;
        Object v14_0 = v15_0.result;
        int v12_0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (v15_0.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(v14_0);
                try {
                    kotlin.reflect.KClass v3 = 0;
                    String v6 = 0;
                    Object vtmp3 = ((com.fasterxml.jackson.databind.ObjectMapper) com.lagradost.cloudstream3.MainAPIKt.getMapper()).readValue(p51, ((com.fasterxml.jackson.core.type.TypeReference) new com.idlix.Idlix$loadLinks$$inlined$parseJson$1()));
                    String v6_1 = ((com.idlix.LoadData) vtmp3);
                } catch (int v0) {
                    v6_1 = 0;
                }
                if (v6_1 != null) {
                    int v0_5 = v6_1;
                    okhttp3.MediaType v4_4 = v0_5.getId();
                    int v0_6 = v0_5.getType();
                    kotlin.reflect.KClass v3_3 = new kotlin.Pair[4];
                    v3_3[0] = kotlin.TuplesKt.to("Referer", new StringBuilder().append(this.getMainUrl()).append(47).toString());
                    v3_3[1] = kotlin.TuplesKt.to("Origin", this.getMainUrl());
                    v3_3[2] = kotlin.TuplesKt.to("Accept", "*/*");
                    v3_3[3] = kotlin.TuplesKt.to("Content-Type", "application/json");
                    String v7_8 = kotlin.collections.MapsKt.mapOf(v3_3);
                    String v5_14 = v7_8;
                    kotlin.reflect.KClass v3_4 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                    okhttp3.MediaType v4_5 = new StringBuilder().append(this.getMainUrl()).append("/api/watch/play-info/").append(v0_6).append(47).append(v4_4).toString();
                    v15_0.L$0 = this;
                    v15_0.L$1 = p53;
                    v15_0.L$2 = p54;
                    v15_0.L$3 = v7_8;
                    v15_0.label = 1;
                    v23 = v7_8;
                    v22 = 1;
                    v1_2 = "application/json";
                    v24 = "\"\n    }\n    ";
                    v21_0 = p54;
                    Object v26 = v12_0;
                    v28_0 = v14_0;
                    v29_0 = v15_0;
                    v14_0 = com.lagradost.nicehttp.Requests.get$default(v3_4, v4_5, v5_14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, v29_0, 4092, 0);
                    v3_2 = v26;
                    if (v14_0 != v3_2) {
                        v4_1 = p53;
                        v5_1 = this;
                        int v0_13 = ((com.lagradost.nicehttp.NiceResponse) v14_0);
                        v6_9 = v0_13.getCookies();
                        try {
                            int v8_2 = v0_13.getParser();
                            kotlin.jvm.internal.Intrinsics.checkNotNull(v8_2);
                            int v8_3 = v8_2.parseSafe(v0_13.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.Res));
                        } catch (int v0_14) {
                            v0_14.printStackTrace();
                            v8_3 = 0;
                        }
                        int v8_4 = ((com.idlix.Res) v8_3);
                        if (v8_4 != 0) {
                            int v0_15 = v8_4;
                            long vtmp74 = kotlin.ranges.RangesKt.coerceAtLeast((v0_15.getUnlockAt() - v0_15.getServerNow()), 0);
                            v9_7 = 0;
                            v13_0 = v3_2;
                            v11_0 = (vtmp74 / ((long) 1000));
                            v7_14 = v21_0;
                            v14_0 = v28_0;
                            v15_0 = v29_0;
                            v3_5 = v0_15;
                            v8_5 = v4_1;
                            v4_8 = v23;
                            v0_16 = p55;
                            if (v9_7 >= v11_0) {
                                long v16_3 = v1_2;
                                int v53_1 = v14_0;
                                int v0_21 = new StringBuilder().append("\n    {\n        \"gateToken\": \"").append(v3_5.getGateToken());
                                v1_12 = v24;
                                int v0_24 = kotlin.text.StringsKt.trimIndent(v0_21.append(v1_12).toString());
                                com.lagradost.nicehttp.Requests v28_1 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                                String v29_1 = new StringBuilder().append(v5_1.getMainUrl()).append("/api/watch/session/claim").toString();
                                v9_8 = v16_3;
                                okhttp3.RequestBody v37_0 = okhttp3.RequestBody.Companion.create(v0_24, okhttp3.MediaType.Companion.get(v9_8));
                                v15_0.L$0 = v5_1;
                                v15_0.L$1 = v8_5;
                                v15_0.L$2 = v7_14;
                                v15_0.L$3 = v4_8;
                                v15_0.L$4 = v6_9;
                                v15_0.L$5 = 0;
                                v15_0.label = 3;
                                v14_0 = com.lagradost.nicehttp.Requests.post$default(v28_1, v29_1, v4_8, 0, 0, v6_9, 0, 0, 0, v37_0, 0, 0, 0, 0, 0, 0, 0, v15_0, 65260, 0);
                                if (v14_0 != v13_0) {
                                    v3_11 = v53_1;
                                    v30_1 = v4_8;
                                    v4_9 = v5_1;
                                    v33_1 = v6_9;
                                    v5_17 = v8_5;
                                    v12_0 = v13_0;
                                    int v0_26 = ((com.lagradost.nicehttp.NiceResponse) v14_0);
                                    try {
                                        int v8_6 = v0_26.getParser();
                                        kotlin.jvm.internal.Intrinsics.checkNotNull(v8_6);
                                        String v6_12 = v8_6.parseSafe(v0_26.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.RedeemRes));
                                    } catch (int v0_27) {
                                        v0_27.printStackTrace();
                                        v6_12 = 0;
                                    }
                                    String v6_13 = ((com.idlix.RedeemRes) v6_12);
                                    if (v6_13 != null) {
                                        int v0_28 = v6_13;
                                        String v6_17 = new StringBuilder().append("\n    {\n        \"claim\": \"").append(v0_28.getClaim());
                                        Object v1_15 = kotlin.text.StringsKt.trimIndent(v6_17.append(v1_12).toString());
                                        com.lagradost.nicehttp.Requests v28_2 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                                        String v29_2 = v0_28.getRedeemUrl();
                                        okhttp3.RequestBody v37_1 = okhttp3.RequestBody.Companion.create(v1_15, okhttp3.MediaType.Companion.get(v9_8));
                                        v15_0.L$0 = v4_9;
                                        v15_0.L$1 = v5_17;
                                        v15_0.L$2 = v7_14;
                                        v15_0.L$3 = 0;
                                        v15_0.L$4 = 0;
                                        v15_0.label = 4;
                                        v14_0 = com.lagradost.nicehttp.Requests.post$default(v28_2, v29_2, v30_1, 0, 0, v33_1, 0, 0, 0, v37_1, 0, 0, 0, 0, 0, 0, 0, v15_0, 65260, 0);
                                        if (v14_0 != v12_0) {
                                            v2_12 = v3_11;
                                            v3_12 = v7_14;
                                            int v0_32 = ((com.lagradost.nicehttp.NiceResponse) v14_0);
                                            try {
                                                String v7_15 = v0_32.getParser();
                                                kotlin.jvm.internal.Intrinsics.checkNotNull(v7_15);
                                                String v6_20 = v7_15.parseSafe(v0_32.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.Iframe));
                                            } catch (int v0_34) {
                                                v0_34.printStackTrace();
                                                v6_20 = 0;
                                            }
                                            String v6_21 = ((com.idlix.Iframe) v6_20);
                                            if (v6_21 != null) {
                                                v0_35 = v6_21;
                                                String v6_22 = v0_35.getUrl();
                                                if (v6_22 != null) {
                                                    String v7_18;
                                                    if (kotlin.text.StringsKt.isBlank(((CharSequence) v6_22))) {
                                                        v7_18 = 0;
                                                    } else {
                                                        v7_18 = v22;
                                                    }
                                                    int v8_11;
                                                    if (v7_18 == null) {
                                                        v8_11 = 0;
                                                    } else {
                                                        v8_11 = v6_22;
                                                    }
                                                    if (v8_11 != 0) {
                                                        String v6_23 = v4_9.getName();
                                                        String v7_19 = v4_9.getName();
                                                        kotlin.jvm.functions.Function2 v10_7 = ((kotlin.jvm.functions.Function2) new com.idlix.Idlix$loadLinks$3$1(v4_9, 0));
                                                        v15_0.L$0 = v5_17;
                                                        v15_0.L$1 = v0_35;
                                                        v15_0.L$2 = v3_12;
                                                        v15_0.label = 5;
                                                        v14_0 = com.lagradost.cloudstream3.utils.ExtractorApiKt.newExtractorLink(v6_23, v7_19, v8_11, com.lagradost.cloudstream3.utils.ExtractorLinkType.M3U8, v10_7, v15_0);
                                                        if (v14_0 != v12_0) {
                                                            v4_11 = v0_35;
                                                            v3_12.invoke(v14_0);
                                                            v0_35 = v4_11;
                                                            v14_2 = v2_12;
                                                            v5_16 = ((Iterable) v0_35.getSubtitles()).iterator();
                                                            v4_7 = v5_17;
                                                            if (!v5_16.hasNext()) {
                                                                return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(v22);
                                                            } else {
                                                                int v2_16 = ((com.idlix.Subtitle) v5_16.next());
                                                                String v6_24 = v2_16.getLabel();
                                                                String v7_20 = v2_16.getPath();
                                                                v15_0.L$0 = v4_7;
                                                                v15_0.L$1 = v5_16;
                                                                v15_0.L$2 = v4_7;
                                                                v15_0.label = 6;
                                                                int v2_18 = com.lagradost.cloudstream3.MainAPIKt.newSubtitleFile$default(v6_24, v7_20, 0, v15_0, 4, 0);
                                                                if (v2_18 != v12_0) {
                                                                    v6_11 = v4_7;
                                                                    v14_0 = v2_18;
                                                                    v2_8 = v14_2;
                                                                    while(true) {
                                                                        v4_7.invoke(v14_0);
                                                                        v14_2 = v2_8;
                                                                        v4_7 = v6_11;
                                                                    }
                                                                } else {
                                                                    return v12_0;
                                                                }
                                                            }
                                                        } else {
                                                            return v12_0;
                                                        }
                                                    }
                                                }
                                            } else {
                                                return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(0);
                                            }
                                        } else {
                                            return v12_0;
                                        }
                                    } else {
                                        return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(0);
                                    }
                                } else {
                                    return v13_0;
                                }
                            } else {
                                Object v52_1 = v0_16;
                                int v53_2 = v14_0;
                                v16_4 = v1_2;
                                com.lagradost.api.Log.INSTANCE.d(v5_1.getName(), new StringBuilder().append("Waiting: ").append(v9_7).append("s / ").append(v11_0).append(115).toString());
                                v15_0.L$0 = v5_1;
                                v15_0.L$1 = v8_5;
                                v15_0.L$2 = v7_14;
                                v15_0.L$3 = v4_8;
                                v15_0.L$4 = v6_9;
                                v15_0.L$5 = v3_5;
                                v15_0.J$0 = v11_0;
                                v15_0.J$1 = v9_7;
                                v15_0.label = 2;
                                if (kotlinx.coroutines.DelayKt.delay(1000, v15_0) != v13_0) {
                                    v0_16 = v52_1;
                                    v14_0 = v53_2;
                                    v1_1 = v3_5;
                                    v2_20 = v6_9;
                                    v6_25 = v4_8;
                                    v3_20 = v9_7;
                                } else {
                                    return v13_0;
                                }
                            }
                        } else {
                            return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(0);
                        }
                    } else {
                        return v3_2;
                    }
                } else {
                    return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(0);
                }
            case 1:
                int v0_2 = ((java.util.Map) v15_0.L$3);
                kotlin.reflect.KClass v3_1 = ((kotlin.jvm.functions.Function1) v15_0.L$2);
                v4_1 = ((kotlin.jvm.functions.Function1) v15_0.L$1);
                v5_1 = ((com.idlix.Idlix) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v23 = v0_2;
                v21_0 = v3_1;
                v1_2 = "application/json";
                v24 = "\"\n    }\n    ";
                v3_2 = v12_0;
                v28_0 = v14_0;
                v29_0 = v15_0;
                v22 = 1;
                break;
            case 2:
                v3_20 = v15_0.J$1;
                kotlin.jvm.functions.Function2 v10_11 = v15_0.J$0;
                String v5_21 = ((java.util.Map) v15_0.L$4);
                v6_25 = ((java.util.Map) v15_0.L$3);
                v7_14 = ((kotlin.jvm.functions.Function1) v15_0.L$2);
                int v13_4 = ((kotlin.jvm.functions.Function1) v15_0.L$1);
                com.idlix.Res v51_1 = ((com.idlix.Res) v15_0.L$5);
                int v0_53 = ((com.idlix.Idlix) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v2_20 = v5_21;
                v16_4 = "application/json";
                v24 = "\"\n    }\n    ";
                v8_5 = v13_4;
                v22 = 1;
                v5_1 = v0_53;
                v0_16 = p55;
                v13_0 = v12_0;
                v1_1 = v51_1;
                v11_0 = v10_11;
                break;
            case 3:
                int v0_40 = ((java.util.Map) v15_0.L$4);
                kotlin.reflect.KClass v3_19 = ((java.util.Map) v15_0.L$3);
                okhttp3.MediaType v4_15 = ((kotlin.jvm.functions.Function1) v15_0.L$2);
                v5_17 = ((kotlin.jvm.functions.Function1) v15_0.L$1);
                kotlin.jvm.functions.Function2 v10_10 = ((com.idlix.Idlix) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v33_1 = v0_40;
                v30_1 = v3_19;
                v7_14 = v4_15;
                v1_12 = "\"\n    }\n    ";
                v4_9 = v10_10;
                v3_11 = v14_0;
                v22 = 1;
                v9_8 = "application/json";
                break;
            case 4:
                int v0_33 = ((kotlin.jvm.functions.Function1) v15_0.L$2);
                kotlin.reflect.KClass v3_14 = ((kotlin.jvm.functions.Function1) v15_0.L$1);
                v4_9 = ((com.idlix.Idlix) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v5_17 = v3_14;
                v2_12 = v14_0;
                v22 = 1;
                v3_12 = v0_33;
                break;
            case 5:
                v3_12 = ((kotlin.jvm.functions.Function1) v15_0.L$2);
                v4_11 = ((com.idlix.Iframe) v15_0.L$1);
                v5_17 = ((kotlin.jvm.functions.Function1) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v2_12 = v14_0;
                v22 = 1;
                break;
            case 6:
                v3 = 0;
                v4_7 = ((kotlin.jvm.functions.Function1) v15_0.L$2);
                v5_16 = ((java.util.Iterator) v15_0.L$1);
                v6_11 = ((kotlin.jvm.functions.Function1) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_0);
                v2_8 = v14_0;
                v22 = 1;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        v9_7 = (1 + v3_20);
        v3_5 = v1_1;
        v4_8 = v6_25;
        v1_2 = v16_4;
        v6_9 = v2_20;
    }

    public Object quickSearch(String p6, kotlin.coroutines.Continuation p7)
    {
        String v0_2;
        if (!(p7 instanceof com.idlix.Idlix$quickSearch$1)) {
            v0_2 = new com.idlix.Idlix$quickSearch$1(this, p7);
        } else {
            v0_2 = ((com.idlix.Idlix$quickSearch$1) p7);
            if ((((com.idlix.Idlix$quickSearch$1) p7).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Idlix$quickSearch$1) p7).label = (((com.idlix.Idlix$quickSearch$1) p7).label - -2147483648);
            }
        }
        int v6_1;
        Object v1_2 = v0_2.result;
        Object v2_0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (v0_2.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(v1_2);
                v0_2.label = 1;
                v6_1 = this.search(p6, 1, v0_2);
                if (v6_1 != v2_0) {
                } else {
                    return v2_0;
                }
            case 1:
                kotlin.ResultKt.throwOnFailure(v1_2);
                v6_1 = v1_2;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        int v6_3;
        int v6_2 = ((com.lagradost.cloudstream3.SearchResponseList) v6_1);
        if (v6_2 == 0) {
            v6_3 = 0;
        } else {
            v6_3 = v6_2.getItems();
        }
        return v6_3;
    }

    public Object search(String p36, int p37, kotlin.coroutines.Continuation p38)
    {
        Integer v0_4;
        if (!(p38 instanceof com.idlix.Idlix$search$1)) {
            v0_4 = new com.idlix.Idlix$search$1(this, p38);
        } else {
            v0_4 = ((com.idlix.Idlix$search$1) p38);
            if ((((com.idlix.Idlix$search$1) p38).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Idlix$search$1) p38).label = (((com.idlix.Idlix$search$1) p38).label - -2147483648);
            }
        }
        com.idlix.Idlix v21;
        Integer v15_0 = v0_4;
        double v14_1 = v15_0.result;
        Integer v0_10 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (v15_0.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(v14_1);
                double v4_4 = new StringBuilder().append(this.getMainUrl()).append("/api/search?q=").append(p36).append("&page=").append(p37).append("&limit=8").toString();
                com.lagradost.cloudstream3.SearchResponseList v3_5 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                v15_0.L$0 = this;
                v15_0.label = 1;
                v21 = this;
                v14_1 = com.lagradost.nicehttp.Requests.get$default(v3_5, v4_4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, v15_0, 4094, 0);
                if (v14_1 != v0_10) {
                } else {
                    return v0_10;
                }
            case 1:
                Integer v0_13 = ((com.idlix.Idlix) v15_0.L$0);
                kotlin.ResultKt.throwOnFailure(v14_1);
                v21 = v0_13;
                int v23 = v15_0;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        Integer v0_19 = ((com.lagradost.nicehttp.NiceResponse) v14_1);
        double v4_2 = 0;
        try {
            java.util.Collection v5_14 = v0_19.getParser();
            kotlin.jvm.internal.Intrinsics.checkNotNull(v5_14);
            java.util.Collection v5_0 = v5_14.parseSafe(v0_19.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.SearchApiResponse));
        } catch (Integer v0_21) {
            v0_21.printStackTrace();
            v5_0 = 0;
        }
        java.util.Collection v5_1 = ((com.idlix.SearchApiResponse) v5_0);
        if (v5_1 != null) {
            java.util.Collection v5_4 = ((java.util.Collection) new java.util.ArrayList());
            java.util.Iterator v8_0 = ((Iterable) v5_1.getResults()).iterator();
            while (v8_0.hasNext()) {
                Integer v0_9 = ((com.idlix.SearchApiResult) v8_0.next());
                String v17_0 = v0_9.getTitle();
                double v14_0 = new StringBuilder().append("https://image.tmdb.org/t/p/w342").append(v0_9.getPosterPath()).toString();
                com.lagradost.cloudstream3.SearchResponse v10_1 = v0_9.getReleaseDate();
                if (v10_1 == null) {
                    v10_1 = v0_9.getFirstAirDate();
                }
                com.lagradost.cloudstream3.SearchResponse v10_3;
                if (v10_1 == null) {
                    v10_3 = v4_2;
                } else {
                    com.lagradost.cloudstream3.SearchResponse v10_2 = kotlin.text.StringsKt.substringBefore$default(v10_1, "-", v4_2, 2, v4_2);
                    if (v10_2 == null) {
                    } else {
                        v10_3 = kotlin.text.StringsKt.toIntOrNull(v10_2);
                    }
                }
                com.lagradost.cloudstream3.SearchResponse v10_24;
                String v26;
                Integer v15_1 = v10_3;
                com.lagradost.cloudstream3.SearchResponse v10_4 = v0_9.getContentType();
                switch (v10_4.hashCode()) {
                    case -905838985:
                        if (v10_4.equals("series")) {
                            v26 = new StringBuilder().append(v21.getMainUrl()).append("/api/series/").append(v0_9.getSlug()).toString();
                            com.lagradost.cloudstream3.SearchResponse v10_20 = v0_9.getVoteAverage();
                            if (!kotlin.jvm.internal.Intrinsics.areEqual(v0_9.getContentType(), "movie")) {
                                v10_24 = ((com.lagradost.cloudstream3.SearchResponse) com.lagradost.cloudstream3.MainAPIKt.newTvSeriesSearchResponse$default(((com.lagradost.cloudstream3.MainAPI) v21), v17_0, v26, com.lagradost.cloudstream3.TvType.TvSeries, 0, new com.idlix.Idlix$$ExternalSyntheticLambda7(v14_0, v15_1, v10_20), 8, 0));
                            } else {
                                com.lagradost.cloudstream3.MainAPI v24_1 = ((com.lagradost.cloudstream3.MainAPI) v21);
                                String v29_1 = new com.idlix.Idlix$$ExternalSyntheticLambda6;
                                double v4 = v14_0;
                                v29_1(v14_0, v15_1, v0_9, v10_20);
                                v10_24 = ((com.lagradost.cloudstream3.SearchResponse) com.lagradost.cloudstream3.MainAPIKt.newMovieSearchResponse$default(v24_1, v17_0, v26, com.lagradost.cloudstream3.TvType.Movie, 0, v29_1, 8, 0));
                            }
                        } else {
                            v10_24 = 0;
                        }
                        break;
                    case 104087344:
                        if (v10_4.equals("movie")) {
                            v26 = new StringBuilder().append(v21.getMainUrl()).append("/api/movies/").append(v0_9.getSlug()).toString();
                        }
                        break;
                    case 2084501204:
                        if (v10_4.equals("tv_series")) {
                        }
                        break;
                    default:
                        v4 = v14_0;
                }
                if (v10_24 != null) {
                    boolean vtmp50 = v5_4.add(v10_24);
                }
                v4_2 = 0;
            }
            return com.lagradost.cloudstream3.MainAPIKt.toNewSearchResponseList$default(((java.util.List) v5_4), 0, 1, 0);
        } else {
            return 0;
        }
    }

    public void setLang(String p1)
    {
        this.lang = p1;
        return;
    }

    public void setMainUrl(String p1)
    {
        this.mainUrl = p1;
        return;
    }

    public void setName(String p1)
    {
        this.name = p1;
        return;
    }
}
