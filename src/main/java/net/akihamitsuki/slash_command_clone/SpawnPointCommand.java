package net.akihamitsuki.slash_command_clone;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


/**
 * /spawnpoint [<targets>] [<pos>]
 */
public class SpawnPointCommand implements CommandExecutor {

  private Player player = null;
  private SpawnPointListener listener = null;


  /**
   * コンストラクタ（最初に実行される）
   */
  public SpawnPointCommand() {
    // リスポーン時のイベントを設定する
    this.listener = new SpawnPointListener();
    Main main = Main.getInstance();
    main.getServer().getPluginManager().registerEvents(this.listener, main);
  }


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

    if (args.length == 0 || args.length == 1 || args.length == 4) {
      return this.spawnPoint(player, args);
    }

    Bukkit.getLogger().warning("引数の数が正しくありません。");
    return false;
  }

  /**
   * 個別のスポーンポイントを設定する
   *
   * @param player 送信者
   * @param args コマンドからの引数
   * @return コマンド成功判定
   */
  private boolean spawnPoint(Player player, String[] args) {

    Location location = player.getLocation();
    if (args.length == 4) {
      String[] coordinates = Arrays.copyOfRange(args, 1, (3 + 1));
      location = CommandUtility.getLocation(player, coordinates);
      // エラーがあった場合はnullが返ってくるので終了させる
      if (location == null) {
        Bukkit.getLogger().warning("座標の値が正しくありません。");
        return false;
      }
    }

    List<Entity> targets = TargetSelector.getTargets(player, "@s");
    if (args.length != 0) {
      targets = TargetSelector.getTargets(player, args[0]);
    }
    for (Entity target: targets) {
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      // リスポーンイベントで使用するために、プレイヤーと座標の組み合わせを保存する
      this.listener.respawnPoints.put(target.getUniqueId(), location);

      // player.setBedSpawnLocation(location);
      // こっちのコマンドはベッドと合わせて使う
    }

    Bukkit.getLogger().info("スポーン位置を設定しました。");
    return true;
  }

}
