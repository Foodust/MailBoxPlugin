package org.foodust.mailBoxPlugin.data;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public record Mail(ItemStack item, @Getter String senderName, @Getter long timestamp) {
    public Mail(ItemStack item, String senderName, long timestamp) {
        this.item = item.clone();
        this.senderName = senderName != null ? senderName : "시스템";
        this.timestamp = timestamp;
    }

    @Override
    public ItemStack item() {
        return item.clone();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("item", item);
        data.put("sender", senderName);
        data.put("timestamp", timestamp);
        return data;
    }

    public static Mail deserialize(Map<String, Object> data) {
        try {
            ItemStack item = (ItemStack) data.get("item");
            String sender = (String) data.get("sender");
            long timestamp = ((Number) data.get("timestamp")).longValue();
            return new Mail(item, sender, timestamp);
        } catch (Exception e) {
            return null;
        }
    }
}