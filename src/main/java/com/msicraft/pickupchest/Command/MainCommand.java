package com.msicraft.pickupchest.Command;

import com.msicraft.pickupchest.PickupChest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pickupchest")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "/pickupchest help");
            }
            if (args.length >= 1) {
                String val = args[0];
                switch (val) {
                    case "help":
                        if (args.length == 1) {
                            sender.sendMessage(ChatColor.YELLOW + "/pickupchest help");
                            sender.sendMessage(ChatColor.YELLOW + "/pickupchest reload : " + ChatColor.WHITE + "Reload config");
                        }
                        break;
                    case "reload":
                        if (args.length == 1) {
                            if (sender.isOp()) {
                                PickupChest.getPlugin().filesReload();
                                sender.sendMessage(PickupChest.getPrefix() + ChatColor.GREEN + " Plugin config reloaded");
                            }
                        }
                        break;
                }
            }
        }
        return false;
    }
}
