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
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 45;

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
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, mails.size());
        
        for (int i = startIndex; i < endIndex; i++) {
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
            
            inventory.setItem(i - startIndex, displayItem);
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
        
        if (currentPage > 0) {
            ItemStack prevItem = createNavigationItem("prev", "§e이전 페이지");
            inventory.setItem(48, prevItem);
        }
        
        ItemStack infoBook = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = infoBook.getItemMeta();
        if (bookMeta != null) {
            bookMeta.setDisplayName("§6우체통 정보");
            List<String> bookLore = new ArrayList<>();
            bookLore.add("§7우편 개수: §f" + mails.size() + " / " + plugin.getConfig().getInt("max-mail-size", 54));
            bookLore.add("§7페이지: §f" + (currentPage + 1) + " / " + (getTotalPages()));
            bookLore.add("");
            bookLore.add("§e아이템을 클릭하여 수령하세요.");
            bookMeta.setLore(bookLore);
            infoBook.setItemMeta(bookMeta);
        }
        inventory.setItem(49, infoBook);
        
        if (currentPage < getTotalPages() - 1) {
            ItemStack nextItem = createNavigationItem("next", "§e다음 페이지");
            inventory.setItem(50, nextItem);
        }
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
    
    private ItemStack createNavigationItem(String type, String displayName) {
        String materialName = plugin.getConfig().getString("inventory." + type + ".material", "PAPER");
        int customModelData = plugin.getConfig().getInt("inventory." + type + ".data", 0);
        
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            material = Material.PAPER;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public void nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            updateInventory();
        }
    }
    
    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateInventory();
        }
    }
    
    private int getTotalPages() {
        return Math.max(1, (int) Math.ceil(mailBox.getMails().size() / (double) ITEMS_PER_PAGE));
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public Mail getMailAtSlot(int slot) {
        int index = currentPage * ITEMS_PER_PAGE + slot;
        List<Mail> mails = mailBox.getMails();
        if (index >= 0 && index < mails.size()) {
            return mails.get(index);
        }
        return null;
    }
}