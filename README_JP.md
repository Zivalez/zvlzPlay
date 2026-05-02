<h1 align="center">CloudStream x zvlzPlay</h1>

<p align="center">
<img src="https://avatars.githubusercontent.com/u/142050504" alt="zvlzPlay" width="80" height="80">
</p>

<p align="center">
アニメ、韓国ドラマ、日本ドラマ、中国ドラマ、海外ドラマ、<br>
映画、ライブTV、ライブ配信などのコンテンツをストリーミング＆ダウンロード。<br>
（NSFWコンテンツを含む）
</p>

<p align="center">
<a href="README.md">Bahasa Indonesia</a> | <a href="README_EN.md">English</a> | <a href="#">日本語</a>
</p>


## CloudStreamとは？

**CloudStream**は、映画、アニメ、ドラマ、TVシリーズを無料で視聴できるオープンソースのAndroidアプリです。広告なし、アカウント登録不要。

CloudStream自体にはコンテンツが内蔵されていません。プレイヤーと検索エンジンとしてのみ機能します。視聴するには、インターネット上のストリーミングサイトから動画データを取得する**拡張機能（プラグイン）**をインストールする必要があります。


## zvlzPlayとは？

**zvlzPlay**は、インドネシアおよび海外のストリーミングサイトに対応したCloudStream用の拡張機能/プロバイダーのコレクションです。

> **CloudStream** = アプリ本体（器）\
> **zvlzPlay** = ストリーミングコンテンツを取得するための拡張機能


## 機能

- 広告なし
- トラッキング/分析なし
- ブックマーク
- スマホとTV対応
- Chromecast対応
- オープニングスキップ
- ログイン不要
- 必要に応じてプロバイダーを選択・切り替え可能
- 解像度はプロバイダーによる（ほとんどが1080p以上）
- ユニバーサル検索、プロバイダーを多くインストールするほど検索結果が充実


## アプリのダウンロード

CloudStreamはこちらからダウンロード：\
https://github.com/recloudstream/cloudstream/releases


## zvlzPlay拡張機能のインストール方法

1. まずCloudStreamアプリをインストールしてください。\
   インストール直後は拡張機能が未インストールのため、画面は空の状態です。
2. **Settings**を開きます。
3. **Extensions**に進みます。
4. **Add Repository**を選択します。
5. 以下のURLをRepository URLとして入力してください：
```
https://cloudstream.zvlz.my.id/builds/repo.json
```
6. リポジトリ名は空欄のままか、任意の名前（例：`zvlzPlay`）を入力してください。
7. リポジトリの追加が完了したら、**zvlzPlay**を開いて使用したいプロバイダーを選択します。
8. **ホーム画面**に戻り、右下からプロバイダーを選択します。
9. 完了です。視聴をお楽しみください。


## プロバイダー一覧

| プロバイダー | コンテンツ | ステータス |
| --- | --- | --- |
| Idlix | 映画、TVシリーズ、アジアドラマ、アニメ | ✅ 稼働中 |
| LayarKaca | 映画、TVシリーズ、アジアドラマ、アニメ映画 | ✅ 稼働中 |
| Pencurimovie | 映画 | ✅ 稼働中 |
| Funmovieslix | 映画、TVシリーズ、アジアドラマ | ✅ 稼働中 |
| Moviebox | 映画、TVシリーズ、アニメ、アジアドラマ | ✅ 稼働中 |
| Samehadaku | アニメ | ✅ 稼働中 |
| Otakudesu | アニメ | ✅ 稼働中 |
| Alqanime | アニメ | ✅ 稼働中 |
| Nontonanimeid | アニメ | ✅ 稼働中 |
| Kuronime | アニメ | ✅ 稼働中 |
| Gomunime | アニメ | ✅ 稼働中 |
| Winbu | アニメ、中国アニメ | ✅ 稼働中 |
| Kuramanime | アニメ、中国アニメ | ✅ 稼働中 |
| Zoronime | アニメ | ❌ 停止中 |
| IPTV | インドネシアのライブTV（RCTI、SCTV、Trans、ANTV、Metro、Kompasなど） | ✅ 稼働中 |
| Twitch | グローバルライブ配信 | ✅ 稼働中 |

ステータスの説明：
- ✅ **稼働中** : 安定して正常に使用可能
- 🧪 **ベータ** : 使用可能だがまだ完全には安定していない
- ❌ **停止中** : サイトまたはサーバーがダウン中、一時的に利用不可


## よくある質問

### インストール後にアプリが空なのはなぜ？
CloudStreamは拡張機能（リポジトリ）が必要です。[インストール方法](#zvlzplay拡張機能のインストール方法)の手順に従ってリポジトリを追加してください。

### アニメに最適なプロバイダーは？
- **Samehadaku** : 最も充実、最速ストリーミング
- **Kuramanime** : 最速更新で安定
- **Otakudesu**、**Alqanime**、**Nontonanimeid**、**Kuronime**、**Gomunime** : その他の選択肢

### ドラマと映画のプロバイダーは？
- **Idlix** : 最も充実
- **LayarKaca**、**Funmovieslix**、**Pencurimovie**、**Moviebox**

### 中国アニメ（Donghua）はある？
- **Kuramanime**と**Winbu**で中国アニメのコンテンツを提供しています。

### インドネシアのライブTV（Indosiar、RCTI、SCTVなど）は見れる？
はい。拡張機能リストから**IPTV**プロバイダーをインストールしてください。インドネシアの人気チャンネルは「Popular」行に表示され、残りはカテゴリ別（News、Sports、Kidsなど）に分類されます。

### Twitchの配信者は見れる？
はい。拡張機能リストから**Twitch**プロバイダーをインストールしてください。

### 検索で結果が見つからないのはなぜ？
- プロバイダーがインストールされているか確認してください。
- 検索画面でプロバイダーフィルターを設定し、すべてのプロバイダータイプを有効にしてください。
- プロバイダーを多くインストールするほど、検索結果が充実します。
