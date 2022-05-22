package net.akihamitsuki.slash_command_clone;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * 共通の処理を行うクラス
 */
public class CommandUtility {

  /**
   * コマンド送信者がプレイヤーであるかを判定する
   *
   * @param sender コマンド送信者
   * @return プレイヤー判定（プレイヤーなら真）
   */
  public static boolean isPlayer(CommandSender sender) {
    if (sender instanceof Player) {
      return true;
    }
    return false;
  }

  /**
   * エンティティが生きているエンティティかどうか
   *
   * @param entity
   * @return 生物判定
   */
  public static boolean isLivingEntity(Entity entity) {
    if (entity instanceof LivingEntity) {
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
  public static EntityType getEntityType(String name) {
    // 例外処理
    // ユーザーが入力する場合は、常に正しい値になるとは限らない
    try {
      // この処理の時に問題が起きれば catch に移る
      // 問題が起きなければ、そのままエンティティ名を返して、このメソッドは終了
      return EntityType.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      // エラーログを表示
      Bukkit.getLogger().warning("エンティティ名が正しくありません。");
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
  public static Location getLocation(Player player, String[] coordinates) {
    // 各座標を取得する
    Double x = stringToCoodinate(player, coordinates[0], 'x');
    Double y = stringToCoodinate(player, coordinates[1], 'y');
    Double z = stringToCoodinate(player, coordinates[2], 'z');
    // いずれかがnullであれば座標(location)を決定できないので、座標インスタンスではなくnullを返す
    if (x == null || y == null || z == null) {
      return null;
    }
    // 以上の情報から座標インスタンスを生成して返す
    World world = player.getWorld();
    return new Location(world, x, y, z);
  }

  /**
   * 文字列を座標に変換する
   *
   * @param string 座標(記号も含むので数字ではなく文字列)
   * @param axis 対象とする座標軸
   * @return 個別の座標
   */
  private static Double stringToCoodinate(Player player, String string, Character axis) {
    // ユーザーが入力する文字を扱う場合には例外処理を行う
    try {
      // 扱う数字の変数
      // 整数(integer)でも問題はないが、座標クラスが要求する型通りの小数(double)にしておく
      Double d = null;

      // 最初の文字列がチルダなら相対座標として扱う
      if (string.charAt(0) == '~') {
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
      // エラーログを表示
      Bukkit.getLogger().warning(String.format("%s座標の値が正しくありません。", axis));
      // エラーの場合はnullを返す
      return null;
    }
  }

  /**
   * 引数からアイテムの種類を取得する
   *
   * @param name アイテム名
   * @return アイテムの種類
   */
  public static Material getMaterial(String name) {
    // 例外処理
    // ユーザーが入力する場合は、常に正しい値になるとは限らない
    try {
      // この処理の時に問題が起きれば catch に移る
      return Material.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      // エラーログを表示
      Bukkit.getLogger().warning("アイテム名が正しくありません。");
      // 全体の処理はここで中断しないので、nullを返して続ける
      return null;
    }
  }

  /**
   * ゲームモードを引数の値から取得する
   *
   * @param arg
   * @return ゲームモード
   */
  public static GameMode getGameMode(String arg) {
    try {
      return GameMode.valueOf(arg.toUpperCase());
    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("ゲームモードが正しくありません。");
      return null;
    }
  }

}
