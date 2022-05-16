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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * /summon: エンティティを呼び出す
 */
public class SummonCommand implements CommandExecutor {

  private Player player = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!isPlayer(sender)) {
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
    player.sendMessage(Component.text(
      "error: 引数の数が正しくありません。",
      NamedTextColor.RED
    ));

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
    EntityType entityType = this.getEntityType(args[0]);
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
      location = this.getLocation(world, coordinates);
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

  /**
   * コマンド送信者がプレイヤーであるかを判定する
   *
   * @param sender コマンド送信者
   * @return 判定（プレイヤーなら真）
   */
  private boolean isPlayer(CommandSender sender) {
    if (sender instanceof Player) {
      return true;
    }
    return false;
  }

  /**
   * 文字列からエンティティの種類を取得する
   *
   * @param name エンティティ名
   * @return エンティティの種類
   */
  private EntityType getEntityType(String name) {
    // 例外処理
    // ユーザーが入力する場合は、常に正しい値になるとは限らない
    try {
      // この処理の時に問題が起きれば catch に移る
      // 問題が起きなければ、そのままエンティティ名を返して、このメソッドは終了
      return EntityType.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      // 使用者に通知する
      player.sendMessage(Component.text(
        "error: エンティティ名が正しくありません。",
        NamedTextColor.RED
      ));
      // 全体の処理はここで中断しないので、nullを返して続ける
      return null;
    }
  }

  /**
   * 呼び出す座標を取得する
   *
   * @param coordinates 座標文字列の配列
   * @param world プレイヤーのいるワールド
   * @return 座標
   */
  private Location getLocation(World world, String[] coordinates) {
    // 各座標を取得する
    Double x = this.stringToCoodinate(coordinates[0], 'x');
    Double y = this.stringToCoodinate(coordinates[1], 'y');
    Double z = this.stringToCoodinate(coordinates[2], 'z');
    // いずれかがnullであれば座標(location)を決定できないので、座標インスタンスではなくnullを返す
    if (x == null || y == null || z == null) {
      return null;
    }
    // 以上の情報から座標インスタンスを生成して返す
    return new Location(world, x, y, z);
  }

  /**
   * 文字列を座標に変換する
   *
   * @param string 座標(記号も含むので数字ではなく文字列)
   * @param axis 対象とする座標軸
   * @return 個別の座標
   */
  private Double stringToCoodinate(String string, Character axis) {
    // ユーザーが入力する文字を扱う場合には例外処理を行う
    try {
      // 扱う数字の変数
      // 整数(integer)でも問題はないが、座標クラスが要求する型通りの小数(double)にしておく
      Double d = null;

      if (string.charAt(0) == '~') {
        // 最初の文字列がチルダなら相対座標として扱う

        // 軸に合わせて取得する値を変える
        if (axis == 'x') {
          d = player.getLocation().getX();
        } else if (axis == 'y') {
          d = player.getLocation().getY();
        } else if (axis == 'z') {
          d = player.getLocation().getZ();
        }

        // 1文字より多ければ、残りを数字として扱い加算する
        if (string.length() > 1) {
          d += Double.parseDouble(string.substring(1));
        }
      } else {
        // 記号がなければ絶対座標なので、そのまま数字として扱う
        d = Double.parseDouble(string);
      }
      // 最終結果の数字を返す
      return d;
    } catch (NumberFormatException e) {
      // エラー内容を通知する
      player.sendMessage(Component.text(
        String.format("error: %s座標の値が正しくありません。", axis),
        NamedTextColor.RED
      ));
      // エラーの場合はnullを返す
      return null;
    }
  }
}
