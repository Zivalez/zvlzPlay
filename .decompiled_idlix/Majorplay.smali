package com.idlix;
public final class Majorplay extends com.lagradost.cloudstream3.utils.ExtractorApi {
    private String mainUrl;
    private String name;
    private final boolean requiresReferer;

    public Majorplay()
    {
        this.name = "Majorplay";
        this.mainUrl = "https://*.majorplay.net";
        this.requiresReferer = 1;
        return;
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

    public Object getUrl(String p27, String p28, kotlin.jvm.functions.Function1 p29, kotlin.jvm.functions.Function1 p30, kotlin.coroutines.Continuation p31)
    {
        IllegalStateException v1_1;
        if (!(p31 instanceof com.idlix.Majorplay$getUrl$1)) {
            v1_1 = new com.idlix.Majorplay$getUrl$1(this, p31);
        } else {
            v1_1 = ((com.idlix.Majorplay$getUrl$1) p31);
            if ((((com.idlix.Majorplay$getUrl$1) p31).label & -2147483648) == 0) {
            } else {
                ((com.idlix.Majorplay$getUrl$1) p31).label = (((com.idlix.Majorplay$getUrl$1) p31).label - -2147483648);
            }
        }
        kotlin.jvm.functions.Function1 v6_3;
        kotlin.jvm.functions.Function1 v7_8;
        java.util.Iterator v11_10;
        com.idlix.Majorplay v5_1;
        int v8_1;
        StringBuilder v14_0;
        int v3_0;
        kotlin.jvm.functions.Function1 v7_9;
        int v9_3;
        int v13_0;
        int v4_8;
        int v12_0;
        kotlin.jvm.functions.Function1 v6_2;
        kotlin.jvm.functions.Function1 v6_1;
        Object v15_1;
        com.idlix.Majorplay v5_2;
        int v4_3;
        int v3_7;
        Object v15_2 = v1_1.result;
        StringBuilder v14_1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int v10_2 = 2;
        switch (v1_1.label) {
            case 0:
                kotlin.ResultKt.throwOnFailure(v15_2);
                int v3_19 = com.lagradost.cloudstream3.MainActivityKt.getApp();
                kotlin.jvm.functions.Function1 v6_7 = this.getMainUrl();
                v1_1.L$0 = this;
                v1_1.L$1 = p29;
                v1_1.L$2 = p30;
                v1_1.label = 1;
                int v16_5 = 0;
                Object v23 = v14_1;
                Object v15_0 = com.lagradost.nicehttp.Requests.get$default(v3_19, p27, 0, v6_7, v16_5, v16_5, 0, 0, 0, 0, 0, 0, 0, v1_1, 4090, 0);
                v13_0 = v23;
                if (v15_0 != v13_0) {
                    v3_0 = v15_0;
                    v15_1 = p29;
                    v14_0 = p30;
                    v12_0 = this;
                    java.util.Iterator v11_0 = ((com.lagradost.nicehttp.NiceResponse) v3_0).getDocument();
                    int v10_0 = v11_0.select("source").attr("src");
                    com.lagradost.api.Log.INSTANCE.d(v12_0.getName(), v10_0);
                    int v4_2 = v12_0.getName();
                    kotlin.jvm.functions.Function1 v6_0 = v12_0.getMainUrl();
                    v1_1.L$0 = v12_0;
                    v1_1.L$1 = v15_1;
                    v1_1.L$2 = v14_0;
                    v1_1.L$3 = v11_0;
                    v1_1.label = 2;
                    org.jsoup.nodes.Document v19_1 = v11_0;
                    com.idlix.Majorplay v22_0 = v12_0;
                    int v3_6 = com.lagradost.cloudstream3.utils.M3u8Helper$Companion.generateM3u8$default(com.lagradost.cloudstream3.utils.M3u8Helper.Companion, v4_2, v10_0, v6_0, 0, 0, 0, v1_1, 56, 0);
                    if (v3_6 != v13_0) {
                        v4_3 = v14_0;
                        v5_1 = v15_1;
                        v6_1 = v22_0;
                        v15_2 = v3_6;
                        v3_7 = v19_1;
                        int v9_2 = ((Iterable) v15_2).iterator();
                        while (v9_2.hasNext()) {
                            v4_3.invoke(v9_2.next());
                        }
                        int v3_8 = v3_7.selectFirst("script:containsData(subtitles)");
                        if (v3_8 != 0) {
                            int v3_9 = v3_8.data();
                            if (v3_9 != 0) {
                                v8_1 = 0;
                                v9_3 = 0;
                                v10_2 = 2;
                                v4_8 = v6_1;
                                v6_2 = kotlin.text.Regex.findAll$default(new kotlin.text.Regex("\\\\\"label\\\\\":\\\\\"([^\\\\\"]*?)\\\\\"[^}]*?\\\\\"path\\\\\":\\\\\"([^\\\\\"]*?)\\\\\""), ((CharSequence) v3_9), 0, 2, 0).iterator();
                                v7_8 = v13_0;
                                if (!v6_2.hasNext()) {
                                    return kotlin.Unit.INSTANCE;
                                } else {
                                    int v8_2;
                                    java.util.Iterator v11_4 = ((kotlin.text.MatchResult) v6_2.next());
                                    int v12_4 = ((String) v11_4.getGroupValues().get(1));
                                    java.util.Iterator v11_6 = ((String) v11_4.getGroupValues().get(v10_2));
                                    if (kotlin.text.StringsKt.startsWith$default(v11_6, "http", v9_3, v10_2, v8_1)) {
                                        v8_2 = v11_6;
                                    } else {
                                        int v13_7 = new StringBuilder();
                                        StringBuilder v14_2 = v4_8.getMainUrl();
                                        int v8_3 = new char[1];
                                        v8_3[v9_3] = 47;
                                        v8_2 = v13_7.append(kotlin.text.StringsKt.trimEnd(v14_2, v8_3)).append(v11_6).toString();
                                    }
                                    v1_1.L$0 = v4_8;
                                    v1_1.L$1 = v5_1;
                                    v1_1.L$2 = v6_2;
                                    v1_1.L$3 = v5_1;
                                    v1_1.label = 3;
                                    v15_2 = com.lagradost.cloudstream3.MainAPIKt.newSubtitleFile$default(v12_4, v8_2, 0, v1_1, 4, 0);
                                    if (v15_2 != v7_8) {
                                        v11_10 = v6_2;
                                        v14_1 = v7_8;
                                        v6_3 = v5_1;
                                        v7_9 = v6_3;
                                        v5_2 = v4_8;
                                    } else {
                                        return v7_8;
                                    }
                                }
                            }
                        }
                        return kotlin.Unit.INSTANCE;
                    } else {
                        return v13_0;
                    }
                } else {
                    return v13_0;
                }
            case 1:
                int v3_17 = ((kotlin.jvm.functions.Function1) v1_1.L$2);
                int v4_11 = ((kotlin.jvm.functions.Function1) v1_1.L$1);
                com.idlix.Majorplay v5_7 = ((com.idlix.Majorplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_2);
                v12_0 = v5_7;
                v13_0 = v14_1;
                v14_0 = v3_17;
                v3_0 = v15_2;
                v15_1 = v4_11;
                break;
            case 2:
                v3_7 = ((org.jsoup.nodes.Document) v1_1.L$3);
                v4_3 = ((kotlin.jvm.functions.Function1) v1_1.L$2);
                v5_1 = ((kotlin.jvm.functions.Function1) v1_1.L$1);
                v6_1 = ((com.idlix.Majorplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_2);
                v13_0 = v14_1;
                break;
            case 3:
                int v4 = 0;
                com.idlix.Majorplay v5_4 = ((kotlin.jvm.functions.Function1) v1_1.L$3);
                kotlin.jvm.functions.Function1 v6_5 = ((java.util.Iterator) v1_1.L$2);
                v7_9 = ((kotlin.jvm.functions.Function1) v1_1.L$1);
                int v8_8 = ((com.idlix.Majorplay) v1_1.L$0);
                kotlin.ResultKt.throwOnFailure(v15_2);
                v9_3 = 0;
                v6_3 = v5_4;
                v5_2 = v8_8;
                v11_10 = v6_5;
                break;
            default:
                throw new IllegalStateException("call to \'resume\' before \'invoke\' with coroutine");
        }
        while(true) {
            v6_3.invoke(v15_2);
            v4_8 = v5_2;
            v5_1 = v7_9;
            v6_2 = v11_10;
            v7_8 = v14_1;
            v8_1 = 0;
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
