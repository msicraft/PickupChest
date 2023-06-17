package com.msicraft.pickupchest.Compatibility;

import com.msicraft.pickupchest.Compatibility.BlockLocker.BlockLockerUtil;
import com.msicraft.pickupchest.Compatibility.Towny.TownyUtil;
import com.msicraft.pickupchest.Compatibility.WorldGuard.WorldGuardUtil;
import com.msicraft.pickupchest.PickupChest;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CompatibilityUtil {

    public static boolean isEnabledCompatibility() {
        boolean check = false;
        if (PickupChest.isEnabledTowny || PickupChest.isEnabledWorldGuard || PickupChest.isEnabledGriefPrevention || PickupChest.isEnabledBlockLocker) {
            check = true;
        }
        return check;
    }

    public static boolean canPickupChest(Player player, Location location, Block block) {
        if (PickupChest.isEnabledBlockLocker) {
            if (block != null) {
                return BlockLockerUtil.canOpenChest(player, block);
            }
        }
        if (PickupChest.isEnabledTowny) {
            if (TownyUtil.isEnabledInOwnTown) {
                return TownyUtil.inOwnTown(player);
            } else {
                return true;
            }
        }
        if (PickupChest.isEnabledWorldGuard) {
            if (WorldGuardUtil.isEnabledIsRegionMember) {
                return WorldGuardUtil.isCurrentRegionMemberOrCanBuild(player, location);
            } else {
                return true;
            }
        }
        if (PickupChest.isEnabledGriefPrevention) {
            //return GriefPreventionUtil.isInSideClaim(player);
        }
        return false;
    }

}
