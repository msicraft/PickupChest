package com.msicraft.pickupchest.Event;

import com.msicraft.pickupchest.PickupChest;
import com.msicraft.pickupchest.Util.PickupUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChestInteractEvent implements Listener {

    private final PickupUtil pickupUtil = new PickupUtil();

    private final ItemStack airStack = new ItemStack(Material.AIR, 1);

    @EventHandler
    public void onPickUpChest(PlayerInteractEvent e) {
        if (PickupChest.getPlugin().getConfig().getBoolean("Setting.Enabled")) {
            Player player = e.getPlayer();
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
                Block block = e.getClickedBlock();
                if (block != null && block.getType() == Material.CHEST) {
                    e.setCancelled(true);
                    Chest chest = (Chest) block.getState();
                    org.bukkit.block.data.type.Chest chestData = (org.bukkit.block.data.type.Chest) chest.getBlockData();
                    List<String> lore = new ArrayList<>();
                    if (chestData.getType() == org.bukkit.block.data.type.Chest.Type.SINGLE) {
                        Inventory inventory = chest.getBlockInventory();
                        ItemStack[] contents = new ItemStack[inventory.getSize()];
                        int size = inventory.getSize();
                        int itemSize = 0;
                        for (int a = 0; a < size; a++) {
                            ItemStack item = inventory.getItem(a);
                            if (item != null && item.getType() != Material.AIR) {
                                contents[a] = item;
                                inventory.setItem(a, airStack);
                                itemSize++;
                            }
                        }
                        boolean displayLore = PickupChest.getPlugin().getConfig().getBoolean("Setting.Display-Lore.Enabled");
                        if (displayLore) {
                            lore = pickupUtil.getChestInfoLore(itemSize, contents);
                        }
                        String data = pickupUtil.itemStackArrayToBase64(contents);
                        String name = PickupChest.getPlugin().getConfig().getString("Message.Pickup-Chest-Name");
                        ItemStack pickupChest = pickupUtil.getChestItemStack(org.bukkit.block.data.type.Chest.Type.SINGLE, name, lore, data);
                        block.setType(Material.AIR);
                        if (pickupUtil.getPlayerEmptySlot(player) != -1) {
                            player.getInventory().addItem(pickupChest);
                        } else {
                            player.getWorld().dropItemNaturally(player.getLocation(), pickupChest);
                        }
                    } else {
                        boolean checkPickupDoubleChest = PickupChest.getPlugin().getConfig().getBoolean("Setting.Pickup-DoubleChest");
                        if (checkPickupDoubleChest) {
                            if (chestData.getType() == org.bukkit.block.data.type.Chest.Type.LEFT || chestData.getType() == org.bukkit.block.data.type.Chest.Type.RIGHT) { //when double chest, pick up single chest
                                Location getOppositeLoc = pickupUtil.getOppositeChestLocation(chest);
                                if (getOppositeLoc != null) {
                                    Inventory inventory = chest.getInventory();
                                    ItemStack[] contents = new ItemStack[inventory.getSize()];
                                    int size = inventory.getSize();
                                    int itemSize = 0;
                                    for (int a = 0; a < size; a++) {
                                        ItemStack item = inventory.getItem(a);
                                        if (item != null && item.getType() != Material.AIR) {
                                            contents[a] = item;
                                            inventory.setItem(a, airStack);
                                            itemSize++;
                                        }
                                    }
                                    boolean displayLore = PickupChest.getPlugin().getConfig().getBoolean("Setting.Display-Lore.Enabled");
                                    if (displayLore) {
                                        lore = pickupUtil.getChestInfoLore(itemSize, contents);
                                    }
                                    String data = pickupUtil.itemStackArrayToBase64(contents);
                                    String name = PickupChest.getPlugin().getConfig().getString("Message.Pickup-Chest-Name");
                                    ItemStack pickupChest = pickupUtil.getChestItemStack(org.bukkit.block.data.type.Chest.Type.LEFT, name, lore, data);
                                    block.setType(Material.AIR);
                                    getOppositeLoc.getBlock().setType(Material.AIR);
                                    if (pickupUtil.getPlayerEmptySlot(player) != -1) {
                                        player.getInventory().addItem(pickupChest);
                                    } else {
                                        player.getWorld().dropItemNaturally(player.getLocation(), pickupChest);
                                    }
                                }
                            }
                        } else {
                            if (chestData.getType() == org.bukkit.block.data.type.Chest.Type.LEFT || chestData.getType() == org.bukkit.block.data.type.Chest.Type.RIGHT) { //when double chest, pick up single chest
                                Location getOppositeLoc = pickupUtil.getOppositeChestLocation(chest);
                                if (getOppositeLoc != null) {
                                    Inventory inventory = chest.getBlockInventory();
                                    ItemStack[] contents = new ItemStack[inventory.getSize()];
                                    int size = inventory.getSize();
                                    int itemSize = 0;
                                    for (int a = 0; a < size; a++) {
                                        ItemStack item = inventory.getItem(a);
                                        if (item != null && item.getType() != Material.AIR) {
                                            contents[a] = item;
                                            inventory.setItem(a, airStack);
                                            itemSize++;
                                        }
                                    }
                                    boolean displayLore = PickupChest.getPlugin().getConfig().getBoolean("Setting.Display-Lore.Enabled");
                                    if (displayLore) {
                                        lore = pickupUtil.getChestInfoLore(itemSize, contents);
                                    }
                                    String data = pickupUtil.itemStackArrayToBase64(contents);
                                    String name = PickupChest.getPlugin().getConfig().getString("Message.Pickup-Chest-Name");
                                    ItemStack pickupChest = pickupUtil.getChestItemStack(org.bukkit.block.data.type.Chest.Type.SINGLE, name, lore, data);
                                    block.setType(Material.AIR);
                                    if (pickupUtil.getPlayerEmptySlot(player) != -1) {
                                        player.getInventory().addItem(pickupChest);
                                    } else {
                                        player.getWorld().dropItemNaturally(player.getLocation(), pickupChest);
                                    }
                                }
                            }
                        }
                    }
                    if (pickupUtil.getDropType().equals("autodrop")) {
                        int maxPickupChest = PickupChest.getPlugin().getConfig().getInt("Setting.Max-Pickup-Chest");
                        if (maxPickupChest != -1) {
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

    @EventHandler
    public void onPlaceChest(BlockPlaceEvent e) {
        ItemStack itemStack = e.getItemInHand();
        if (itemStack.getType() == Material.CHEST) {
            if (pickupUtil.isPickupChest(itemStack) && itemStack.getItemMeta() != null) {
                String chestData = itemStack.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest"), PersistentDataType.STRING);
                Block block = e.getBlockPlaced();
                Chest chest = (Chest) block.getState();
                BlockData blockData = block.getBlockData();
                Player player = e.getPlayer();
                Location blockLoc = block.getLocation();
                if (pickupUtil.isDoubleChest(itemStack)) {
                    if (blockData instanceof Directional) {
                        BlockFace blockFace = ((Directional) blockData).getFacing();
                        if (pickupUtil.setDoubleChest(block, blockFace)) {
                            Bukkit.getScheduler().runTaskLater(PickupChest.getPlugin(), ()-> {
                                try {
                                    Chest c = (Chest) blockLoc.getBlock().getState();
                                    Inventory inventory = pickupUtil.fromBase64(chestData);
                                    int count = 0;
                                    for (ItemStack invItem : inventory.getContents()) {
                                        if (invItem != null && invItem.getType() != Material.AIR) {
                                            c.getInventory().setItem(count, invItem);
                                        }
                                        count++;
                                        if (count >= c.getInventory().getSize()) {
                                            break;
                                        }
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            },1L);
                        } else {
                            e.setCancelled(true);
                            String message = PickupChest.getPlugin().getConfig().getString("Message.Fail-Place-DoubleChest");
                            if (message != null && !message.equals("")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            }
                        }
                    }
                } else {
                    Bukkit.getScheduler().runTaskLater(PickupChest.getPlugin(), ()-> {
                        try {
                            Chest c = (Chest) blockLoc.getBlock().getState();
                            if (c.getInventory().getSize() == 54) {
                                Inventory originalInv = c.getInventory();
                                ItemStack[] contents = new ItemStack[55];
                                int contentsC = 0;
                                for (ItemStack originalStack : originalInv.getContents()) {
                                    if (originalStack != null && originalStack.getType() != Material.AIR) {
                                        contents[contentsC] = originalStack;
                                        contentsC++;
                                    }
                                }
                                Inventory inventory= pickupUtil.fromBase64(chestData);
                                for (ItemStack placeStack : inventory.getContents()) {
                                    if (placeStack != null && placeStack.getType() != Material.AIR) {
                                        contents[contentsC] = placeStack;
                                        contentsC++;
                                    }
                                }
                                originalInv.clear();
                                int count = 0;
                                for (ItemStack stack : contents) {
                                    if (stack != null && stack.getType() != Material.AIR) {
                                        originalInv.setItem(count, stack);
                                    }
                                    count++;
                                    if (count >= chest.getInventory().getSize()) {
                                        break;
                                    }
                                }
                            } else {
                                block.setType(Material.CHEST);
                                if (blockData instanceof Directional) {
                                    ((Directional) blockData).setFacing(e.getPlayer().getFacing().getOppositeFace());
                                    block.setBlockData(blockData);
                                }
                                Inventory inventory = pickupUtil.fromBase64(chestData);
                                int count = 0;
                                for (ItemStack invItem : inventory.getContents()) {
                                    if (invItem != null && invItem.getType() != Material.AIR) {
                                        chest.getInventory().setItem(count, invItem);
                                    }
                                    count++;
                                    if (count >= chest.getInventory().getSize()) {
                                        break;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }, 1L);
                }
            }
        }
    }

}
