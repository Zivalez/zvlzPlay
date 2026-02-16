# zvlzPlay

**zvlzPlay** adalah kumpulan ekstensi CloudStream untuk menonton anime dan serial dengan subtitle Indonesia 🇮🇩

---

## Ekstensi

| Nama Ekstensi | Region     | Status     |
| ------------- | ---------- | ---------- |
| Winbu         | Indonesia  | ✅ Active |

---

## Cara Menggunakan

1. Buka aplikasi CloudStream.
   Jika belum punya, download [DISINI](https://github.com/recloudstream/cloudstream/releases)
2. Masuk ke menu **"Pengaturan"** > **"Ekstensi"**
3. Klik tombol **"Tambahkan Repositori"**
4. Masukkan URL repositori zvlzPlay:
   ```
   https://raw.githubusercontent.com/Zivalez/zvlzPlay/builds/repo.json
   ```
5. Klik **"Tambahkan"**
6. Ekstensi yang tersedia akan muncul di daftar
7. Pilih ekstensi yang ingin diinstal dan klik **"Instal"**

---

## Development

Project ini dibuat berdasarkan template [TestPlugins](https://github.com/recloudstream/TestPlugins) dari CloudStream.

### Build Otomatis
Push ke branch `main` atau `master` akan otomatis trigger GitHub Actions yang:
1. Build semua plugin
2. Generate `plugins.json`
3. Push hasil build ke branch `builds`

### Build Manual
```bash
./gradlew make makePluginsJson
```

---

## Credits

- [CloudStream](https://github.com/recloudstream/cloudstream) — Aplikasi streaming
- [TestPlugins](https://github.com/recloudstream/TestPlugins) — Template dasar
- [AnimeX](https://github.com/Asm0d3usX/AnimeX) — Referensi provider Samehadaku
