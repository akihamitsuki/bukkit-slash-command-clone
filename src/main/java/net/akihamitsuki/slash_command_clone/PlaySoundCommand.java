package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * /playsound <sound> <source> <targets> [<pos>] [<volume>] [<pitch>] [<minVolume>]
 */
public class PlaySoundCommand implements CommandExecutor {

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

    // 引数の数が1であれば
    if (args.length == 0) {
      this.example();
      return true;
    } else if (args.length >= 3 && args.length <= 9) {
      return this.playSound(player, args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * コード例
   */
  private void example() {
    // サウンドを発生させるワールド
    World world = player.getWorld();
    // サウンドを発生させる場所
    Location location = player.getLocation();
    location.add(0.0f, 5.0f, 0.0f);
    // サウンドの種類
    Sound sound = Sound.ENTITY_GENERIC_EXPLODE;
    // 音量 (0.00 ~ 1.00, 1.0が標準）
    float volume = 1.00f;
    // 音程 (0.00 ~ 1.00, 1.0が標準）
    float pitch = 0.55f;
    // 座標を基準にサウンドを生成する
    // 他のエンティティが発生させた音は座標を基準にする
    world.playSound(location, sound, volume, pitch);

    // 座標の代わりにプレイヤー自体を指定してもよい
    // プレイヤーに対する効果音はプレイヤーを基準にする
    // world.playSound(player, sound, volume, pitch);
  }

  private boolean playSound(Player player, String[] args) {
    Sound sound = CommandUtility.getSound(args[0]);
    if (sound == null) {
      Bukkit.getLogger().warning("音声の値が正しくありません");
      return false;
    }

    SoundCategory category = CommandUtility.getSoundCategory(args[1]);
    if (category == null) {
      Bukkit.getLogger().warning("音声カテゴリの値が正しくありません");
      return false;
    }

    Location location = null;
    if (args.length > 5) {
      String[] coordinates = Arrays.copyOfRange(args, 3, (5 + 1));
      location = CommandUtility.getLocation(player, coordinates);
    }

    float volume = 1.00f;
    if (args.length > 6) {
      volume = this.getFloat(args[6]);
    }

    float pitch = 1.00f;
    if (args.length > 7) {
      pitch = this.getFloat(args[7]);
    }

    // 対象者を取得
    List<Entity> targets = TargetSelector.getTargets(player, args[2]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      if (location == null) {
        ((Player) target).playSound(target.getLocation(), sound, category, volume, pitch);
      } else {
        ((Player) target).playSound(location, sound, category, volume, pitch);
      }
    }

    return true;
  }

  private float getFloat(String number) {
    try {
      return Float.parseFloat(number);
    } catch (NumberFormatException e) {
      return 1.00f;
    }
  }

}
