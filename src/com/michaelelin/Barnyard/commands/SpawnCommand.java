package com.michaelelin.Barnyard.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class SpawnCommand extends BarnyardCommand {
    
    public SpawnCommand(BarnyardPlugin plugin) {
        super(plugin, 1);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (!checkPermission(sender, "barnyard.spawn") && !checkPermission(sender, "barnyard.spawnany")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.manager.canSpawnPet(player)) {
                try {
                    EntityType type = EntityType.valueOf(args[0].toUpperCase());
                    if (plugin.ALLOWED_TYPES.contains(type) || checkPermission(sender, "barnyard.spawnany")) {
                        plugin.manager.spawnPet(type, player);
                    } else {
                        plugin.message(sender, "Allowed types: " + plugin.ALLOWED_TYPES.toString());
                    }
                } catch (IllegalArgumentException e) {
                    plugin.message(sender, "Allowed types: " + plugin.ALLOWED_TYPES.toString());
                }
            } else {
                plugin.message(sender, "You can't spawn any more pets.");
            }
        } else {
            plugin.message(sender, "You must be a player to run this command.");
        }
        return true;
    }

    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getDefaultUsage() {
        return "<type>";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return getDefaultUsage();
    }
    
    @Override
    public String getAdditionalInfo() {
        return "Allowed types: " + plugin.ALLOWED_TYPES.toString();
    }
    
}
