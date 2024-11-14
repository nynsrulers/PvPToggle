package com.aelithron.pvptoggle;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ToggleManager {
    private List<UUID> pvpOn = new ArrayList<>();

    // singleton
    private static ToggleManager instance;
    public static ToggleManager getInstance() {
        if (instance == null) {
            instance = new ToggleManager();
        }
        return instance;
    }

    public void togglePvP(Player player) {
        if (pvpOn.contains(player.getUniqueId())) {
            pvpOn.remove(player.getUniqueId());
        } else {
            pvpOn.add(player.getUniqueId());
        }
    }

    public void addPlayer(Player player) {
        pvpOn.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        pvpOn.remove(player.getUniqueId());
    }

    public boolean checkStatus(Player player) {
        return pvpOn.contains(player.getUniqueId());
    }

    public void disable() {
        pvpOn.clear();
        instance = null;
    }
}
