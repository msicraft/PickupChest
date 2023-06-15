/*
package com.msicraft.pickupchest.Compatibility.GriefPrevention;

import com.msicraft.pickupchest.PickupChest;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefPreventionUtil {

    public static boolean isEnabledInClaim = false;

    public static void reloadVariables() {
        isEnabledInClaim = PickupChest.getPlugin().getConfig().contains("Compatibility.GriefPrevention.Prevent-Options.InClaim") && PickupChest.getPlugin().getConfig().getBoolean("Compatibility.GriefPrevention.Prevent-Options.InClaim");
    }

    public static boolean isInSideClaim(Player player) {
        Location location = player.getLocation();
        DataStore dataStore = GriefPrevention.instance.dataStore;
        return dataStore.getClaimAt(location, false, dataStore.getPlayerData(player.getUniqueId()).lastClaim) != null;
    }

}

 */
