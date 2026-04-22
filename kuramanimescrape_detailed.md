# Kuramanime Scraping Analysis (Detailed)

## 1. Overview

Halaman anime di Kuramanime **tidak langsung memberikan data asli** ketika diakses pertama kali.
Sebaliknya, website menggunakan **mekanisme proteksi berbasis token** untuk mencegah scraping langsung.

Akibatnya:

* Request biasa hanya menghasilkan halaman “dummy”
* Data penting seperti **download link / video URL tidak tersedia**
* Dibutuhkan proses tambahan untuk mendapatkan konten asli

---

## 2. Perbedaan Halaman

### 2.1 `kuramanimev1.html` (Initial Response)

Halaman ini adalah hasil ketika:

* Mengakses URL tanpa parameter khusus
* Belum melewati sistem proteksi

Ciri-ciri:

* Berisi banyak **meta tag SEO (keyword spam)**
* Struktur HTML terlihat lengkap, tapi **konten penting tidak ada**
* Tidak terdapat:

  * link download
  * direct video URL
* Berfungsi sebagai **decoy / placeholder page**

Kesimpulan:

> Halaman ini bukan sumber data sebenarnya dan tidak bisa digunakan untuk scraping konten video.

---

### 2.2 `kuramanimev2.html` (Bypassed / Real Page)

Halaman ini muncul setelah:

* Token valid diberikan
* Request berhasil melewati sistem proteksi

Ciri-ciri:

* Struktur halaman lengkap (CSS, JS, UI)
* Menampilkan:

  * player video
  * link download
  * data episode lengkap

Kesimpulan:

> Ini adalah halaman target yang harus didapatkan oleh scraper.

---

## 3. Mekanisme Proteksi

Kuramanime menggunakan sistem **token-based access control** yang dikombinasikan dengan konfigurasi dari JavaScript.

---

### 3.1 Konfigurasi dari JavaScript

File:

```
/assets/js/wzl3ClXO8shDECR.js
```

Isi penting:

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

Penjelasan:

* `MIX_AUTH_ROUTE_PARAM` → lokasi file token
* `MIX_PAGE_TOKEN_KEY` → parameter URL untuk autentikasi halaman
* `MIX_STREAM_SERVER_KEY` → parameter untuk memilih server video

---

### 3.2 Pengambilan Token

File:

```
/assets/Ks6sqSgloPTlHMl.txt
```

Contoh isi:

```
KZGACd7xDq
```

Penjelasan:

* Ini adalah **token dinamis**
* Digunakan untuk validasi akses ke halaman sebenarnya

---

### 3.3 Pembentukan URL Valid

URL awal:

```
/anime/{id}/{slug}/episode/{episode}
```

Setelah ditambahkan parameter:

```
/anime/{id}/{slug}/episode/{episode}
?Ub3BzhijicHXZdv=KZGACd7xDq
&C2XAPerzX1BM7V9=kuramadrive
&page=1
```

Penjelasan parameter:

* `Ub3BzhijicHXZdv` → key dari `MIX_PAGE_TOKEN_KEY`
* `KZGACd7xDq` → token dari file `.txt`
* `C2XAPerzX1BM7V9` → server video
* `page=1` → pagination internal

---

### 3.4 Request ke Server

Method:

```
POST
```

Endpoint:

```
/anime/{id}/{slug}/episode/{episode}?<params>
```

Hasil:

* Mengembalikan HTML lengkap (setara dengan `kuramanimev2.html`)
* Sudah berisi:

  * video source
  * download link

---

## 4. Alur Lengkap (Flow)

```
1. Request halaman awal
   ↓
2. Ambil file JS (config)
   ↓
3. Extract parameter penting
   ↓
4. Request file token (.txt)
   ↓
5. Dapatkan auth token
   ↓
6. Bangun URL dengan parameter
   ↓
7. Kirim POST request ke endpoint
   ↓
8. Terima halaman asli (v2)
   ↓
9. Extract direct video URL
```

---

## 5. Insight Teknis

### 5.1 Bukan Scraping Biasa

Pendekatan ini bukan sekadar:

```
GET → parse HTML
```

Melainkan:

```
Multi-step request + token injection
```

---

### 5.2 Proteksi Ringan (Obfuscation)

Sistem ini:

* Tidak menggunakan captcha
* Tidak menggunakan Cloudflare challenge berat
* Hanya:

  * token dinamis
  * parameter tersembunyi

Tujuan:

> Menghentikan scraper sederhana, bukan advanced scraper

---

### 5.3 Halaman Awal = Decoy

Halaman pertama sengaja:

* terlihat valid
* tapi tidak mengandung data penting

Tujuan:

* mengelabui bot
* mengurangi scraping langsung

---

## 6. Kesimpulan

Untuk mendapatkan direct video URL dari Kuramanime:

* Tidak cukup hanya mengambil HTML awal
* Harus mengikuti mekanisme autentikasi internal

Proses utama:

```
JS config → token → parameter → request ulang
```

Hanya setelah itu:

> Data asli (video & download link) bisa diakses

---

## 7. Catatan Tambahan

* Token dapat berubah sewaktu-waktu
* Nama parameter tidak statis
* Scraper harus:

  * fleksibel
  * mampu parsing JS secara dinamis

---

## 8. Summary Singkat

Kuramanime menggunakan sistem:

> Token-based gate + dynamic parameter injection

Untuk memisahkan:

* halaman publik (v1)
* halaman asli (v2)

Scraper harus meniru perilaku ini untuk mendapatkan data sebenarnya.

---
