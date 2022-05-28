package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * /stopsound <targets> [<source>] [<sound>]
 */
public class StopSoundCommand implements CommandExecutor {

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

    // 引数の数が正しければ
    if (args.length == 0) {
      this.example();
      return true;
    } else if (args.length >= 1 && args.length <= 3) {
      return this.stopSound(player, args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * コード例
   */
  private void example() {
    // 再生中のすべての音声を停止する
    player.stopAllSounds();
  }

  private boolean stopSound(Player player, String[] args) {
    SoundCategory category = null;
    if (args.length > 1) {
      category = CommandUtility.getSoundCategory(args[1]);
    }

    Sound sound = null;
    if (args.length > 2) {
      sound = CommandUtility.getSound(args[2]);
    }

    // 対象者を取得
    List<Entity> targets = TargetSelector.getTargets(player, args[0]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      if (sound == null) {
        ((Player) target).stopAllSounds();
      } else {
        ((Player) target).stopSound(sound, category);
      }
    }

    return true;
  }

}
