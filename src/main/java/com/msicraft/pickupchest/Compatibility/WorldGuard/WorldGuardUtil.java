package com.msicraft.pickupchest.Compatibility.WorldGuard;

import com.msicraft.pickupchest.PickupChest;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldGuardUtil {

    private static List<String> ignoreRegions = new ArrayList<>();

    public static void reloadVariables() {
        ignoreRegions = PickupChest.getPlugin().getConfig().contains("Compatibility.WorldGuard.Ignore-Regions") ? PickupChest.getPlugin().getConfig().getStringList("Compatibility.WorldGuard.Ignore-Regions") : Collections.emptyList();
    }

    public static boolean inBypassRegion(Player player, Location clickLocation) {
        boolean check = false;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));
        if (regionManager != null) {
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(clickLocation);
            ApplicableRegionSet set = regionManager.getApplicableRegions(loc.toVector().toBlockPoint());
            for (ProtectedRegion region : set) {
                if (ignoreRegions.contains(region.getId())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    public static boolean isCurrentRegionMemberOrCanBuild(Player player, Location clickLocation) {
        boolean check = false;
        Location playerLoc = player.getLocation();
        com.sk89q.worldedit.util.Location location = BukkitAdapter.adapt(clickLocation);
        World world = null;
        if (playerLoc.getWorld() != null) {
            world = BukkitAdapter.adapt(playerLoc.getWorld());
        }
        if (world != null) {
            RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regionManager = regionContainer.get(world);
            if (regionManager != null) {
                ApplicableRegionSet set = regionManager.getApplicableRegions(location.toVector().toBlockPoint());
                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                if (set.testState(localPlayer, Flags.BUILD)) {
                    return true;
                }
                for (ProtectedRegion region : set) {
                    if (containMember(player, region)) {
                        check = true;
                        break;
                    }
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
