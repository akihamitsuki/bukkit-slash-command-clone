package net.akihamitsuki.slash_command_clone;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * プラグイン全体を管理するクラス
 */
public class Main extends JavaPlugin {

  /**
   * プラグインが有効(enable)になったときに起動するメソッド
   */
  @Override
  public void onEnable() {
    // コマンドを登録する
    this.getCommand("clear-").setExecutor(new ClearCommand());
    this.getCommand("summon-").setExecutor(new SummonCommand());
  }

}
