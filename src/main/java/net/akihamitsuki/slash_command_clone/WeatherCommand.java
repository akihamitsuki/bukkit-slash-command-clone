package net.akihamitsuki.slash_command_clone;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /weather: 天気を操作するコマンド
 */
public class WeatherCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!CommandUtility.isPlayer(sender)) {
      sender.sendMessage("このコマンドはコンソール画面からは使えません");
      // このコマンドは失敗である
      return false;
    }

    // コマンド送信者をプレイヤー型に変換する
    Player player = (Player) sender;

    // 引数の数が0であれば
    if (args.length == 1) {
      return this.weather(player, args);
    }
    return false;
  }

  private boolean weather(Player player, String[] args) {
    World world = player.getWorld();

    // NOTE: ここの適切な書き方がよくわからない。とりあえずこれで動く。

    // 晴れ
    if (args[0].equals("clear")) {
      // 雨は降らない
      world.setStorm(false);
      // 雷も鳴らない（おそらくこの記述は無くてもいい）
      world.setThundering(false);

      return true;
    }
    // 雨
    if (args[0].equals("rain")) {
      // 雨が降る
      world.setStorm(true);
      // 雷は鳴らない
      world.setThundering(false);

      return true;
    }
    // 雷雨
    if (args[0].equals("thunder")) {
      // 雨が降る
      world.setStorm(true);
      // 雷も鳴る
      world.setThundering(true);

      return true;
    }

    return false;
  }

}
