package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * /summon: エンティティを呼び出す
 */
public class SummonCommand implements CommandExecutor {

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
      this.summonSample();
      // コマンド成功
      return true;
    } else if (args.length == 1 || args.length == 4) {
      // 具体的な処理は別メソッドに分ける
      // 真偽値で成功判定が返ってくる
      return this.summon(args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * エンティティを呼び出す処理のサンプル
   */
  private void summonSample() {
    // エンティティを呼び出すために、ワールドと座標を指定する
    // /summonのように座標だけでは呼び出せない(自動的に今のワールドにならない)

    // 今のワールドは自分の情報から取得できる
    World world = player.getWorld();
    // 同じように自分の座標は自分から取得できる
    Location location = player.getLocation();
    // これらの情報を使って、その位置にエンティティを呼び出す
    world.spawnEntity(location, EntityType.PIG);

    // 呼び出した後で何かしたい場合は、目的のエンティティに併せて「キャスト（型変換）」する。この場合は豚のPig。
    /*
    Pig pig = (Pig) world.spawnEntity(location, EntityType.PIG);
    pig.setHealth(1.0f);
    */
  }

  private boolean summon(String[] args) {
    // ワールドは使用したプレイヤーと同じで固定
    World world = player.getWorld();

    // エンティティ型を取得する
    EntityType entityType = CommandUtility.getEntityType(args[0]);
    // エラーがあった場合はnullが返ってくるので終了させる
    // この場合はのfalseはコマンド失敗の意味
    if (entityType == null) {
      return false;
    }

    // スポーン位置を取得する
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

    // 生成した値でエンティティのスポーンを実行する
    world.spawnEntity(location, entityType);

    // trueはコマンド成功
    return true;
  }

}
