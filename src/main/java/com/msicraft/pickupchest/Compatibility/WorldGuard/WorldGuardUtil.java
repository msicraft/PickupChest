package com.msicraft.pickupchest.Compatibility.WorldGuard;

import com.msicraft.pickupchest.PickupChest;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardUtil {

    public static boolean isEnabledIsRegionMember = false;

    public static void reloadVariables() {
        isEnabledIsRegionMember = PickupChest.getPlugin().getConfig().contains("Compatibility.WorldGuard.Prevent-Options.IsRegionMember") && PickupChest.getPlugin().getConfig().getBoolean("Compatibility.WorldGuard.Prevent-Options.IsRegionMember");
    }

    public static boolean isCurrentRegionMember(Player player) {
        boolean check = false;
        Location playerLoc = player.getLocation();
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(playerLoc);
        World world = null;
        if (playerLoc.getWorld() != null) {
            world = BukkitAdapter.adapt(playerLoc.getWorld());
        }
        if (world != null) {
            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = regionContainer.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(location);
            for (ProtectedRegion region : set) {
                if (containMember(player, region)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    private static boolean containMember(Player player, ProtectedRegion region) {
        boolean check = false;
        DefaultDomain defaultDomain = region.getMembers();
        if (defaultDomain.contains(player.getUniqueId())) {
            check = true;
        }
        return check;
    }

}
