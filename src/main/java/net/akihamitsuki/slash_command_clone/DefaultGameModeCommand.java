package net.akihamitsuki.slash_command_clone;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * /defaultgamemode <mode>
 */
public class DefaultGameModeCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // 引数の数が1であれば
    if (args.length == 1) {
      return this.defaultgamemode(args);
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  /**
   * ゲームモードの初期値を設定する
   */
  private boolean defaultgamemode(String[] args) {
    // ゲームモードを引数から取得する
    GameMode mode = this.getGameMode(args[0]);
    if (mode == null) {
      return false;
    }

    // ゲームモードの初期値を設定する
    Bukkit.setDefaultGameMode(mode);
    Bukkit.getLogger().info(String.format("ゲームモードの初期値を %s に設定しました。", mode.toString()));

    return true;
  }

  /**
   * ゲームモードを引数の値から取得する
   *
   * @param arg
   * @return ゲームモード
   */
  private GameMode getGameMode(String arg) {
    try {
      return GameMode.valueOf(arg.toUpperCase());
    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("ゲームモードが正しくありません。");
      return null;
    }
  }

}
