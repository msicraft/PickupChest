package com.msicraft.pickupchest.Compatibility.Towny;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TownyUtil {

    public static boolean inOwnTownCanBuild(Player player, Block block) {
        boolean check = false;
        if (PlayerCacheUtil.getCachePermission(player, block.getLocation(), block.getType(), TownyPermission.ActionType.BUILD)) {
            return true;
        }
        Town town = TownyAPI.getInstance().getTown(block.getLocation());
        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident != null && town != null && resident.hasTown() && resident.getTownOrNull().equals(town)) {
            return true;
        }
        return check;
    }

}
