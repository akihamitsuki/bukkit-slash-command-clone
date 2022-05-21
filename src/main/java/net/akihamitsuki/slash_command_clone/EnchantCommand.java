package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * /enchant: エンチャントする
 */
public class EnchantCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 2 || args.length == 3) {
      return this.enchant(sender, args);
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
  private boolean enchant(CommandSender sender, String[] args) {
    List<Entity> entities = TargetSelector.getTargets(sender, args[0]);

    Enchantment enchantment = this.getEnchantment(args[1]);
    if (enchantment == null) {
      // エラーログを表示
      Bukkit.getLogger().warning("エンチャント名が正しくありません。");
      // コマンド失敗
      return false;
    }

    // レベル: 初期値は1
    Integer level = 1;
    // 引数にレベルが指定されていれば取得処理に入る
    if (args.length >= 2) {
      // レベルを取得
      level = this.getLevel(args[2], enchantment);
      // nullなら取得失敗なのでエラーとして終了させる
      if (level == null) {
        // エラーログを表示
        Bukkit.getLogger().warning("レベルが正しくありません。");
        // コマンド失敗
        return false;
      }
    }

    // 対象ごとにループ
    for (Entity entity: entities) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(entity)) {
        continue;
      }

      // 対象の利き手に持っているアイテムを取得する
      ItemStack item = ((Player) entity).getInventory().getItemInMainHand();
      // そのアイテムにエンチャントできなければ次のループへ
      if (!enchantment.canEnchantItem(item)) {
        continue;
      }

      // そのアイテムにエンチャントをする
      item.addEnchantment(enchantment, level);
    }

    return true;
  }

  /**
   * 引数からエンチャントの種類を取得する
   *
   * @param name エンチャント名
   * @return エンチャントの種類
   */
  private Enchantment getEnchantment(String name) {
    // 例外処理
    // ユーザーが入力する場合は、常に正しい値になるとは限らない
    try {
      // この処理の時に問題が起きれば catch に移る
      return Enchantment.getByKey(NamespacedKey.fromString(name));
    } catch (IllegalArgumentException e) {
      // 全体の処理はここで中断しないので、nullを返して続ける
      return null;
    }
  }

  /**
   * 引数からレベルを取得する
   *
   * @param arg
   * @param enchantment
   * @return
   */
  private Integer getLevel(String arg, Enchantment enchantment) {
    // レベルの初期値
    Integer level = null;

    // ユーザーからの入力は適正な値とは限らないので例外処理をする
    try {
      // 文字列型(String)を整数型(Integer)に変換する
      level = Integer.parseInt(arg);
    } catch (NumberFormatException e) {
      // 例外が起きたらnullを返す
      return null;
    }

    // 取得した数字がそのエンチャントの適正範囲外ならnullを返す
    if (level < enchantment.getStartLevel() || level > enchantment.getMaxLevel()) {
      return null;
    }

    // ここまでで問題なければ（おそらく）正しいので、この値をレベルとして返す
    return level;
  }

}
