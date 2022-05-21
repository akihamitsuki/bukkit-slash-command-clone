package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /particle: パーティクルを発生させる
 */
public class ParticleCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!CommandUtility.isPlayer(sender)) {
      Bukkit.getLogger().warning("このコマンドはコンソール画面からは使えません。");
      // このコマンドは失敗である
      return false;
    }

    // コマンド送信者をプレイヤー型に変換する
    Player player = (Player) sender;

    // 引数の数が適正であれば
    if (args.length == 1 || args.length == 4) {
      return this.particle(player, args);
    }

    return false;
  }

  /**
   * パーティクル発生処理
   *
   * @param player
   * @param args
   * @return
   */
  private boolean particle(Player player, String[] args) {
    // パーティクルを発生させるワールド
    World world = player.getWorld();
    // パーティクルの種類
    Particle particle = this.getParticle(args[0]);
    if (particle == null) {
      Bukkit.getLogger().warning("パーティクル名が正しくありません。");
      return false;
    }

    // パーティクルを発生させる座標
    Location location = null;
    // 引数の数で座標の指定方法を変える
    if (args.length == 1) {
      // 座標引数がない場合は自分の位置
      location = player.getLocation();
    } else if (args.length == 4) {
      // 座標引数がある場合は数字から座標インスタンスを作成する

      // 引数から特定の範囲を切り出して、新しい配列を作る
      // copyOfRange([0, 1, 2, 3, 4], 1, 4)の場合は [1,2,3] を取得する。終了値に注意
      String[] coordinates = Arrays.copyOfRange(args, 1, (3 + 1));
      location = CommandUtility.getLocation(player, coordinates);
      // エラーがあった場合はnullが返ってくるので終了させる
      if (location == null) {
        return false;
      }
    } else {
      // 引数の数は確認済みなので、このelseは起こらないはずだが一応書いておく
      return false;
    }

    // パーティクルの数
    int count = 3;
    // パーティクルを生成する
    world.spawnParticle(particle, location, count);

    // プレイヤーを基準にしても作成できる
    // player.spawnParticle(particle, location, count);

    return true;
  }

  /**
   * パーティクルの種類を取得する
   *
   * @param name パーティクル名
   * @return パーティクルの種類
   */
  private Particle getParticle(String name) {
    // 例外処理
    // ユーザーが入力する場合は、常に正しい値になるとは限らない
    try {
      // この処理の時に問題が起きれば catch に移る
      return Particle.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      // 全体の処理はここで中断しないので、nullを返して続ける
      return null;
    }
  }

}
