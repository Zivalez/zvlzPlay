package com.idlix;

import com.lagradost.cloudstream3.Actor;
import com.lagradost.cloudstream3.LoadResponse;
import com.lagradost.cloudstream3.MainAPI;
import com.lagradost.cloudstream3.MainAPIKt;
import com.lagradost.cloudstream3.MainPageData;
import com.lagradost.cloudstream3.MovieLoadResponse;
import com.lagradost.cloudstream3.MovieSearchResponse;
import com.lagradost.cloudstream3.Score;
import com.lagradost.cloudstream3.SearchResponse;
import com.lagradost.cloudstream3.SubtitleFile;
import com.lagradost.cloudstream3.TvSeriesLoadResponse;
import com.lagradost.cloudstream3.TvSeriesSearchResponse;
import com.lagradost.cloudstream3.TvType;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: IdlixProvider.kt */
/* JADX INFO: loaded from: E:\coding\cloudstream\zvlzPlay\.codex-idlix-cs3\classes.dex */
@Metadata(d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u001e\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0096@¢\u0006\u0002\u0010%J\u001e\u0010&\u001a\n\u0012\u0004\u0012\u00020'\u0018\u00010\u001c2\u0006\u0010(\u001a\u00020\u0005H\u0096@¢\u0006\u0002\u0010)J \u0010*\u001a\u0004\u0018\u00010+2\u0006\u0010(\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\"H\u0096@¢\u0006\u0002\u0010,J\u0016\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u0005H\u0096@¢\u0006\u0002\u0010)JF\u00100\u001a\u00020\u000e2\u0006\u00101\u001a\u00020\u00052\u0006\u00102\u001a\u00020\u000e2\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u000205\u0012\u0004\u0012\u000206042\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u000208\u0012\u0004\u0012\u00020604H\u0096@¢\u0006\u0002\u00109R\u001a\u0010\u0004\u001a\u00020\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR\u0014\u0010\r\u001a\u00020\u000eX\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u0005X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0007\"\u0004\b\u0013\u0010\tR\u0014\u0010\u0014\u001a\u00020\u000eX\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010R\u001a\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u0006:"}, d2 = {"Lcom/idlix/IdlixProvider;", "Lcom/lagradost/cloudstream3/MainAPI;", "<init>", "()V", "mainUrl", "", "getMainUrl", "()Ljava/lang/String;", "setMainUrl", "(Ljava/lang/String;)V", "name", "getName", "setName", "hasMainPage", "", "getHasMainPage", "()Z", "lang", "getLang", "setLang", "hasDownloadSupport", "getHasDownloadSupport", "supportedTypes", "", "Lcom/lagradost/cloudstream3/TvType;", "getSupportedTypes", "()Ljava/util/Set;", "mainPage", "", "Lcom/lagradost/cloudstream3/MainPageData;", "getMainPage", "()Ljava/util/List;", "Lcom/lagradost/cloudstream3/HomePageResponse;", "page", "", "request", "Lcom/lagradost/cloudstream3/MainPageRequest;", "(ILcom/lagradost/cloudstream3/MainPageRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "quickSearch", "Lcom/lagradost/cloudstream3/SearchResponse;", "query", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "search", "Lcom/lagradost/cloudstream3/SearchResponseList;", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "load", "Lcom/lagradost/cloudstream3/LoadResponse;", "url", "loadLinks", "data", "isCasting", "subtitleCallback", "Lkotlin/Function1;", "Lcom/lagradost/cloudstream3/SubtitleFile;", "", "callback", "Lcom/lagradost/cloudstream3/utils/ExtractorLink;", "(Ljava/lang/String;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "IdlixProvider"}, k = 1, mv = {2, 4, 0}, xi = 48)
@SourceDebugExtension({"SMAP\nIdlixProvider.kt\nKotlin\n*S Kotlin\n*F\n+ 1 IdlixProvider.kt\ncom/idlix/IdlixProvider\n+ 2 NiceResponse.kt\ncom/lagradost/nicehttp/NiceResponse\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 5 AppUtils.kt\ncom/lagradost/cloudstream3/utils/AppUtils\n+ 6 Extensions.kt\ncom/fasterxml/jackson/module/kotlin/ExtensionsKt\n*L\n1#1,459:1\n73#2,5:460\n73#2,5:470\n73#2,5:489\n73#2,5:512\n73#2,5:534\n73#2,5:561\n73#2,5:566\n73#2,5:571\n1739#3:465\n1814#3,2:466\n1816#3:469\n1795#3,10:475\n2068#3:485\n2069#3:487\n1805#3:488\n1795#3,10:494\n2068#3:504\n2069#3:506\n1805#3:507\n1739#3:508\n1814#3,3:509\n1795#3,10:517\n2068#3:527\n2069#3:529\n1805#3:530\n2068#3,2:531\n2068#3:533\n2068#3,2:539\n2069#3:541\n2068#3,2:576\n1#4:468\n1#4:486\n1#4:505\n1#4:528\n1#4:543\n63#5:542\n64#5,15:544\n50#6:559\n43#6:560\n*S KotlinDebug\n*F\n+ 1 IdlixProvider.kt\ncom/idlix/IdlixProvider\n*L\n73#1:460,5\n103#1:470,5\n140#1:489,5\n174#1:512,5\n235#1:534,5\n323#1:561,5\n351#1:566,5\n366#1:571,5\n74#1:465\n74#1:466,2\n74#1:469\n105#1:475,10\n105#1:485\n105#1:487\n105#1:488\n151#1:494,10\n151#1:504\n151#1:506\n151#1:507\n153#1:508\n153#1:509,3\n174#1:517,10\n174#1:527\n174#1:529\n174#1:530\n210#1:531,2\n228#1:533\n240#1:539,2\n228#1:541\n376#1:576,2\n105#1:486\n151#1:505\n174#1:528\n302#1:543\n302#1:542\n302#1:544,15\n302#1:559\n302#1:560\n*E\n"})
public final class IdlixProvider extends MainAPI {

    @NotNull
    private String mainUrl = MainAPIKt.base64Decode("aHR0cHM6Ly96Mi5pZGxpeGt1LmNvbQ==");

    @NotNull
    private String name = "Idlix";
    private final boolean hasMainPage = true;

    @NotNull
    private String lang = "id";
    private final boolean hasDownloadSupport = true;

    @NotNull
    private final Set<TvType> supportedTypes = SetsKt.setOf(new TvType[]{TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.AsianDrama});

    @NotNull
    private final List<MainPageData> mainPage = MainAPIKt.mainPageOf(new Pair[]{TuplesKt.to(getMainUrl() + "/api/movies?page=%d&limit=36&sort=createdAt", "Movie Terbaru"), TuplesKt.to(getMainUrl() + "/api/series?page=%d&limit=36&sort=createdAt", "TV Series Terbaru"), TuplesKt.to(getMainUrl() + "/api/browse?page=%d&limit=36&sort=latest&network=prime-video", "Amazon Prime"), TuplesKt.to(getMainUrl() + "/api/browse?page=%d&limit=36&sort=latest&network=apple-tv-plus", "Apple TV+"), TuplesKt.to(getMainUrl() + "/api/browse?page=%d&limit=36&sort=latest&network=disney-plus", "Disney+"), TuplesKt.to(getMainUrl() + "/api/browse?page=%d&limit=36&sort=latest&network=hbo", "HBO"), TuplesKt.to(getMainUrl() + "/api/browse?page=%d&limit=36&sort=latest&network=netflix", "Netflix")});

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$getMainPage$1, reason: invalid class name */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider", f = "IdlixProvider.kt", i = {0, 0, 0}, l = {73}, m = "getMainPage", n = {"request", "url", "page"}, nl = {460}, s = {"L$0", "L$1", "I$0"}, v = 2)
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return IdlixProvider.this.getMainPage(0, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$load$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider", f = "IdlixProvider.kt", i = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, l = {138, 173, 234, 259, 274}, m = "load", n = {"url", "url", "response", "data", "title", "poster", "backdrop", "year", "tags", "logourl", "actors", "trailer", "rating", "relatedUrl", "weburl", "url", "response", "data", "title", "poster", "backdrop", "year", "tags", "logourl", "actors", "trailer", "rating", "relatedUrl", "weburl", "recommendations", "episodes", "$this$forEach$iv", "element$iv", "season", "seasonUrl", "seasonNum", "url", "response", "data", "title", "poster", "backdrop", "year", "tags", "logourl", "actors", "trailer", "rating", "relatedUrl", "weburl", "recommendations", "episodes", "url", "response", "data", "title", "poster", "backdrop", "year", "tags", "logourl", "actors", "trailer", "rating", "relatedUrl", "weburl", "recommendations"}, nl = {140, 174, 235, 274, 207}, s = {"L$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "L$14", "L$15", "L$16", "L$18", "L$19", "L$20", "I$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "L$14", "L$15", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "L$14"}, v = 2)
    static final class C00001 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        Object L$10;
        Object L$11;
        Object L$12;
        Object L$13;
        Object L$14;
        Object L$15;
        Object L$16;
        Object L$17;
        Object L$18;
        Object L$19;
        Object L$2;
        Object L$20;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        int label;
        /* synthetic */ Object result;

        C00001(Continuation<? super C00001> continuation) {
            super(continuation);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return IdlixProvider.this.load(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$loadLinks$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider", f = "IdlixProvider.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, l = {317, 334, 344, 359, 372, 379}, m = "loadLinks", n = {"data", "subtitleCallback", "callback", "parsed", "contentId", "contentType", "headers", "isCasting", "data", "subtitleCallback", "callback", "parsed", "contentId", "contentType", "headers", "playResponse", "cookies", "playInfo", "isCasting", "waitTime", "totalWait", "elapsed", "data", "subtitleCallback", "callback", "parsed", "contentId", "contentType", "headers", "playResponse", "cookies", "playInfo", "claimJson", "isCasting", "waitTime", "totalWait", "elapsed", "data", "subtitleCallback", "callback", "parsed", "contentId", "contentType", "headers", "playResponse", "cookies", "playInfo", "claimJson", "claimApi", "redeemJson", "isCasting", "waitTime", "totalWait", "elapsed", "data", "subtitleCallback", "callback", "parsed", "contentId", "contentType", "headers", "playResponse", "cookies", "playInfo", "claimJson", "claimApi", "redeemJson", "iframeResponse", "streamUrl", "isCasting", "waitTime", "totalWait", "elapsed", "data", "subtitleCallback", "callback", "parsed", "contentId", "contentType", "headers", "playResponse", "cookies", "playInfo", "claimJson", "claimApi", "redeemJson", "iframeResponse", "isCasting", "waitTime", "totalWait", "elapsed"}, nl = {322, 335, 351, 366, 376, 388}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "Z$0", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "Z$0", "J$0", "J$1", "J$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "Z$0", "J$0", "J$1", "J$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "Z$0", "J$0", "J$1", "J$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "L$14", "Z$0", "J$0", "J$1", "J$2", "L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "L$8", "L$9", "L$10", "L$11", "L$12", "L$13", "Z$0", "J$0", "J$1", "J$2"}, v = 2)
    static final class C00011 extends ContinuationImpl {
        long J$0;
        long J$1;
        long J$2;
        Object L$0;
        Object L$1;
        Object L$10;
        Object L$11;
        Object L$12;
        Object L$13;
        Object L$14;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        Object L$9;
        boolean Z$0;
        int label;
        /* synthetic */ Object result;

        C00011(Continuation<? super C00011> continuation) {
            super(continuation);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return IdlixProvider.this.loadLinks(null, false, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$quickSearch$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider", f = "IdlixProvider.kt", i = {0}, l = {99}, m = "quickSearch", n = {"query"}, nl = {-1}, s = {"L$0"}, v = 2)
    static final class C00031 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C00031(Continuation<? super C00031> continuation) {
            super(continuation);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return IdlixProvider.this.quickSearch(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$search$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider", f = "IdlixProvider.kt", i = {0, 0, 0}, l = {103}, m = "search", n = {"query", "url", "page"}, nl = {460}, s = {"L$0", "L$1", "I$0"}, v = 2)
    static final class C00041 extends ContinuationImpl {
        int I$0;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C00041(Continuation<? super C00041> continuation) {
            super(continuation);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return IdlixProvider.this.search(null, 0, (Continuation) this);
        }
    }

    @NotNull
    public String getMainUrl() {
        return this.mainUrl;
    }

    public void setMainUrl(@NotNull String str) {
        this.mainUrl = str;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String str) {
        this.name = str;
    }

    public boolean getHasMainPage() {
        return this.hasMainPage;
    }

    @NotNull
    public String getLang() {
        return this.lang;
    }

    public void setLang(@NotNull String str) {
        this.lang = str;
    }

    public boolean getHasDownloadSupport() {
        return this.hasDownloadSupport;
    }

    @NotNull
    public Set<TvType> getSupportedTypes() {
        return this.supportedTypes;
    }

    @NotNull
    public List<MainPageData> getMainPage() {
        return this.mainPage;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0018  */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object getMainPage(int r31, @org.jetbrains.annotations.NotNull com.lagradost.cloudstream3.MainPageRequest r32, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super com.lagradost.cloudstream3.HomePageResponse> r33) {
        /*
            Method dump skipped, instruction units count: 490
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.idlix.IdlixProvider.getMainPage(int, com.lagradost.cloudstream3.MainPageRequest, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit getMainPage$lambda$0$1(String $poster, ApiItem $item, MovieSearchResponse $this$newMovieSearchResponse) {
        String strSubstringBefore$default;
        $this$newMovieSearchResponse.setPosterUrl($poster);
        String releaseDate = $item.getReleaseDate();
        Integer intOrNull = null;
        if (releaseDate != null && (strSubstringBefore$default = StringsKt.substringBefore$default(releaseDate, "-", (String) null, 2, (Object) null)) != null) {
            intOrNull = StringsKt.toIntOrNull(strSubstringBefore$default);
        }
        $this$newMovieSearchResponse.setYear(intOrNull);
        $this$newMovieSearchResponse.setQuality(IdlixProviderKt.getSearchQuality($item.getQuality()));
        $this$newMovieSearchResponse.setScore(Score.Companion.from10($item.getVoteAverage()));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit getMainPage$lambda$0$2(String $poster, ApiItem $item, TvSeriesSearchResponse $this$newTvSeriesSearchResponse) {
        String strSubstringBefore$default;
        $this$newTvSeriesSearchResponse.setPosterUrl($poster);
        String releaseDate = $item.getReleaseDate();
        Integer intOrNull = null;
        if (releaseDate != null && (strSubstringBefore$default = StringsKt.substringBefore$default(releaseDate, "-", (String) null, 2, (Object) null)) != null) {
            intOrNull = StringsKt.toIntOrNull(strSubstringBefore$default);
        }
        $this$newTvSeriesSearchResponse.setYear(intOrNull);
        $this$newTvSeriesSearchResponse.setScore(Score.Companion.from10($item.getVoteAverage()));
        $this$newTvSeriesSearchResponse.setQuality(IdlixProviderKt.getSearchQuality($item.getQuality()));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object quickSearch(@org.jetbrains.annotations.NotNull java.lang.String r5, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super java.util.List<? extends com.lagradost.cloudstream3.SearchResponse>> r6) {
        /*
            r4 = this;
            boolean r0 = r6 instanceof com.idlix.IdlixProvider.C00031
            if (r0 == 0) goto L14
            r0 = r6
            com.idlix.IdlixProvider$quickSearch$1 r0 = (com.idlix.IdlixProvider.C00031) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r1 = r0.label
            int r1 = r1 - r2
            r0.label = r1
            goto L19
        L14:
            com.idlix.IdlixProvider$quickSearch$1 r0 = new com.idlix.IdlixProvider$quickSearch$1
            r0.<init>(r6)
        L19:
            java.lang.Object r1 = r0.result
            java.lang.Object r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r3 = r0.label
            switch(r3) {
                case 0: goto L36;
                case 1: goto L2c;
                default: goto L24;
            }
        L24:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L2c:
            java.lang.Object r2 = r0.L$0
            r5 = r2
            java.lang.String r5 = (java.lang.String) r5
            kotlin.ResultKt.throwOnFailure(r1)
            r3 = r1
            goto L49
        L36:
            kotlin.ResultKt.throwOnFailure(r1)
            java.lang.Object r3 = kotlin.coroutines.jvm.internal.SpillingKt.nullOutSpilledVariable(r5)
            r0.L$0 = r3
            r3 = 1
            r0.label = r3
            java.lang.Object r3 = r4.search(r5, r3, r0)
            if (r3 != r2) goto L49
            return r2
        L49:
            com.lagradost.cloudstream3.SearchResponseList r3 = (com.lagradost.cloudstream3.SearchResponseList) r3
            if (r3 == 0) goto L52
            java.util.List r2 = r3.getItems()
            goto L53
        L52:
            r2 = 0
        L53:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.idlix.IdlixProvider.quickSearch(java.lang.String, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0174, code lost:
    
        if (r0.equals("tv_series") == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x01a6, code lost:
    
        if (r0.equals("series") == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x01b1, code lost:
    
        r30 = getMainUrl() + "/api/series/" + r24.getSlug();
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:37:0x0162. Please report as an issue. */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:7:0x001a  */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object search(@org.jetbrains.annotations.NotNull java.lang.String r38, int r39, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super com.lagradost.cloudstream3.SearchResponseList> r40) {
        /*
            Method dump skipped, instruction units count: 612
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.idlix.IdlixProvider.search(java.lang.String, int, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit search$lambda$0$1(String $poster, Integer $year, SearchApiResult $item, double $rating, MovieSearchResponse $this$newMovieSearchResponse) {
        $this$newMovieSearchResponse.setPosterUrl($poster);
        $this$newMovieSearchResponse.setYear($year);
        $this$newMovieSearchResponse.setQuality(MainAPIKt.getQualityFromString($item.getQuality()));
        $this$newMovieSearchResponse.setScore(Score.Companion.from10(Double.valueOf($rating)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit search$lambda$0$2(String $poster, Integer $year, double $rating, TvSeriesSearchResponse $this$newTvSeriesSearchResponse) {
        $this$newTvSeriesSearchResponse.setPosterUrl($poster);
        $this$newTvSeriesSearchResponse.setYear($year);
        $this$newTvSeriesSearchResponse.setScore(Score.Companion.from10(Double.valueOf($rating)));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: com.lagradost.cloudstream3.ErrorLoadingException */
    /* JADX WARN: Can't wrap try/catch for region: R(17:184|269|185|186|290|187|188|298|189|190|271|191|192|284|193|194|(1:196)(17:197|275|198|199|286|200|201|273|202|203|277|204|213|(2:296|215)(1:217)|218|(1:247)(4:238|(4:241|(2:243|311)(2:244|310)|245|239)|309|246)|248)) */
    /* JADX WARN: Can't wrap try/catch for region: R(17:197|(1:275)|198|199|286|200|201|273|202|203|277|204|213|(2:296|215)(1:217)|218|(1:247)(4:238|(4:241|(2:243|311)(2:244|310)|245|239)|309|246)|248) */
    /* JADX WARN: Can't wrap try/catch for region: R(17:197|275|198|199|286|200|201|273|202|203|277|204|213|(2:296|215)(1:217)|218|(1:247)(4:238|(4:241|(2:243|311)(2:244|310)|245|239)|309|246)|248) */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x08ec, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x08ee, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x08ef, code lost:
    
        r53 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x08f2, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x08f3, code lost:
    
        r52 = r1;
        r53 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x08f7, code lost:
    
        r0.printStackTrace();
        r0 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x0937, code lost:
    
        r1 = r23;
        r9 = r47;
        r29 = r4;
        r31 = r7;
        r23 = r14;
        r7 = r20;
        r14 = r26;
        r33 = r32;
        r4 = r1;
        r26 = r3;
        r20 = r15;
        r15 = r19;
        r32 = r24;
        r3 = r53;
        r19 = r10;
        r24 = r18;
        r18 = r28;
        r27 = r5;
        r28 = r12;
        r5 = r16;
        r16 = r1;
        r30 = r2;
        r12 = r8;
        r8 = r13;
        r13 = r34;
        r2 = r52;
        r11 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x096f, code lost:
    
        r1 = r23;
        r29 = r4;
        r31 = r7;
        r23 = r14;
        r7 = r20;
        r14 = r26;
        r33 = r32;
        r4 = r1;
        r26 = r3;
        r20 = r15;
        r15 = r19;
        r32 = r24;
        r3 = r53;
        r19 = r10;
        r24 = r18;
        r18 = r28;
        r27 = r5;
        r28 = r12;
        r5 = r16;
        r16 = r1;
        r30 = r2;
        r12 = r8;
        r8 = r13;
        r13 = r34;
        r2 = r52;
        r11 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x09a5, code lost:
    
        r1 = r23;
        r29 = r4;
        r31 = r7;
        r23 = r14;
        r7 = r20;
        r14 = r26;
        r33 = r32;
        r4 = r1;
        r26 = r3;
        r20 = r15;
        r15 = r19;
        r32 = r24;
        r3 = r53;
        r19 = r10;
        r24 = r18;
        r18 = r28;
        r27 = r5;
        r28 = r12;
        r5 = r16;
        r16 = r30;
        r30 = r2;
        r12 = r8;
        r8 = r13;
        r13 = r34;
        r2 = r52;
        r11 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x09db, code lost:
    
        r18 = r1;
        r1 = r23;
        r29 = r4;
        r31 = r7;
        r23 = r14;
        r7 = r20;
        r14 = r26;
        r33 = r32;
        r4 = r1;
        r26 = r3;
        r20 = r15;
        r15 = r19;
        r32 = r24;
        r3 = r53;
        r19 = r10;
        r24 = r18;
        r18 = r28;
        r27 = r5;
        r28 = r12;
        r5 = r16;
        r16 = r30;
        r30 = r2;
        r12 = r8;
        r8 = r13;
        r13 = r34;
        r2 = r52;
        r11 = r11;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Path cross not found for [B:107:0x0535, B:142:0x0679], limit reached: 316 */
    /* JADX WARN: Path cross not found for [B:236:0x0a64, B:247:0x0adb], limit reached: 316 */
    /* JADX WARN: Path cross not found for [B:247:0x0adb, B:236:0x0a64], limit reached: 316 */
    /* JADX WARN: Removed duplicated region for block: B:155:0x070f  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x07b3  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0905  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0a64  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0b4a  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0c18  */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0cdb  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x0900 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0294  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0018  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:197:0x089b -> B:275:0x08cb). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:233:0x0a45 -> B:235:0x0a62). Please report as a decompilation issue!!! */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object load(@org.jetbrains.annotations.NotNull java.lang.String r52, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super com.lagradost.cloudstream3.LoadResponse> r53) throws com.lagradost.cloudstream3.ErrorLoadingException {
        /*
            Method dump skipped, instruction units count: 3316
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.idlix.IdlixProvider.load(java.lang.String, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit load$lambda$4$1(String $poster, ApiItem $item, MovieSearchResponse $this$newMovieSearchResponse) {
        String strSubstringBefore$default;
        $this$newMovieSearchResponse.setPosterUrl($poster);
        String releaseDate = $item.getReleaseDate();
        if (releaseDate == null) {
            releaseDate = $item.getFirstAirDate();
        }
        Integer intOrNull = null;
        if (releaseDate != null && (strSubstringBefore$default = StringsKt.substringBefore$default(releaseDate, "-", (String) null, 2, (Object) null)) != null) {
            intOrNull = StringsKt.toIntOrNull(strSubstringBefore$default);
        }
        $this$newMovieSearchResponse.setYear(intOrNull);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit load$lambda$4$2(String $poster, ApiItem $item, TvSeriesSearchResponse $this$newTvSeriesSearchResponse) {
        String strSubstringBefore$default;
        $this$newTvSeriesSearchResponse.setPosterUrl($poster);
        String releaseDate = $item.getReleaseDate();
        if (releaseDate == null) {
            releaseDate = $item.getFirstAirDate();
        }
        Integer intOrNull = null;
        if (releaseDate != null && (strSubstringBefore$default = StringsKt.substringBefore$default(releaseDate, "-", (String) null, 2, (Object) null)) != null) {
            intOrNull = StringsKt.toIntOrNull(strSubstringBefore$default);
        }
        $this$newTvSeriesSearchResponse.setYear(intOrNull);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit load$lambda$5$0(Episode $ep, DetailResponse $data, com.lagradost.cloudstream3.Episode $this$newEpisode) {
        $this$newEpisode.setName($ep.getName());
        $this$newEpisode.setSeason($data.getFirstSeason().getSeasonNumber());
        $this$newEpisode.setEpisode($ep.getEpisodeNumber());
        $this$newEpisode.setDescription($ep.getOverview());
        $this$newEpisode.setRunTime($ep.getRuntime());
        Score.Companion companion = Score.Companion;
        Object voteAverage = $ep.getVoteAverage();
        $this$newEpisode.setScore(companion.from10(voteAverage != null ? voteAverage.toString() : null));
        MainAPIKt.addDate$default($this$newEpisode, $ep.getAirDate(), (String) null, 2, (Object) null);
        String it = $ep.getStillPath();
        $this$newEpisode.setPosterUrl(it != null ? "https://image.tmdb.org/t/p/w300" + it : null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit load$lambda$6$0$0(Episode $ep, int $seasonNum, com.lagradost.cloudstream3.Episode $this$newEpisode) {
        $this$newEpisode.setName($ep.getName());
        $this$newEpisode.setSeason(Integer.valueOf($seasonNum));
        $this$newEpisode.setEpisode($ep.getEpisodeNumber());
        $this$newEpisode.setDescription($ep.getOverview());
        $this$newEpisode.setRunTime($ep.getRuntime());
        Score.Companion companion = Score.Companion;
        Object voteAverage = $ep.getVoteAverage();
        $this$newEpisode.setScore(companion.from10(voteAverage != null ? voteAverage.toString() : null));
        MainAPIKt.addDate$default($this$newEpisode, $ep.getAirDate(), (String) null, 2, (Object) null);
        String it = $ep.getStillPath();
        $this$newEpisode.setPosterUrl(it != null ? "https://image.tmdb.org/t/p/w300" + it : null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$load$4, reason: invalid class name */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lcom/lagradost/cloudstream3/TvSeriesLoadResponse;"}, k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider$load$4", f = "IdlixProvider.kt", i = {0}, l = {268}, m = "invokeSuspend", n = {"$this$newTvSeriesLoadResponse"}, nl = {269}, s = {"L$0"}, v = 2)
    static final class AnonymousClass4 extends SuspendLambda implements Function2<TvSeriesLoadResponse, Continuation<? super Unit>, Object> {
        final /* synthetic */ List<Actor> $actors;
        final /* synthetic */ String $backdrop;
        final /* synthetic */ DetailResponse $data;
        final /* synthetic */ String $logourl;
        final /* synthetic */ String $poster;
        final /* synthetic */ Object $rating;
        final /* synthetic */ List<SearchResponse> $recommendations;
        final /* synthetic */ List<String> $tags;
        final /* synthetic */ String $trailer;
        final /* synthetic */ Integer $year;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(String str, String str2, String str3, Integer num, DetailResponse detailResponse, List<String> list, Object obj, List<Actor> list2, String str4, List<? extends SearchResponse> list3, Continuation<? super AnonymousClass4> continuation) {
            super(2, continuation);
            this.$poster = str;
            this.$backdrop = str2;
            this.$logourl = str3;
            this.$year = num;
            this.$data = detailResponse;
            this.$tags = list;
            this.$rating = obj;
            this.$actors = list2;
            this.$trailer = str4;
            this.$recommendations = list3;
        }

        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            Continuation<Unit> anonymousClass4 = new AnonymousClass4(this.$poster, this.$backdrop, this.$logourl, this.$year, this.$data, this.$tags, this.$rating, this.$actors, this.$trailer, this.$recommendations, continuation);
            anonymousClass4.L$0 = obj;
            return anonymousClass4;
        }

        public final Object invoke(TvSeriesLoadResponse tvSeriesLoadResponse, Continuation<? super Unit> continuation) {
            return create(tvSeriesLoadResponse, continuation).invokeSuspend(Unit.INSTANCE);
        }

        public final Object invokeSuspend(Object $result) {
            LoadResponse loadResponse = (TvSeriesLoadResponse) this.L$0;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    loadResponse.setPosterUrl(this.$poster);
                    loadResponse.setBackgroundPosterUrl(this.$backdrop);
                    loadResponse.setLogoUrl(this.$logourl);
                    loadResponse.setYear(this.$year);
                    loadResponse.setPlot(this.$data.getOverview());
                    loadResponse.setTags(this.$tags);
                    Score.Companion companion = Score.Companion;
                    Object obj = this.$rating;
                    loadResponse.setScore(companion.from10(obj != null ? obj.toString() : null));
                    LoadResponse.Companion.addActorsOnly(loadResponse, this.$actors);
                    this.L$0 = loadResponse;
                    this.label = 1;
                    if (LoadResponse.Companion.addTrailer$default(LoadResponse.Companion, loadResponse, this.$trailer, (String) null, false, (Continuation) this, 6, (Object) null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            LoadResponse.Companion.addTMDbId(loadResponse, this.$data.getTmdbId());
            LoadResponse.Companion.addImdbId(loadResponse, this.$data.getImdbId());
            loadResponse.setRecommendations(this.$recommendations);
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$load$5, reason: invalid class name */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lcom/lagradost/cloudstream3/MovieLoadResponse;"}, k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider$load$5", f = "IdlixProvider.kt", i = {0}, l = {286}, m = "invokeSuspend", n = {"$this$newMovieLoadResponse"}, nl = {287}, s = {"L$0"}, v = 2)
    static final class AnonymousClass5 extends SuspendLambda implements Function2<MovieLoadResponse, Continuation<? super Unit>, Object> {
        final /* synthetic */ List<Actor> $actors;
        final /* synthetic */ String $backdrop;
        final /* synthetic */ DetailResponse $data;
        final /* synthetic */ String $logourl;
        final /* synthetic */ String $poster;
        final /* synthetic */ Object $rating;
        final /* synthetic */ List<SearchResponse> $recommendations;
        final /* synthetic */ List<String> $tags;
        final /* synthetic */ String $trailer;
        final /* synthetic */ Integer $year;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(String str, String str2, String str3, Integer num, DetailResponse detailResponse, List<String> list, Object obj, List<Actor> list2, String str4, List<? extends SearchResponse> list3, Continuation<? super AnonymousClass5> continuation) {
            super(2, continuation);
            this.$poster = str;
            this.$backdrop = str2;
            this.$logourl = str3;
            this.$year = num;
            this.$data = detailResponse;
            this.$tags = list;
            this.$rating = obj;
            this.$actors = list2;
            this.$trailer = str4;
            this.$recommendations = list3;
        }

        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            Continuation<Unit> anonymousClass5 = new AnonymousClass5(this.$poster, this.$backdrop, this.$logourl, this.$year, this.$data, this.$tags, this.$rating, this.$actors, this.$trailer, this.$recommendations, continuation);
            anonymousClass5.L$0 = obj;
            return anonymousClass5;
        }

        public final Object invoke(MovieLoadResponse movieLoadResponse, Continuation<? super Unit> continuation) {
            return create(movieLoadResponse, continuation).invokeSuspend(Unit.INSTANCE);
        }

        public final Object invokeSuspend(Object $result) {
            LoadResponse loadResponse = (MovieLoadResponse) this.L$0;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    loadResponse.setPosterUrl(this.$poster);
                    loadResponse.setBackgroundPosterUrl(this.$backdrop);
                    loadResponse.setLogoUrl(this.$logourl);
                    loadResponse.setYear(this.$year);
                    loadResponse.setPlot(this.$data.getOverview());
                    loadResponse.setTags(this.$tags);
                    Score.Companion companion = Score.Companion;
                    Object obj = this.$rating;
                    loadResponse.setScore(companion.from10(obj != null ? obj.toString() : null));
                    LoadResponse.Companion.addActorsOnly(loadResponse, this.$actors);
                    this.L$0 = loadResponse;
                    this.label = 1;
                    if (LoadResponse.Companion.addTrailer$default(LoadResponse.Companion, loadResponse, this.$trailer, (String) null, false, (Continuation) this, 6, (Object) null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            LoadResponse.Companion.addTMDbId(loadResponse, this.$data.getTmdbId());
            LoadResponse.Companion.addImdbId(loadResponse, this.$data.getImdbId());
            loadResponse.setRecommendations(this.$recommendations);
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Path cross not found for [B:169:0x0324, B:49:0x0339], limit reached: 161 */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0677  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0772  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0777  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0836 A[LOOP:0: B:134:0x0830->B:136:0x0836, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:143:0x08f4 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:144:0x08f5  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0357  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x035c  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0471  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x04ae  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0568  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0018  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0672  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:76:0x052b -> B:77:0x054b). Please report as a decompilation issue!!! */
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object loadLinks(@org.jetbrains.annotations.NotNull java.lang.String r58, boolean r59, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super com.lagradost.cloudstream3.SubtitleFile, kotlin.Unit> r60, @org.jetbrains.annotations.NotNull kotlin.jvm.functions.Function1<? super com.lagradost.cloudstream3.utils.ExtractorLink, kotlin.Unit> r61, @org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super java.lang.Boolean> r62) {
        /*
            Method dump skipped, instruction units count: 2334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.idlix.IdlixProvider.loadLinks(java.lang.String, boolean, kotlin.jvm.functions.Function1, kotlin.jvm.functions.Function1, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: renamed from: com.idlix.IdlixProvider$loadLinks$4, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: IdlixProvider.kt */
    @Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "subtitle", "Lcom/idlix/Subtitle;"}, k = 3, mv = {2, 4, 0}, xi = 48)
    @DebugMetadata(c = "com.idlix.IdlixProvider$loadLinks$4", f = "IdlixProvider.kt", i = {0}, l = {381}, m = "invokeSuspend", n = {"subtitle"}, nl = {380}, s = {"L$0"}, v = 2)
    static final class C00024 extends SuspendLambda implements Function2<Subtitle, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function1<SubtitleFile, Unit> $subtitleCallback;
        /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00024(Function1<? super SubtitleFile, Unit> function1, Continuation<? super C00024> continuation) {
            super(2, continuation);
            this.$subtitleCallback = function1;
        }

        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            Continuation<Unit> c00024 = new C00024(this.$subtitleCallback, continuation);
            c00024.L$0 = obj;
            return c00024;
        }

        public final Object invoke(Subtitle subtitle, Continuation<? super Unit> continuation) {
            return create(subtitle, continuation).invokeSuspend(Unit.INSTANCE);
        }

        public final Object invokeSuspend(Object $result) {
            Object objNewSubtitleFile$default;
            Function1<SubtitleFile, Unit> function1;
            Subtitle subtitle = (Subtitle) this.L$0;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Function1<SubtitleFile, Unit> function12 = this.$subtitleCallback;
                    this.L$0 = SpillingKt.nullOutSpilledVariable(subtitle);
                    this.L$1 = function12;
                    this.label = 1;
                    objNewSubtitleFile$default = MainAPIKt.newSubtitleFile$default(subtitle.getLabel(), subtitle.getPath(), (Function2) null, (Continuation) this, 4, (Object) null);
                    if (objNewSubtitleFile$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    function1 = function12;
                    break;
                    break;
                case 1:
                    function1 = (Function1) this.L$1;
                    ResultKt.throwOnFailure($result);
                    objNewSubtitleFile$default = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            function1.invoke(objNewSubtitleFile$default);
            return Unit.INSTANCE;
        }
    }
}
