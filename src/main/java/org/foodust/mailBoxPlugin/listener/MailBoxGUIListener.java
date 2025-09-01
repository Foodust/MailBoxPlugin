package org.foodust.mailBoxPlugin.listener;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.foodust.mailBoxPlugin.MailBoxPlugin;
import org.foodust.mailBoxPlugin.data.Mail;
import org.foodust.mailBoxPlugin.gui.MailBoxGUI;
import org.foodust.mailBoxPlugin.manager.MailBoxManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MailBoxGUIListener implements Listener {
    private final MailBoxPlugin plugin;
    private final MailBoxManager mailBoxManager;
    private final Map<UUID, MailBoxGUI> openGUIs;

    public MailBoxGUIListener(MailBoxPlugin plugin) {
        this.plugin = plugin;
        this.mailBoxManager = plugin.getMailBoxManager();
        this.openGUIs = new HashMap<>();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof MailBoxGUI gui)) {
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() != gui.getInventory()) {
            return;
        }

        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        
        // 이전 페이지 버튼 (slot 48)
        if (slot == 48) {
            gui.previousPage();
            return;
        }
        
        // 다음 페이지 버튼 (slot 50)
        if (slot == 50) {
            gui.nextPage();
            return;
        }
        
        // 일반 아이템 슬롯이 아닌 경우
        if (slot < 0 || slot >= 45) {
            return;
        }

        Mail mail = gui.getMailAtSlot(slot);
        if (mail == null) {
            return;
        }

        ItemStack item = mail.item();

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage("§c[우체통] 인벤토리에 공간이 부족합니다!");
            return;
        }

        int actualIndex = gui.getCurrentPage() * 45 + slot;
        gui.getMailBox().removeMail(actualIndex);
        player.getInventory().addItem(item);
        
        mailBoxManager.saveMailBox(player.getUniqueId());
        
        player.sendMessage("§a[우체통] 아이템을 수령했습니다!");
        
        gui.refresh();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof MailBoxGUI) {
            openGUIs.remove(event.getPlayer().getUniqueId());
        }
    }

    public void openMailBox(Player player) {
        MailBoxGUI gui = new MailBoxGUI(plugin, player, mailBoxManager.getMailBox(player.getUniqueId()));
        openGUIs.put(player.getUniqueId(), gui);
        gui.open();
    }

    public void closeAll() {
        for (MailBoxGUI gui : openGUIs.values()) {
            gui.getInventory().getViewers().forEach(HumanEntity::closeInventory);
        }
        openGUIs.clear();
    }
}