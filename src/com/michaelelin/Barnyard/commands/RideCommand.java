package com.michaelelin.Barnyard.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class RideCommand extends BarnyardCommand {
    
    public RideCommand(BarnyardPlugin plugin) {
        super(plugin, 1);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (!checkPermission(sender, "barnyard.ride")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                int id = Integer.parseInt(args[0]);
                LivingEntity pet = plugin.manager.getPet(player, id - 1);
                if (pet == null) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                    return true;
                }
                if (pet.getVehicle() == player) {
                    pet.leaveVehicle();
                }
                pet.eject();
                pet.setPassenger(player);
            } catch (NumberFormatException e) {
                plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
            }
        } else {
            plugin.message(sender, "You must be a player to run this command.");
        }
        return true;
    }

    public String getName() {
        return "ride";
    }

    @Override
    public String getDefaultUsage() {
        return "<id>";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return getDefaultUsage();
    }
    
}
