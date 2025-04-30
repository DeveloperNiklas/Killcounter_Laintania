package de.devniklas.bewerbungsplugin.listeners;

import de.devniklas.bewerbungsplugin.Killcounter_Laintania;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityKillListener implements Listener {

    private final Killcounter_Laintania plugin;

    public EntityKillListener(Killcounter_Laintania plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null && event.getEntityType().isAlive()) {
            plugin.getKillStorage().addKill(killer.getUniqueId(), event.getEntityType());
        }
    }
}