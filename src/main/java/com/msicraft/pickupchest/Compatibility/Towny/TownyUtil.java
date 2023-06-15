package com.msicraft.pickupchest.Compatibility.Towny;

import com.msicraft.pickupchest.PickupChest;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.entity.Player;

public class TownyUtil {

    public static boolean isEnabledInOwnTown = false;

    public static void reloadVariables() {
        isEnabledInOwnTown = PickupChest.getPlugin().getConfig().contains("Compatibility.Towny.Prevent-Options.InOwnTown") && PickupChest.getPlugin().getConfig().getBoolean("Compatibility.Towny.Prevent-Options.InOwnTown");
    }

    public static boolean inOwnTown(Player player) {
        boolean check = false;
        if (TownyAPI.getInstance().isWilderness(player.getLocation())) {
            return false;
        }
        Town town = TownyAPI.getInstance().getTown(player.getLocation());
        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident != null && town != null) {
            if (resident.hasTown() && resident.getTownOrNull().equals(town)) {
                check = true;
            }
        }
        return check;
    }

}
