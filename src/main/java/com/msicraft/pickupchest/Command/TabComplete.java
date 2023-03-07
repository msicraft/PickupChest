package com.msicraft.pickupchest.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("pickupchest")) {
            if (args.length == 1) {
                List<String> arguments = new ArrayList<>();
                arguments.add("help");
                arguments.add("reload");
                return arguments;
            }
        }
        return null;
    }
}
