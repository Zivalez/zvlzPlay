# Kuramanime Scraping Analysis (Detailed)

> **Catatan revisi (28 Apr 2026)**: Dokumen di-update setelah investigasi live di `v17.kuramanime.ink` pakai Playwright (network trace + JS inspection + hidden input audit). Versi sebelumnya menyimpan beberapa asumsi yang **ternyata salah**, terutama soal `MIX_AUTH_KEY`/`MIX_AUTH_TOKEN` (sebelumnya dianggap gak terpakai) dan body POST (sebelumnya dianggap kosong).

---

## 1. Overview

Halaman episode di Kuramanime **tidak langsung memberikan data asli** ketika diakses pertama kali. Website pakai **mekanisme proteksi multi-step berbasis token + custom headers + body** untuk mencegah scraping langsung.

Akibatnya:

* Request `GET` biasa cuma dapat halaman "dummy" (decoy)
* Data penting seperti **download link / video URL tidak tersedia**
* Dibutuhkan **6 step request** dengan headers + body khusus untuk dapatin konten asli
* Tanpa header tertentu, server malah kasih **token bogus** (palsu, terlihat valid tapi reject di step berikutnya)

---

## 2. Perbedaan Halaman

### 2.1 `kuramanimev1.html` (Initial / Decoy Response)

Hasil request ke URL episode tanpa parameter, headers auth, atau body khusus.

Ciri:

* Banyak **meta tag SEO** (keyword spam)
* `<div id="animeDownloadLink">` cuma berisi placeholder error: `<span class="reload-error">cobalah untuk memuat ulang...</span>`
* Element `<video id="player">` ada tapi tanpa `src`
* Ada `<div id="tokenErrorText" style="display:none">` (placeholder error JS popup)

> Halaman ini **bukan** sumber data sebenarnya. Tidak bisa dipakai untuk scraping konten.

---

### 2.2 `kuramanimev2.html` (Bypassed / Real Page)

Hasil ketika request berhasil melewati semua proteksi (POST dengan token URL valid + body `authorization` + headers benar).

Ciri:

* `<div id="animeDownloadLink">` berisi `<h6>` (label kualitas) + `<a>` (link download) bergantian
* Element `<video id="player">` populated dengan `data-hls-src`
* Source download: `pixeldrain.com`, `mypikpak.com`, `dropbox.com`, `mega.co.nz`, dan endpoint internal `v1.kuramadrive.com/kdrive/...` & `/kturbo/...`

> Ini target halaman yang harus didapatkan scraper.

⚠️ **Catatan penting**: halaman v2 **tetap mengandung** string `"Terjadi kesalahan"` di dalam `<div id="tokenErrorText" style="display:none">`. Jadi deteksi sukses bypass **tidak bisa** pakai `text.contains("Terjadi kesalahan")`. Harus pakai struktur DOM: `#animeDownloadLink .reload-error` empty + `#animeDownloadLink a` non-empty.

---

## 3. Mekanisme Proteksi (Confirmed via Network Trace)

Kuramanime memakai kombinasi:

* **CSRF token** per session (40 char) — diambil dari `<meta name="csrf-token">`
* **Custom auth headers** non-standar (`x-fuck-id`, `x-csrf-token`, `x-request-id`, `x-request-index`, `x-requested-with`)
* **2 token terpisah** — satu di URL query, satu di body POST
* **Method strict POST** dengan `Content-Type: application/x-www-form-urlencoded`
* **JS config terpencar** di file dengan nama yang bisa berubah (referensi via attribute `data-kk`)
* **JS wrapper obfuscated** (`leviathan.js`) yang inject Authorization header

---

### 3.1 Discovery: Hidden Input di HTML v1

Halaman v1 (decoy) sudah expose banyak endpoint via hidden `<input>`. Yang relevan untuk bypass:

| Input ID / Attribute | Value (saat investigasi) | Fungsi |
|---|---|---|
| `appUrl` | `https://v17.kuramanime.ink` | Base URL |
| `keepAliveTokenRoute` | `/misc/token/keep-alive` | Init session token |
| `refreshTokenUrl` | `/misc/token/refresh-token` | Refresh CSRF |
| `tokenAuthJs` | `/storage/leviathan.js?v=1448` | URL JS wrapper HTTP |
| `[data-kk]` (di `<div>`) | `wzl3ClXO8shDECR` | Nama file config JS (tanpa ekstensi) |

`data-kk` muncul di `<div class="col-lg-12 mt-3" data-kk="...">`. **Bisa berubah** kapan saja, jadi scraper sebaiknya parse dinamis dari sini.

---

### 3.2 Konfigurasi dari JavaScript

File: `/assets/js/{data-kk}.js` (saat ini `wzl3ClXO8shDECR.js`)

```js
window.process = {
    env: {
        MIX_PREFIX_AUTH_ROUTE_PARAM: 'assets/',
        MIX_AUTH_ROUTE_PARAM: 'Ks6sqSgloPTlHMl.txt',
        MIX_AUTH_KEY: 'rFj8fp1nxMuNfKq',
        MIX_AUTH_TOKEN: 'ijjAwj6Jze0kscx',
        MIX_PAGE_TOKEN_KEY: 'Ub3BzhijicHXZdv',
        MIX_STREAM_SERVER_KEY: 'C2XAPerzX1BM7V9',
    }
};
```

Penjelasan tiap field (versi yang **benar**, hasil network trace):

| Field | Fungsi |
|---|---|
| `MIX_PREFIX_AUTH_ROUTE_PARAM` + `MIX_AUTH_ROUTE_PARAM` | Path file token URL → `/assets/Ks6sqSgloPTlHMl.txt` |
| **`MIX_AUTH_KEY` + `MIX_AUTH_TOKEN`** | **Disusun jadi header `x-fuck-id: KEY:TOKEN`** untuk auth saat fetch token file. ⚠️ Tanpa header ini, server kasih token bogus! |
| `MIX_PAGE_TOKEN_KEY` | Nama key untuk query parameter (saat ini `Ub3BzhijicHXZdv`) |
| `MIX_STREAM_SERVER_KEY` | Nama key untuk server video di query (saat ini `C2XAPerzX1BM7V9`) |

> **Koreksi versi sebelumnya**: dokumen lama bilang `MIX_AUTH_KEY` & `MIX_AUTH_TOKEN` "tidak terpakai". Itu **salah**. Keduanya digabung jadi header `x-fuck-id` (lihat 3.5).

---

### 3.3 CSRF Token

Diambil dari meta tag halaman v1:

```html
<meta name="csrf-token" content="iZEJhCywjbk9E6oZ0jUJlJ1jNL0eU8Qv9gqjXQcG">
```

Dipakai di **semua** request lanjutan sebagai header:

```
x-csrf-token: iZEJhCywjbk9E6oZ0jUJlJ1jNL0eU8Qv9gqjXQcG
x-requested-with: XMLHttpRequest
```

---

### 3.4 `leviathan.js` — Wrapper HTTP Obfuscated

File `/storage/leviathan.js?v=NNN` adalah **JavaScript yang heavy obfuscated** (obfuscator.io style: RC4 string encryption + control flow flattening + dead code injection + bracket math). URL versinya (`?v=1448`) ada di hidden `<input id="tokenAuthJs">`.

Fungsi: define beberapa helper di `window` yang dipakai JS halaman buat fire request authenticated. Pattern minimal yang berhasil di-extract dari ujung file:

```js
// Helper: jQuery AJAX wrapper dengan Bearer auth
window['<obfuscated>'] = async function(url, method, data, success, error) {
    return $.ajax({
        url, method, data, success, error,
        headers: { Authorization: 'Bearer ' + _bearer }
    });
};

// Helper: fetch wrapper dengan JSON body
window['<obfuscated>'] = async function(url, method, payload) {
    return fetch(url, {
        method,
        headers: {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });
};
```

Implikasi:

* Algoritma penyusunan token tidak bisa di-replicate persis di Kotlin tanpa effort besar (deobfuscate manual atau eksekusi via Rhino/QuickJS yang sudah ada di dependency)
* Approach native scraping (tanpa WebView) jadi rapuh
* Network trace menunjukkan `Authorization: Bearer` di kode JS akhirnya muncul sebagai `x-fuck-id: KEY:TOKEN` saat hit endpoint Kuramanime sendiri (kemungkinan ada interceptor yang rewrite)

---

### 3.5 Fetch Token URL — `/assets/Ks6sqSgloPTlHMl.txt`

**Method**: `GET`

**Headers wajib** (confirmed dari network trace):

```http
GET /assets/Ks6sqSgloPTlHMl.txt HTTP/1.1
Host: v17.kuramanime.ink
x-fuck-id: rFj8fp1nxMuNfKq:ijjAwj6Jze0kscx
x-csrf-token: <csrf_token_dari_meta>
x-request-id: <random_short_string>
x-request-index: 0
x-requested-with: XMLHttpRequest
referer: https://v17.kuramanime.ink/anime/{id}/{slug}/episode/{ep}
accept: text/plain, */*; q=0.01
```

**Bukti header sensitivity** (test direct):

| Request | Token Returned | Kegunaan |
|---|---|---|
| `GET /assets/Ks6sqSgloPTlHMl.txt` (header minimum) | `1kE4VTfQbH` | **Bogus** — POST dengan token ini akan reject |
| `GET ...` dengan `x-fuck-id` + `x-csrf-token` lengkap | `A38kCgU3eT` | **Valid** — work di POST |

Server kemungkinan ada middleware yang validate `x-fuck-id` (= `MIX_AUTH_KEY:MIX_AUTH_TOKEN`); kalau gagal/missing, kasih token decoy untuk membingungkan scraper.

Token return-nya **string pendek** (8–12 char), case-sensitive, **berubah tiap request**.

---

### 3.6 GET `/check-episode` — Page Number (BUKAN Authorization)

> 🔄 **Koreksi (28 Apr 2026)**: Hipotesis awal saya **salah**. `/check-episode` cuma return `"1"` (page number), **bukan** authorization token.

Browser fire request:

```http
GET /anime/{id}/episode/{ep}/check-episode HTTP/1.1
x-csrf-token: <csrf>
x-requested-with: XMLHttpRequest
```

**Response**: `"1"` (literally — angka 1 sebagai string/integer).

Bukti dari `anime-episode.min.js`:

```js
function firstCheck() {
    window.checkUrl = $("#checkEp").val();   // /anime/.../check-episode
    refetchAuthJs();                          // re-load leviathan.js
    $.ajax({
        url: checkUrl, method: "GET", dataType: "JSON",
        success: function(a) {
            // a = response = 1
            loadFullPage({ to: location.href, page: a, ... });
            //                                  ^^^^^ ← "1" dipakai jadi PAGE
        }
    });
}
```

Jadi `page=1` di query string POST step terakhir adalah **value dari `/check-episode` response**, bukan hardcoded.

### 3.6b Body `authorization=...` — Generated Client-Side

Body POST `authorization=kJuHHkaqcBFXiGMHQf6bJw8YAyDcwGD8Ur` di-generate **di dalam** function `jLoadSecure()` yang ada di `leviathan.js` (obfuscated). Algoritma exact-nya:

* Tidak terlihat dari source — heavy obfuscation (RC4 + control flow flatten)
* Kemungkinan kombinasi: csrf-token + cookies + URL params + timestamp/random
* **Tidak feasible direplikasi native di Kotlin** tanpa eksekusi JS via Rhino/QuickJS dengan fake DOM (kompleks)

> ⚠️ **Implikasi**: Native HTTP client tidak akan bisa mereplikasi POST body ini. Satu-satunya cara reliable = **pakai WebView** untuk biarkan browser eksekusi `leviathan.js` secara natural.

Flow JS asli:

```js
function loadFullPage(e) {
    teardownPlayer();
    getStTk(token => fetchTokenSuccess(e, token), fetchTokenError);
    // getStTk = fetch token URL (.txt file dengan x-fuck-id)
}

function fetchTokenSuccess(e, a) {
    const t = new URL(e.to);
    t.searchParams.set(process.env.MIX_PAGE_TOKEN_KEY, a);
    t.searchParams.set(process.env.MIX_STREAM_SERVER_KEY, streamServer);
    t.searchParams.set("page", e.page);
    jLoadSecure(e.class, t.toString(), {}, callback);
    // jLoadSecure = POST dengan body `authorization=<computed>` (di leviathan.js)
}
```

---

### 3.7 POST Episode dengan Body — Step Final

URL:

```
POST /anime/{id}/{slug}/episode/{ep}
     ?{MIX_PAGE_TOKEN_KEY}={token_url}
     &{MIX_STREAM_SERVER_KEY}=kuramadrive
     &page=1
```

Headers:

```http
x-csrf-token: <csrf>
x-requested-with: XMLHttpRequest
content-type: application/x-www-form-urlencoded; charset=UTF-8
accept: text/html, */*; q=0.01
referer: https://v17.kuramanime.ink/anime/{id}/{slug}/episode/{ep}
```

**Body** (form-encoded):

```
authorization=<token_dari_check-episode>
```

**Contoh nyata** dari trace 28 Apr 2026:

```http
POST /anime/4811/tongari-boushi-no-atelier/episode/4
     ?Ub3BzhijicHXZdv=A38kCgU3eT
     &C2XAPerzX1BM7V9=kuramadrive
     &page=1

authorization=kJuHHkaqcBFXiGMHQf6bJw8YAyDcwGD8Ur
```

Response: HTML halaman v2 (full, dengan `<a>` download links populated di `#animeDownloadLink`).

Pilihan value untuk `MIX_STREAM_SERVER_KEY` (selain `kuramadrive`) — observed dari `<select id="changeServer">` di halaman v2:

* `kuramadrive` (default, server internal — return halaman dengan link download lengkap)
* `doodstream` / `filemoon` / `mega` / `rpmshare` / `streamp2p` (server eksternal — halaman akan punya iframe/embed berbeda)

Untuk dapat **download link**, pakai `kuramadrive`. Untuk dapat embed streaming alternatif, pakai server lain.

---

## 4. Alur Lengkap (Flow)

```
1. GET /anime/{id}/{slug}/episode/{ep}
       ↓ dapat HTML decoy + cookie session
       ↓ extract dari HTML:
         - <meta name="csrf-token" content="..."> → CSRF
         - <div data-kk="..."> → nama file JS config
         - <input id="tokenAuthJs"> → URL leviathan.js (kalau perlu)
         - cookie set oleh server (XSRF-TOKEN, dll)

2. (opsional warmup, browser asli melakukan ini tapi mungkin bisa di-skip)
   GET  /banner/get?is_home=
   POST /misc/token/keep-alive

3. GET /assets/js/{data-kk}.js
       ↓ parse window.process.env via regex:
         - MIX_AUTH_KEY, MIX_AUTH_TOKEN
         - MIX_AUTH_ROUTE_PARAM, MIX_PREFIX_AUTH_ROUTE_PARAM
         - MIX_PAGE_TOKEN_KEY, MIX_STREAM_SERVER_KEY

4. GET /anime/{id}/episode/{ep}/check-episode
   Headers:
     x-csrf-token: {csrf}
     x-requested-with: XMLHttpRequest
       ↓ parse response (JSON?) → extract "authorization" token

5. GET /{MIX_PREFIX_AUTH_ROUTE_PARAM}{MIX_AUTH_ROUTE_PARAM}
       (= /assets/Ks6sqSgloPTlHMl.txt)
   Headers:
     x-fuck-id: {MIX_AUTH_KEY}:{MIX_AUTH_TOKEN}
     x-csrf-token: {csrf}
     x-request-id: {random_short_string}
     x-request-index: 0
     x-requested-with: XMLHttpRequest
       ↓ dapat token URL (8-12 char, case-sensitive)

6. POST /anime/{id}/{slug}/episode/{ep}
        ?{MIX_PAGE_TOKEN_KEY}={token_url}
        &{MIX_STREAM_SERVER_KEY}=kuramadrive
        &page=1
   Headers:
     x-csrf-token: {csrf}
     x-requested-with: XMLHttpRequest
     content-type: application/x-www-form-urlencoded; charset=UTF-8
   Body:
     authorization={token_dari_check-episode}
       ↓ HTML halaman v2 lengkap

7. Parse #animeDownloadLink:
   - <h6>: label kualitas (e.g. "MKV 480p (Softsub) — (196.00 MB)")
   - <a href="...">: link download
   - Iterasi children secara stateful (h6 set quality, a punya href ke quality terakhir)
```

---

## 5. Insight Teknis

### 5.1 Bukan Scraping Biasa

Pendekatan minimal yang work:

```
Multi-step authenticated request
+ custom headers (x-fuck-id, x-csrf-token, x-request-id, x-request-index)
+ body form-encoded (authorization=...)
+ proper sequencing (check-episode SEBELUM token URL, keduanya SEBELUM POST)
+ cookie session di-forward antar request
```

GET sederhana → parse HTML **tidak akan pernah** dapat link download.

---

### 5.2 Server "Decoy yang Pintar"

Server gak return `401`/`403` kalau auth gagal. Sebaliknya:

* GET halaman tanpa auth → kasih HTML decoy v1 (terlihat normal, tapi data kosong)
* GET token tanpa `x-fuck-id` → kasih token bogus (terlihat valid, tapi reject di POST)
* POST tanpa body → kemungkinan return halaman v1 lagi

Strategi anti-scraper: **bikin scraper berpikir mereka sukses padahal data palsu**, sehingga waktu debug terbuang sebelum sadar pendekatannya salah.

---

### 5.3 Obfuskasi Sebagai Pertahanan

Proteksi ini **bukan** pakai:

* Cloudflare Turnstile / heavy challenge
* Captcha visual
* Browser fingerprinting agresif

Tapi pakai:

* Custom JS obfuscator (RC4 string encryption + control flow flattening)
* Token rotasi cepat (per request, dynamic)
* Header naming non-konvensional (`x-fuck-id` jelas non-standard, sengaja awkward)
* Layered token (URL token + authorization token = dua pintu)

Tujuan: **menghentikan scraper sederhana**, sambil tetap mudah diakses oleh user real.

---

### 5.4 Halaman v1 = Decoy Page

Halaman v1 punya UI lengkap dan bahkan rendering player kosong + tombol dummy, tapi:

* Player `<video>` tanpa `src`
* Section download isi placeholder error (`reload-error`)
* Banyak SEO keyword spam di body
* Section `tokenErrorText` (hidden) menyimpan teks error placeholder untuk JS popup

Tujuan: **bikin scraper berpikir sudah dapat halaman valid** dan stop investigasi.

---

## 6. Kesimpulan

Untuk dapat direct video / download URL dari Kuramanime, scraper harus mereplikasi **seluruh 6-step sequence** di section 4. Tidak cukup hanya:

* ❌ Ambil HTML awal saja
* ❌ GET dengan token saja (tanpa header & body)
* ❌ POST tanpa body
* ❌ Skip step `/check-episode`
* ❌ Skip header `x-fuck-id`

Proses utama (versi yang benar):

```
JS config → CSRF token → check-episode (authorization)
    → token URL (dengan x-fuck-id) → POST dengan body
```

Hanya setelah seluruh chain ini berhasil, halaman v2 yang berisi download link akan keluar.

---

## 7. Catatan Tambahan

### 7.1 Hal yang Bisa Berubah Sewaktu-waktu (Dynamic)

* Nama file JS config (saat ini `wzl3ClXO8shDECR.js`) → ambil dari `[data-kk]`
* Nama file token (saat ini `Ks6sqSgloPTlHMl.txt`) → parse dari JS config (`MIX_AUTH_ROUTE_PARAM`)
* Nama header `x-fuck-id` → kalau ganti, terpaksa reverse-engineer leviathan.js lagi
* Nama param URL (`Ub3BzhijicHXZdv`, `C2XAPerzX1BM7V9`) → parse dari JS config
* Nilai `MIX_AUTH_KEY` & `MIX_AUTH_TOKEN` → parse dari JS config
* Versi `leviathan.js?v=N` → ambil dari `<input id="tokenAuthJs">`
* Token URL (8-12 char) — rotasi tiap request
* Token authorization body — rotasi tiap request

### 7.2 Hal yang (Relatively) Stabil

* Struktur URL `/anime/{id}/{slug}/episode/{ep}`
* Endpoint pattern `/anime/{id}/episode/{ep}/check-episode`
* Penempatan CSRF di `<meta name="csrf-token">`
* Hidden input pattern di halaman v1 (`appUrl`, `tokenAuthJs`, dll)
* Format `<div id="animeDownloadLink">` dengan `<h6>` + `<a>` bergantian (stable bertahun-tahun)
* Path `/assets/js/...` & `/assets/...` & `/storage/leviathan.js`

### 7.3 Open Questions (yang Belum Resolved)

* ✅ **`/check-episode` response** — confirmed: cuma `"1"` (page number)
* Algoritma exact `jLoadSecure()` di `leviathan.js` — masih obfuscated, tapi **tidak penting** karena pakai WebView
* Cara generate `x-request-id` — kelihatannya random short string, server tampaknya gak validate strict
* Apakah `x-request-index` harus increment per request? (kemungkinan bukan critical)

---

## 8. Bug-Bug di `Kuramanime.kt` (Sebelum Fix) — ✅ FIXED

Compare network trace browser vs kode versi awal:

| # | Problem | Status |
|---|---|---|
| **B1** | Native fast path mencoba GET token tanpa header `x-fuck-id`, `x-csrf-token` → dapat token bogus | ✅ Fixed: native fast path **dihapus total** karena tidak feasible (lihat §9.1) |
| **B2** | POST tanpa body `authorization=...` → server reject | ✅ Fixed: WebView yang handle POST dengan body computed |
| **B3** | Detection bypass success pakai `!text.contains("Terjadi kesalahan")` → broken | ✅ Fixed: pakai `#animeDownloadLink a[href]` non-empty (struktur DOM) |
| **B4** | Skip step `/check-episode` di native flow | ✅ Fixed: WebView eksekusi seluruh flow JS termasuk `/check-episode` |
| **B5** | Hardcode nama param yang dynamic | ⚠️ Tetap ada di var query di flow, tapi WebView selalu pakai value yang fresh dari JS config |

Fix utama: refactor `loadLinks()` jadi **WebView-first** dengan detection DOM-based.

---

## 9. Strategi Implementasi

### 9.1 Native Kotlin — TIDAK FEASIBLE

Replicate full bypass di Kotlin native **tidak praktis** karena:

* Body POST `authorization=...` di-generate oleh `leviathan.js` (heavy obfuscated)
* Tidak ada endpoint terpisah yang return token ini — murni client-side computation
* Reverse engineering algoritma = effort tinggi + perlu re-RE setiap update site
* Eksekusi JS via Rhino/QuickJS butuh fake DOM, jQuery, fetch — kompleks dan rapuh

### 9.2 WebViewResolver — RECOMMENDED

Biarkan WebView Android jalanin `leviathan.js` lengkap, intercept response final dari POST episode (yang berisi halaman v2).

**Approach ini valid di CloudStream extension** — bukti:

* Docs CloudStream eksplisit recommend untuk encrypted JS case ([finding_video_links](https://recloudstream.github.io/csdocs/devs/scraping/finding_video_links/))
* Provider lain di workspace (`Idlix.kt:402`) memakai pattern ini sebagai fallback
* Flag `usesWebView = true` ada di `MainAPI` declaration
* `WebViewResolver` adalah part of `cloudstream3` SDK

**Pro**: Tahan banting — bahkan kalau Kuramanime ganti algoritma JS-nya total, WebView tetap work.
**Con**: Lambat (~5-10 detik), bergantung WebView Android availability.

### 9.3 Implementasi Aktual di `Kuramanime.kt`

Flow sekarang (after fix):

```kotlin
// Step 1: Initial GET → kemungkinan halaman v1 (decoy)
val initial = app.get(data, headers = commonHeaders)

// Step 2: Detect via DOM (BUKAN substring text)
val isDecoy = doc.select("#animeDownloadLink .reload-error").isNotEmpty() ||
              doc.select("#animeDownloadLink a[href]").isEmpty()

if (isDecoy) {
    // Step 3: Bypass via WebView
    val resolver = WebViewResolver(
        interceptUrl = Regex("""/anime/\d+/[^/]+/episode/\d+\?[^#]*page=\d+"""),
        additionalUrls = listOf(Regex("""/anime/\d+/[^/]+/episode/\d+\?""")),
        userAgent = userAgent,
        useOkhttp = false,
        timeout = 20_000L
    )
    val webResp = app.get(data, headers = commonHeaders, 
                          interceptor = resolver, cookies = initial.cookies)
    if (webResp.code == 200 && 
        webResp.document.select("#animeDownloadLink a[href]").isNotEmpty()) {
        document = webResp.document
    }
}

// Step 4-5: Process video player + download links via loadExtractor
```

Detection success/failure **menggunakan struktur DOM** (`#animeDownloadLink a[href]` not empty), bukan substring text matching.

---

## 10. Summary Singkat

Kuramanime memakai **6-step authenticated request flow** dengan:

* CSRF token + custom auth headers (`x-fuck-id`)
* Token rotasi (URL token + authorization token, dua-duanya wajib)
* JS wrapper obfuscated (`leviathan.js`) sebagai layer abstraksi
* Decoy response untuk request yang gak lengkap (anti-scraper "smart")

Scraper yang mau work harus mengikuti seluruh urutan ini, **tidak bisa shortcut**. Approach native HTTP client (Kotlin/NiceHttp) bisa work tapi sangat fragile; **WebView fallback** tetap recommended sebagai backup safety net.

---
