package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * /give <target> <item> [<count>]
 */
public class GiveCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 2 || args.length == 3) {
      return this.give(sender, args);
    }
    return false;
  }

  /**
   * エンチャント
   *
   * @param sender
   * @param args
   * @return
   */
  private boolean give(CommandSender sender, String[] args) {
    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(sender, args[0]);

    // アイテムを取得する
    Material material = this.getMaterial(args[1]);
    if (material == null) {
      return false;
    }

    // 個数
    Integer count = 1;
    // 引数にレベルが指定されていれば取得処理に入る
    if (args.length >= 3) {
      // レベルを取得
      count = this.getCount(args[2], material);
      if (count == null) {
        return false;
      }
    }

    // TODO: アイテムのスタック上限を設定する
    // 1個しかスタックできないアイテムでも、何も設定しなければ64個まで持てる（エラーにはならない）
    // material.getMaxStackSize();

    // アイテム名と個数から、アイテムスタック（まとまり）を作成する
    ItemStack itemStack = new ItemStack(material, count);

    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      // 対象のインベントリにアイテムを加える
      ((Player) target).getInventory().addItem(itemStack);
    }

    return true;
  }

  /**
   * 引数からアイテムの種類を取得する
   *
   * @param name アイテム名
   * @return アイテムの種類
   */
  private Material getMaterial(String name) {
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
   * 引数からレベルを取得する
   *
   * @param arg
   * @param material
   * @return
   */
  private Integer getCount(String arg, Material material) {
    // レベルの初期値
    Integer count = null;

    // ユーザーからの入力は適正な値とは限らないので例外処理をする
    try {
      // 文字列型(String)を整数型(Integer)に変換する
      count = Integer.parseInt(arg);
    } catch (NumberFormatException e) {
      // エラーログを表示
      Bukkit.getLogger().warning("個数の値が正しくありません。");
      // 例外が起きたらnullを返す
      return null;
    }

    if (count < 1) {
      // エラーログを表示
      Bukkit.getLogger().warning("個数の値は1以上でなくてはいけません。");
      return null;
    }

    // ここまでで問題なければ（おそらく）正しいので、この値を返す
    return count;
  }

}
