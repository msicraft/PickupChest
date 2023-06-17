package com.msicraft.pickupchest.Compatibility.BlockLocker;

import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockLockerUtil {

    public static boolean canOpenChest(Player player, Block block) {
        return BlockLockerAPIv2.isAllowed(player, block, true);
    }

}
