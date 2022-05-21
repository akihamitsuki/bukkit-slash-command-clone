package net.akihamitsuki.slash_command_clone;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;


public class KillCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    // 引数の数が1であれば
    if (args.length == 1) {
      return this.kill(sender, args);
    }

    return false;
  }

  /**
   * エンティティを倒す・消す
   *
   * @param sender 送信者
   * @param args コマンドからの引数
   * @return コマンド成功判定
   */
  private boolean kill(CommandSender sender, String[] args) {
    // 対象を取得する
    List<Entity> targets = TargetSelector.getTargets(sender, args[0]);

    // 対象ごとにループして処理をする
    for (Entity target: targets) {
      // そのエンティティが生きているかどうかで処理を変える
      if (target instanceof LivingEntity) {
        // 生きているならHPを0にする（アイテムを落とす）
        // 1行でキャストし、返還後のメソッドを呼び出している
        ((LivingEntity) target).setHealth(0);

        // 2行で書くと次のようになる
        // LivingEntity entity = (LivingEntity) target;
        // entity.setHealth(0);
      } else {
        // 無生物(アイテム・発射物)なら取り除く
        target.remove();
      }
    }

    return true;
  }

}
