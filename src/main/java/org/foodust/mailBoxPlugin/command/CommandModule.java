package org.foodust.mailBoxPlugin.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.foodust.mailBoxPlugin.MailBoxPlugin;

import java.util.Arrays;

public class CommandModule {

    private final MailBoxPlugin plugin;

    public CommandModule(MailBoxPlugin plugin) {
        this.plugin = plugin;
    }

    public void commandStart(CommandSender sender, String[] data) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c[우체통] 이 명령어는 플레이어만 사용할 수 있습니다!");
            return;
        }
        
        plugin.getMailBoxGUIListener().openMailBox(player);
    }

    public void commandReload(CommandSender sender, String[] data) {
        
        plugin.getMailBoxGUIListener().closeAll();
        
        plugin.getMailBoxManager().saveAll();
        
        plugin.reloadConfig();
        
        sender.sendMessage("§a[우체통] 플러그인이 리로드되었습니다!");
    }
}