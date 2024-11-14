package com.aelithron.pvptoggle;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminTabCompleter implements TabCompleter {
    private PvPToggle plugin;
    public AdminTabCompleter(PvPToggle plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("pvptadmin") || command.getName().equalsIgnoreCase("pvpta") || command.getName().equalsIgnoreCase("pvptoggleadmin")) {
            if (args.length == 1) {
                return Arrays.asList("reload", "status", "set", "list");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("set")) {
                    // Suggesting players
                    List<String> playerNames = new ArrayList<>();
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        playerNames.add(player.getName());
                    }
                    return playerNames;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    return Arrays.asList("on", "off");
                }
            }
        }
        return new ArrayList<>();
    }
}
