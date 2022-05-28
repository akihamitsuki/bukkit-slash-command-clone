package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * /setworldspawn [<pos>] [<angle>]
 */
public class SetWorldSpawnCommand implements CommandExecutor {

  private Player player = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!CommandUtility.isPlayer(sender)) {
      // 使えるようにもできるが、まだその設定をしていない
      Bukkit.getLogger().warning("このコマンドはコンソール画面からは使えません");
      // このコマンドは失敗である
      return false;
    }

    // コマンド送信者をプレイヤー型に変換する
    player = (Player) sender;

    if (args.length == 0 || args.length == 3 || args.length == 4) {
      return this.setWorldSpawn(player, args);
    }

    Bukkit.getLogger().warning("引数の数が正しくありません。");
    return false;
  }

  /**
   * エンティティを移動させる
   *
   * @param sender 送信者
   * @param args コマンドからの引数
   * @return コマンド成功判定
   */
  private boolean setWorldSpawn(Player player, String[] args) {
    World world = player.getWorld();

    Location location = player.getLocation();
    if (args.length >= 3) {
      String[] coordinates = Arrays.copyOfRange(args, 0, (2 + 1));
      location = CommandUtility.getLocation(player, coordinates);
      // エラーがあった場合はnullが返ってくるので終了させる
      if (location == null) {
        Bukkit.getLogger().warning("座標の値が正しくありません。");
        return false;
      }
    }

    Float angle = 1.0f;
    if (args.length == 4) {
      angle = this.getAngle(args[3]);
      if (angle == null) {
        return false;
      }

      int x = (int) location.getX();
      int y = (int) location.getY();
      int z = (int) location.getZ();
      world.setSpawnLocation(x, y, z, angle);
    } else {
      world.setSpawnLocation(location);
    }

    Bukkit.getLogger().info("ワールドのスポーン位置に設定しました。");
    return true;
  }

  public Float getAngle(String number) {
    try {
      return Float.parseFloat(number);
    } catch (NumberFormatException e) {
      Bukkit.getLogger().warning("向きの値が正しくありません");
      return null;
    }
  }

}
