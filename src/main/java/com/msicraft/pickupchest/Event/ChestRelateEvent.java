package com.msicraft.pickupchest.Event;

import com.msicraft.pickupchest.PickupChest;
import com.msicraft.pickupchest.Util.PickupUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class ChestRelateEvent implements Listener {

    private final PickupUtil pickupUtil = new PickupUtil();

    private HashMap<UUID, Long> messageCooldown = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (PickupChest.getPlugin().getConfig().getBoolean("Setting.Enabled")) {
            Player player = e.getPlayer();
            int maxPickupChest = PickupChest.getPlugin().getConfig().getInt("Setting.Max-Pickup-Chest");
            if (maxPickupChest != -1) {
                int getPickupChest = pickupUtil.getInvPickupChest(player);
                if (getPickupChest > maxPickupChest) {
                    e.setCancelled(true);
                    String message = PickupChest.getPlugin().getConfig().getString("Message.Reach-Max-Pickup-Chest");
                    if (message != null && !message.equals("")) {
                        if (messageCooldown.containsKey(player.getUniqueId())) {
                            if (messageCooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
                                return;
                            }
                        }
                        long cool = (long) (1 * 1000);
                        messageCooldown.put(player.getUniqueId(), System.currentTimeMillis() + cool);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                }
            }
        }
    }

    private final ItemStack airStack = new ItemStack(Material.AIR, 1);

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (PickupChest.getPlugin().getConfig().getBoolean("Setting.Enabled")) {
            if (e.getEntityType() == EntityType.PLAYER) {
                if (pickupUtil.getDropType().equals("autodrop")) {
                    int maxPickupChest = PickupChest.getPlugin().getConfig().getInt("Setting.Max-Pickup-Chest");
                    if (maxPickupChest != -1) {
                        Player player = (Player) e.getEntity();
                        Bukkit.getScheduler().runTask(PickupChest.getPlugin(), ()-> {
                            int getPickupChest = pickupUtil.getInvPickupChest(player);
                            if (getPickupChest > maxPickupChest) {
                                int slot = pickupUtil.getPickupChestSlot(player);
                                if (slot != -1) {
                                    ItemStack pickupChest = player.getInventory().getItem(slot);
                                    if (pickupChest != null) {
                                        player.getInventory().setItem(slot, airStack);
                                        player.getWorld().dropItemNaturally(player.getLocation(), pickupChest);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

}
