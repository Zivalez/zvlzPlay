<h1 align="center">CloudStream x zvlzPlay</h1>

<p align="center">
<a href="https://github.com/recloudstream/cloudstream"><picture><img src="https://cloudstream.zvlz.my.id/cloudstream.png" alt="CloudStream" width="100" height="100"></picture></a>
<a href="https://github.com/Zivalez/zvlzPlay"><picture><img src="https://avatars.githubusercontent.com/u/142050504" alt="zvlzPlay" width="100" height="100"></picture></a>
</p>

<p align="center">
A CloudStream plugin repository for streaming and downloading Anime, Movies,<br>
TV Series, Asian Drama, and Donghua.<br>
(Contains NSFW content)<br><br>
<a target="_blank" href="https://github.com/Zivalez/zvlzPlay/tree/builds"><img src="https://img.shields.io/github/last-commit/Zivalez/zvlzPlay/builds?label=last%20update"/></a>
</p>

<p align="center">
English | <a href="README_ID.md">Bahasa Indonesia</a>
</p>

## What is CloudStream?

**CloudStream** is an open-source Android app for watching movies, anime, drama, and TV series for free with no ads.

CloudStream does not include content by default. Think of it as a media player and search engine. To start watching, you need to install **extensions** that fetch video data from various streaming websites.


## What is zvlzPlay?

**zvlzPlay** is a CloudStream extension repository containing multiple providers from Indonesian and international streaming sites.

> **CloudStream** = the app/container\
> **zvlzPlay** = extensions for finding streaming sources


## Features

- Ad-free
- No tracking or analytics
- Bookmarks
- Phone and TV support
- Chromecast
- Skip opening
- Switch between providers as needed
- Resolution depends on each provider, many support 1080p or higher
- Universal search, the more providers installed, the more complete the results


## App Download

<p>
  <a href="https://github.com/recloudstream/cloudstream/releases/latest" target="_blank">
    <img src="https://img.shields.io/badge/Download_CloudStream-latest-0090FF?style=for-the-badge&logo=android&logoColor=white" alt="Download APK" />
  </a>
</p>


## How to Install zvlzPlay Extensions

1. Install CloudStream first.\
   If the app looks empty after installation, that is normal because no extensions are installed yet.
2. Open **Settings**.
3. Go to **Extensions**.
4. Select **Add Repository**.
5. Enter the following URL as the Repository URL:
```
https://cloudstream.zvlz.my.id/builds/repo.json
```
6. The repository name can be left empty or filled with anything you like, for example `zvlzPlay`.
7. After the repository is added, open **zvlzPlay** and install the providers you want to use.
8. Go back to the **Home Screen**, then select a provider from the bottom-right corner.
9. Done.


## Provider List

**Health Check** · Last checked: **2026-11-18 01:25 WIB**

Checked automatically every 12 hours against the live sites: home feed, search, detail/episodes, and link sources. This is a scheduled check, not continuous monitoring.

Like the in-app CloudStream provider test, the links check tests a single title: if that title's links are dead, the provider is marked as failed, even if other titles might still play.
- **Health:** Share of the 4 checks that passed.
- **Status:** 🟢 means that check returned real data, 🔴 means it failed, 🔵 means the provider is not covered by the checker yet.

> [!NOTE]
> *Auto-repair:* Failed checks are automatically diagnosed, repaired, and re-checked for **Active** providers only. **No Longer Maintained** providers are checked, but never repaired.

### Active Providers

| Provider | Health | Status Check |
| :--- | :---: | :--- |
| **Idlix** <br> <sub>Movie, TV Series, Asian Drama, Anime</sub> | `▰▰▰▱`<br> **75%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🔴 Links</sub> |
| **Samehadaku** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Otakudesu** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Alqanime** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Nontonanimeid** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Kuramanime** <br> <sub>Anime, Donghua</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Sokuja** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Kuronime** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |

### No Longer Maintained

> [!WARNING]
> The following providers are no longer actively maintained. I maintain too many providers on my own and I'm getting busy, so I can't keep up with all of them. They may still work if the source websites are operational, but **issues will not be fixed**.

| Provider | Health | Status Check |
| :--- | :---: | :--- |
| **LayarKaca** <br> <sub>Movie, TV Series, Asian Drama, Anime Movie</sub> | `▰▰▰▱`<br> **75%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🔴 Links</sub> |
| **Dramacool** <br> <sub>Asian Drama, Variety Show</sub> | `▱▱▱▱`<br> **0%** | <sub>🔴 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔴 Links</sub> |
| **Gomunime** <br> <sub>Anime</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Winbu** <br> <sub>Anime, Donghua</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Zoronime** <br> <sub>Anime</sub> | `▱▱▱▱`<br> **0%** | <sub>🔴 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔴 Links</sub> |
| **Twitch** <br> <sub>Global Live Streamers</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **Loklok** <br> <sub>Movie, TV Series</sub> | `▱▱▱▱`<br> **0%** | <sub>🔴 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔴 Links</sub> |
| **Moviebox** <br> <sub>Movie, TV Series, Anime, Asian Drama</sub> | `▰▱▱▱`<br> **25%** | <sub>🟢 Home <br> 🔴 Search <br> 🔴 Detail <br> 🔴 Links</sub> |
| **Pencurimovie** <br> <sub>Movie</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |
| **IPTV** <br> <sub>Indonesian Live TV (RCTI, SCTV, Trans, ANTV, Metro, Kompas, etc.)</sub> | `▰▰▰▰`<br> **100%** | <sub>🟢 Home <br> 🟢 Search <br> 🟢 Detail <br> 🟢 Links</sub> |


## FAQ

### Why is the app empty after installation?
CloudStream requires extensions first. Follow the steps in [How to Install](#how-to-install-zvlzplay-extensions) to add this repository.

### Which provider is best for Anime?
- **Samehadaku** : fairly complete with fast streaming
- **Kuramanime** : fast updates and stable
- **Otakudesu**, **Alqanime**, **Nontonanimeid**, **Sokuja**, **Kuronime** : alternatives when other providers have issues

### Which providers are best for Drama and Movies?
- **Idlix** : usually fairly complete

### Is Donghua available?
- Yes. Try **Kuramanime**.

### Why does search return no results?
- Make sure the providers are installed.
- Check the search page filters and make sure the provider is enabled.
- The more providers installed, the more likely search results will appear.

### A provider shows no data / fails to load (Indonesia)?
Some source sites are blocked by Indonesian ISPs via DNS (redirected to
Internet Positif). Turn on **Private DNS** in Android settings
(Settings → Network → Private DNS, e.g. `dns.google`) or use DNS-over-HTTPS
so the app can resolve the real site addresses.
