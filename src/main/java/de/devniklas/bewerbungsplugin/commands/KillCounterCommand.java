package de.devniklas.bewerbungsplugin.commands;

import de.devniklas.bewerbungsplugin.Killcounter_Laintania;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KillCounterCommand implements CommandExecutor, TabCompleter {

    private final Killcounter_Laintania plugin;

    public KillCounterCommand(Killcounter_Laintania plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl ist nur für Spieler!");
            return true;
        }

        Player player = (Player) sender;
        Map<EntityType, Integer> kills = plugin.getKillStorage().getKills(player.getUniqueId());

        player.sendMessage("§6Deine Monster-Kills:");
        if (kills.isEmpty()) {
            player.sendMessage("§7Du hast noch keine Monster getötet!");
        } else {
            kills.forEach((type, count) ->
                    player.sendMessage("§e" + type.name() + ": §a" + count));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList(); // Keine Vorschläge anzeigen
    }
}
