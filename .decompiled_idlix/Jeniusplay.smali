package com.idlix;
public final class Jeniusplay extends com.lagradost.cloudstream3.utils.ExtractorApi {
    private String mainUrl;
    private String name;
    private final boolean requiresReferer;

    public Jeniusplay()
    {
        this.name = "Jeniusplay";
        this.mainUrl = "https://jeniusplay.com";
        this.requiresReferer = 1;
        return;
    }

    private final String getLanguage(String p4)
    {
        String v0_3;
        if ((!kotlin.text.StringsKt.contains(((CharSequence) p4), ((CharSequence) "indonesia"), 1)) && (!kotlin.text.StringsKt.contains(((CharSequence) p4), ((CharSequence) "bahasa"), 1))) {
            v0_3 = p4;
        } else {
            v0_3 = "Indonesian";
        }
        return v0_3;
    }

    public String getMainUrl()
    {
        return this.mainUrl;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean getRequiresReferer()
    {
        return this.requiresReferer;
    }

    public Object getUrl(String p34, String p35, kotlin.jvm.functions.Function1 p36, kotlin.jvm.functions.Function1 p37, kotlin.coroutines.Continuation p38)
    {
        kotlin.coroutines.Continuation v1_1;
        if (!(p38 instanceof com.idlix.Jeniusplay$getUrl$1)) {
            v1_1 = new com.idlix.Jeniusplay$getUrl$1(this, p38);
        } else {
            v1_1 = ((com.idlix.Jeniusplay$getUrl$1) p38);
            if ((((com.idlix.Jeniusplay$getUrl$1) p38).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Jeniusplay$getUrl$1) p38).label = (((com.idlix.Jeniusplay$getUrl$1) p38).label - -2147483648);
            }
        }
        boolean v9_7;
        int v6_10;
        int v4_13;
        int v6_0;
        int v15_2;
        int v11_8;
        boolean v9_15;
        java.util.Collection v14_4;
        int v5_1;
        Object v16_11;
        kotlin.jvm.functions.Function1 v29;
        int v10_22;
        int v13_8;
        kotlin.coroutines.Continuation v21_1;
        boolean v3_10;
        java.util.Iterator v7_14;
        Exception v0_14;
        int v5_11;
        int v5_10;
        int v12_4;
        int v13_5;
        int v5_12;
        int v17_1;
        java.util.Collection v14_6;
        int v15_7;
        int v15_8;
        int v27;
        int v8_9;
        int v6_11;
        Exception v0_2;
        java.util.Iterator v7_15;
        int v4_12;
        int v4_9;
        java.util.Collection v14_5;
        int v11_16;
        boolean v3_27;
        boolean v3_16;
        int v8_13;
        boolean v3_1;
        java.util.Collection v14_1;
        boolean v3_20;
        int v15_6;
        Object v16_10;
        Exception v0_1;
        int v13_3;
        int v15_3 = v1_1.result;
        java.util.Collection v14_2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (v1_1.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(v15_3);
                boolean v3_0 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                v1_1.L$0 = this;
                v1_1.L$1 = p34;
                v1_1.L$2 = p35;
                v1_1.L$3 = p36;
                v1_1.L$4 = p37;
                v1_1.label = 1;
                int v19_0 = 0;
                v27 = 1;
                Object v28 = v14_2;
                v29 = v15_3;
                int v15_1 = com.lagradost.nicehttp.Requests.get$default(v3_0, p34, 0, p35, 0, 0, v19_0, v19_0, 0, 0, 0, 0, 0, v1_1, 4090, 0);
                v6_0 = v28;
                if (v15_1 != v6_0) {
                    v14_1 = this;
                    v3_1 = v15_1;
                    v15_2 = p36;
                    v5_1 = p34;
                    v0_1 = p37;
                    v21_1 = p35;
                    int v13_0 = ((com.lagradost.nicehttp.NiceResponse) v3_1).getDocument();
                    int v5_2 = kotlin.text.StringsKt.substringAfter$default(((String) kotlin.collections.CollectionsKt.last(kotlin.text.StringsKt.split$default(((CharSequence) v5_1), new String[] {"/"}), 0, 0, 6, 0))), "data=", 0, 2, 0);
                    boolean v3_7 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                    int v4_8 = new StringBuilder().append(v14_1.getMainUrl()).append("/player/index.php?data=").append(v5_2).append("&do=getVideo").toString();
                    java.util.Iterator v7_6 = new kotlin.Pair[2];
                    v7_6[0] = kotlin.TuplesKt.to("hash", v5_2);
                    v7_6[v27] = kotlin.TuplesKt.to("r", String.valueOf(v21_1));
                    boolean v9_4 = kotlin.collections.MapsKt.mapOf(v7_6);
                    int v5_5 = kotlin.collections.MapsKt.mapOf(kotlin.TuplesKt.to("X-Requested-With", "XMLHttpRequest"));
                    v1_1.L$0 = v14_1;
                    v1_1.L$1 = v15_2;
                    v1_1.L$2 = v0_1;
                    v1_1.L$3 = v13_0;
                    v1_1.L$4 = 0;
                    v1_1.label = 2;
                    org.jsoup.nodes.Document v24 = v13_0;
                    int v25_0 = v14_1;
                    kotlin.jvm.functions.Function1 v26 = v15_2;
                    kotlin.jvm.functions.Function1 v30 = v6_0;
                    int v15_5 = com.lagradost.nicehttp.Requests.post$default(v3_7, v4_8, v5_5, v21_1, 0, 0, v9_4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, v1_1, 65496, 0);
                    v13_3 = v30;
                    if (v15_5 != v13_3) {
                        v14_4 = v0_1;
                        v3_10 = v15_5;
                        v0_2 = v24;
                        v12_4 = v25_0;
                        v15_6 = v26;
                        boolean v3_11 = ((com.lagradost.nicehttp.NiceResponse) v3_10);
                        int v5_7 = v3_11.getParser();
                        kotlin.jvm.internal.Intrinsics.checkNotNull(v5_7);
                        int v5_9 = kotlin.text.StringsKt.replace$default(((com.idlix.Jeniusplay$ResponseSource) v5_7.parse(v3_11.getText(), kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(com.idlix.Jeniusplay$ResponseSource))).getVideoSource(), ".txt", ".m3u8", 0, 4, 0);
                        int v4_11 = v12_4.getName();
                        int v6_5 = v12_4.getMainUrl();
                        v1_1.L$0 = v12_4;
                        v1_1.L$1 = v15_6;
                        v1_1.L$2 = v14_4;
                        v1_1.L$3 = v0_2;
                        v1_1.label = 3;
                        int v25_1 = v12_4;
                        boolean v3_15 = com.lagradost.cloudstream3.utils.M3u8Helper$Companion.generateM3u8$default(com.lagradost.cloudstream3.utils.M3u8Helper.Companion, v4_11, v5_9, v6_5, 0, 0, 0, v1_1, 56, 0);
                        if (v3_15 != v13_3) {
                            v4_12 = v15_6;
                            v5_10 = v25_1;
                            v15_3 = v3_15;
                            v3_16 = v14_4;
                            int v8_11 = ((Iterable) v15_3).iterator();
                            while (v8_11.hasNext()) {
                                v3_16.invoke(v8_11.next());
                            }
                            v7_14 = ((Iterable) v0_2.select("script")).iterator();
                            v14_5 = v13_3;
                            v15_7 = v29;
                            v6_10 = v5_10;
                            v5_11 = v4_12;
                            v4_13 = 0;
                            v3_20 = v1_1;
                            if (!v7_14.hasNext()) {
                                return kotlin.Unit.INSTANCE;
                            } else {
                                Exception v0_8 = ((org.jsoup.nodes.Element) v7_14.next());
                                if (!kotlin.text.StringsKt.contains$default(((CharSequence) v0_8.data()), ((CharSequence) "eval(function(p,a,c,k,e,d)"), 0, 2, 0)) {
                                } else {
                                    int v10_17;
                                    Exception v0_9 = kotlin.text.StringsKt.substringBefore$default(kotlin.text.StringsKt.substringAfter$default(com.lagradost.cloudstream3.utils.ExtractorApiKt.getAndUnpack(v0_8.data()), "\"tracks\":[", 0, 2, 0), "],", 0, 2, 0);
                                    Exception v0_10 = new StringBuilder().append(91).append(v0_9).append(93).toString();
                                    if (v0_10 != null) {
                                        try {
                                            int v17 = 0;
                                            int v12_11 = ((com.fasterxml.jackson.databind.ObjectMapper) com.lagradost.cloudstream3.MainAPIKt.getMapper()).readValue(v0_10, ((com.fasterxml.jackson.core.type.TypeReference) new com.idlix.Jeniusplay$getUrl$lambda$1$$inlined$tryParseJson$1()));
                                        } catch (Exception v0) {
                                            v12_11 = 0;
                                        }
                                        v10_17 = v12_11;
                                    } else {
                                        v10_17 = 0;
                                    }
                                    int v10_18 = ((java.util.List) v10_17);
                                    if (v10_18 == 0) {
                                    } else {
                                        Exception v0_13 = ((Iterable) v10_18);
                                        v0_14 = v6_10;
                                        v13_8 = 0;
                                        v6_11 = v14_5;
                                        v16_10 = v15_7;
                                        v15_8 = v5_11;
                                        v11_16 = 0;
                                        v14_6 = ((java.util.Collection) new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault(v0_13, 10)));
                                        v10_22 = v4_13;
                                        v8_13 = v7_14;
                                        v7_15 = v0_13.iterator();
                                        v9_15 = v3_20;
                                        if (!v7_15.hasNext()) {
                                            v6_10 = v0_14;
                                            v14_5 = v6_11;
                                            v3_20 = v9_15;
                                            v4_13 = v10_22;
                                            v5_11 = v15_8;
                                            v15_7 = v16_10;
                                            v7_14 = v8_13;
                                        } else {
                                            int v4_16 = ((com.idlix.Jeniusplay$Tracks) v7_15.next());
                                            boolean v3_24 = v4_16.getLabel();
                                            if (!v3_24) {
                                                v3_24 = "";
                                            }
                                            boolean v3_25 = v0_14.getLanguage(v3_24);
                                            int v4_17 = v4_16.getFile();
                                            v9_15.L$0 = v0_14;
                                            v9_15.L$1 = v15_8;
                                            v9_15.L$2 = v8_13;
                                            v9_15.L$3 = v14_6;
                                            v9_15.L$4 = v7_15;
                                            v9_15.L$5 = v15_8;
                                            v9_15.L$6 = v14_6;
                                            v9_15.label = 4;
                                            Iterable v31 = v6_11;
                                            int v20_2 = v7_15;
                                            com.idlix.Jeniusplay$getUrl$lambda$1$$inlined$tryParseJson$1 v18_6 = v8_13;
                                            boolean v3_26 = com.lagradost.cloudstream3.MainAPIKt.newSubtitleFile$default(v3_25, v4_17, 0, v9_15, 4, 0);
                                            int v4_18 = v31;
                                            if (v3_26 != v4_18) {
                                                v6_11 = v4_18;
                                                v4_9 = v11_16;
                                                v5_12 = v13_8;
                                                v8_9 = v14_6;
                                                v11_8 = v18_6;
                                                v13_5 = v20_2;
                                                v17_1 = v16_10;
                                                v16_11 = v15_8;
                                                v15_3 = v3_26;
                                                v3_27 = v9_15;
                                                v9_7 = v16_11;
                                                while(true) {
                                                    v9_7.invoke(v15_3);
                                                    v8_9.add(kotlin.Unit.INSTANCE);
                                                    v9_15 = v3_27;
                                                    v8_13 = v11_8;
                                                    v7_15 = v13_5;
                                                    v15_8 = v16_11;
                                                    v16_10 = v17_1;
                                                    v11_16 = v4_9;
                                                    v13_8 = v5_12;
                                                }
                                            } else {
                                                return v4_18;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            return v13_3;
                        }
                    } else {
                        return v13_3;
                    }
                } else {
                    return v6_0;
                }
            case 1:
                v0_1 = ((kotlin.jvm.functions.Function1) v1_1.L$4);
                boolean v3_33 = ((kotlin.jvm.functions.Function1) v1_1.L$3);
                int v4_23 = ((String) v1_1.L$2);
                v5_1 = ((String) v1_1.L$1);
                int v6_15 = ((com.idlix.Jeniusplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_3);
                v21_1 = v4_23;
                v27 = 1;
                v29 = v15_3;
                v15_2 = v3_33;
                v3_1 = v29;
                v14_1 = v6_15;
                v6_0 = v14_2;
                break;
            case 2:
                v0_2 = ((org.jsoup.nodes.Document) v1_1.L$3);
                boolean v3_31 = ((kotlin.jvm.functions.Function1) v1_1.L$2);
                int v4_21 = ((kotlin.jvm.functions.Function1) v1_1.L$1);
                int v5_17 = ((com.idlix.Jeniusplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_3);
                v12_4 = v5_17;
                v13_3 = v14_2;
                v29 = v15_3;
                v14_4 = v3_31;
                v3_10 = v29;
                v15_6 = v4_21;
                break;
            case 3:
                v0_2 = ((org.jsoup.nodes.Document) v1_1.L$3);
                v3_16 = ((kotlin.jvm.functions.Function1) v1_1.L$2);
                v4_12 = ((kotlin.jvm.functions.Function1) v1_1.L$1);
                v5_10 = ((com.idlix.Jeniusplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_3);
                v13_3 = v14_2;
                v29 = v15_3;
                break;
            case 4:
                v4_9 = 0;
                v8_9 = ((java.util.Collection) v1_1.L$6);
                v9_7 = ((kotlin.jvm.functions.Function1) v1_1.L$5);
                v13_5 = ((java.util.Iterator) v1_1.L$4);
                int v10_9 = ((java.util.Collection) v1_1.L$3);
                v11_8 = ((java.util.Iterator) v1_1.L$2);
                int v12_8 = ((kotlin.jvm.functions.Function1) v1_1.L$1);
                v0_14 = ((com.idlix.Jeniusplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_3);
                v16_11 = v12_8;
                v17_1 = v15_3;
                v5_12 = 0;
                v6_11 = v14_2;
                v14_6 = v10_9;
                v10_22 = 0;
                v3_27 = v1_1;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
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
