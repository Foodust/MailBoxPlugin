package org.foodust.mailBoxPlugin.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.foodust.mailBoxPlugin.MailBoxPlugin;
import org.foodust.mailBoxPlugin.data.Mail;
import org.foodust.mailBoxPlugin.data.PlayerMailBox;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MailBoxManager {
    private final MailBoxPlugin plugin;
    private final Map<UUID, PlayerMailBox> mailBoxes;
    private final File mailFolder;
    private final int maxMailSize;

    public MailBoxManager(MailBoxPlugin plugin) {
        this.plugin = plugin;
        this.mailBoxes = new ConcurrentHashMap<>();
        this.mailFolder = new File(plugin.getDataFolder(), "mail");
        this.maxMailSize = plugin.getConfig().getInt("max-mail-size", 54);
        
        if (!mailFolder.exists()) {
            mailFolder.mkdirs();
        }
    }

    public PlayerMailBox getMailBox(UUID playerUUID) {
        return mailBoxes.computeIfAbsent(playerUUID, this::loadMailBox);
    }

    private PlayerMailBox loadMailBox(UUID playerUUID) {
        File file = new File(mailFolder, playerUUID.toString() + ".yml");
        PlayerMailBox mailBox = new PlayerMailBox(playerUUID);
        
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<Map<?, ?>> mailList = config.getMapList("mails");
            
            for (Map<?, ?> mailData : mailList) {
                Mail mail = Mail.deserialize((Map<String, Object>) mailData);
                if (mail != null) {
                    mailBox.addMail(mail);
                }
            }
        }
        
        return mailBox;
    }

    public void saveMailBox(UUID playerUUID) {
        PlayerMailBox mailBox = mailBoxes.get(playerUUID);
        if (mailBox == null) return;
        
        File file = new File(mailFolder, playerUUID.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        
        List<Map<String, Object>> mailList = new ArrayList<>();
        for (Mail mail : mailBox.getMails()) {
            mailList.add(mail.serialize());
        }
        
        config.set("mails", mailList);
        
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save mailbox for " + playerUUID);
        }
    }

    public boolean addItem(UUID playerUUID, ItemStack item, String senderName) {
        PlayerMailBox mailBox = getMailBox(playerUUID);
        
        if (mailBox.getMails().size() >= maxMailSize) {
            return false;
        }
        
        Mail mail = new Mail(item, senderName, System.currentTimeMillis());
        mailBox.addMail(mail);
        saveMailBox(playerUUID);
        
        return true;
    }

    public boolean hasSpace(UUID playerUUID) {
        PlayerMailBox mailBox = getMailBox(playerUUID);
        return mailBox.getMails().size() < maxMailSize;
    }

    public int getMailCount(UUID playerUUID) {
        PlayerMailBox mailBox = getMailBox(playerUUID);
        return mailBox.getMails().size();
    }

    public boolean clearMailBox(UUID playerUUID) {
        PlayerMailBox mailBox = getMailBox(playerUUID);
        mailBox.clearMails();
        saveMailBox(playerUUID);
        return true;
    }

    public void saveAll() {
        for (UUID uuid : mailBoxes.keySet()) {
            saveMailBox(uuid);
        }
    }

    public void unloadMailBox(UUID playerUUID) {
        saveMailBox(playerUUID);
        mailBoxes.remove(playerUUID);
    }
}