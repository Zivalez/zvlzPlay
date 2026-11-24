<div align="center">

<br>

<p align="center">
<a href="https://github.com/recloudstream/cloudstream"><picture><img src="https://cloudstream.zvlz.my.id/cloudstream.png" alt="CloudStream" width="220" height="220" style="vertical-align: middle;"></picture></a>
<a href="https://github.com/Zivalez/zvlzPlay"><picture><img src="https://raw.githubusercontent.com/Zivalez/zvlzPlay/builds/zvlzPlay128.png" alt="zvlzPlay" width="110" height="110" style="vertical-align: middle;"></picture></a>
</p>

# CloudStream × zvlzPlay

*A quiet extension repository for Anime, Movies, TV Series, Asian Drama, and Donghua.*

<br>

<a href="https://github.com/Zivalez/zvlzPlay/tree/builds"><img src="https://img.shields.io/badge/last%20update-22%20Nov%202026%2018%3A28-aa472a?style=flat-square&labelColor=aa472a&color=fdf0d7" alt="Last update"/></a>&ensp;
<a href="https://github.com/Zivalez/zvlzPlay/stargazers"><img src="https://img.shields.io/github/stars/Zivalez/zvlzPlay?style=flat-square&labelColor=aa472a&color=fdf0d7" alt="Stars"/></a>&ensp;
<img src="https://img.shields.io/badge/platform-Android-fdf0d7?style=flat-square&labelColor=aa472a" alt="Android"/>&ensp;
<img src="https://img.shields.io/badge/content-NSFW%20opt--in-fdf0d7?style=flat-square&labelColor=aa472a" alt="NSFW"/>

<br>

[Install](#install) &nbsp;·&nbsp; [Providers](#providers) &nbsp;·&nbsp; [Which one](#which-one) &nbsp;·&nbsp; [FAQ](#faq)

English &nbsp;/&nbsp; [Bahasa Indonesia](README_ID.md)

<br>

</div>

## About

**CloudStream** is an open-source Android app for watching movies, anime, drama, and TV series for free, with no ads. It ships with no content of its own. Think of it as a media player and search engine, powered entirely by extensions.

**zvlzPlay** is one such extension repository. It bundles providers from Indonesian and international streaming sites, so you add it once and choose only the ones you need.

> [!NOTE]
> CloudStream is the app, the container.\
> zvlzPlay is the set of extensions that find the streaming sources.

<br>

## Features

- No ads, no tracking or analytics
- Phone and TV support, with Chromecast
- Skip opening
- Up to 1080p or higher, depending on the provider
- Bookmarks
- Universal search across every installed provider
- Switch between providers whenever you need

<br>

<a id="install"></a>

## Install

<a href="https://github.com/recloudstream/cloudstream/releases/latest" target="_blank">
  <img src="https://img.shields.io/badge/Download_CloudStream-latest-fdf0d7?style=for-the-badge&logo=android&logoColor=white&labelColor=aa472a" alt="Download APK" />
</a>

### Quick Add

Type this shortcode into CloudStream:

```
zvlzplay
```

1. Install **CloudStream**.\
   An empty app afterwards is normal, since no extensions are installed yet.
2. Open **Settings**, go to **Extensions**, then select **Add Repository**.
3. Enter the shortcode `zvlzplay`.
4. Open **zvlzPlay** and install the providers you want.
5. Return to the **Home Screen** and pick a provider from the bottom-right corner.

<details>
<summary>Prefer the full URL?</summary>

```
https://cloudstream.zvlz.my.id/builds/repo.json
```

If asked for a name, you can leave it empty or set it to anything, for example `zvlzPlay`.

</details>

> [!TIP]
> If a provider fails to load in Indonesia, your ISP may be blocking it through DNS. See the [fix in the FAQ](#faq-dns).

<br>

<a id="providers"></a>

## Providers

> [!NOTE]
> **Health Check** · Last checked: **2026-11-24 13:41 WIB**
>
> Each check is run 5 times with different titles, sampled randomly from the provider's catalog. A check passes if at least one attempt succeeds. This is a scheduled check every 12 hours, not continuous monitoring.

Like the in-app CloudStream provider test, the links check tests real titles: if all 5 titles' links are dead, the provider is marked as failed, even if other titles might still play.

- **Status Check:** 🟢 means all 5 attempts passed, 🟡 means some attempts failed but at least one passed, 🔴 means all 5 attempts failed, 🔵 means the provider is not covered by the checker yet.
- **Health:** share of the 4 checks that passed (a yellow check still counts as passed).
- **Condition:** how many of the 5 attempts passed, shown only for checks that were not perfect. "Links 4/5" means 4 out of 5 titles passed.

> [!NOTE]
> *Auto-repair:* Failed checks are automatically diagnosed, repaired, and re-checked for **Active** providers only. **No Longer Maintained** providers are checked, but never repaired.

### Active Providers

| Provider | Status Check | Health | Condition |
| :--- | :--- | :---: | :---: |
| **Idlix** <br> <sub>Movie, TV Series, Asian Drama, Anime</sub> | <sub>🟢 Home <br> 🟡 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Search 4/5 |
| **Samehadaku** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Otakudesu** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Alqanime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Nontonanimeid** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Kuramanime** <br> <sub>Anime, Donghua</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Sokuja** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Kuronime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |

### No Longer Maintained

> [!WARNING]
> These providers are no longer actively maintained. The maintainer has too many providers alone and is getting busy. They may still work if the source sites are up, but issues will not be fixed.

| Provider | Status Check | Health | Condition |
| :--- | :--- | :---: | :---: |
| **LayarKaca** <br> <sub>Movie, TV Series, Asian Drama, Anime Movie</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟡 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | Detail 4/5 |
| **Dramacool** <br> <sub>Asian Drama, Variety Show</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/10 <br> Search 0/4 |
| **Gomunime** <br> <sub>Anime</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Winbu** <br> <sub>Anime, Donghua</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Zoronime** <br> <sub>Anime</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/10 <br> Search 0/4 |
| **Twitch** <br> <sub>Global Live Streamers</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **Loklok** <br> <sub>Movie, TV Series</sub> | <sub>🔴 Home <br> 🔴 Search <br> 🔵 Detail <br> 🔵 Links</sub> | `▱▱▱▱`<br> **0%** | Home 0/5 <br> Search 0/4 |
| **Moviebox** <br> <sub>Movie, TV Series, Anime, Asian Drama</sub> | <sub>🟢 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔵 Links</sub> | `▰▱▱▱`<br> **25%** | Search 0/5 <br> Detail 0/5 |
| **Pencurimovie** <br> <sub>Movie</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |
| **IPTV** <br> <sub>Indonesian Live TV (RCTI, SCTV, Trans, ANTV, Metro, Kompas, etc.)</sub> | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> | `▰▰▰▰`<br> **100%** | — |

### NSFW Providers

> [!NOTE]
> These providers serve NSFW content. They are hidden by default. They are not covered by the health checker, and if they break, they stay broken.

<details>
<summary>Show 4 providers</summary>

- **Hentaicop**
- **Hentaihaven**
- **King**
- **KingV2**

</details>


<br>

<a id="which-one"></a>

## Which provider should I use

| Looking for | Recommended |
| :--- | :--- |
| **Anime** | **Samehadaku**, fairly complete with fast streaming.<br>**Kuramanime**, fast updates and stable. |
| **Drama and Movies** | **Idlix**, usually fairly complete. |
| **Donghua** | **Kuramanime** |

When other providers have issues, **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Sokuja**, and **Kuronime** are good alternatives for anime.

<br>

<a id="faq"></a>

## FAQ

### Why is the app empty after installation?

CloudStream needs extensions first. Follow the steps in [Install](#install) to add this repository.

### Is Donghua available?

Yes. Try **Kuramanime**.

### Why does search return no results?

- Make sure the providers are installed.
- Check the search page filters and make sure the provider is enabled.
- The more providers installed, the more likely search results will appear.

<a id="faq-dns"></a>

### A provider shows no data or fails to load (Indonesia)?

Some source sites are blocked by Indonesian ISPs through DNS, and redirected to Internet Positif. Turn on **Private DNS** in Android settings so the app can resolve the real site addresses.

Go to **Settings → Network → Private DNS** and enter, for example:

```
dns.google
```

DNS-over-HTTPS works as well.

<br>

---

<div align="center">

If zvlzPlay is useful to you, a ⭐ on the [repository](https://github.com/Zivalez/zvlzPlay) is always appreciated.

<sub>Built for <a href="https://github.com/recloudstream/cloudstream">CloudStream</a> &nbsp;·&nbsp; <a href="https://github.com/Zivalez/zvlzPlay">zvlzPlay</a></sub>

</div>