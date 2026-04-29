# Riset IPTV - Pemahaman dan Kompatibilitas dengan CloudStream

## Apa itu IPTV?

**IPTV (Internet Protocol Television)** adalah penyampaian konten televisi melalui jaringan IP. Berbeda dengan televisi siaran tradisional, kabel, atau satelit, IPTV menggunakan internet untuk menyampaikan program TV.

### Komponen Utama

1. **Playlist M3U** - Format file standar untuk playlist multimedia yang berisi URL stream
2. **EPG (Electronic Program Guide)** - Data jadwal/panduan TV digital
3. **Stream** - Konten video aktual yang disampaikan melalui protokol HLS (.m3u8) atau DASH (.mpd)
4. **Metadata** - Informasi saluran, logo, kategori, bahasa

## Ekosistem IPTV-ORG

### 1. Repository iptv-org/iptv
- **Tujuan**: Koleksi saluran IPTV publik dari seluruh dunia
- **Struktur**: 200+ playlist M3U spesifik negara di direktori `streams/`
- **Otomatisasi**: Update otomatis harian melalui GitHub Actions
- **Output**: Diorganisir berdasarkan kategori, bahasa, dan negara
- **Deployment**: Disajikan melalui GitHub Pages di `https://iptv-org.github.io/iptv/`

### 2. Repository iptv-org/database
- **Tujuan**: Manajemen data terpusat untuk metadata saluran TV
- **Penyimpanan**: File CSV (channels.csv, feeds.csv, blocklist.csv)
- **Model Data**:
  - **Channels**: ID, nama, jaringan, pemilik, negara, bahasa, kategori, dll.
  - **Feeds**: Stream siaran aktual untuk saluran (banyak feed per saluran)
  - **Blocklist**: Saluran yang diblokir (DMCA, NSFW)
- **Output**: Diekspor ke JSON untuk konsumsi API

### 3. Repository iptv-org/api
- **Tujuan**: Akses programatik ke data IPTV
- **Endpoints**:
  - `/channels.json` - Metadata saluran
  - `/streams.json` - URL stream dengan kualitas, headers
  - `/feeds.json` - Informasi feed
  - `/guides.json` - Pemetaan panduan EPG
  - `/countries.json`, `/languages.json`, `/categories.json` - Data referensi
  - `/blocklist.json` - Saluran yang diblokir

### 4. iptv-epg.org
- **Tujuan**: Data Electronic Program Guide
- **Format**: Format XMLTV (epg-{negara}.xml)
- **Cakupan**: 137 saluran Indonesia dengan 60.528 acara

## Data IPTV Indonesia

### Sumber Data yang Tersedia

1. **Playlist**: `https://iptv-org.github.io/iptv/countries/id.m3u`
2. **Panduan EPG**: `https://iptv-epg.org/files/epg-id.xml`
3. **API Channels**: Filter dengan `country: "ID"`
4. **API Streams**: Filter dengan ID saluran Indonesia

### Contoh Saluran Indonesia

Dari data API, saluran Indonesia meliputi:
- **BRTV.id** - BRTV (Batam TV)
- **BTV.id** - BTV (Balikpapan TV)
- Dan banyak lagi (137 total menurut EPG)

### Dukungan Protokol Stream

Stream Indonesia terutama menggunakan:
- **HLS (.m3u8)** - Format paling umum
- **DASH (.mpd)** - Beberapa provider seperti IndihomeTV
- **HTTP headers** - Beberapa stream memerlukan referrer/user-agent

## Analisis Kompatibilitas CloudStream

### Arsitektur CloudStream

CloudStream adalah aplikasi Android yang:
- Menggunakan ekstensi (plugin) untuk meng-scrape konten dari situs streaming
- Mendukung konten on-demand (film, serial TV, anime)
- Memiliki antarmuka MainAPI untuk provider
- Mendukung streaming HLS/DASH
- Dapat menangani custom headers (referrer, user-agent)

### Kasus Penggunaan IPTV vs CloudStream

| Aspek | IPTV | CloudStream |
|--------|------|-------------|
| **Tipe Konten** | Saluran TV langsung (24/7) | On-demand (film, serial) |
| **Jadwal** | Berbasis waktu (EPG) | Ketersediaan on-demand |
| **Navigasi** | Channel surfing | Cari/browse perpustakaan |
| **Metadata** | Jadwal EPG | Detail episode/film |
| **Streaming** | Stream langsung | Stream VOD |

### Kompatibilitas Teknis

**✅ Aspek yang Kompatibel:**
1. **Format Stream**: Keduanya mendukung HLS (.m3u8) dan DASH (.mpd)
2. **Headers**: CloudStream dapat menangani custom headers (referrer, user-agent)
3. **Metadata**: Dapat mengurai informasi saluran dari JSON
4. **Kualitas**: Keduanya mendukung opsi kualitas beragam

**❌ Aspek yang Tidak Kompatibel:**
1. **Model TV Langsung**: CloudStream didesain untuk VOD, bukan streaming langsung
2. **Integrasi EPG**: CloudStream tidak memiliki UI jadwal/EPG
3. **Channel Surfing**: Tidak ada antarmuka penggantian saluran
4. **Konten Berbasis Waktu**: CloudStream tidak menangani pemrograman berbasis waktu

### Pendekatan Implementasi Potensial

#### Opsi 1: IPTV sebagai Kategori TV Langsung
- Buat provider baru yang mencantumkan saluran Indonesia sebagai "episode"
- Setiap saluran = satu "episode" dengan stream langsung
- **Kelebihan**: Memanfaatkan infrastruktur CloudStream yang ada
- **Kekurangan**: UX buruk (tidak ada channel surfing, tidak ada EPG, tidak ada navigasi berbasis waktu)

#### Opsi 2: IPTV sebagai Fitur Terpisah
- Memerlukan perubahan inti CloudStream untuk mendukung TV langsung
- Tambahkan integrasi EPG
- Tambahkan antarmuka channel surfing
- **Kelebihan**: Pengalaman TV langsung yang tepat
- **Kekurangan**: Memerlukan pengembangan aplikasi CloudStream (bukan hanya ekstensi)

#### Opsi 3: Integrasi Pemutar IPTV Eksternal
- Ekstensi CloudStream meluncurkan pemutar IPTV eksternal
- Gunakan playlist M3U dari iptv-org
- **Kelebihan**: Pengalaman IPTV yang tepat, perubahan CloudStream minimal
- **Kekurangan**: Tidak terintegrasi, memerlukan aplikasi terpisah

## Rekomendasi

**IPTV TIDAK langsung kompatibel dengan arsitektur CloudStream saat ini** untuk pengalaman pengguna yang baik karena:

1. CloudStream didesain untuk konten on-demand (VOD)
2. IPTV memerlukan fitur TV langsung (channel surfing, EPG, pemrograman berbasis waktu)
3. Pengalaman pengguna akan buruk tanpa UI TV langsung yang tepat

### Pendekatan Terbaik untuk Pengguna Indonesia

**Opsi 3** (Pemutar IPTV Eksternal) direkomendasikan:
1. Gunakan aplikasi IPTV khusus (IPTV Smarters, TiviMate, dll.)
2. Muat playlist M3U Indonesia: `https://iptv-org.github.io/iptv/countries/id.m3u`
3. Muat panduan EPG: `https://iptv-epg.org/files/epg-id.xml`
4. Dapatkan pengalaman TV langsung yang tepat dengan channel surfing dan EPG

### Alternatif: Pendekatan Hibrida

Jika Anda ingin mengintegrasikan beberapa konten IPTV ke CloudStream:
1. **Fokus pada konten VOD dari saluran Indonesia** - Beberapa saluran memiliki perpustakaan on-demand
2. **Buat provider untuk layanan catch-up Indonesia** - Seperti Mola TV, Vidio, dll.
3. **Pisahkan IPTV langsung** - Gunakan aplikasi IPTV khusus untuk saluran langsung

## Kesimpulan

Meskipun secara teknis memungkinkan untuk streaming konten IPTV melalui CloudStream (protokol sama, headers didukung), perbedaan mendasar dalam model penyampaian konten (TV langsung vs on-demand) membuatnya tidak cocok. Untuk pengalaman TV Indonesia terbaik, gunakan aplikasi IPTV khusus dengan data iptv-org, dan pertahankan CloudStream untuk konten on-demand (film, serial, anime).
