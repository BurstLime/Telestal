# Telestal
PaperMC Plugin - 転移結晶システム

<h2>必須プラグイン</h2>
ProtocolLib (4.8.0) -> https://github.com/dmulloy2/ProtocolLib<br>
Holographic Displays (3.0.1) -> https://github.com/filoghost/HolographicDisplays

<h2>Commands</h2>
/telestal - メインコマンド<br>
  alias: /ts<br>
<br>
/ts help <page> - ヘルプを表示<br>
/ts give <portal> <amount> <player> - 転移結晶を与える<br>
/ts create <name> - ポータルを作成<br>
/ts remove <portal> - ポータルを削除<br>
/ts set <portal> - ポータルの位置を再設定<br>
/ts reset <player> - プレイヤーデータの削除<br>
/ts activate <portal> <player> - 指定プレイヤーのポータルを有効化<br>
/ts inactivate <portal> <player> - 指定プレイヤーのポータルを無効化<br>
/ts reload - リロード（config.ymlとポータルの再読み込み）<br>
/ts info [portal|player] <portal|player> - ポータル・プレイヤーの情報を表示<br>
/ts list <page> - ポータルのリストを表示<br>
/ts tp <portal> <player> - ポータルにテレポート<br>

<h2>Permission</h2>
telestal ─── commands ─── help<br>
          │─ use       │─ give<br>
          │─ discover  │─ create<br>
                       │─ remove<br>
                       │─ set<br>
                       │─ reset<br>
                       │─ activate<br>
                       │─ inactivate<br>
                       │─ reload<br>
                       │─ info<br>
                       │─ list<br>
                       │─ tp<br>
