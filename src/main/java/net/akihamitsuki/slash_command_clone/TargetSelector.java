package net.akihamitsuki.slash_command_clone;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TargetSelector {

  public static List<Entity> getTargets(CommandSender sender, String target) {
    List<Entity> entities = new ArrayList<Entity>();
    World world = getWorld(sender);

    if (target.charAt(0) == '@') {
      if (target.equals("@s")) {
        if (CommandUtility.isPlayer(sender)) {
          entities.add((Entity) sender);
        }
      } else if (target.equals("@a")) {
        List<Player> players = world.getPlayers();
        for (Player player: players) {
          entities.add((Entity) player);
        }
      } else if (target.equals("@e")) {
        entities = world.getEntities();
      }
    } else {
      // ユーザー名として処理
      // 存在しなければnullが返ってくる
      Player player = Bukkit.getPlayer(target);
      if (player != null) {
        entities.add((Entity) player);
      }
    }

    return entities;
  }

  private static World getWorld(CommandSender sender) {
    if (CommandUtility.isPlayer(sender)) {
      return ((Player) sender).getWorld();
    }

    return Bukkit.getWorld("world");
  }

}
