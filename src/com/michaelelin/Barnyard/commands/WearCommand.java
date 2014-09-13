package com.michaelelin.Barnyard.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class WearCommand extends BarnyardCommand {
    
    public WearCommand(BarnyardPlugin plugin) {
        super(plugin, 0, 1);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (args.length == 0) {
            if (!checkPermission(sender, "barnyard.wear")) return true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Entity passenger = player.getPassenger();
                if (passenger == null) return false;
                passenger.leaveVehicle();
            } else {
                plugin.message(sender, "You must be a player to run this command.");
            }
        }
        else if (args.length == 1) {
            if (!checkPermission(sender, "barnyard.wear")) return true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                try {
                    int id = Integer.parseInt(args[0]);
                    LivingEntity pet = plugin.manager.getPet(player, id - 1);
                    if (pet == null) {
                        plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                        return true;
                    }
                    // This is kind of necessary for some reason.
                    pet.getLocation().getChunk().load();
                    pet.leaveVehicle();
                    if (pet.getPassenger() == player) {
                        player.leaveVehicle();
                    }
                    pet.teleport(player);
                    player.eject();
                    player.setPassenger(pet);
                } catch (NumberFormatException e) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                }
            } else {
                plugin.message(sender, "You must be a player to run this command.");
            }
        }
        return true;
    }

    public String getName() {
        return "wear";
    }

    @Override
    public String getDefaultUsage() {
        return "[id]";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return getDefaultUsage();
    }
    
}
