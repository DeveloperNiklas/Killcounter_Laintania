package de.devniklas.bewerbungsplugin;

import org.bukkit.plugin.java.JavaPlugin;
import de.devniklas.bewerbungsplugin.commands.KillCounterCommand;
import de.devniklas.bewerbungsplugin.listeners.EntityKillListener;
import de.devniklas.bewerbungsplugin.storage.KillStorage;

public class Killcounter_Laintania extends JavaPlugin {

    private KillStorage killStorage;

    @Override
    public void onEnable() {
        // Initialisiere Speicher
        this.killStorage = new KillStorage(this);
        killStorage.loadData();

        // Registriere Listener
        getServer().getPluginManager().registerEvents(new EntityKillListener(this), this);

        // Registriere Command + TabCompleter
        KillCounterCommand command = new KillCounterCommand(this);
        getCommand("killcounter").setExecutor(command);
        getCommand("killcounter").setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        // Speichere Daten beim Herunterfahren
        killStorage.saveData();
    }

    public KillStorage getKillStorage() {
        return killStorage;
    }
}
