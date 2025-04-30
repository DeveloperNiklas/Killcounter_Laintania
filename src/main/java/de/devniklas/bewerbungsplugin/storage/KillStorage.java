package de.devniklas.bewerbungsplugin.storage;

import de.devniklas.bewerbungsplugin.Killcounter_Laintania;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillStorage {

    private final Killcounter_Laintania plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final Map<UUID, Map<EntityType, Integer>> playerKills;

    public KillStorage(Killcounter_Laintania plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "kills.yml");
        this.playerKills = new HashMap<>();
        createDataFile();
    }

    private void createDataFile() {
        if (!dataFile.getParentFile().exists()) {
            dataFile.getParentFile().mkdirs();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Konnte kills.yml nicht erstellen: " + e.getMessage());
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        for (String uuidStr : dataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            Map<EntityType, Integer> kills = new HashMap<>();
            for (String entity : dataConfig.getConfigurationSection(uuidStr).getKeys(false)) {
                try {
                    EntityType type = EntityType.valueOf(entity);
                    int count = dataConfig.getInt(uuidStr + "." + entity);
                    kills.put(type, count);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Ungültiger EntityType: " + entity);
                }
            }
            playerKills.put(uuid, kills);
        }
    }

    public void saveData() {
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration newConfig = new YamlConfiguration();
                for (Map.Entry<UUID, Map<EntityType, Integer>> entry : playerKills.entrySet()) {
                    UUID uuid = entry.getKey();
                    for (Map.Entry<EntityType, Integer> kill : entry.getValue().entrySet()) {
                        newConfig.set(uuid.toString() + "." + kill.getKey().name(), kill.getValue());
                    }
                }
                try {
                    newConfig.save(dataFile);
                } catch (IOException e) {
                    plugin.getLogger().severe("Konnte kills.yml nicht speichern: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void addKill(UUID playerUUID, EntityType entityType) {
        playerKills.computeIfAbsent(playerUUID, k -> new HashMap<>());
        Map<EntityType, Integer> kills = playerKills.get(playerUUID);
        kills.put(entityType, kills.getOrDefault(entityType, 0) + 1);
        saveData();
    }

    public Map<EntityType, Integer> getKills(UUID playerUUID) {
        return playerKills.getOrDefault(playerUUID, new HashMap<>());
    }
}
