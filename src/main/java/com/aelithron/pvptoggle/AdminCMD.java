package com.aelithron.pvptoggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCMD implements CommandExecutor {
    private PvPToggle plugin;
    public AdminCMD(PvPToggle plugin) {
        this.plugin = plugin;
    }
    String prefix = CoreTools.getInstance().getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (!sender.hasPermission("pvptoggle.admin")) {
            sender.sendMessage(prefix + ChatColor.RED + "You must be an admin to use this command.");
            return false;
        }
        if (args.length == 0) {
            sendHelpMessage(sender);
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(prefix + ChatColor.GREEN + "Plugin reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            if (args.length == 1) {
                sendHelpMessage(sender);
                return false;
            }
            Player togglePlayer = plugin.getServer().getPlayer(args[1]);
            if (togglePlayer == null) {
                sender.sendMessage(prefix + ChatColor.RED + "Player " + args[1] + " not found.");
                return false;
            }
            boolean status = ToggleManager.getInstance().checkStatus(togglePlayer);
            if (status) {
                sender.sendMessage(prefix + ChatColor.GREEN + "PvP status is " + ChatColor.BOLD + "on " + ChatColor.GREEN + "for " + togglePlayer.getName() + ".");
            } else {
                sender.sendMessage(prefix + ChatColor.GREEN + "PvP status is " + ChatColor.RED + ChatColor.BOLD + "off " + ChatColor.GREEN + "for " + togglePlayer.getName() + ".");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 3) {
                sendHelpMessage(sender);
                return false;
            }
            Player togglePlayer = plugin.getServer().getPlayer(args[1]);
            if (togglePlayer == null) {
                sender.sendMessage(prefix + ChatColor.RED + "Player " + args[1] + " not found.");
                return false;
            }
            if (args[2].equalsIgnoreCase("on")) {
                ToggleManager.getInstance().addPlayer(togglePlayer);
            } else if (args[2].equalsIgnoreCase("off")) {
                ToggleManager.getInstance().removePlayer(togglePlayer);
            } else {
                sendHelpMessage(sender);
                return false;
            }
            sender.sendMessage(prefix + ChatColor.GREEN + "PvP status updated to " + args[2] + " for " + togglePlayer.getName());
            togglePlayer.sendMessage(prefix + ChatColor.GREEN + "Your PvP status has been set to " + args[2] + " by an administrator.");
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(prefix + ChatColor.DARK_AQUA + "PvP Status List");
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                boolean status = ToggleManager.getInstance().checkStatus(player);
                if (status) {
                    sender.sendMessage(ChatColor.GREEN + player.getName() + " • Enabled");
                } else {
                    sender.sendMessage(ChatColor.RED + player.getName() + " • Disabled");
                }
            }
            return true;
        }
        sendHelpMessage(sender);
        return false;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(prefix + ChatColor.RED + "Incorrect usage!");
        sender.sendMessage(ChatColor.AQUA + "/pvptadmin reload: Reloads the plugin.");
        sender.sendMessage(ChatColor.AQUA + "/pvptadmin status (player): Checks a player's PvP status.");
        sender.sendMessage(ChatColor.AQUA + "/pvptadmin set (player) (on|off): Sets a player's PvP status.");
        sender.sendMessage(ChatColor.AQUA + "/pvptadmin list: Lists all players with their PvP status.");
    }
}
