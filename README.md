# Telestal
PaperMC Plugin - 転移結晶プラグイン<br>
<br>
Bukkit、Spigotは非対応です。<br>
バージョンは1.12.2を想定して作られています。

##プラグイン説明
ポータルに近づきアクティベートすると、転移結晶を使って転移することができるようになるプラグインです。<br>
アクティベートしていないポータルには転移結晶を使用しても、転移することができません。<br>
転移結晶は使用するごとに1個ずつ消費していきます。<br>
主にRPGサーバーでの使用に適しています。
RPGサーバーの移動システムに使ってみてはどうでしょうか？

## 必須プラグイン
ProtocolLib (4.8.0) -> https://github.com/dmulloy2/ProtocolLib<br>
Holographic Displays (3.0.1) -> https://github.com/filoghost/HolographicDisplays

## Commands
/telestal - メインコマンド<br>
  alias: /ts<br>
```
/ts help <page> - ヘルプを表示
/ts give <portal> <amount> <player> - 転移結晶を与える
/ts create <name> - ポータルを作成
/ts remove <portal> - ポータルを削除
/ts set <portal> - ポータルの位置を再設定
/ts reset <player> - プレイヤーデータの削除
/ts activate <portal> <player> - 指定プレイヤーのポータルを有効化
/ts inactivate <portal> <player> - 指定プレイヤーのポータルを無効化
/ts reload - リロード（config.ymlとポータルの再読み込み）
/ts info [portal|player] <portal|player> - ポータル・プレイヤーの情報を表示
/ts list <page> - ポータルのリストを表示
/ts tp <portal> <player> - ポータルにテレポート
```

## Permission
```
telestal.use
telestal.discover
telestal.commands.help
telestal.commands.give
telestal.commands.create
telestal.commands.remove
telestal.commands.set
telestal.commands.reset
telestal.commands.activate
telestal.commands.inactivate
telestal.commands.reload
telestal.commands.info
telestal.commands.list
telestal.commands.tp
```

## config.ymlについて
メッセージの変更ができます。<br>
自鯖で使うために作られたプラグインなので一部変更できないメッセージがあります。<br>
時間があれば、変更できるオプションを増やそうと考えています。<br>
  
## ライセンス
<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="クリエイティブ・コモンズ・ライセンス" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />この 作品 は <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">クリエイティブ・コモンズ 表示 - 非営利 - 継承 4.0 国際 ライセンス</a>の下に提供されています。
