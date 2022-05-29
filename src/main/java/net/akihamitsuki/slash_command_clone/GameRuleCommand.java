package net.akihamitsuki.slash_command_clone;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * /gamerule <rule name> [<value>]
 */
public class GameRuleCommand implements CommandExecutor {

  CommandSender sender = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    this.sender = sender;

    if (args.length == 1) {
      return this.getGameRule(args);
    }

    if (args.length == 2) {
      return this.setGameRule(args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * ゲームルールの状態を取得
   */
  private boolean getGameRule(String[] args) {
    World world = Bukkit.getWorld("world");
    var rule = GameRule.getByName(args[0]);
    String value = world.getGameRuleValue(rule).toString();
    sender.sendMessage(value);

    return true;
  }

  /**
   * ゲームルールの状態を設定
   */
  private boolean setGameRule(String[] args) {
    World world = Bukkit.getWorld("world");
    GameRule<Boolean> rule = GameRule.DO_DAYLIGHT_CYCLE;
    world.setGameRule(rule, false);
    sender.sendMessage("設定しました。");

    // TODO: 型が不定なので、引数からうまく設定する方法がわからない
    // var rule = GameRule.getByName(args[0]);
    // world.setGameRule(rule, args[1]);

    return true;
  }

}
