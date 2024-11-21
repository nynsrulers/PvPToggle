package com.aelithron.pvptoggle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class PvPToggle extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Events
        getServer().getPluginManager().registerEvents(this, this);
        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        // Some extra staging
        CoreTools.getInstance().setPlugin(this);
        // Commands
        getCommand("pvp").setExecutor(new PvPCMD(this));
        getCommand("pvptadmin").setExecutor(new AdminCMD(this));
        // Tab Completers
        getCommand("pvp").setTabCompleter(new PvPTabCompleter(this));
        getCommand("pvptadmin").setTabCompleter(new AdminTabCompleter(this));
        // PlaceholderAPI Support
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().log(Level.INFO, "PlaceholderAPI found, adding placeholders!");
            new PvPPlaceholders(this).register();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ToggleManager.getInstance().disable();
        CombatTimerManager.getInstance().disable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getConfig().getBoolean("PvPDefaultOn")) {
            ToggleManager.getInstance().addPlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamageSource().getCausingEntity() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        String prefix = CoreTools.getInstance().getPrefix();
        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamageSource().getCausingEntity();
        if (!ToggleManager.getInstance().checkStatus(attacker)) {
            event.setCancelled(true);
            attacker.sendMessage(prefix + ChatColor.RED + "Your PvP is disabled! Enable with '/pvp t'.");
            return;
        }
        if (!ToggleManager.getInstance().checkStatus(victim)) {
            event.setCancelled(true);
            attacker.sendMessage(prefix + ChatColor.RED + "Your opponent's PvP is disabled!");
            return;
        }
        CombatTimerManager.getInstance().addPlayer(attacker, this);
        CombatTimerManager.getInstance().addPlayer(victim, this);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        CombatTimerManager.getInstance().removePlayer(event.getPlayer());
        ToggleManager.getInstance().removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        CombatTimerManager.getInstance().removePlayer(event.getEntity());
        if (getConfig().getBoolean("DeathToggle") && event.getDamageSource().getCausingEntity() != null && event.getDamageSource().getCausingEntity() instanceof Player) {
            ToggleManager.getInstance().removePlayer(event.getEntity());
            event.getEntity().sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "Your PvP has been disabled automatically due to death. To re-enable it, run '/pvp t'.");
        }
    }
}
