package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * /effect give <targets> <effect> [<seconds>] [<amplifier>] [<hideParticles>]
 * /effect clear [<targets>] [<effect>]
 */
public class EffectCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      return false;
    }

    // giveとclearで処理が大きく分かれる
    if (args[0].equals("give")) {
      if (args.length >= 3 || args.length <= 6) {
        return this.givePotionEffect(sender, args);
      }
    } else if (args[0].equals("clear")) {
      if (args.length <= 3) {
        return this.clearPotionEffect(sender, args);
      }
    }

    Bukkit.getLogger().warning("引数の数が正しくありません。");
    return false;
  }

  /**
   * エフェクトを与える
   *
   * @param sender
   * @param args
   * @return
   */
  private boolean givePotionEffect(CommandSender sender, String[] args) {
    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(sender, args[1]);

    // 効果を取得する
    PotionEffectType effectType = this.getPotionEffectType(args[2]);
    if (effectType == null) {
      return false;
    }

    // 秒数
    Integer seconds = 30;
    if (args.length >= 4) {
      seconds = this.getSeconds(args[3]);
      if (seconds == null) {
        return false;
      }
    }

    // 効果の強さ
    Integer amplifier = 0;
    if (args.length >= 5) {
      amplifier = this.getAmplifier(args[4]);
      if (amplifier == null) {
        return false;
      }
    }

    // パーティクルを隠すか
    Boolean hideParticles = false;
    if (args.length >= 6) {
      hideParticles = this.getHideParticles(args[5]);
      if (hideParticles == null) {
        return false;
      }
    }

    // ポーション効果を作成する（まだ適用されない）
    PotionEffect effect = new PotionEffect(
      effectType,  // 効果の種類
      seconds * 20,  // 効果時間（ティック）
      amplifier,  // 効果の強さ(レベルではないので0から効果が現れる)
      hideParticles  // パーティクルの表示
    );

    // 対象ごとにループ
    for (Entity target: targets) {
      if (target instanceof LivingEntity) {
        // 作成したポーション効果(potion effect)を対象に追加(add)する
        ((LivingEntity) target).addPotionEffect(effect);
      }
    }

    return true;
  }

  /**
   * エフェクトを消す
   *
   * @param sender
   * @param args
   * @return
   */
  private boolean clearPotionEffect(CommandSender sender, String[] args) {
    // 対象エンティティを取得
    List<Entity> targets = TargetSelector.getTargets(sender, "@s");
    if (args.length >= 2) {
      targets = TargetSelector.getTargets(sender, args[1]);
    }

    // 効果を取得する
    PotionEffectType effectType = null;
    if (args.length >= 3) {
      effectType = this.getPotionEffectType(args[2]);
      if (effectType == null) {
        return false;
      }
    }

    // 対象ごとにループ
    for (Entity target: targets) {
      // 生きているエンティティでなければ次へ
      if (!CommandUtility.isLivingEntity(target)) {
        continue;
      }

      LivingEntity entity = (LivingEntity) target;
      // 効果が指定されていなければ
      if (effectType == null) {
        // そのエンティティが持っているすべての効果を取得して繰り返し
        for (PotionEffect effect: entity.getActivePotionEffects()) {
          // その効果を消す
          entity.removePotionEffect(effect.getType());
        }
      } else {
        // 指定された効果を消す
        entity.removePotionEffect(effectType);
      }
    }

    return true;
  }

  /**
   * 引数から効果の種類を取得する
   *
   * @param name 効果名
   * @return 効果の種類
   */
  private PotionEffectType getPotionEffectType(String name) {
    // 例外処理
    // ユーザーが入力する場合は、常に正しい値になるとは限らない
    try {
      // この処理の時に問題が起きれば catch に移る
      return PotionEffectType.getByKey(NamespacedKey.fromString(name));
    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("効果名が正しくありません。");
      // 全体の処理はここで中断しないので、nullを返して続ける
      return null;
    }
  }

  /**
   * 引数から秒数を取得する
   *
   * @param arg
   * @return
   */
  private Integer getSeconds(String arg) {
    // レベルの初期値
    Integer seconds = null;

    // ユーザーからの入力は適正な値とは限らないので例外処理をする
    try {
      // 文字列型(String)を整数型(Integer)に変換する
      seconds = Integer.parseInt(arg);
    } catch (NumberFormatException e) {
      // エラーログを表示
      Bukkit.getLogger().warning("秒数の値が正しくありません。");
      // 例外が起きたらnullを返す
      return null;
    }

    if (seconds < 0) {
      // エラーログを表示
      Bukkit.getLogger().warning("秒数の値は0以上でなくてはいけません。");
      return null;
    }

    // ここまでで問題なければ（おそらく）正しいので、この値を返す
    return seconds;
  }

  /**
   * 引数から効果の強さを取得する
   *
   * @param arg
   * @return
   */
  private Integer getAmplifier(String arg) {
    // レベルの初期値
    Integer seconds = null;

    // ユーザーからの入力は適正な値とは限らないので例外処理をする
    try {
      // 文字列型(String)を整数型(Integer)に変換する
      seconds = Integer.parseInt(arg);
    } catch (NumberFormatException e) {
      // エラーログを表示
      Bukkit.getLogger().warning("強さの値が正しくありません。");
      // 例外が起きたらnullを返す
      return null;
    }

    if (seconds < 0) {
      // エラーログを表示
      Bukkit.getLogger().warning("強さの値は0以上でなくてはいけません。");
      return null;
    }

    if (seconds > 255) {
      // エラーログを表示
      Bukkit.getLogger().warning("強さの値は255以下でなくてはいけません。");
      return null;
    }

    // ここまでで問題なければ（おそらく）正しいので、この値を返す
    return seconds;
  }

  /**
   * 引数からパーティクル表示の可否を取得する
   *
   * @param arg
   * @return
   */
  private Boolean getHideParticles(String arg) {
    // 文字列型(String)を整数型(Integer)に変換する
    // この場合、文字列が「true」以外はすべてfalseになる。スペルが間違っていてもエラーではなくfalse。
    return Boolean.parseBoolean(arg);
  }

}
