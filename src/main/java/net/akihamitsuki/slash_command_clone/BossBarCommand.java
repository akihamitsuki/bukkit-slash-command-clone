package net.akihamitsuki.slash_command_clone;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * /bossbar add <id> <name>
 * /bossbar get <id> (max|players|value|visible)
 * /bossbar list
 * /bossbar remove <id>
 * /bossbar set id color (blue|green|pink|purple|red|white|yellow)
 * /bossbar set id max <max>
 * /bossbar set id name <name>
 * /bossbar set id players [<targets>]
 * /bossbar set id style (notched_6|notched_10|notched_12|notched_20|progress)
 * /bossbar set id value <value>
 * /bossbar set id visible <visible>
 *
 * ボスバーには次の2系統がある
 * BossBar: コマンドで操作できないがJavaでの操作が少し簡単
 * KeyedBossBar: コマンドでも操作できるが、そのための余分な操作が加わる
 * 作成後にコマンドで操作する必要がないなら「BossBar」クラスの方を使う
 * ここではコマンド操作に合わせるため「KeyedBossBar」のみを扱う
 *
 */
public class BossBarCommand implements CommandExecutor {

  CommandSender sender = null;

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    this.sender = sender;

    // 引数の数が正しければ
    if (args.length == 1 && args[0].equals("list")) {
      return this.listBossBar();
    }

    if (args.length == 2 && args[0].equals("remove")) {
      return this.removeBossBar(args);
    }

    if (args.length == 3) {
      if (args[0].equals("add")) {
        return this.addBossBar(args);
      }
    }

    if (args.length == 3) {
      if (args[0].equals("get")) {
        if (args[2].equals("max")) {
          Bukkit.getLogger().warning("このコマンドに対応する操作はありません。最大値は1.0で固定です。");
          return true;
        }
        if (args[2].equals("players")) {
          return this.getPlayers(args);
        }
        if (args[2].equals("value")) {
          return this.getValue(args);
        }
        if (args[2].equals("visible")) {
          return this.getVisible(args);
        }

        Bukkit.getLogger().warning("第3引数の値が正しくありません。");
        return true;
      }
    }

    if (args.length == 4) {
      if (args[0].equals("set")) {
        if (args[2].equals("color")) {
          return this.setColor(args);
        }
        if (args[2].equals("max")) {
          Bukkit.getLogger().warning("このコマンドに対応する操作はありません。最大値は1.0で固定です。");
          return true;
        }
        if (args[2].equals("name")) {
          return this.setName(args);
        }
        if (args[2].equals("players")) {
          return this.setPlayers(args);
        }
        if (args[2].equals("style")) {
          return this.setStyle(args);
        }
        if (args[2].equals("value")) {
          return this.setValue(args);
        }
        if (args[2].equals("visible")) {
          return this.setVisible(args);
        }

        Bukkit.getLogger().warning("第3引数の値が正しくありません。");
        return true;
      }
    }

    // 引数が適正な数でなければコマンド失敗
    Bukkit.getLogger().warning("引数の数が正しくありません。");

    return false;
  }

  private boolean addBossBar(String[] args) {
    NamespacedKey id = NamespacedKey.fromString(args[1]);
    String title = args[2];

    BarColor color = this.getBarColor(null);
    if (args.length > 3) {
      color = this.getBarColor(args[3]);
      if (color == null) {
        return false;
      }
    }

    BarStyle style = BarStyle.SOLID;
    if (args.length > 4) {
      style = BarStyle.valueOf(args[4].toUpperCase());
    }
    Bukkit.createBossBar(id, title, color, style);
    // この段階ではまだ表示されず、次に誰に表示するかを指定する必要がある
    // bossbar set <id> players [<targets>]

    return true;
  }

  private boolean removeBossBar(String[] args) {
    NamespacedKey name = NamespacedKey.fromString(args[1]);
    Bukkit.removeBossBar(name);

    return true;
  }

  private boolean listBossBar() {
    Iterator<KeyedBossBar> bossBars = Bukkit.getBossBars();
    while (bossBars.hasNext()) {
      KeyedBossBar bossBar = bossBars.next();
      sender.sendMessage(String.format("%s (%s)", bossBar.getKey(), bossBar.getTitle()));
    }

    return true;
  }

  // getter

  private boolean getPlayers(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    for (Player player: bossBar.getPlayers()) {
      sender.sendMessage(player.getName());
    }

    return true;
  }

  private boolean getValue(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    sender.sendMessage(String.valueOf(bossBar.getProgress()));

    return true;
  }

  private boolean getVisible(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    sender.sendMessage(String.valueOf(bossBar.isVisible()));

    return true;
  }

  private KeyedBossBar getBossBar(String id) {
    KeyedBossBar bossBar = Bukkit.getBossBar(NamespacedKey.fromString(id));
    if (bossBar == null) {
      Bukkit.getLogger().warning("IDが正しくありません。");
    }
    return bossBar;
  }

  private BarColor getBarColor(String name) {
    if (name == null) {
      return BarColor.WHITE;
    }

    try {
      return BarColor.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("色名が正しくありません。");
      return null;
    }
  }

  private BarStyle getBarStyle(String name) {
    if (name == null) {
      return BarStyle.SOLID;
    }

    try {
      return BarStyle.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("スタイル名が正しくありません。");
      return null;
    }
  }

  // setter

  private boolean setColor(String[] args) {
    BarColor color = this.getBarColor(args[2]);
    if (color == null) {
      return false;
    }

    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    bossBar.setColor(color);

    return true;
  }

  private boolean setName(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    bossBar.setTitle(args[2]);

    return true;
  }

  private boolean setPlayers(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }

    List<Entity> targets = TargetSelector.getTargets(this.sender, args[2]);
    // 対象ごとにループ
    for (Entity target: targets) {
      // プレイヤーでなければ次のループへ
      if (!CommandUtility.isPlayer(target)) {
        continue;
      }

      bossBar.addPlayer((Player) target);
    }

    return true;
  }

  private boolean setStyle(String[] args) {
    BarStyle style = this.getBarStyle(args[2]);
    if (style == null) {
      return false;
    }

    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    bossBar.setStyle(style);

    return true;
  }

  private boolean setValue(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }
    try {
      Float value = Float.parseFloat(args[2]);
      bossBar.setProgress(value);

      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean setVisible(String[] args) {
    KeyedBossBar bossBar = this.getBossBar(args[1]);
    if (bossBar == null) {
      return false;
    }

    if (args[2].equalsIgnoreCase("true")) {
      bossBar.setVisible(true);
    } else if (args[2].equalsIgnoreCase("false")) {
      bossBar.setVisible(false);
    }

    return false;
  }

}
