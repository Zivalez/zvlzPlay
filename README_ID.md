<div align="center">

<br>

<p align="center">
<a href="https://github.com/recloudstream/cloudstream"><picture><img src="https://cloudstream.zvlz.my.id/cloudstream.png" alt="CloudStream" width="220" height="220" style="vertical-align: middle;"></picture></a>
<a href="https://github.com/Zivalez/zvlzPlay"><picture><img src="https://raw.githubusercontent.com/Zivalez/zvlzPlay/builds/zvlzPlay128.png" alt="zvlzPlay" width="110" height="110" style="vertical-align: middle;"></picture></a>
</p>

# CloudStream × zvlzPlay

*Repositori ekstensi yang tenang untuk Anime, Movie, TV Series, Drama Asia, dan Donghua.*

<br>

<a href="https://github.com/Zivalez/zvlzPlay/tree/builds"><img src="https://img.shields.io/badge/last%20update-20%20Nov%202026%2015%3A28-aa472a?style=flat-square&labelColor=aa472a&color=fdf0d7" alt="Last update"/></a>&ensp;
<a href="https://github.com/Zivalez/zvlzPlay/stargazers"><img src="https://img.shields.io/github/stars/Zivalez/zvlzPlay?style=flat-square&labelColor=aa472a&color=fdf0d7" alt="Stars"/></a>&ensp;
<img src="https://img.shields.io/badge/platform-Android-fdf0d7?style=flat-square&labelColor=aa472a" alt="Android"/>&ensp;
<img src="https://img.shields.io/badge/content-NSFW%20opt--in-fdf0d7?style=flat-square&labelColor=aa472a" alt="NSFW"/>

<br>

[Instalasi](#install) &nbsp;·&nbsp; [Provider](#providers) &nbsp;·&nbsp; [Yang mana](#which-one) &nbsp;·&nbsp; [FAQ](#faq)

Bahasa Indonesia &nbsp;/&nbsp; [English](README.md)

<br>

</div>

## Tentang

**CloudStream** adalah aplikasi Android open-source untuk menonton film, anime, drama, dan serial TV secara gratis, tanpa iklan. Aplikasi ini tidak membawa konten sendiri. Anggap saja sebagai pemutar media dan mesin pencari, yang sepenuhnya digerakkan oleh ekstensi.

**zvlzPlay** adalah salah satu repositori ekstensi tersebut. Repositori ini mengumpulkan provider dari situs streaming Indonesia dan internasional, jadi Anda cukup menambahkannya sekali lalu memilih provider yang dibutuhkan.

> [!NOTE]
> CloudStream adalah aplikasinya, wadahnya.\
> zvlzPlay adalah kumpulan ekstensi yang mencari sumber streaming.

<br>

## Fitur

- Tanpa iklan, tanpa pelacakan atau analitik
- Dukungan ponsel dan TV, dengan Chromecast
- Lewati pembuka (skip opening)
- Hingga 1080p atau lebih tinggi, tergantung provider
- Bookmark
- Pencarian universal di semua provider yang terpasang
- Berpindah antar provider kapan pun dibutuhkan

<br>

<a id="install"></a>

## Instalasi

<a href="https://github.com/recloudstream/cloudstream/releases/latest" target="_blank">
  <img src="https://img.shields.io/badge/Download_CloudStream-latest-fdf0d7?style=for-the-badge&logo=android&logoColor=white&labelColor=aa472a" alt="Download APK" />
</a>

### Tambah Cepat

Ketik shortcode ini di CloudStream:

```
zvlzplay
```

1. Instal **CloudStream**.\
   Aplikasi yang kosong setelahnya itu normal, karena belum ada ekstensi yang terpasang.
2. Buka **Pengaturan**, masuk ke **Ekstensi**, lalu pilih **Tambah Repositori**.
3. Masukkan shortcode `zvlzplay`.
4. Buka **zvlzPlay** dan instal provider yang Anda inginkan.
5. Kembali ke **Layar Utama** dan pilih provider dari pojok kanan bawah.

<details>
<summary>Lebih suka URL lengkapnya?</summary>

```
https://cloudstream.zvlz.my.id/builds/repo.json
```

Kalau diminta nama, boleh dikosongkan atau diisi apa saja, misalnya `zvlzPlay`.

</details>

> [!TIP]
> Jika provider gagal dimuat di Indonesia, ISP Anda mungkin memblokirnya lewat DNS. Lihat [perbaikan di FAQ](#faq-dns).

<br>

<a id="providers"></a>

## Daftar Provider

> [!NOTE]
> **Health Check** · Terakhir dicek: **2026-11-21 01:39 WIB**
>
> Setiap cek dijalankan 5 kali dengan judul yang berbeda, diambil acak dari katalog provider. Cek dinyatakan lolos kalau setidaknya satu percobaan berhasil. Ini cek terjadwal tiap 12 jam, bukan pemantauan terus-menerus.

Seperti tes provider di aplikasi CloudStream, cek links menguji judul asli: kalau link kelima judul itu mati semua, provider dinyatakan gagal, walaupun judul lain mungkin masih bisa diputar.

- **Status Check:** 🟢 artinya kelima percobaan lolos, 🟡 artinya sebagian percobaan gagal tapi setidaknya satu lolos, 🔴 artinya kelima percobaan gagal, 🔵 artinya provider itu belum di-cover checker.
- **Health:** persentase dari 4 cek yang lolos (cek kuning tetap dihitung lolos).
- **Condition:** jumlah percobaan yang lolos dari 5 percobaan, hanya ditampilkan untuk cek yang tidak sempurna. "Links 4/5" artinya 4 dari 5 judul lolos.

> [!NOTE]
> *Auto-repair:* Cek yang gagal otomatis didiagnosis, diperbaiki, dan dicek ulang, hanya untuk provider **Aktif**. Provider yang **Tidak Diurus Lagi** hanya dicek, tidak pernah diperbaiki.

### Provider Aktif

| Provider | Status Check | Health | Kondisi |
| :--- | :--- | :---: | :---: |
| **Idlix** <br> <sub>Movie, TV Series, Drama Asia, Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Samehadaku** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Otakudesu** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Alqanime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Nontonanimeid** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Kuramanime** <br> <sub>Anime, Donghua</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟡 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Detail 4/5 |
| **Sokuja** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Kuronime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |

### Tidak Diurus Lagi

> [!WARNING]
> Provider berikut sudah tidak diurus aktif. Maintainer mengurus terlalu banyak provider sendirian dan makin sibuk, jadi tidak bisa mengikuti semuanya. Provider ini masih bisa jalan kalau situs sumbernya aktif, tapi **masalah tidak akan diperbaiki**.

| Provider | Status Check | Health | Kondisi |
| :--- | :--- | :---: | :---: |
| **LayarKaca** <br> <sub>Movie, TV Series, Drama Asia, Anime Movie</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟡 Links</sub> | `▰▰▰▰`<br> **100%** | Links 2/4 |
| **Dramacool** <br> <sub>Drama Asia, Variety Show</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/10 <br> Search 0/4 |
| **Gomunime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟡 Links</sub> | `▰▰▰▰`<br> **100%** | Links 4/5 |
| **Winbu** <br> <sub>Anime, Donghua</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Zoronime** <br> <sub>Anime</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/10 <br> Search 0/4 |
| **Twitch** <br> <sub>Live Streamer Global</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Loklok** <br> <sub>Movie, TV Series</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/5 <br> Search 0/4 |
| **Moviebox** <br> <sub>Movie, TV Series, Anime, Drama Asia</sub> | <sub>🟢 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔵 Links</sub> | `▰▱▱▱`<br> **25%** | Search 0/5 <br> Detail 0/5 |
| **Pencurimovie** <br> <sub>Movie</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **IPTV** <br> <sub>Live TV Indonesia (RCTI, SCTV, Trans, ANTV, Metro, Kompas, dll)</sub> | <sub>🟢 Home <br> 🟡 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Search 4/5 |

### Provider NSFW

> [!NOTE]
> Provider berikut menyajikan konten NSFW. Tersembunyi secara default. Nggak di-cover health checker, dan kalau rusak, nggak diperbaiki.

<details>
<summary>Lihat 4 provider</summary>

- **Hentaicop**
- **Hentaihaven**
- **King**
- **KingV2**

</details>


<br>

<a id="which-one"></a>

## Provider mana yang harus saya gunakan

| Mencari | Disarankan |
| :--- | :--- |
| **Anime** | **Samehadaku**, cukup lengkap dengan streaming cepat.<br>**Kuramanime**, update cepat dan stabil. |
| **Drama dan Film** | **Idlix**, biasanya cukup lengkap. |
| **Donghua** | **Kuramanime** |

Ketika provider lain bermasalah, **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Sokuja**, dan **Kuronime** adalah alternatif yang bagus untuk anime.

<br>

<a id="faq"></a>

## FAQ

### Kenapa aplikasinya kosong setelah instalasi?

CloudStream butuh ekstensi dulu. Ikuti langkah di [Instalasi](#install) untuk menambah repositori ini.

### Apakah Donghua tersedia?

Ya. Coba **Kuramanime**.

### Kenapa pencarian tidak mengembalikan hasil?

- Pastikan provider sudah terpasang.
- Periksa filter halaman pencarian dan pastikan provider diaktifkan.
- Semakin banyak provider terpasang, semakin besar kemungkinan hasil pencarian muncul.

<a id="faq-dns"></a>

### Provider tidak menunjukkan data atau gagal dimuat (Indonesia)?

Beberapa situs sumber diblokir ISP Indonesia lewat DNS, dan dialihkan ke Internet Positif. Nyalakan **Private DNS** di pengaturan Android agar aplikasi bisa menyelesaikan alamat situs yang sebenarnya.

Buka **Pengaturan → Jaringan → Private DNS** dan masukkan, misalnya:

```
dns.google
```

DNS-over-HTTPS juga bisa dipakai.

<br>

---

<div align="center">

Jika zvlzPlay bermanfaat untuk Anda, ⭐ di [repositori](https://github.com/Zivalez/zvlzPlay) selalu dihargai.

<sub>Built for <a href="https://github.com/recloudstream/cloudstream">CloudStream</a> &nbsp;·&nbsp; <a href="https://github.com/Zivalez/zvlzPlay">zvlzPlay</a></sub>

</div>
