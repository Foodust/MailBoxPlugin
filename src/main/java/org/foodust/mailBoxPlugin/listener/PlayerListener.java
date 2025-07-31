package org.foodust.mailBoxPlugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.foodust.mailBoxPlugin.MailBoxPlugin;
import org.foodust.mailBoxPlugin.manager.MailBoxManager;

public class PlayerListener implements Listener {
    private final MailBoxPlugin plugin;
    private final MailBoxManager mailBoxManager;

    public PlayerListener(MailBoxPlugin plugin) {
        this.plugin = plugin;
        this.mailBoxManager = plugin.getMailBoxManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int mailCount = mailBoxManager.getMailCount(player.getUniqueId());
        
        if (mailCount > 0) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage("§a[우체통] §f새로운 우편이 " + mailCount + "개 있습니다!");
            }, 40L);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        mailBoxManager.unloadMailBox(player.getUniqueId());
    }
}