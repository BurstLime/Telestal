# Telestal
PaperMC Plugin - 転移結晶プラグイン
Bukkit、Spigotは非対応です。
PaperMCでのみ利用可能です。

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
telestal.use - 転移結晶の使用<br>
telestal.discover - ポータルアクティベート<br>
telestal.commands.help<br>
telestal.commands.give<br>
telestal.commands.create<br>
telestal.commands.remove<br>
telestal.commands.set<br>
telestal.commands.reset<br>
telestal.commands.activate<br>
telestal.commands.inactivate<br>
telestal.commands.reload<br>
telestal.commands.info<br>
telestal.commands.list<br>
telestal.commands.tp<br>
  
<h2>config.ymlについて</h2>
メッセージの変更ができます。<br>
自サーバーで使うために作られたプラグインなので一部変更できないメッセージがあります。<br>
時間があれば、変更できるオプションを増やそうと考えています。<br>
  
<h2>ライセンス</h2>
<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="クリエイティブ・コモンズ・ライセンス" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />この 作品 は <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">クリエイティブ・コモンズ 表示 - 非営利 - 継承 4.0 国際 ライセンス</a>の下に提供されています。
