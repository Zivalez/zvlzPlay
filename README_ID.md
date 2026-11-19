<div align="center">



<br>



<p align="center">

<a href="https://github.com/recloudstream/cloudstream"><picture><img src="https://cloudstream.zvlz.my.id/cloudstream.png" alt="CloudStream" width="100" height="100"></picture></a>

<a href="https://github.com/Zivalez/zvlzPlay"><picture><img src="https://avatars.githubusercontent.com/u/142050504" alt="zvlzPlay" width="100" height="100"></picture></a>

</p>



# CloudStream × zvlzPlay



*Repositori ekstensi untuk Anime, Movie, TV Series, Drama Asia, dan Donghua.*



<br>



<a href="https://github.com/Zivalez/zvlzPlay/tree/builds"><img src="https://img.shields.io/github/last-commit/Zivalez/zvlzPlay/builds?label=last%20update&style=flat-square&labelColor=161b22&color=30363d" alt="Last update"/></a>

<a href="https://github.com/Zivalez/zvlzPlay/stargazers"><img src="https://img.shields.io/github/stars/Zivalez/zvlzPlay?style=flat-square&labelColor=161b22&color=30363d" alt="Stars"/></a>

<img src="https://img.shields.io/badge/platform-Android-30363d?style=flat-square&labelColor=161b22" alt="Android"/>

<img src="https://img.shields.io/badge/content-NSFW-30363d?style=flat-square&labelColor=161b22" alt="NSFW"/>



<br>



[Instalasi](#install) &nbsp;·&nbsp; [Provider](#providers) &nbsp;·&nbsp; [Yang mana](#which-one) &nbsp;·&nbsp; [FAQ](#faq)



[English](README.md) &nbsp;/&nbsp; Bahasa Indonesia



<br>



</div>



---



## Tentang



**CloudStream** itu aplikasi Android open-source buat nonton film, anime, drama, sama TV series. Gratis, tanpa iklan. Aplikasinya sendiri tidak bawa konten apa-apa. Anggap aja dia media player plus mesin pencari, yang jalan sepenuhnya dari ekstensi.



**zvlzPlay** itu salah satu repositori ekstensi. Isinya provider dari situs streaming Indonesia dan internasional, jadi kamu cukup tambah sekali terus pilih yang kamu butuhkan.



> [!NOTE]

> CloudStream itu aplikasinya, wadahnya.\

> zvlzPlay itu kumpulan ekstensi yang nyari sumber streaming.



<br>



## Fitur



- Tanpa iklan, tanpa tracking atau analytics

- Dukungan HP dan TV, plus Chromecast

- Skip opening

- Sampai 1080p atau lebih tinggi, tergantung provider

- Bookmark

- Universal search di semua provider yang terpasang

- Bisa gonta-ganti provider kapan pun



<br>



<a id="install"></a>



## Instalasi



<a href="https://github.com/recloudstream/cloudstream/releases/latest" target="_blank">

&#x20; <img src="https://img.shields.io/badge/Download_CloudStream-terbaru-30363d?style=for-the-badge&logo=android&logoColor=white&labelColor=161b22" alt="Download APK" />

</a>



1. Install **CloudStream**.\

&#x20;  Kalau habis install tampilannya kosong, itu normal, soalnya belum ada ekstensi yang terpasang.

2. Buka **Settings**, masuk ke **Extensions**, terus pilih **Add Repository**.

3. Masukkan ini sebagai Repository URL:



&#x20;  ```

&#x20;  https://cloudstream.zvlz.my.id/builds/repo.json

&#x20;  ```



4. Namanya boleh dikosongin atau diisi apa aja, contohnya `zvlzPlay`.

5. Buka **zvlzPlay** terus install provider yang kamu mau.

6. Balik ke **Home Screen** terus pilih provider dari pojok kanan bawah.



> [!TIP]

> Kalau provider gagal loading di Indonesia, mungkin ISP kamu ngeblok lewat DNS. Lihat [solusinya di FAQ](#faq-dns).



<br>



<a id="providers"></a>



## Daftar Provider

> [!NOTE]
> **Health Check** · Terakhir dicek: **2026-11-19 06:38 WIB**
>
> Dicek otomatis tiap 12 jam ke situs aslinya: home feed, search, detail/episode, dan sumber link. Ini cek terjadwal, bukan pemantauan terus-menerus.

Setiap cek dijalankan 5 kali dengan judul yang berbeda, diambil acak dari katalog provider. Cek dinyatakan lolos kalau setidaknya satu percobaan berhasil. Ini cek terjadwal tiap 12 jam, bukan pemantauan terus-menerus.

- **Status Check:** 🟢 cek berhasil dan mengembalikan data. 🟡 cek lolos, tapi tidak semua judul yang dites berhasil. 🔴 cek gagal. 🔵 provider belum di-cover checker.
- **Health:** Persentase dari 4 cek yang lolos.
- **Condition:** Detail untuk cek yang tidak sepenuhnya bersih, ditampilkan sebagai lolos/dites (contoh `Links 4/5`). Tanda strip berarti tidak ada yang perlu dilaporkan.

Seperti tes provider di aplikasi CloudStream, cek links menguji judul asli: kalau link kelima judul itu mati semua, provider dinyatakan gagal, walaupun judul lain mungkin masih bisa diputar.

> [!NOTE]
> *Auto-repair:* Cek yang gagal otomatis didiagnosis, diperbaiki, dan dicek ulang, hanya untuk provider **Aktif**. Provider yang **Tidak Diurus Lagi** hanya dicek, tidak pernah diperbaiki.

### Provider Aktif

| Provider | Status Check | Health | Condition |
| :--- | :--- | :---: | :---: |
| **Idlix** <br> <sub>Movie, TV Series, Drama Asia, Anime</sub> | <sub>🟢 Home <br> 🟡 Search <br> 🟢 Detail <br> 🟡 Links</sub> | `▰▰▰▰`<br> **100%** | Search 4/5 <br> Links 4/5 |
| **Samehadaku** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Otakudesu** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Alqanime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Nontonanimeid** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Kuramanime** <br> <sub>Anime, Donghua</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Sokuja** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟡 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Detail 2/5 |
| **Kuronime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟡 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Search 4/5 |

### Tidak Aktif Lagi

> [!WARNING]
> Provider berikut sudah tidak diurus aktif. Aku mengurus terlalu banyak provider sendirian dan makin sibuk, jadi tidak bisa mengikuti semuanya. Provider ini masih bisa jalan kalau situs sumbernya aktif, tapi **masalah tidak akan diperbaiki**.

| Provider | Status Check | Health | Condition |
| :--- | :--- | :---: | :---: |
| **LayarKaca** <br> <sub>Movie, TV Series, Drama Asia, Anime Movie</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟡 Detail <br> 🟡 Links</sub> | `▰▰▰▰`<br> **100%** | Detail 4/5 <br> Links 3/4 |
| **Dramacool** <br> <sub>Drama Asia, Variety Show</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/10 <br> Search 0/4 |
| **Gomunime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟡 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Detail 4/5 |
| **Winbu** <br> <sub>Anime, Donghua</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Zoronime** <br> <sub>Anime</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/10 <br> Search 0/4 |
| **Twitch** <br> <sub>Live Streamer Global</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟡 Links</sub> | `▰▰▰▰`<br> **100%** | Links 4/5 |
| **Loklok** <br> <sub>Movie, TV Series</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/5 <br> Search 0/4 |
| **Moviebox** <br> <sub>Movie, TV Series, Anime, Drama Asia</sub> | <sub>🟢 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔵 Links</sub> | `▰▱▱▱`<br> **25%** | Search 0/5 <br> Detail 0/5 |
| **Pencurimovie** <br> <sub>Movie</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **IPTV** <br> <sub>Live TV Indonesia (RCTI, SCTV, Trans, ANTV, Metro, Kompas, dll)</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |


<br>


<a id="which-one"></a>



## Provider mana yang harus saya gunakan



| Mencari | Rekomendasi |

| :--- | :--- |

| **Anime** | **Samehadaku**, cukup lengkap dan streamingnya kencang.<br>**Kuramanime**, update cepat dan stabil. |

| **Drama dan Movie** | **Idlix**, biasanya cukup lengkap. |

| **Donghua** | **Kuramanime** |



Kalau provider lain lagi bermasalah, **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Sokuja**, dan **Kuronime** itu alternatif yang bagus buat anime.



<br>



<a id="faq"></a>



## FAQ



### Kenapa setelah install aplikasinya kosong?



CloudStream butuh ekstensi dulu. Ikuti langkah di [Instalasi](#install) buat nambahin repositori ini.



### Ada Donghua?



Ada. Coba **Kuramanime**.



### Kenapa search tidak menemukan hasil?



- Pastiin provider sudah terpasang.

- Cek filter di halaman search, jangan sampai providernya belum aktif.

- Makin banyak provider yang dipasang, makin besar kemungkinan hasil search muncul.



<a id="faq-dns"></a>



### Provider tidak ada datanya atau gagal loading (Indonesia)?



Beberapa situs sumber diblokir ISP Indonesia lewat DNS, lalu dialihkan ke Internet Positif. Nyalakan **Private DNS** di pengaturan Android biar aplikasi bisa resolve alamat asli situsnya.



Buka **Settings → Network → Private DNS** terus masukkan, contohnya:



```

dns.google

```



DNS-over-HTTPS juga bisa.



<br>



---



<div align="center">



Kalau zvlzPlay berguna buat kamu, ⭐ di [repository](https://github.com/Zivalez/zvlzPlay) selalu dihargai.



<sub>Dibuat untuk <a href="https://github.com/recloudstream/cloudstream">CloudStream</a> &nbsp;·&nbsp; <a href="https://github.com/Zivalez/zvlzPlay">zvlzPlay</a></sub>



</div>
