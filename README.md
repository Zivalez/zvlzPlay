# CloudStream x zvlzPlay

Aplikasi Streaming/Download berbagai konten seperti:\
**Anime / K-Drama / J-Drama / C-Drama / TV Series / Movies / Live TV / Live Streamer / dll**

## Apa itu CloudStream dan zvlzPlay?

**CloudStream** itu aplikasi Android open-source yang dipake buat nonton film, anime, drama, dan TV series.\
Enaknya apa? ga ada iklan, ga perlu login, full gratis.

Tapi CloudStream itu nggak punya konten bawaan.\
Dia cuma jadi “player + mesin pencari”.

Supaya bisa nonton, perlu pasang yang namanya Ekstensi (plugin).\
Ekstensi ini yang tugasnya ngambil (scraping) data video dari berbagai situs streaming di internet, trus nampilin di dalam CloudStream.

**zvlzPlay** itu proyek khusus yang isinya kumpulan provider buat situs streaming Indonesia/internasional.\
Jadi gampangnya:

CloudStream = aplikasinya (wadahnya)\
zvlzPlay = isi/ekstensi buat dapetin konten streaming


## Fitur

-    AdFree, No ads whatsoever
-    No tracking/analytics
-    Bookmarks
-    Phone and TV support
-    Chromecast
-    Bisa skip opening
-    Ga perlu login
-    Bisa pilih/ganti provider, sesuai kebutuhan
-    Resolusi tergantung provider (kebanyakan 1080p ke atas)
-    Universal search (kalo masang banyak provider lebih bagus, karna lebih lengkap dan beragam hasilnya)


## Download Aplikasi

https://github.com/recloudstream/cloudstream/releases


## Cara Install & Pasang Extension zvlzPlay

1.  Install aplikasi\
(btw setelah install, jangan kaget kalo kosong. karna blm ada extension yang kepasang)
2.  Masuk ke **Settings** atau kalau bahasa Indonesia, masuk ke **Pengaturan**
3.  Klik **Extensions** / **Ekstensi**
4.  Pilih **Add Repository** / **Tambah Repository**
5.  Copy link ini, lalu masukkan ke **Repository URL** (nama repository/nama arsip dikosongin aja)
```markdown
https://cloudstream.zvlz.my.id/builds/repo.json
```
6.  Setelah nambahin repository, masuk ke **zvlzPlay**
7.  Pilih provider yang mau dipake/digunakan
8.  Balik ke **Home Screen**, pilih provider di pojok kanan bawah
9.  Done


## Daftar Provider

| Provider | Konten | Status |
| ------ | ------ | ------ |
| Idlix | Movie, TV Series, Drama Asia, Anime | ✅ aktif |
| LayarKaca | Movie, TV Series, Drama Asia, Anime Movie | ✅ aktif |
| Pencurimovie | Movie | ✅ aktif |
| Funmovieslix | Movie, TV Series, Drama Asia | ✅ aktif |
| Moviebox | Movie, TV Series, Anime, Drama Asia | ✅ aktif |
| Samehadaku | Anime | ✅ aktif |
| Otakudesu | Anime | ✅ aktif |
| Alqanime | Anime | ✅ aktif |
| Nontonanimeid | Anime | ✅ aktif |
| Kuronime | Anime | ✅ aktif |
| Gomunime | Anime | ✅ aktif |
| Winbu | Anime, Donghua | ✅ aktif |
| Kuramanime | Anime, Donghua | ✅ aktif |
| Zoronime | Anime | ❌ mati |
| IPTV | Live TV Indonesia (RCTI, SCTV, Trans, ANTV, Metro, Kompas, dll) | ✅ aktif |
| Twitch | Live streamer global | ✅ aktif |

Keterangan status:
- ✅ **aktif** — stabil & bisa dipake normal
- 🧪 **beta** — bisa dipake tapi belum stabil, kadang error
- ❌ **mati** — situs/server lagi down, sementara ga bisa dipake

## FAQ (Pertanyaan yang sering ditanyain)


### Kenapa setelah install aplikasinya kosong?
- Karena CloudStream butuh extension (repository) dulu.

### Provider mana yang paling bagus buat nonton Anime?
- **Samehadaku** (paling lengkap & streaming tercepat)
- **Kuramanime** (streaming terupdate, tercepat & stabil)
- **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Kuronime**, **Gomunime** (alternatif)

### Provider mana buat nonton Drama & Movie?
- **Idlix** (paling lengkap)
- **LayarKaca**, **Funmovieslix**, **Pencurimovie**, **Moviebox**

### Donghua ada?
- Ada, bisa lewat **Winbu** (kategori Donghua)
- **Kuramanime** juga ada Donghua, tapi statusnya masih beta

### Bisa nonton TV live (Indosiar, RCTI, SCTV, dll)?
- Bisa, install **IPTV** dari list provider.
- Channel populer Indonesia langsung ada di row "Popular". Sisanya dikelompokin per kategori (News, General, Sports, Religious, Kids, dll).

### Mau nonton streamer di Twitch?
- Install **Twitch** dari list provider.

### Kok search gak nemu?
- Pastiin provider sudah di install
- Di bagian search atur filter provider, diaktifkan semua jenis providernya
- Lebih bagus install banyak provider biar hasil search lebih lengkap
