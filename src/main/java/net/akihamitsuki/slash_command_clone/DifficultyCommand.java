package net.akihamitsuki.slash_command_clone;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * /difficulty [easy|hard|normal|peaceful]
 */
public class DifficultyCommand implements CommandExecutor {

  CommandSender sender = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    this.sender = sender;

    if (args.length == 0) {
      return this.getDifficulty();
    }

    if (args.length == 1) {
      return this.setDifficulty(args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * ゲーム難易度を取得
   */
  private boolean getDifficulty() {
    World world = Bukkit.getWorld("world");
    Difficulty difficulty = world.getDifficulty();
    sender.sendMessage(difficulty.name());

    return true;
  }

  /**
   * ゲーム難易度を設定
   */
  private boolean setDifficulty(String[] args) {
    World world = Bukkit.getWorld("world");
    Difficulty difficulty = null;
    try {
      difficulty = Difficulty.valueOf(args[0].toUpperCase());
    } catch (IllegalArgumentException e) {
      sender.sendMessage("難易度の値が正しくありません。");
      return false;
    }
    world.setDifficulty(difficulty);
    sender.sendMessage("設定しました。");

    return true;
  }

}
