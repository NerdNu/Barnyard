package com.michaelelin.Barnyard.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class NameCommand extends BarnyardCommand {
    
    public NameCommand(BarnyardPlugin plugin) {
        super(plugin, 1, -1);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (args.length == 1) {
            if (!checkPermission(sender, "barnyard.name")) return true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                try {
                    int id = Integer.parseInt(args[0]);
                    LivingEntity pet = plugin.manager.getPet(player, id - 1);
                    if (pet == null) {
                        plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                        return true;
                    }
                    pet.setCustomName("");
                    pet.setCustomNameVisible(false);
                } catch (NumberFormatException e) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                }
            } else {
                plugin.message(sender, "You must be a player to run this command.");
            }
        }
        else if (args.length >= 2) {
            if (!checkPermission(sender, "barnyard.name")) return true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                try {
                    int id = Integer.parseInt(args[0]);
                    LivingEntity pet = plugin.manager.getPet(player, id - 1);
                    if (pet == null) {
                        plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                        return true;
                    }
                    String newName = args[1];
                    
                    if (args.length > 2) {
                        StringBuilder sb = new StringBuilder();
                        // Join our list of args with spaces
                        for (int i = 1; i < args.length; i++) {
                            sb.append(args[i]);
                            sb.append(" ");
                        }
                        newName = sb.toString();
                    }
                    pet.setCustomName(newName);
                    pet.setCustomNameVisible(true);
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
        return "name";
    }

    @Override
    public String getDefaultUsage() {
        return "<id> [name]";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return getDefaultUsage();
    }
    
}
