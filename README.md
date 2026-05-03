<h1 align="center">CloudStream x zvlzPlay</h1>

<p align="center">
  <a href="https://github.com/recloudstream/cloudstream">
    <img src="https://cloudstream.zvlz.my.id/cloudstream.png" alt="CloudStream" width="80" height="80">
  </a>
  <a href="https://github.com/Zivalez/zvlzPlay">
    <img src="https://avatars.githubusercontent.com/u/142050504" alt="zvlzPlay" width="80" height="80">
  </a>
</p>

<p align="center">
Streaming dan download berbagai konten seperti Anime, K-Drama, J-Drama, C-Drama,<br>
TV Series, Movies, Live TV, Live Streamer, dan lainnya.<br>
(mengandung konten NSFW)
</p>

<p align="center">
<a href="#">Bahasa Indonesia</a> | <a href="README_EN.md">English</a> | <a href="README_JP.md">日本語</a>
</p>


## Apa itu CloudStream?

**CloudStream** adalah aplikasi Android open-source untuk menonton film, anime, drama, dan TV series secara gratis, tanpa iklan, dan tanpa perlu membuat akun.

CloudStream sendiri tidak menyediakan konten bawaan, aplikasi ini hanya berfungsi sebagai player dan mesin pencari. Untuk bisa menonton, kamu perlu memasang **ekstensi (plugin)** yang bertugas mengambil data video dari berbagai situs streaming di internet.


## Apa itu zvlzPlay?

**zvlzPlay** adalah kumpulan ekstensi/provider untuk CloudStream yang mendukung berbagai situs streaming Indonesia dan internasional.

> **CloudStream** = aplikasinya (wadah)\
> **zvlzPlay** = ekstensi untuk mendapatkan konten streaming


## Fitur https://cloudstream.zvlz.my.id/cloudstream.png

- Tanpa iklan
- Tanpa tracking/analytics
- Bookmark
- Dukungan HP dan TV
- Chromecast
- Skip opening
- Tidak perlu login
- Bisa memilih dan mengganti provider sesuai kebutuhan
- Resolusi tergantung provider (sebagian besar 1080p ke atas)
- Universal search, semakin banyak provider yang dipasang, semakin lengkap hasil pencariannya


## Download Aplikasi

Unduh CloudStream di sini:\
https://github.com/recloudstream/cloudstream/releases


## Cara Install dan Pasang Ekstensi zvlzPlay

1. Install aplikasi CloudStream terlebih dahulu.\
   Setelah install, tampilan akan kosong karena belum ada ekstensi yang terpasang.
2. Buka `Settings`.
3. Masuk ke `Extensions`.
4. Pilih `Add Repository`.
5. Masukkan URL berikut sebagai Repository URL:
```
https://cloudstream.zvlz.my.id/builds/repo.json
```
6. Nama repository bisa dikosongkan atau diisi sesuai keinginan (misalnya: `zvlzPlay`).
7. Setelah repository berhasil ditambahkan, buka **zvlzPlay** dan pilih provider yang ingin digunakan.
8. Kembali ke **Home Screen**, lalu pilih provider di pojok kanan bawah.
9. Selesai, selamat menonton.


## Daftar Provider

| Provider | Konten | Status |
| --- | --- | --- |
| Idlix | Movie, TV Series, Drama Asia, Anime | ✅ Aktif |
| LayarKaca | Movie, TV Series, Drama Asia, Anime Movie | ✅ Aktif |
| Pencurimovie | Movie | ✅ Aktif |
| Funmovieslix | Movie, TV Series, Drama Asia | ✅ Aktif |
| Moviebox | Movie, TV Series, Anime, Drama Asia | ✅ Aktif |
| Samehadaku | Anime | ✅ Aktif |
| Otakudesu | Anime | ✅ Aktif |
| Alqanime | Anime | ✅ Aktif |
| Nontonanimeid | Anime | ✅ Aktif |
| Kuronime | Anime | ✅ Aktif |
| Gomunime | Anime | ✅ Aktif |
| Winbu | Anime, Donghua | ✅ Aktif |
| Kuramanime | Anime, Donghua | ✅ Aktif |
| Zoronime | Anime | ❌ Mati |
| IPTV | Live TV Indonesia (RCTI, SCTV, Trans, ANTV, Metro, Kompas, dll) | ✅ Aktif |
| Twitch | Live Streamer Global | ✅ Aktif |

Keterangan status:
- ✅ **Aktif** : stabil dan bisa digunakan secara normal
- 🧪 **Beta** : bisa digunakan tetapi belum sepenuhnya stabil
- ❌ **Mati** : situs atau server sedang down, sementara tidak bisa digunakan


## FAQ

### Kenapa setelah install aplikasinya kosong?
CloudStream membutuhkan ekstensi (repository) terlebih dahulu. Ikuti langkah di bagian [Cara Install](#cara-install-dan-pasang-ekstensi-zvlzplay) untuk menambahkan repository.

### Provider mana yang paling bagus untuk Anime?
- **Samehadaku** : paling lengkap dengan streaming tercepat
- **Kuramanime** : update tercepat dan stabil
- **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Kuronime**, **Gomunime** : alternatif lainnya

### Provider mana untuk Drama dan Movie?
- **Idlix** : paling lengkap
- **LayarKaca**, **Funmovieslix**, **Pencurimovie**, **Moviebox**

### Ada Donghua?
- **Kuramanime** dan **Winbu** menyediakan konten Donghua.

### Bisa nonton TV live (Indosiar, RCTI, SCTV, dll)?
Bisa. Install provider **IPTV** dari daftar ekstensi. Channel populer Indonesia langsung tersedia di baris "Popular", sisanya dikelompokkan per kategori (News, Sports, Kids, dll).

### Bisa nonton streamer di Twitch?
Bisa. Install provider **Twitch** dari daftar ekstensi.

### Kenapa search tidak menemukan hasil?
- Pastikan provider sudah terpasang.
- Di bagian search, atur filter provider dan aktifkan semua jenis provider.
- Semakin banyak provider yang terpasang, semakin lengkap hasil pencariannya.
