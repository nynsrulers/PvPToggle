package com.aelithron.pvptoggle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PvPTabCompleter implements TabCompleter {
    private PvPToggle plugin;
    public PvPTabCompleter(PvPToggle plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("pvp")) {
            if (args.length == 1) {
                return Arrays.asList("toggle", "check");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("check")) {
                    // Suggesting players
                    List<String> playerNames = new ArrayList<>();
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        playerNames.add(player.getName());
                    }
                    return playerNames;
                }
            }
        }
        return new ArrayList<>();
    }
}
