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
    this.getCommand("effect-").setExecutor(new EffectCommand());
    this.getCommand("enchant-").setExecutor(new EnchantCommand());
    this.getCommand("give-").setExecutor(new GiveCommand());
    this.getCommand("kill-").setExecutor(new KillCommand());
    this.getCommand("particle-").setExecutor(new ParticleCommand());
    this.getCommand("setblock-").setExecutor(new SetblockCommand());
    this.getCommand("summon-").setExecutor(new SummonCommand());
    this.getCommand("teleport-").setExecutor(new TeleportCommand());
    this.getCommand("time-").setExecutor(new TimeCommand());
    this.getCommand("weather-").setExecutor(new WeatherCommand());
  }

}
