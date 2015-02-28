package com.michaelelin.Barnyard.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class RemoveCommand extends BarnyardCommand {
    
    public RemoveCommand(BarnyardPlugin plugin) {
        super(plugin, 1, 2);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (args.length == 1) {
            if (!checkPermission(sender, "barnyard.remove")) return true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                try {
                    int id = Integer.parseInt(args[0]);
                    if (plugin.manager.removePet(player, id - 1)) {
                        plugin.message(sender, "Pet removed.");
                    } else {
                        plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                    }
                } catch (NumberFormatException e) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                }
            } else {
                plugin.message(sender, "You must be a player to run this command.");
            }
        }
        else if (args.length == 2) {
            if (!sender.hasPermission("barnyard.other.remove")) return false;
            OfflinePlayer player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                player = plugin.getServer().getOfflinePlayer(args[0]);
            }
            try {
                int id = Integer.parseInt(args[1]);
                if (plugin.manager.removePet(player, id - 1)) {
                    plugin.message(sender, "Pet removed.");
                } else {
                    plugin.message(sender, "That player doesn't have a pet with ID '" + args[0] + "'.");
                }
            } catch (NumberFormatException e) {
                plugin.message(sender,  "That player doesn't have a pet with ID '" + args[0] + "'.");
            }
        }
        return true;
    }

    public String getName() {
        return "remove";
    }

    @Override
    public String getDefaultUsage() {
        return "<id>";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return sender.hasPermission("barnyard.other.remove") ? "[player] <id>" : getDefaultUsage();
    }
    
}
