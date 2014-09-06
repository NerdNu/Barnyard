package com.michaelelin.Barnyard.commands;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;
import com.michaelelin.Barnyard.PetData;

public class ListCommand extends BarnyardCommand {
    
    public ListCommand(BarnyardPlugin plugin) {
        super(plugin, 0, 1);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (args.length == 0) {
            if (!checkPermission(sender, "barnyard.list")) return true;
            if (sender instanceof Player) {
                Player player = (Player) sender;
                List<PetData> pets = plugin.manager.listPets(player);
                if (pets.isEmpty()) {
                    plugin.message(sender, "You don't have any pets.");
                } else {
                    plugin.message(sender, "Your pets (" + pets.size() + "/" + plugin.MAXIMUM_PETS + "):");
                    for (int i = 0; i < pets.size(); i++) {
                        plugin.message(sender, "  [" + (i + 1) + "] : " + EntityType.values()[pets.get(i).getType()]);
                    }
                }
            } else {
                plugin.message(sender, "You must be a player to run this command.");
            }
        }
        else if (args.length == 1) {
            if (!sender.hasPermission("barnyard.other.list")) return false;
            OfflinePlayer player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                player = plugin.getServer().getOfflinePlayer(args[0]);
            }
            List<PetData> pets = plugin.manager.listPets(player);
            if (pets.isEmpty()) {
                plugin.message(sender, "That player doesn't have any pets.");
            } else {
                plugin.message(sender, player.getName() + "'s pets (" + pets.size() + "/" + plugin.MAXIMUM_PETS + "):");
                for (int i = 0; i < pets.size(); i++) {
                    plugin.message(sender, "  [" + (i + 1) + "] : " + EntityType.values()[pets.get(i).getType()]);
                }
            }
        }
        return true;
    }

    public String getName() {
        return "list";
    }

    @Override
    public String getDefaultUsage() {
        return "";
    }

    @Override
    public String getUsage(CommandSender sender) {
        return sender.hasPermission("barnyard.other.list") ? "[player]" : getDefaultUsage();
    }
    
}
