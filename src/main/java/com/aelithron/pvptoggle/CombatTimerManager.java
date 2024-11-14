package com.aelithron.pvptoggle;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CombatTimerManager {
    private HashMap<UUID, UUID> inCombat = new HashMap<>();

    // singleton
    private static CombatTimerManager instance;
    public static CombatTimerManager getInstance() {
        if (instance == null) {
            instance = new CombatTimerManager();
        }
        return instance;
    }

    public void addPlayer(Player player, PvPToggle plugin) {
        UUID timerID = UUID.randomUUID();
        if (checkStatus(player)) {
            inCombat.remove(player.getUniqueId());
        }
        inCombat.put(player.getUniqueId(), timerID);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (this.checkStatus(player) && this.getTimerID(player.getUniqueId()) == timerID) {
                inCombat.remove(player.getUniqueId());
            }
        }, plugin.getConfig().getInt("CombatTimerDuration") * 20L);
    }

    public void removePlayer(Player player) {
        inCombat.remove(player.getUniqueId());
    }

    public boolean checkStatus(Player player) {
        return inCombat.containsKey(player.getUniqueId());
    }

    private UUID getTimerID(UUID playerID) {
        return inCombat.get(playerID);
    }

    public void disable() {
        inCombat.clear();
        instance = null;
    }
}
