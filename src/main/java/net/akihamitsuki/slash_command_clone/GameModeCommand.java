package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

/**
 * /gamemode (adventure|creative|spectator|survival) [<target>]
 */
public class GameModeCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // 引数の数が1であれば
    if (args.length == 1 || args.length == 2) {
      return this.gamemode(sender, args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * プレイヤーのゲームモードを設定する
   */
  private boolean gamemode(CommandSender sender, String[] args) {
    // ゲームモードを引数から取得する
    GameMode mode = CommandUtility.getGameMode(args[0]);
    if (mode == null) {
      return false;
    }

    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(sender, "@s");
    if (args.length >= 2) {
      targets = TargetSelector.getTargets(sender, args[1]);
    }

    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次へ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }
      // NPCにも設定できるらしいので、PlayerではなくHumanEntityになる
      ((HumanEntity) target).setGameMode(mode);
    }

    return true;
  }

}
