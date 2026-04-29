# IPTV Research — Pemahaman & Analisis untuk CloudStream

> Riset untuk implementasi provider IPTV (khusus Indonesia) di CloudStream 3.
> Sumber: `iptv-org/iptv`, `iptv-org/database`, `iptv-org/api`, `iptv-org/epg`, `iptv-epg.org`,
> dan referensi `recloudstream/cloudstream-extensions-multilingual` (`IptvorgProvider`).

---

## 1. Apa itu IPTV?

**IPTV (Internet Protocol Television)** = penyiaran channel TV linear (live) lewat protokol HTTP/HLS/DASH bukan via gelombang/satelit. Setiap channel adalah satu URL stream (umumnya `.m3u8` / HLS, kadang `.mpd` / DASH, atau MPEG-TS langsung).

**Format playlist standar**: file `.m3u` extended (lihat §3) yang berisi banyak entri channel. Aplikasi IPTV (VLC, Tivimate, Kodi, dst) tinggal load file ini lalu menampilkan daftar channel.

**Komponen IPTV ekosistem**:

- **Channel list** — metadata (nama, logo, negara, kategori).
- **Stream URL** — manifest live (HLS/DASH/MPEG-TS).
- **EPG (Electronic Program Guide)** — jadwal acara (XMLTV format).
- **Logo** — URL gambar channel.

---

## 2. Ekosistem `iptv-org`

Empat repo terpisah dengan tanggung jawab jelas:

| Repo | Isi | URL |
|---|---|---|
| `iptv-org/database` | Metadata channel & feed (CSV) | tidak dipanggil langsung |
| `iptv-org/iptv` | Stream URL & playlist M3U yang sudah digenerate | `https://iptv-org.github.io/iptv/...` |
| `iptv-org/api` | JSON API hasil agregasi database+streams | `https://iptv-org.github.io/api/...` |
| `iptv-org/epg` | XMLTV EPG generators | tidak dipakai langsung; pakai `iptv-epg.org` |

### 2.1. `iptv-org/database` (sumber metadata)

Hanya CSV. **Tidak menyimpan stream URL.** Aplikasi tidak fetch ini langsung — fetch hasil agregasinya di `/api`.

Schema penting:

- `channels.csv` — `id` (mis. `MetroTV.id`), `name`, `country` (kode ISO 2 huruf, ID untuk Indonesia), `categories`, `is_nsfw`, `launched`, `closed`, `replaced_by`. **Logo TIDAK di sini** — pindah ke `logos.csv`.
- `feeds.csv` — `channel`, `id`, `name`, `is_main`, `broadcast_area`, `timezones`, `languages`, `format` (`1080p`, `720i`, dst). Satu channel bisa punya banyak feed (HD/SD/regional).
- Reference: `categories.csv`, `languages.csv`, `countries.csv`, `subdivisions.csv`, `regions.csv`, `timezones.csv`, `blocklist.csv`.

Identifikasi channel Indonesia: `country == "ID"` atau ID berakhiran `.id` (`MetroTV.id`, `RCTI.id`, `TransTV.id`, dst).

### 2.2. `iptv-org/iptv` (playlist M3U siap pakai)

Repo ini menggenerate banyak M3U otomatis dari database+streams. **Ini yang paling relevan untuk player**.

URL yang penting:

```
https://iptv-org.github.io/iptv/index.m3u           # ALL channels (~10k+) — terlalu besar
https://iptv-org.github.io/iptv/countries/id.m3u    # ★ Indonesia saja (~250 channel)
https://iptv-org.github.io/iptv/categories/news.m3u # by category
https://iptv-org.github.io/iptv/languages/ind.m3u   # by language code (ISO 639-3)
https://iptv-org.github.io/iptv/regions/asia.m3u
https://iptv-org.github.io/iptv/raw/id.m3u          # raw, tanpa filter
```

**`countries/id.m3u`** sudah cukup untuk seluruh kebutuhan provider Indonesia.

### 2.3. `iptv-org/api` (JSON aggregator)

Static JSON di `https://iptv-org.github.io/api/`:

| Endpoint | Isi | Field penting |
|---|---|---|
| `channels.json` | Semua metadata channel | `id, name, country, categories, is_nsfw, closed, replaced_by, website` |
| `feeds.json` | Variasi feed per channel | `channel, id, is_main, format, languages` |
| `logos.json` | URL logo (terpisah dari channel) | `channel, feed, url, width, height, format` |
| `streams.json` | Stream URL + headers | `channel, feed, title, url, referrer, user_agent, quality, label` |
| `guides.json` | Mapping channel → EPG site | `channel, feed, site, site_id, lang` |
| `categories.json` | Master kategori | `id, name, description` |
| `countries.json` | Master negara | `code, name, flag, languages` |

**⚠️ Catatan penting**: semua adalah **STATIC FILE** di GitHub Pages.
Query string seperti `?channel=MetroTV.id` **TIDAK didukung** (di-ignore oleh Pages, tapi response-nya tetap full file). Kalau mau filter per channel, harus download full JSON lalu filter di sisi klien.

### 2.4. EPG / Program Guide

- **`iptv-epg.org`** — sediakan XMLTV per negara: `https://iptv-epg.org/files/epg-id.xml` (Indonesia, ~137 channel, ~60k acara). Tersedia juga `.xml.gz`.
- **`iptv-org/epg`** — kumpulan scraper XMLTV; biasa di-self-host, tapi untuk consumer paling enak pakai `iptv-epg.org` di atas.
- **CloudStream sendiri belum dukung EPG** secara native (live entry hanya menampilkan satu "Now Playing", tidak ada program list). Jadi EPG di CS3 hanya cosmetic kalau dipakai.

---

## 3. Format M3U Extended (yang dipakai iptv-org)

```m3u
#EXTM3U

#EXTINF:-1 tvg-id="MetroTV.id@SD" tvg-logo="https://i.imgur.com/xxx.png" group-title="News",Metro TV (1080p)
http://example-cdn/metrotv/index.m3u8

#EXTINF:-1 tvg-id="ANTV.id@SD" tvg-logo="..." group-title="General",ANTV (720p)
#EXTVLCOPT:http-user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 ...
http://103.58.160.157:8278/720-ANTV/playlist.m3u8

#EXTINF:-1 tvg-id="AstroBlitarTV.id@SD" tvg-logo="..." http-referrer="https://abtelevisi.com/" group-title="General",Astro Blitar TV (720p)
#EXTVLCOPT:http-referrer=https://abtelevisi.com/
https://5bf7b725107e5.streamlock.net/abtv/abtv/playlist.m3u8
```

### Aturan parsing

| Tag | Fungsi |
|---|---|
| `#EXTM3U` | header wajib di baris pertama |
| `#EXTINF:-1 [attrs],TITLE` | metadata 1 entri channel; `-1` = live (durasi tak hingga) |
| `tvg-id` | ID unik channel (link ke EPG) |
| `tvg-logo` | URL logo |
| `tvg-name` | nama internal (jarang dipakai) |
| `group-title` | kategori (News, Sports, Kids, …) |
| `#EXTVLCOPT:http-user-agent=…` | UA khusus untuk stream ini |
| `#EXTVLCOPT:http-referrer=…` | Referer khusus |
| `#KODIPROP:inputstream.adaptive.license_type=…` | DRM (jarang di iptv-org) |
| baris non-`#` | URL stream |
| `URL\|User-Agent=...&Referer=...` | UA/Referer juga bisa di-encode pipe-style di URL |

Channel name sering ada suffix `(720p)`, `(1080p)`, atau label `[Geo-blocked]`, `[Not 24/7]`. Bisa di-strip kalau mau bersih.

---

## 4. Channel Indonesia (sample dari `countries/id.m3u`)

~250 channel total. Yang relevan untuk user umum:

- **News**: Metro TV, BeritaSatu, BN Channel, CNN Indonesia (kalau ada), CNBC, Kompas TV
- **General**: ANTV, RCTI, MNCTV, Trans TV, Trans 7, Indosiar, SCTV, GTV, NET, TVRI
- **Religious**: Al-Bahjah, Al-Iman, Alwafa Tarim, Angel TV Indonesia
- **Kids**: Biznet Kids, Mentari TV
- **Lifestyle**: Biznet Lifestyle, Biznet Adventure
- **Daerah**: Bali TV, Bandung TV, Banten TV, Atambua TV, Balikpapan TV, dll.

Banyak channel butuh **header khusus** (User-Agent custom, Referer). Contoh: ANTV, BN Channel, Astro Blitar — kalau header tidak diteruskan, stream 403/404.

⚠️ Beberapa channel premium yang ada di iptv-org **geo-blocked** untuk IP non-ID atau marked `[Not 24/7]` (siaran tidak 24 jam).

---

## 5. Bisakah IPTV dipakai di CloudStream? **YA.**

CloudStream sudah support penuh live stream:

- `TvType.Live`
- `LiveSearchResponse`
- `newLiveStreamLoadResponse`
- `ExtractorLink` dengan `ExtractorLinkType.M3U8` + custom `headers`

Bukti: ada provider resmi `IptvorgProvider` & `FreeTVProvider` di `recloudstream/cloudstream-extensions-multilingual` yang sudah live sejak 2022. Kode lengkapnya saya simpan di `iptv-research/IptvorgProvider-ref.kt` & `FreeTVProvider-ref.kt` sebagai referensi.

**Pendekatan yang diakui CS3** (urut dari paling sederhana):

1. **M3U-only** (paling simple, dipakai `IptvorgProvider`):
   - Fetch satu file M3U (`countries/id.m3u`).
   - Parse manual (regex/state-machine).
   - Tiap entri = satu `LiveSearchResponse` → `LiveStreamLoadResponse` → `ExtractorLink M3U8`.
   - Bawa `userAgent` & `referer` dari `#EXTVLCOPT` ke `ExtractorLink.headers`.
2. **JSON API** (`channels.json` + `streams.json` + `logos.json` digabung manual). Lebih kompleks, tidak ada keuntungan signifikan.
3. **JSON API + EPG (XMLTV)** — overkill untuk CloudStream karena UI live-nya tidak tampilkan jadwal acara.

**Keterbatasan CS3 untuk IPTV**:

- Tidak ada UI native untuk EPG (jadwal acara). Jadwal hanya bisa di-cram ke field `plot`.
- Tidak ada channel-zapping (Up/Down channel) layaknya app IPTV dedicated.
- Cast support tergantung apakah HLS-nya bisa di-cast ke device target.

---

## 6. Analisis Provider Anda (`IPTV/.../IPTVProvider.kt`)

### Verdict: **konsep benar, eksekusi salah di banyak titik kritis.**

### 6.1. Bug kritis (bikin gak jalan / sumber salah)

#### ❌ Bug A — `streams.json?channel=X` tidak filter apa-apa

```kotlin
// IPTVProvider.kt:112
val streamApiUrl = "$mainUrl/api/streams.json?channel=$channelId"
```

`streams.json` adalah **static file di GitHub Pages**. Query string `?channel=...` di-ignore. Server return full `streams.json` (~10k+ stream global, bisa puluhan MB) setiap user buka satu channel.

Lalu di `IPTVExtractor.getUrl()`:

```kotlin
val streams = app.get(url).parsedSafe<List<Stream>>() ?: emptyList()
streams.forEach { stream -> callback.invoke(...) }   // → return SEMUA stream global!
```

Dampak: user klik "Metro TV" → app tampilkan ~10000 source link dari seluruh dunia, none-nya benar.

**Fix**: download streams.json sekali, filter `it.channel == channelId` di sisi klien. Atau lebih baik: drop pendekatan JSON, pakai M3U country file.

#### ❌ Bug B — `Channel.logo` selalu null

```kotlin
data class Channel(
    val id: String? = null,
    val name: String? = null,
    val country: String? = null,
    val logo: String? = null,        // ← API channels.json TIDAK PUNYA field ini
    val categories: List<String>? = null
)
```

Schema asli `channels.json` (lihat §2.3):
```
id, name, alt_names, network, owners, country, categories, is_nsfw, launched, closed, replaced_by, website
```

Logo ada di **`logos.json` terpisah**. Akibatnya semua `posterUrl = channel.logo ?: ""` selalu kosong → user lihat thumbnail polos.

**Fix**: fetch `logos.json` juga, build `Map<channelId, logoUrl>` dari entri yang `in_use == true`. Atau pakai M3U yang sudah inline `tvg-logo`.

#### ❌ Bug C — Dual main page row dengan URL sama

```kotlin
override val mainPage = mainPageOf(
    "$mainUrl/api/channels.json" to indonesiaChannels,
    "$mainUrl/api/channels.json" to allChannels  // ← row "all" = ~10k channel global
)
```

Filter "indonesia" via `request.name` works, tapi row "all" akan tampilkan **semua channel dunia** termasuk NSFW, yang tidak relevan dengan tujuan "khusus Indonesia". Dan list 10k+ bikin lag berat.

**Fix**: hapus row `allChannels`, atau ganti jadi "by category" / "by region" yang masih ID-only.

#### ❌ Bug D — `channels.json` tidak punya stream URL

User memanggil `channels.json` di `getMainPage()` & `search()`, tapi stream URL hanya bisa diambil via `streams.json` atau M3U. Kalau channel tidak punya entri di `streams.json`, akan hilang dari layar. Tapi sebaliknya, **channel yang muncul di list mungkin tidak ada stream-nya** → user klik → blank.

**Fix**: filter daftar channel yang BENAR-BENAR punya stream. Lebih efisien: pakai M3U langsung — tiap entri pasti punya URL.

#### ❌ Bug E — `loadLinks` mengandalkan registered extractor

```kotlin
// IPTVProvider.kt:148
override suspend fun loadLinks(...): Boolean {
    return loadExtractor(data, subtitleCallback, callback)
}
```

`loadExtractor()` cari `ExtractorApi` yang `mainUrl`-nya cocok dengan `data` URL. `IPTVExtractor.mainUrl = "https://iptv-org.github.io"` sehingga *secara teknis* cocok dengan `data = "https://iptv-org.github.io/api/streams.json?..."`.

Tapi ini sangat fragile, dan semua hal di IPTVExtractor menderita Bug A. Lebih bersih: hapus `IPTVExtractor`, langsung emit `ExtractorLink` di `loadLinks`.

#### ⚠️ Bug F — Header stream tidak diteruskan

Banyak channel ID butuh `User-Agent` / `Referer` khusus (ANTV, BN Channel, Astro Blitar). Saat ini meskipun `IPTVExtractor` sudah set `this.headers` & `this.referer`, alur Bug A bikin extractor tidak pernah pegang stream yang benar.

#### ⚠️ Bug G — Search re-fetch setiap query

```kotlin
override suspend fun search(query: String): List<SearchResponse>? {
    val channels = app.get(apiUrl).parsedSafe<List<Channel>>() ?: emptyList()
    ...
}
```

Tiap keystroke search → download lagi 10k+ channel (~3MB JSON). Cache!

#### ⚠️ Bug H — `lang = "id"` untuk row "All World"

Kalau row `allChannels` dipertahankan, semua channel non-Indonesia akan di-tag `lang=id` di CloudStream. Misleading.

### 6.2. Tidak benar-benar bug, tapi anti-pattern

- `MainAPI()` punya `mainUrl = "https://iptv-org.github.io"` — fine, tapi setiap fetch tetap pakai full URL — gak bermasalah.
- Tag `Country: $country` redundant (semua ID).
- Tidak ada quickSearch().
- Tidak ada handling untuk `is_nsfw == true` (terbaca via channels.json) — beberapa muncul di list "all" termasuk konten dewasa.
- Channel `closed != null` atau `replaced_by != null` tidak di-skip — bakal ada channel mati di list.

### 6.3. Skor

| Aspek | Skor |
|---|---|
| Pemilihan TvType.Live & LiveSearchResponse | ✅ benar |
| Struktur `MainAPI` 4-method | ✅ benar |
| Pemahaman API endpoint iptv-org | ❌ salah (Bug A, B) |
| Stream resolution | ❌ tidak akan jalan karena Bug A |
| Logo | ❌ tidak jalan karena Bug B |
| Header forwarding | ⚠️ kode ada tapi tidak pernah dipakai |
| Filter ID-only | ⚠️ benar, tapi ada row "all" yang merusak |

**Kesimpulan**: provider Anda dalam bentuk sekarang **tidak akan bisa memutar channel apa pun** karena Bug A bikin `IPTVExtractor` mengembalikan ~10000 link random.

---

## 7. Rekomendasi Refactor

**Opsi terbaik**: ganti pendekatan ke **M3U country file**, mengikuti pattern `IptvorgProvider` resmi.

### 7.1. Sumber data

```
https://iptv-org.github.io/iptv/countries/id.m3u
```

Satu file, semua info ada (URL, logo, kategori, header). Update otomatis tiap commit ke `iptv-org/iptv`.

### 7.2. Arsitektur singkat

```
getMainPage() {
    1x fetch id.m3u → parse → cache hasil dalam memory.
    Group by `group-title` → render tiap group sebagai HomePageList row.
}

load(channelData_json) {
    parse JSON yang dibawa dari mainPage → build LiveStreamLoadResponse
    bawa stream URL + headers ke `loadLinks` lewat dataUrl
}

loadLinks(data) {
    parse JSON → emit 1 ExtractorLink (M3U8) dengan referer + headers.
    Tidak perlu IPTVExtractor sama sekali.
}

search(query) {
    pakai cache yang sama, filter by name contains query.
}
```

### 7.3. Pseudocode parser M3U

(Sudah ada referensi siap pakai di `iptv-research/IptvorgProvider-ref.kt` baris 144-354 — bisa di-copy hampir 1:1.)

```kotlin
data class M3uEntry(
    val name: String,        // dari title
    val url: String,         // baris setelah #EXTINF
    val tvgId: String?,      // attr tvg-id
    val tvgLogo: String?,    // attr tvg-logo
    val groupTitle: String?, // attr group-title
    val userAgent: String?,  // dari #EXTVLCOPT atau attr http-user-agent
    val referrer: String?,   // dari #EXTVLCOPT atau attr http-referrer
)

fun parse(content: String): List<M3uEntry> {
    val lines = content.lineSequence().filter { it.isNotBlank() }.toList()
    val out = mutableListOf<M3uEntry>()
    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        if (line.startsWith("#EXTINF")) {
            val attrs = parseAttrs(line)
            val name = line.substringAfter(",").trim()
            var ua: String? = attrs["http-user-agent"]
            var ref: String? = attrs["http-referrer"]
            // peek opsi #EXTVLCOPT
            var j = i + 1
            while (j < lines.size && lines[j].startsWith("#EXTVLCOPT")) {
                val opt = lines[j]
                if (opt.contains("http-user-agent")) ua = opt.substringAfter("=").trim()
                if (opt.contains("http-referrer")) ref = opt.substringAfter("=").trim()
                j++
            }
            if (j < lines.size && !lines[j].startsWith("#")) {
                val urlRaw = lines[j].trim()
                // dukung URL|User-Agent=..&Referer=..
                val url = urlRaw.substringBefore("|")
                out.add(M3uEntry(name, url, attrs["tvg-id"], attrs["tvg-logo"], attrs["group-title"], ua, ref))
                i = j + 1
                continue
            }
        }
        i++
    }
    return out
}
```

### 7.4. Sketsa provider sederhana

```kotlin
class IPTVProvider : MainAPI() {
    override var mainUrl = "https://iptv-org.github.io"
    override var name = "IPTV Indonesia"
    override var lang = "id"
    override val supportedTypes = setOf(TvType.Live)
    override val hasMainPage = true

    private val playlistUrl = "$mainUrl/iptv/countries/id.m3u"

    private suspend fun loadAll(): List<M3uEntry> {
        // TODO: cache 1 jam
        return parse(app.get(playlistUrl).text)
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val all = loadAll()
        val grouped = all.groupBy { it.groupTitle ?: "Other" }
        val rows = grouped.map { (group, items) ->
            HomePageList(
                group,
                items.map { entry ->
                    newLiveSearchResponse(entry.name, entry.toJson(), TvType.Live) {
                        posterUrl = entry.tvgLogo
                    }
                },
                isHorizontalImages = true
            )
        }
        return newHomePageResponse(rows, hasNext = false)
    }

    override suspend fun load(url: String): LoadResponse {
        val entry = AppUtils.parseJson<M3uEntry>(url)
        return newLiveStreamLoadResponse(entry.name, url, url) {
            posterUrl = entry.tvgLogo
            tags = listOfNotNull(entry.groupTitle)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> =
        loadAll().filter { it.name.contains(query, ignoreCase = true) }
            .map { entry ->
                newLiveSearchResponse(entry.name, entry.toJson(), TvType.Live) {
                    posterUrl = entry.tvgLogo
                }
            }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val entry = AppUtils.parseJson<M3uEntry>(data)
        val headers = buildMap<String, String> {
            entry.userAgent?.let { put("User-Agent", it) }
        }
        callback.invoke(
            newExtractorLink(name, entry.name, entry.url, ExtractorLinkType.M3U8) {
                this.referer = entry.referrer ?: ""
                this.quality = Qualities.Unknown.value
                if (headers.isNotEmpty()) this.headers = headers
            }
        )
        return true
    }
}
```

Hilangkan `IPTVExtractor` sepenuhnya. Plugin file:

```kotlin
@CloudstreamPlugin
class IPTVPlugin : BasePlugin() {
    override fun load() {
        registerMainAPI(IPTVProvider())
    }
}
```

### 7.5. Optional improvement

- **Caching**: simpan parsed M3U di `var` provider dengan TTL 1 jam (M3U di-update sekali sehari di iptv-org).
- **Filter channel dead**: sebelum tampilkan, opsional skip entri yang URL-nya 4xx (mahal, lebih baik skip).
- **Sort kategori**: prioritas "News", "General" di atas, "Undefined" di bawah.
- **Strip suffix kotor**: hapus `(720p)`, `[Not 24/7]`, `[Geo-blocked]` dari nama jika tidak diinginkan, atau pertahankan sebagai badge.
- **EPG (kalau mau)**: parse `https://iptv-epg.org/files/epg-id.xml` → match `tvg-id` → tampilkan acara now/next di `plot`. Tapi format CloudStream tidak benar-benar mendukung jadwal jadi nilainya marjinal.

---

## 8. Ringkasan Eksekutif

1. **IPTV bisa di CloudStream** — sudah ada precedent resmi (`IptvorgProvider`).
2. **Provider Anda saat ini mengandung 5 bug kritis** yang membuat pemutaran tidak akan bekerja sama sekali (Bug A paling fatal: `?channel=` tidak filter, lalu return semua stream global).
3. **Pendekatan paling tepat** untuk Indonesia = **M3U `countries/id.m3u`** (single source of truth), bukan JSON API. M3U sudah bawa URL, logo, header VLC sekaligus.
4. **Refactor ringan**: ~150 baris kode + parser M3U sederhana sudah cukup. Referensi: `iptv-research/IptvorgProvider-ref.kt`.
5. **EPG**: optional, tidak ada UI native CS3 untuk itu — bisa di-skip.

**Recommended next step**: ganti `IPTVProvider.kt` ke pendekatan M3U-based (bisa saya lakukan kalau Anda mau switch ke Code mode / approve).

---

## File terkait di folder ini

- `IptvorgProvider-ref.kt` — referensi resmi dari recloudstream-extensions-multilingual.
- `FreeTVProvider-ref.kt` — provider sejenis lain (gabungan iptv-org + Free-TV).
- `sample-id.m3u` — snapshot Indonesia M3U (~250 channel) per April 2026 untuk inspeksi.
