package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * /setblock <pos> <block> [destroy|keep|replace]
 */
public class SetblockCommand implements CommandExecutor {

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

    // TODO: 置き換えモードに対応する

    // 引数の数が1であれば
    if (args.length == 0) {
      this.example();
      // コマンド成功
      return true;
    } else if (args.length == 4) {
      // 具体的な処理は別メソッドに分ける
      // 真偽値で成功判定が返ってくる
      return this.setBlock(args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * エンティティを呼び出す処理のサンプル
   */
  private void example() {
    // 場所を決める
    Location location = player.getLocation();
    // ブロックを決める
    Material material = Material.GOLD_BLOCK;
    // その場所のブロックの種類を変更する
    location.getBlock().setType(material);
  }

  /**
   * ブロックを置き換える
   *
   * @param args
   * @return
   */
  private boolean setBlock(String[] args) {
    // 引数から特定の範囲を切り出して、新しい配列を作る
    // copyOfRange([0, 1, 2, 3, 4], 1, 4)の場合は [1,2,3] を取得する。終了値に注意
    String[] coordinates = Arrays.copyOfRange(args, 0, (2 + 1));
    Location location = CommandUtility.getLocation(player, coordinates);
    // エラーがあった場合はnullが返ってくるので終了させる
    if (location == null) {
      return false;
    }

    // ブロックを取得する
    Material material = CommandUtility.getMaterial(args[3]);
    if (material == null || !material.isBlock()) {
      return false;
    }

    // ブロックを置き換える
    location.getBlock().setType(material);

    // trueはコマンド成功
    return true;
  }

}
