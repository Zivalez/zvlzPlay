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
    <img src="https://img.shields.io/badge/Download_CloudStream-latest-E53935?style=for-the-badge&logo=android&logoColor=white" alt="Download APK" />
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

### Active Providers

| Provider | Content | Status |
| --- | --- | --- |
| Idlix | Movie, TV Series, Asian Drama, Anime | ✅ Active |
| Samehadaku | Anime | ✅ Active |
| Otakudesu | Anime | ✅ Active |
| Alqanime | Anime | ✅ Active |
| Nontonanimeid | Anime | ✅ Active |
| Kuramanime | Anime, Donghua | ✅ Active |
| Sokuja | Anime | ✅ Active |
| Kuronime | Anime | ✅ Active |

### No Longer Maintained

The following providers are no longer actively maintained. I maintain too many
providers on my own and I'm getting busy, so I can't keep up with all of them.
They may still work if the source websites are operational, but issues will not be fixed.

| Provider | Content | Status |
| --- | --- | --- |
| LayarKaca | Movie, TV Series, Asian Drama, Anime Movie | ⚠️ No Longer Maintained |
| Dramacool | Asian Drama, Variety Show | ⚠️ No Longer Maintained |
| Gomunime | Anime | ⚠️ No Longer Maintained |
| Winbu | Anime, Donghua | ⚠️ No Longer Maintained |
| Zoronime | Anime | ⚠️ No Longer Maintained |
| Twitch | Global Live Streamers | ⚠️ No Longer Maintained |
| Loklok | Movie, TV Series | ⚠️ No Longer Maintained |
| Moviebox | Movie, TV Series, Anime, Asian Drama | ⚠️ No Longer Maintained |
| Pencurimovie | Movie | ⚠️ No Longer Maintained |
| IPTV | Indonesian Live TV (RCTI, SCTV, Trans, ANTV, Metro, Kompas, etc.) | ⚠️ No Longer Maintained |

Status legend:
- ✅ **Active** : safe to use and should work normally
- ⚠️ **No Longer Maintained** : not actively maintained, may still work if source website is operational


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
