package com.michaelelin.Barnyard.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class StackCommand extends BarnyardCommand {
    
    public StackCommand(BarnyardPlugin plugin) {
        super(plugin, 2, 10);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (!checkPermission(sender, "barnyard.stack")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            LivingEntity[] stack = new LivingEntity[args.length];
            for (int i = 0; i < args.length; i++) {
                try {
                    int id = Integer.parseInt(args[i]);
                    stack[i] = plugin.manager.getPet(player, id);
                    if (stack[i] == null) {
                        plugin.message(sender, "You don't have a pet with ID '" + args[i] + "'.");
                        return true;
                    }
                    
                } catch (NumberFormatException e) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[i] + "'.");
                    return true;
                }
            }
            for (int i = 1; i < stack.length; i++) {
                stack[i - 1].setPassenger(stack[i]);
            }
        } else {
            plugin.message(sender, "You must be a player to run this command.");
        }
        return true;
    }

    public String getName() {
        return "stack";
    }

    @Override
    public String getDefaultUsage() {
        return "<id> <id> [id...]";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return getDefaultUsage();
    }
    
}
