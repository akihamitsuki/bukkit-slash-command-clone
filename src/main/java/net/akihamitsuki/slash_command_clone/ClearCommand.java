package net.akihamitsuki.slash_command_clone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ClearCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    if (!CommandUtility.isPlayer(sender)) {
      sender.sendMessage("このコマンドはコンソール画面からは使えません");
      // このコマンドは失敗である
      return false;
    }

    // コマンド送信者をプレイヤー型に変換する
    Player player = (Player) sender;

    // 引数の数が0であれば
    // TODO: 引数有りにも対応する
    // clear [<targets>] [<item>] [<maxCount>]
    if (args.length == 0) {
      this.clear(player);
      return true;
    }
    return false;
  }

  /**
   * 対象プレイヤーのインベントリを空にする
   *
   * @param player
   */
  private void clear(Player player) {
    // プレイヤーからインベントリ情報を取得する
    Inventory inventory = player.getInventory();
    // インベントリ内のデータをすべて削除する
    inventory.clear();

    // 当然1行でもできる
    // player.getInventory().clear();
  }

}
