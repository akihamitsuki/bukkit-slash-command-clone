# Javaプラグインのコマンド例文


## このプラグインについて

マインクラフトのコマンドをJavaプラグインで再現したものです。
このプラグインの内容自体は、通常のコマンドの劣化版なので、そのまま使ってもまったく意味はありません。  

対応するコマンドの内容をJavaで書いたときの例文として参照してください。
例えば、エンティティを呼び出す処理がわからない場合は`/summon`の記述を調べると、プラグインでのエンティティの呼び出し方がわかると思います。


## 対象者

* マインクラフトのコマンドをおおよそ理解している
* Java言語の基本文法を理解している
* プラグイン(bukkit)での書き方がよくわからない


## 記述済みのコマンド一覧

コマンド名が重ならないように、ここで追加されるコマンド名には最後に`-`が付く。

`/summon` -> `/summon-`


### アイテム
* [`/clear`: インベントリ内のアイテムを消去する](src/main/java/net/akihamitsuki/slash_command_clone/ClearCommand.java)


### エンティティ
* [`/summon`: エンティティを呼び出す](src/main/java/net/akihamitsuki/slash_command_clone/SummonCommand.java)


### ワールド
* [`/time`: 時間を操作する](src/main/java/net/akihamitsuki/slash_command_clone/TimeCommand.java)
* [`/weather`: 天気を操作する](src/main/java/net/akihamitsuki/slash_command_clone/WeatherCommand.java)

