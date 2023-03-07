package com.msicraft.pickupchest.Util;

import com.msicraft.pickupchest.PickupChest;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PickupUtil {

    public int getPlayerEmptySlot(Player player) {
        int slot = -1;
        int size = 36;
        for (int a = 0; a<size; a++) {
            ItemStack itemStack = player.getInventory().getItem(a);
            if (itemStack == null) {
                slot = a;
                break;
            }

        }
        return slot;
    }

    public int getPickupChestSlot(Player player) {
        int slot = -1;
        int size = player.getInventory().getSize();
        for (int a = 0; a<size; a++) {
            ItemStack itemStack = player.getInventory().getItem(a);
            if (itemStack != null && isPickupChest(itemStack)) {
                slot = a;
                break;
            }
        }
        return slot;
    }

    public String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public boolean isPickupChest(ItemStack itemStack) {
        boolean check = false;
        if (itemStack != null && itemStack.getItemMeta() != null) {
            PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
            if (data.has(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest"), PersistentDataType.STRING)) {
                check = true;
            }
        }
        return check;
    }

    public String getDropType() {
        String type = "drop";
        if (PickupChest.getPlugin().getConfig().contains("Setting.Reach-Max-Chest.Type")) {
            String typeS = PickupChest.getPlugin().getConfig().getString("Setting.Reach-Max-Chest.Type");
            if (typeS != null) {
                if (typeS.equals("drop")) {
                    type = typeS;
                } else if (typeS.equals("autodrop")) {
                    type = typeS;
                }
            }
        }
        return type;
    }

    public ItemStack getChestItemStack(org.bukkit.block.data.type.Chest.Type chestType, String chestName, List<String> lore, String chestData) {
        ItemStack itemStack = new ItemStack(Material.CHEST, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            dataContainer.set(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest"), PersistentDataType.STRING, chestData);
            dataContainer.set(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest-Unstack"), PersistentDataType.STRING, UUID.randomUUID().toString());
            if (chestName != null && !chestName.equals("")) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', chestName));
            }
            if (!lore.isEmpty()) {
                List<String> temp = new ArrayList<>();
                if (chestType == Chest.Type.SINGLE) {
                    temp.add(ChatColor.GREEN + "(Single)");
                } else {
                    dataContainer.set(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest-Double"), PersistentDataType.STRING, "double");
                    temp.add(ChatColor.GREEN + "(Double)");
                }
                temp.addAll(lore);
                itemMeta.setLore(temp);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public boolean isDoubleChest(ItemStack itemStack) {
        boolean check = false;
        if (itemStack.getItemMeta() != null) {
            PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
            if (data.has(new NamespacedKey(PickupChest.getPlugin(), "PickupChest-PickupChest-Double"), PersistentDataType.STRING)) {
                check = true;
            }
        }
        return check;
    }

    public List<String> getChestInfoLore(int itemSize, ItemStack[] contents) {
        ArrayList<String> lore = new ArrayList<>();
        int maxItemLore = PickupChest.getPlugin().getConfig().getInt("Setting.Display-Lore.NumberOfDisplayPerLine");
        if (maxItemLore > itemSize) {
            maxItemLore = itemSize;
        }
        lore.add(ChatColor.WHITE + "" + itemSize + " items");
        ArrayList<String> items = new ArrayList<>();
        for (ItemStack itemStack : contents) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                int amount = itemStack.getAmount();
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null && itemMeta.hasDisplayName()) {
                    items.add(ChatColor.WHITE + itemMeta.getDisplayName() + "x" + amount);
                } else {
                    items.add(ChatColor.WHITE + itemStack.getType().name().toUpperCase() + "x" + amount);
                }
            }
        }
        int maxLine = PickupChest.getPlugin().getConfig().getInt("Setting.Display-Lore.MaxLoreLine");
        if (itemSize > 0) {
            int repeat = (itemSize / maxItemLore) + 1;
            int displayCount = 0;
            for (int a = 0; a<repeat; a++) {
                if (a > maxLine) {
                    if ((itemSize - displayCount) > 0) {
                        lore.add(ChatColor.GRAY + "more... " + (itemSize - displayCount) + " items");
                    }
                    break;
                }
                ArrayList<String> temp = new ArrayList<>();
                int checkCount = 1;
                for (int b = displayCount; b<items.size(); b++) {
                    if (checkCount > maxItemLore) {
                        break;
                    }
                    temp.add(ChatColor.WHITE + items.get(b));
                    displayCount++;
                    checkCount++;
                }
                lore.add(ChatColor.WHITE + temp.toString());
            }
        }
        return lore;
    }

    public Location getOppositeChestLocation(org.bukkit.block.Chest clickChest) {
        org.bukkit.block.data.type.Chest clickChestData = (org.bukkit.block.data.type.Chest) clickChest.getBlockData();
        Location chestLocation = clickChest.getLocation();
        double x = chestLocation.getX();
        for (double a = (x-1); a<(x+2); a++) {
            Location otherLoc = new Location(chestLocation.getWorld(), a, chestLocation.getY(), chestLocation.getZ());
            Block checkBlock = otherLoc.getBlock();
            if (checkBlock.getType() == Material.CHEST) {
                org.bukkit.block.data.type.Chest checkChestData = (org.bukkit.block.data.type.Chest) checkBlock.getState().getBlockData();
                switch (clickChestData.getType()) {
                    case LEFT:
                        if (checkChestData.getType() == Chest.Type.RIGHT) {
                            return otherLoc;
                        }
                        break;
                    case RIGHT:
                        if (checkChestData.getType() == Chest.Type.LEFT) {
                            return otherLoc;
                        }
                        break;
                }
            }
        }
        double z = chestLocation.getZ();
        for (double a = (z-1); a<(z+2); a++) {
            Location otherLoc = new Location(chestLocation.getWorld(), chestLocation.getX(), chestLocation.getY(), a);
            Block checkBlock = otherLoc.getBlock();
            if (checkBlock.getType() == Material.CHEST) {
                org.bukkit.block.data.type.Chest checkChestData = (org.bukkit.block.data.type.Chest) checkBlock.getState().getBlockData();
                switch (clickChestData.getType()) {
                    case LEFT:
                        if (checkChestData.getType() == Chest.Type.RIGHT) {
                            return otherLoc;
                        }
                        break;
                    case RIGHT:
                        if (checkChestData.getType() == Chest.Type.LEFT) {
                            return otherLoc;
                        }
                        break;
                }
            }
        }
        return null;
    }

    public boolean setDoubleChest(Block block, BlockFace blockFace) {
        Block leftBlock;
        Block rightBlock;
        switch (blockFace) {
            case EAST:
                leftBlock = block.getRelative(BlockFace.SOUTH);
                if (leftBlock.getType() == Material.AIR) {
                    leftBlock.setType(Material.CHEST);
                    org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) leftBlock.getState();
                    org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                    leftData.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
                    leftData.setFacing(blockFace);
                    leftData.setType(Chest.Type.RIGHT);
                    leftBlock.setBlockData(leftData, false);
                    org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) block.getState();
                    org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                    rightData.setType(Chest.Type.LEFT);
                    rightData.setFacing(blockFace);
                    block.setBlockData(rightData, false);
                    return true;
                } else {
                    rightBlock = block.getRelative(BlockFace.NORTH);
                    if (rightBlock.getType() == Material.AIR) {
                        rightBlock.setType(Material.CHEST);
                        org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) rightBlock.getState();
                        org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                        rightData.setType(Chest.Type.LEFT);
                        rightData.setFacing(blockFace);
                        rightBlock.setBlockData(rightData, false);
                        org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) block.getState();
                        org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                        leftData.setType(Chest.Type.RIGHT);
                        leftData.setFacing(blockFace);
                        block.setBlockData(leftData, false);
                        return true;
                    }
                }
                break;
            case WEST:
                rightBlock = block.getRelative(BlockFace.NORTH);
                if (rightBlock.getType() == Material.AIR) {
                    rightBlock.setType(Material.CHEST);
                    org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) rightBlock.getState();
                    org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                    rightData.setType(Chest.Type.RIGHT);
                    rightData.setFacing(blockFace);
                    rightBlock.setBlockData(rightData, false);
                    org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) block.getState();
                    org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                    leftData.setType(Chest.Type.LEFT);
                    leftData.setFacing(blockFace);
                    block.setBlockData(leftData, false);
                    return true;
                } else {
                    leftBlock = block.getRelative(BlockFace.SOUTH);
                    if (leftBlock.getType() == Material.AIR) {
                        leftBlock.setType(Material.CHEST);
                        org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) leftBlock.getState();
                        org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                        leftData.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
                        leftData.setFacing(blockFace);
                        leftData.setType(Chest.Type.LEFT);
                        leftBlock.setBlockData(leftData, false);
                        org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) block.getState();
                        org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                        rightData.setType(Chest.Type.RIGHT);
                        rightData.setFacing(blockFace);
                        block.setBlockData(rightData, false);
                        return true;
                    }
                }
                break;
            case NORTH:
                leftBlock = block.getRelative(BlockFace.EAST);
                if (leftBlock.getType() == Material.AIR) {
                    leftBlock.setType(Material.CHEST);
                    org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) leftBlock.getState();
                    org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                    leftData.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
                    leftData.setFacing(blockFace);
                    leftData.setType(Chest.Type.RIGHT);
                    leftBlock.setBlockData(leftData, false);
                    org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) block.getState();
                    org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                    rightData.setType(Chest.Type.LEFT);
                    rightData.setFacing(blockFace);
                    block.setBlockData(rightData, false);
                    return true;
                } else {
                    rightBlock = block.getRelative(BlockFace.WEST);
                    if (rightBlock.getType() == Material.AIR) {
                        rightBlock.setType(Material.CHEST);
                        org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) rightBlock.getState();
                        org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                        rightData.setType(Chest.Type.LEFT);
                        rightData.setFacing(blockFace);
                        rightBlock.setBlockData(rightData, false);
                        org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) block.getState();
                        org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                        leftData.setType(Chest.Type.RIGHT);
                        leftData.setFacing(blockFace);
                        block.setBlockData(leftData, false);
                        return true;
                    }
                }
                break;
            case SOUTH:
                rightBlock = block.getRelative(BlockFace.WEST);
                if (rightBlock.getType() == Material.AIR) {
                    rightBlock.setType(Material.CHEST);
                    org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) rightBlock.getState();
                    org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                    rightData.setType(Chest.Type.RIGHT);
                    rightData.setFacing(blockFace);
                    rightBlock.setBlockData(rightData, false);
                    org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) block.getState();
                    org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                    leftData.setType(Chest.Type.LEFT);
                    leftData.setFacing(blockFace);
                    block.setBlockData(leftData, false);
                    return true;
                } else {
                    leftBlock = block.getRelative(BlockFace.EAST);
                    if (leftBlock.getType() == Material.AIR) {
                        leftBlock.setType(Material.CHEST);
                        org.bukkit.block.Chest leftChest = (org.bukkit.block.Chest) leftBlock.getState();
                        org.bukkit.block.data.type.Chest leftData = (org.bukkit.block.data.type.Chest) leftChest.getBlockData();
                        leftData.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
                        leftData.setFacing(blockFace);
                        leftData.setType(Chest.Type.LEFT);
                        leftBlock.setBlockData(leftData, false);
                        org.bukkit.block.Chest rightChest = (org.bukkit.block.Chest) block.getState();
                        org.bukkit.block.data.type.Chest rightData = (org.bukkit.block.data.type.Chest) rightChest.getBlockData();
                        rightData.setType(Chest.Type.RIGHT);
                        rightData.setFacing(blockFace);
                        block.setBlockData(rightData, false);
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public int getInvPickupChest(Player player) {
        int count = 0;
        ItemStack[] itemStacks = player.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (isPickupChest(itemStack)) {
                count++;
            }
        }
        return count;
    }

}
