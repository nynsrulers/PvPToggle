package com.aelithron.pvptoggle;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PvPPlaceholders extends PlaceholderExpansion {
    private final PvPToggle plugin;

    public PvPPlaceholders(PvPToggle plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "pvptoggle";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("status_short")) {
            if (player == null || !player.isOnline() || player.getPlayer() == null) {
                return null;
            }
            return ChatColor.translateAlternateColorCodes('&', ToggleManager.getInstance().checkStatus(player.getPlayer()) ? "&a🗡" : "&c🛡");
        }
        if (params.equalsIgnoreCase("status")) {
            if (player == null || !player.isOnline() || player.getPlayer() == null) {
                return null;
            }
            return ChatColor.translateAlternateColorCodes('&', ToggleManager.getInstance().checkStatus(player.getPlayer()) ? "&8[&aPvP On&8]" : "&8[&cPvP Off&8]");
        }
        return null;
    }

}
