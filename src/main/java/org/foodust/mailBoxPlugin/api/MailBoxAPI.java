package org.foodust.mailBoxPlugin.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.foodust.mailBoxPlugin.MailBoxPlugin;
import org.foodust.mailBoxPlugin.manager.MailBoxManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MailBoxAPI {
    private static MailBoxAPI instance;
    private final MailBoxPlugin plugin;
    private final MailBoxManager mailBoxManager;

    private MailBoxAPI(MailBoxPlugin plugin) {
        this.plugin = plugin;
        this.mailBoxManager = plugin.getMailBoxManager();
    }

    public static void init(MailBoxPlugin plugin) {
        if (instance == null) {
            instance = new MailBoxAPI(plugin);
        }
    }

    public static MailBoxAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MailBoxAPI가 초기화되지 않았습니다!");
        }
        return instance;
    }

    public boolean sendItem(UUID targetUUID, ItemStack item) {
        return sendItem(targetUUID, item, null);
    }

    public boolean sendItem(UUID targetUUID, ItemStack item, String senderName) {
        if (targetUUID == null || item == null) {
            return false;
        }
        return mailBoxManager.addItem(targetUUID, item, senderName);
    }

    public boolean sendItem(Player target, ItemStack item) {
        return sendItem(target.getUniqueId(), item, null);
    }

    public boolean sendItem(Player target, ItemStack item, String senderName) {
        return sendItem(target.getUniqueId(), item, senderName);
    }

    public boolean sendItems(UUID targetUUID, ItemStack[] items) {
        return sendItems(targetUUID, items, null);
    }

    public boolean sendItems(UUID targetUUID, ItemStack[] items, String senderName) {
        if (targetUUID == null || items == null || items.length == 0) {
            return false;
        }
        
        boolean success = true;
        for (ItemStack item : items) {
            if (item != null && !mailBoxManager.addItem(targetUUID, item, senderName)) {
                success = false;
            }
        }
        return success;
    }

    public CompletableFuture<Boolean> sendItemAsync(UUID targetUUID, ItemStack item) {
        return sendItemAsync(targetUUID, item, null);
    }

    public CompletableFuture<Boolean> sendItemAsync(UUID targetUUID, ItemStack item, String senderName) {
        return CompletableFuture.supplyAsync(() -> sendItem(targetUUID, item, senderName));
    }

    public boolean hasSpace(UUID playerUUID) {
        return mailBoxManager.hasSpace(playerUUID);
    }

    public int getMailCount(UUID playerUUID) {
        return mailBoxManager.getMailCount(playerUUID);
    }

    public boolean clearMailBox(UUID playerUUID) {
        return mailBoxManager.clearMailBox(playerUUID);
    }
}