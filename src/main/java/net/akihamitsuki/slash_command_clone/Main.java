package net.akihamitsuki.slash_command_clone;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * プラグイン全体を管理するクラス
 */
public class Main extends JavaPlugin {

  private static Main instance;

  /**
   * プラグインが有効(enable)になったときに起動するメソッド
   */
  @Override
  public void onEnable() {
    // インスタンスをクラス変数に残す
    Main.instance = this;

    // コマンドを登録する
    this.getCommand("bossbar-").setExecutor(new BossBarCommand());
    this.getCommand("clear-").setExecutor(new ClearCommand());
    this.getCommand("defaultgamemode-").setExecutor(new DefaultGameModeCommand());
    this.getCommand("effect-").setExecutor(new EffectCommand());
    this.getCommand("enchant-").setExecutor(new EnchantCommand());
    this.getCommand("gamemode-").setExecutor(new GameModeCommand());
    this.getCommand("gamerule-").setExecutor(new GameRuleCommand());
    this.getCommand("give-").setExecutor(new GiveCommand());
    this.getCommand("kill-").setExecutor(new KillCommand());
    this.getCommand("particle-").setExecutor(new ParticleCommand());
    this.getCommand("playsound-").setExecutor(new PlaySoundCommand());
    this.getCommand("setblock-").setExecutor(new SetblockCommand());
    this.getCommand("setworldspawn-").setExecutor(new SetWorldSpawnCommand());
    this.getCommand("spawnpoint-").setExecutor(new SpawnPointCommand());
    this.getCommand("stopsound-").setExecutor(new StopSoundCommand());
    this.getCommand("summon-").setExecutor(new SummonCommand());
    this.getCommand("teleport-").setExecutor(new TeleportCommand());
    this.getCommand("time-").setExecutor(new TimeCommand());
    this.getCommand("title-").setExecutor(new TitleCommand());
    this.getCommand("weather-").setExecutor(new WeatherCommand());
  }

  /**
   * Main(Plugin)インスタンスを取得する
   *
   * これにより、どこからでもMainインスタンスを取得できる
   * この一連の手法は「シングルトン(singleton)」という
   *
   * @return Mainインスタンス
   */
  public static Main getInstance() {
    return instance;
  }

}
