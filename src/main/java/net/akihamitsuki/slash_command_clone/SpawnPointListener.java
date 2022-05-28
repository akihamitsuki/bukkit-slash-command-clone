package net.akihamitsuki.slash_command_clone;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;


public class SpawnPointListener implements Listener {

  public HashMap<UUID, Location> respawnPoints = new HashMap<>();

  /**
   * プレイヤーがリスポーンするときのイベント
   *
   * @param event
   */
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    // 該当プレイヤーのリスポーン座標を取得する。無ければnull
    Location spawnPoint = this.respawnPoints.get(event.getPlayer().getUniqueId());
    // 座標が設定されていれば、リスポーン座標を変更
    if (spawnPoint != null) {
      event.setRespawnLocation(spawnPoint);
    }
  }

}
