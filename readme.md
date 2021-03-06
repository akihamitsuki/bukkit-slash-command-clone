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


### エンティティ
* [`/summon`: エンティティを呼び出す](src/main/java/net/akihamitsuki/slash_command_clone/SummonCommand.java)
* [`/kill`: エンティティを倒す・消す](src/main/java/net/akihamitsuki/slash_command_clone/KillCommand.java)
* [`/teleport`: エンティティを移動させる](src/main/java/net/akihamitsuki/slash_command_clone/TeleportCommand.java)
* [`/effect`: エンティティにポーション効果を与える](src/main/java/net/akihamitsuki/slash_command_clone/EffectCommand.java)


### プレイヤー
* [`/spawnpoint`: プレイヤーのスポーンポイントを設定する](src/main/java/net/akihamitsuki/slash_command_clone/SpawnPointCommand.java)
* [`/experience`: プレイヤーの経験値を設定する](src/main/java/net/akihamitsuki/slash_command_clone/SpawnPointCommand.java)


### ブロック
* [`/setblock`: ブロックを置く](src/main/java/net/akihamitsuki/slash_command_clone/SetblockCommand.java)


### アイテム
* [`/give`: プレイヤーにアイテムを与える](src/main/java/net/akihamitsuki/slash_command_clone/GiveCommand.java)
* [`/clear`: インベントリ内のアイテムを消去する](src/main/java/net/akihamitsuki/slash_command_clone/ClearCommand.java)
* [`/enchant`: アイテムにエンチャントする](src/main/java/net/akihamitsuki/slash_command_clone/EnchantCommand.java)


### ワールド
* [`/time`: 時間を操作する](src/main/java/net/akihamitsuki/slash_command_clone/TimeCommand.java)
* [`/weather`: 天気を操作する](src/main/java/net/akihamitsuki/slash_command_clone/WeatherCommand.java)
* [`/setworldspawn`: ワールドのスポーン位置を設定する](src/main/java/net/akihamitsuki/slash_command_clone/SetWorldSpawnCommand.java)


### ゲーム設定
* [`/defaultgamemode`: ゲームモードの初期値を設定する](src/main/java/net/akihamitsuki/slash_command_clone/DefaultGameModeCommand.java)
* [`/difficulty`: ゲームの難易度を設定する](src/main/java/net/akihamitsuki/slash_command_clone/DifficultyCommand.java)
* [`/gamemode`: プレイヤーのゲームモードを設定する](src/main/java/net/akihamitsuki/slash_command_clone/GameModeCommand.java)
* [`/gamerule`: ワールドのゲームルールを設定する](src/main/java/net/akihamitsuki/slash_command_clone/GameModeCommand.java)


### 演出
* [`/particle`: パーティクルを発生させる](src/main/java/net/akihamitsuki/slash_command_clone/ParticleCommand.java)
* [`/title`: 字幕を表示する](src/main/java/net/akihamitsuki/slash_command_clone/TitleCommand.java)
* [`/playsound`: 音声を再生する](src/main/java/net/akihamitsuki/slash_command_clone/PlaySoundCommand.java)
* [`/stopsound`: 音声を停止する](src/main/java/net/akihamitsuki/slash_command_clone/StopSoundCommand.java)
* [`/bossbar`: ボスバーを設定する](src/main/java/net/akihamitsuki/slash_command_clone/BossBarCommand.java)


