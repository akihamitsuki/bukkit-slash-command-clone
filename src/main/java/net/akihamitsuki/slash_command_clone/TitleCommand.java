package net.akihamitsuki.slash_command_clone;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

/**
 * /title <targets> (clear|reset)
 * /title <targets> (title|subtitle|actionbar) <title>
 * /title <targets> times <fadeIn> <stay> <fadeOut>
 */
public class TitleCommand implements CommandExecutor {

  // 1ティックあたりのミリ秒
  // (second / tick) * (millisecond / second) = millisecond / tick
  private Double MILLISECOND_PER_TICK = 0.05 * 1000;

  private HashMap<UUID, Component> subtitleComponents = new HashMap<>();
  private HashMap<UUID, List<Integer>> timeMap = new HashMap<>();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!CommandUtility.isPlayer(sender)) {
      Bukkit.getLogger().warning("このコマンドはコンソール画面からは使えません。");
      // このコマンドは失敗である
      return false;
    }

    // コマンド送信者をプレイヤー型に変換する
    Player player = (Player) sender;

    if (args.length == 0) {
      this.example(player);
      return true;
    }

    if (args.length == 2) {
      if (args[1].equals("clear")) {
        this.clear(player, args);
        return true;
      }
      if (args[1].equals("reset")) {
        this.reset(player);
        return true;
      }
    }

    if (args.length == 3) {
      if (args[1].equals("title")) {
        this.title(player, args);
        return true;
      }
      if (args[1].equals("subtitle")) {
        this.setSubtitle(player, args);
        return true;
      }
      if (args[1].equals("actionbar")) {
        this.actionbar(player, args);
        return true;
      }
    }

    if (args.length == 5) {
      if (args[1].equals("times")) {
        return this.setTime(player, args);
      }
    }

    Bukkit.getLogger().warning("引数の数が正しくありません。");
    return false;
  }

  /**
   * 使用例
   *
   * @param player
   */
  private void example(Player player) {
    // 文字部分はComponent型で作成する
    Component titleComponent = Component.text("タイトルを表示する");
    Component subtitleComponent = Component.text("色も付けられる", NamedTextColor.GOLD);

    // 時間の設定。小数秒に対応するためミリ秒で計算する
    Duration fadeIn = Duration.ofMillis(this.convertTickToMillisecond(10)); // 0.5秒
    Duration stay = Duration.ofMillis(this.convertTickToMillisecond(70));  // 3.5秒
    Duration fadeOut = Duration.ofMillis(this.convertTickToMillisecond(20));  // 1.0秒
    Times times = Title.Times.times(fadeIn, stay, fadeOut);

    // 以上の情報からTitleを作成する
    Title title = Title.title(titleComponent, subtitleComponent, times);
    // 対象プレイヤーに表示する
    player.showTitle(title);
  }

  /**
   * ティックをミリ秒に変換する
   *
   * @param tick
   * @return ミリ秒
   */
  private Long convertTickToMillisecond(Integer tick) {
    // tick * (millisecond / tick) = millisecond
    return (long) (tick * this.MILLISECOND_PER_TICK);
  }

  /**
   * 字幕表示
   *
   * @param player
   * @param args コマンドの引数
   */
  private void title(Player player, String[] args) {
    // タイトル用の文章を作成
    Component titleComponent = Component.text(args[2]);
    // サブタイトルを取得する
    Component subtitleComponent = this.getSubtitle(player);
    // 表示時間を取得する
    Times times = this.getTime(player);
    // 表示するタイトルを作成
    Title title = Title.title(titleComponent, subtitleComponent, times);

    // 対象者を取得
    List<Entity> targets = TargetSelector.getTargets(player, args[0]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }
      // 表示中のタイトルを消す
      ((Player) target).showTitle(title);
    }
  }

  /**
   * アクションバーに表示する
   *
   * @param player
   * @param args コマンドの引数
   */
  private void actionbar(Player player, String[] args) {
    Component titleComponent = Component.text(args[2]);

    // 対象者を取得
    List<Entity> targets = TargetSelector.getTargets(player, args[0]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }
      // 表示中のタイトルを消す
      ((Player) target).sendActionBar(titleComponent);
    }
  }

  /**
   * 字幕を取得する
   *
   * @param player 使用者
   */
  private Component getSubtitle(Player player) {
    Component subtitle = subtitleComponents.get(player.getUniqueId());
    subtitleComponents.remove(player.getUniqueId());
    if (subtitle == null) {
      // nullではなく何もないコンポーネントを返す
      return Component.text("");
    }
    return subtitle;
  }

  /**
   * 字幕を設定する
   *
   * @param player 使用者
   * @param args
   */
  private void setSubtitle(Player player, String[] args) {
    subtitleComponents.put(player.getUniqueId(), Component.text(args[2]));
  }

  /**
   * 表示時間を設定する
   *
   * @param player 使用者
   */
  private Times getTime(Player player) {
    // 初期値
    // 透明から不透明になるまでの時間
    Duration fadeIn = Duration.ofMillis(this.convertTickToMillisecond(10));
    // 不透明のまま表示し続ける時間
    Duration stay = Duration.ofMillis(this.convertTickToMillisecond(70));
    // 不透明から透明になるまでの時間
    Duration fadeOut = Duration.ofMillis(this.convertTickToMillisecond(20));

    // 設定した値があれば上書き
    List<Integer> timeList = timeMap.get(player.getUniqueId());
    if (timeList != null) {
      fadeIn = Duration.ofMillis(this.convertTickToMillisecond(timeList.get(0)));
      stay = Duration.ofMillis(this.convertTickToMillisecond(timeList.get(1)));
      fadeOut = Duration.ofMillis(this.convertTickToMillisecond(timeList.get(2)));
    }

    return Title.Times.times(fadeIn, stay, fadeOut);
  }

  /**
   * 表示時間を設定する
   *
   * @param player 使用者
   * @param args 設定する表示時間
   */
  private boolean setTime(Player player, String[] args) {
    List<Integer> times = new ArrayList<Integer>();
    try {
      times.add(Integer.parseInt(args[2]));
      times.add(Integer.parseInt(args[3]));
      times.add(Integer.parseInt(args[4]));
      timeMap.put(player.getUniqueId(), times);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * 表示中のタイトルを消す
   *
   * @param player 使用者
   */
  private void clear(Player player, String[] args) {
    // 対象者を取得
    List<Entity> targets = TargetSelector.getTargets(player, args[0]);

    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      // 表示中のタイトルを消す
      ((Player) target).clearTitle();
    }
  }

  /**
   * 設定を削除する
   *
   * @param player 使用者
   */
  private void reset(Player player) {
    subtitleComponents.remove(player.getUniqueId());
    timeMap.remove(player.getUniqueId());
  }

}
