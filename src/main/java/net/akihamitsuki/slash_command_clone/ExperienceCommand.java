package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * /experience add <targets> <amount> [levels|points]
 * /experience set <targets> <amount> [levels|points]
 * /experience query <targets> (levels|points)
 */
public class ExperienceCommand implements CommandExecutor {

  Player player = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!CommandUtility.isPlayer(sender)) {
      sender.sendMessage("このコマンドはコンソール画面からは使えません");
      // このコマンドは失敗である
      return false;
    }

    // コマンド送信者をプレイヤー型に変換する
    player = (Player) sender;

    if (args.length == 3 || args.length == 4) {
      if (args[0].equals("add")) {
        return this.addExperience(args);
      }

      if (args[0].equals("set")) {
        return this.setExperience(args);
      }
    }

    if (args.length == 3) {
      if (args[0].equals("query")) {
        return this.queryExperience(args);
      }
    }

    sender.sendMessage("引数の数が正しくありません。");

    return false;
  }

  /**
   * 経験値を追加する
   *
   * @param args
   * @return
   */
  private boolean addExperience(String[] args) {
    // 設定先がレベルかどうか
    boolean isLevel = false;
    if (args.length == 4 && args[3].equalsIgnoreCase("levels")) {
      isLevel = true;
    }

    // 数量
    Integer amount = 0;
    try {
      amount = Integer.parseInt(args[2]);
    } catch (NumberFormatException e) {
      return false;
    }

    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(player, args[1]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      if (isLevel) {
        // 対象にレベルを加算する
        ((Player) target).giveExpLevels(amount);
      } else {
        // 対象に経験値を加算する
        ((Player) target).giveExp(amount);
      }
    }

    return true;
  }

  /**
   * 経験値を設定する
   *
   * @param args
   * @return
   */
  private boolean setExperience(String[] args) {
    // 設定先がレベルかどうか
    boolean isLevel = false;
    if (args.length == 4 && args[3].equalsIgnoreCase("levels")) {
      isLevel = true;
    }

    // 数量
    Integer amount = 0;
    try {
      amount = Integer.parseInt(args[2]);
      if (amount < 0) {
        player.sendMessage("負の数は指定できません。");
        return false;
      }
    } catch (NumberFormatException e) {
      return false;
    }

    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(player, args[1]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      if (isLevel) {
        // 対象にレベルを加算する
        ((Player) target).setLevel(amount);
      } else {
        // 対象に経験値を加算する（一旦そのレベル内での経験値を0にしてから加算する）
        ((Player) target).setExp(0.0f);
        ((Player) target).giveExp(amount);
      }
    }

    return true;
  }

  /**
   * 経験値の状態を表示する
   *
   * @param args
   * @return
   */
  private boolean queryExperience(String[] args) {
    // 設定先がレベルかどうか
    boolean isLevel = false;
    if (args[2].equalsIgnoreCase("levels")) {
      isLevel = true;
    } else if (args[2].equalsIgnoreCase("points")) {
      isLevel = false;
    } else {
      player.sendMessage("正しい値を設定してください");
      return false;
    }

    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(player, args[1]);
    // 対象ごとにループ
    // NOTE: 元のコマンドは単体にしか使えない
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }
      Player targetPlayer = (Player) target;

      String message = null;
      if (isLevel) {
        // 対象のレベルを取得する
        Integer level = targetPlayer.getLevel();
        message = String.valueOf(level);
      } else {
        // 対象の経験値を取得する
        // ここで取得されるのはそのレベルの最大値を1.0とした比率
        Float exp = targetPlayer.getExp();
        // そのレベル内での総経験値量を取得
        Integer expToLevel = targetPlayer.getExpToLevel();
        // この二つをかけ合わせると、そのレベル内での経験値量が取得できる
        message = String.valueOf(Math.round(exp * expToLevel));
      }

      // 使用者に対して表示する
      player.sendMessage(targetPlayer.getName() + ": " + message);
    }

    return true;
  }

}
