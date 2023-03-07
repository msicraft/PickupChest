package com.msicraft.pickupchest.Event;

import com.msicraft.pickupchest.PickupChest;
import com.msicraft.pickupchest.Util.PickupUtil;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PreventDropEvent implements Listener {

    private final PickupUtil pickupUtil = new PickupUtil();

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (PickupChest.getPlugin().getConfig().getBoolean("Setting.Prevent-Drop")) {
            ItemStack itemStack = e.getItemDrop().getItemStack();
            if (itemStack.getItemMeta() != null) {
                PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
                if (data.has(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest"), PersistentDataType.STRING)) {
                    int maxPickupChest = PickupChest.getPlugin().getConfig().getInt("Setting.Max-Pickup-Chest");
                    if (maxPickupChest != -1) {
                        if (pickupUtil.getDropType().equals("drop") || pickupUtil.getDropType().equals("autodrop")) {
                            int getPickupChest = pickupUtil.getInvPickupChest(e.getPlayer()) + 1;
                            if (getPickupChest > maxPickupChest) {
                                return;
                            }
                        }
                    }
                    e.setCancelled(true);
                    String getMessage = PickupChest.getPlugin().getConfig().getString("Message.Prevent-Drop");
                    if (getMessage != null) {
                        if (!getMessage.equals("")) {
                            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage));
                        }
                    }
                }
            }
        }
    }

}
