package org.foodust.mailBoxPlugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.foodust.mailBoxPlugin.MailBoxPlugin;
import org.foodust.mailBoxPlugin.data.Mail;
import org.foodust.mailBoxPlugin.data.PlayerMailBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MailBoxGUI implements InventoryHolder {
    private final MailBoxPlugin plugin;
    private final Player player;
    private final PlayerMailBox mailBox;
    private final Inventory inventory;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MailBoxGUI(MailBoxPlugin plugin, Player player, PlayerMailBox mailBox) {
        this.plugin = plugin;
        this.player = player;
        this.mailBox = mailBox;
        this.inventory = Bukkit.createInventory(this, 54, "§0우체통");
        updateInventory();
    }

    private void updateInventory() {
        inventory.clear();
        
        List<Mail> mails = mailBox.getMails();
        for (int i = 0; i < Math.min(mails.size(), 45); i++) {
            Mail mail = mails.get(i);
            ItemStack displayItem = mail.item().clone();
            ItemMeta meta = displayItem.getItemMeta();
            
            if (meta != null) {
                List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                lore.add("");
                lore.add("§7발신자: §f" + mail.senderName());
                lore.add("§7날짜: §f" + dateFormat.format(new Date(mail.timestamp())));
                lore.add("");
                lore.add("§e클릭하여 수령");
                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }
            
            inventory.setItem(i, displayItem);
        }
        
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName("§r");
            glassPane.setItemMeta(glassMeta);
        }
        
        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, glassPane);
        }
        
        ItemStack infoBook = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = infoBook.getItemMeta();
        if (bookMeta != null) {
            bookMeta.setDisplayName("§6우체통 정보");
            List<String> bookLore = new ArrayList<>();
            bookLore.add("§7우편 개수: §f" + mails.size() + " / " + plugin.getConfig().getInt("max-mail-size", 54));
            bookLore.add("");
            bookLore.add("§e아이템을 클릭하여 수령하세요.");
            bookMeta.setLore(bookLore);
            infoBook.setItemMeta(bookMeta);
        }
        inventory.setItem(49, infoBook);
    }

    public void open() {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public PlayerMailBox getMailBox() {
        return mailBox;
    }

    public void refresh() {
        updateInventory();
    }
}