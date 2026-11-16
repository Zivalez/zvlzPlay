<h1 align="center">CloudStream x zvlzPlay</h1>

<p align="center">
<a href="https://github.com/recloudstream/cloudstream"><picture><img src="https://cloudstream.zvlz.my.id/cloudstream.png" alt="CloudStream" width="100" height="100"></picture></a>
<a href="https://github.com/Zivalez/zvlzPlay"><picture><img src="https://avatars.githubusercontent.com/u/142050504" alt="zvlzPlay" width="100" height="100"></picture></a>
</p>

<p align="center">
Kumpulan plugin CloudStream buat nonton dan download Anime, Movie,<br>
TV Series, Drama Asia, dan Donghua.<br>
(Mengandung konten NSFW)<br><br>
<a target="_blank" href="https://github.com/Zivalez/zvlzPlay/tree/builds"><img src="https://img.shields.io/github/last-commit/Zivalez/zvlzPlay/builds?label=last%20update"/></a>
</p>

<p align="center">
<a href="README.md">English</a> | Bahasa Indonesia
</p>

## Apa itu CloudStream?

**CloudStream** itu aplikasi open-source buat nonton film, anime, drama, sama TV series. Gratis, tanpa iklan.

Tapi CloudStream ga langsung bawa konten dari awal. Anggap aja dia cuma media player + mesin pencari. Biar bisa dipake nonton, kamu perlu pasang **ekstensi** yang ngambil data video dari berbagai situs streaming.


## Apa itu zvlzPlay?

**zvlzPlay** itu sebuah ekstensi, kumpulan provider CloudStream yang isinya berbagai situs streaming, kebanyakan Indonesia tapi ada juga yang internasional.

> **CloudStream** = aplikasinya (wadah)\
> **zvlzPlay** = ekstensi buat nyari sumber tontonan


## Fitur

- Tanpa iklan
- Tanpa tracking/analytics
- Bookmark
- Dukungan HP dan TV
- Chromecast
- Skip opening
- Bisa pilih dan gonta-ganti provider sesuka hati
- Resolusi tergantung provider, banyak yang 1080p ke atas
- Universal search, makin banyak provider yang dipasang, makin rame hasilnya


## Download Aplikasi

<p>
  <a href="https://github.com/recloudstream/cloudstream/releases/latest" target="_blank">
    <img src="https://img.shields.io/badge/Download_CloudStream-terbaru-0090FF?style=for-the-badge&logo=android&logoColor=white" alt="Download APK" />
  </a>
</p>



## Cara Install dan Pasang Ekstensi zvlzPlay

1. Install CloudStream dulu.\
   Kalo habis install tampilannya kosong, itu normal karna blm ada ekstensi.
2. Buka **Settings**.
3. Masuk ke **Extensions**.
4. Pilih **Add Repository**.
5. Masukkan URL berikut sebagai Repository URL:
```
https://cloudstream.zvlz.my.id/builds/repo.json
```
6. Nama repository bebas, boleh dikosongin atau isi aja `zvlzPlay`.
7. Kalo repository udah masuk, buka **zvlzPlay** lalu install provider yang mau dipake.
8. Balik ke **Home Screen**, terus pilih provider dari pojok kanan bawah.
9. Kelar.


## Daftar Provider

Terakhir dicek: **2026-11-17 04:17 WIB**

**Health Check**

Dicek otomatis tiap 12 jam ke situs aslinya: home feed, search, detail/episode, dan sumber link. Ini cek terjadwal, bukan pemantauan terus-menerus.
- **Health:** Persentase dari 4 cek yang lolos.
- **Status:** 🟢 artinya cek itu berhasil, 🔴 artinya gagal, 🔵 artinya provider itu belum di-cover checker.

> [!NOTE]
> *Auto-repair:* Cek yang gagal otomatis didiagnosis, diperbaiki, dan dicek ulang, hanya untuk provider **Aktif**. Provider yang **Tidak Diurus Lagi** hanya dicek, tidak pernah diperbaiki.

### Provider Aktif

| Provider | Health | Status |
| :--- | :---: | :--- |
| **Idlix** <br> <sub>Movie, TV Series, Drama Asia, Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Samehadaku** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Otakudesu** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Alqanime** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Nontonanimeid** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Kuramanime** <br> <sub>Anime, Donghua</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Sokuja** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Kuronime** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |

### Tidak Aktif Lagi

> [!WARNING]
> Provider berikut sudah tidak diurus aktif. Aku mengurus terlalu banyak provider sendirian dan makin sibuk, jadi tidak bisa mengikuti semuanya. Provider ini masih bisa jalan kalau situs sumbernya aktif, tapi **masalah tidak akan diperbaiki**.

| Provider | Health | Status |
| :--- | :---: | :--- |
| **LayarKaca** <br> <sub>Movie, TV Series, Drama Asia, Anime Movie</sub> | `▰▰▱▱`<br> **50%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🔴 Detail &nbsp; 🔴 Links</sub> |
| **Dramacool** <br> <sub>Drama Asia, Variety Show</sub> | `▱▱▱▱`<br> **0%** | <sub>🔴 Home &nbsp; 🔴 Search &nbsp; 🔴 Detail &nbsp; 🔴 Links</sub> |
| **Gomunime** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Winbu** <br> <sub>Anime, Donghua</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Zoronime** <br> <sub>Anime</sub> | `▱▱▱▱`<br> **0%** | <sub>🔴 Home &nbsp; 🔴 Search &nbsp; 🔴 Detail &nbsp; 🔴 Links</sub> |
| **Twitch** <br> <sub>Live Streamer Global</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **Loklok** <br> <sub>Movie, TV Series</sub> | `▱▱▱▱`<br> **0%** | <sub>🔴 Home &nbsp; 🔴 Search &nbsp; 🔴 Detail &nbsp; 🔴 Links</sub> |
| **Moviebox** <br> <sub>Movie, TV Series, Anime, Drama Asia</sub> | `▰▱▱▱`<br> **25%** | <sub>🟢 Home &nbsp; 🔴 Search &nbsp; 🔴 Detail &nbsp; 🔴 Links</sub> |
| **Pencurimovie** <br> <sub>Movie</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |
| **IPTV** <br> <sub>Live TV Indonesia (RCTI, SCTV, Trans, ANTV, Metro, Kompas, dll)</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home &nbsp; 🟢 Search &nbsp; 🟢 Detail &nbsp; 🟢 Links</sub> |


## FAQ

### Kenapa setelah install aplikasinya kosong?
Karna CloudStream butuh ekstensi dulu. Ikuti langkah di bagian [Cara Install](#cara-install-dan-pasang-ekstensi-zvlzplay) buat nambahin repository.

### Provider mana yang paling bagus untuk Anime?
- **Samehadaku** : lumayan lengkap dan streamingnya kenceng
- **Kuramanime** : update cepet dan stabil
- **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Sokuja**, **Kuronime** : bisa jadi cadangan kalo yang lain lagi rewel

### Provider mana untuk Drama dan Movie?
- **Idlix** : biasanya lumayan lengkap

### Ada Donghua?
- Ada. Coba **Kuramanime**.

### Kenapa search tidak menemukan hasil?
- Pastiin provider udah terpasang.
- Cek filter di halaman search, jangan sampai providernya blm aktif.
- Makin banyak provider yang dipasang, makin banyak juga kemungkinan hasilnya muncul.

### Provider tidak ada datanya / gagal loading (Indonesia)?
Beberapa situs sumber diblokir ISP Indonesia lewat DNS (dialihkan ke
Internet Positif). Nyalakan **Private DNS** di pengaturan Android
(Settings → Network → Private DNS, misal `dns.google`) atau pakai
DNS-over-HTTPS biar aplikasinya bisa resolve alamat asli situsnya.
