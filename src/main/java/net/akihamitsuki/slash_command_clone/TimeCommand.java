package net.akihamitsuki.slash_command_clone;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /time: 時間を操作するコマンド
 */
public class TimeCommand implements CommandExecutor {

  World world = null;
  CommandSender commandSender = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // 引数の数が正しければ
    if (args.length == 2) {
      commandSender = sender;
      if (!CommandUtility.isPlayer(sender)) {
        world = ((Player) sender).getWorld();
      } else {
        world = Bukkit.getWorld("world");
      }

      this.time(args);
      return true;
    }
    return false;
  }

  /**
   * 時間コマンド
   *
   * @param args
   * @return コマンド成功判定
   */
  private boolean time(String[] args) {
    if (args[0].equals("set")) {
      return this.set(args[1]);
    }

    if (args[0].equals("add")) {
      return this.add(args[1]);
    }

    if (args[0].equals("query")) {
      return this.query(args[1]);
    }

    return false;
  }

  /**
   * 時間を設定する
   *
   * @param arg ティック
   * @return コマンド成功判定
   */
  private boolean set(String arg) {
    if (arg.equals("day")) {
      world.setTime(1000L);  // 7時
    } else if (arg.equals("noon")) {
      world.setTime(6000L);  // 12時
    } else if (arg.equals("night")) {
      world.setTime(13000L);  // 19時
    } else if (arg.equals("midnight")) {
      world.setTime(18000L);  // 24時
    } else {
      // 上のどれでもなければ数字として扱う
      try {
        world.setTime(Long.parseLong(arg));
      } catch (NumberFormatException e) {
        return false;
      }
    }

    return true;
  }

  /**
   * 時間を加算する
   *
   * @param arg 加算するティック
   * @return コマンド成功判定
   */
  private boolean add(String arg) {
    Long amount = 0L;
    try {
      amount = Long.parseLong(arg);
    } catch (NumberFormatException e) {
      return false;
    }
    world.setTime(world.getTime() + amount);

    return true;
  }

  /**
   * 現在の状態を表示
   *
   * @param arg 表示する対象
   * @return コマンド成功判定
   */
  private boolean query(String arg) {
    if (arg.equals("daytime")) {
      // 1日の中の時間（ティック）
      commandSender.sendMessage(Long.toString(world.getTime()));
    } else if (arg.equals("gametime")) {
      // ゲーム開始からの通算時間（ティック）
      commandSender.sendMessage(Long.toString(world.getGameTime()));
    } else if (arg.equals("day")) {
      // ゲーム内で経過した日数
      // 単位を意識して計算すること

      // 通算時間(tick)
      Long gameTick = world.getGameTime();
      // 1日のティック数(tick/day)
      // (20ティック / 1秒) * (60秒 / 1分) * (20分 / (マイクラ世界の)1日)
      Long tickParDay = Long.valueOf(20 * 60 * 20);
      // 日数に直す(tick / (tick/day) = day)
      // 本来は余りが出るが、長整数(Long)どうしの計算なので結果も長整数となり、ここでは余りが無視されている
      Long day = gameTick / tickParDay;
      commandSender.sendMessage(Long.toString(day));
    } else {
      // それ以外は無効
      return false;
    }

    return true;
  }

}
