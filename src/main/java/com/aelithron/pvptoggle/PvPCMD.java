package com.aelithron.pvptoggle;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPCMD implements CommandExecutor {
    private PvPToggle plugin;
    public PvPCMD(PvPToggle plugin) {
        this.plugin = plugin;
    }
    String prefix = CoreTools.getInstance().getPrefix();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.RED + "You must be a player to use this command.");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 1 && (args[0].equalsIgnoreCase("t") || args[0].equalsIgnoreCase("toggle"))) {
            if (CombatTimerManager.getInstance().checkStatus(player)) {
                player.sendMessage(prefix + ChatColor.RED + "You cannot toggle PvP while in combat!");
                return false;
            }
            ToggleManager.getInstance().togglePvP(player);
            player.sendMessage(prefix + ChatColor.GREEN + "Your PvP status is now " + ChatColor.BOLD + (ToggleManager.getInstance().checkStatus(player) ? "on" : "off") + ChatColor.GREEN + ".");
            return true;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("check"))) {
            Player checkedPlayer = plugin.getServer().getPlayer(args[1]);
            if (checkedPlayer == null) {
                player.sendMessage(prefix + ChatColor.RED + "Player " + args[1] + " not found.");
                return false;
            }
            boolean status = ToggleManager.getInstance().checkStatus(checkedPlayer);
            player.sendMessage(prefix + ChatColor.GREEN + "PvP status is " + ChatColor.BOLD + (status ? "on" : "off") + ChatColor.GREEN + " for " + checkedPlayer.getName() + ".");
            return true;

        }
        player.sendMessage(prefix + ChatColor.GREEN + "Your PvP status is " + ChatColor.BOLD + (ToggleManager.getInstance().checkStatus(player) ? "on" : "off") + ChatColor.GREEN + ".");
        player.sendMessage(ChatColor.AQUA + "Use '/pvp t' to toggle your PvP status.");
        player.sendMessage(ChatColor.AQUA + "Use '/pvp c (player)' to check a player's PvP status.");
        return true;
    }
}
