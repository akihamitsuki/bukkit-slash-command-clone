package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


/**
 * /teleport
 */
public class TeleportCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!CommandUtility.isPlayer(sender)) {
      Bukkit.getLogger().warning("このコマンドはコンソール画面からは使えません。");
      // このコマンドは失敗である
      return false;
    }

    // TODO: ほかの引数にも対応する
    // teleport <destination>
    // teleport <targets> <destination>
    // teleport <location>
    // teleport <targets> <location> <rotation>
    // teleport <targets> <location> facing <facingLocation>
    // teleport <targets> <location> facing entity <facingEntity> [<facingAnchor>]

    if (args.length == 4) {
      // /tereport <targets> <location>
      return this.teleport(sender, args);
    }

    return false;
  }

  /**
   * エンティティを移動させる
   *
   * @param sender 送信者
   * @param args コマンドからの引数
   * @return コマンド成功判定
   */
  private boolean teleport(CommandSender sender, String[] args) {
    // 対象を取得する
    List<Entity> targets = TargetSelector.getTargets(sender, args[0]);

    String[] coordinates = Arrays.copyOfRange(args, 1, (3 + 1));
    Location location = CommandUtility.getLocation(((Player) sender), coordinates);
    // エラーがあった場合はnullが返ってくるので終了させる
    if (location == null) {
      return false;
    }

    // 対象ごとにループして処理をする
    for (Entity target: targets) {
      target.teleport(location);
    }

    return true;
  }

}
