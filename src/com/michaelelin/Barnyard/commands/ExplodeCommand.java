package com.michaelelin.Barnyard.commands;

import net.minecraft.server.v1_7_R3.WorldServer;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.michaelelin.Barnyard.BarnyardPlugin;

public class ExplodeCommand extends BarnyardCommand {
    
    public ExplodeCommand(BarnyardPlugin plugin) {
        super(plugin, 1);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!checkArgs(args)) return false;
        if (!checkPermission(sender, "barnyard.explode")) return true;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                int id = Integer.parseInt(args[0]);
                LivingEntity pet = plugin.manager.getPet(player, id);
                if (pet == null) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                    return true;
                }
                pet.getWorld().playSound(pet.getEyeLocation(), Sound.EXPLODE, 1, 1);
                WorldServer world = ((CraftWorld) pet.getWorld()).getHandle();
                float health = (float) pet.getMaxHealth();
                world.a("largeexplode", (float) pet.getLocation().getX(), (float) pet.getLocation().getY(), (float) pet.getLocation().getZ(), 10, 1, 2, 1, 0);
                world.a("blockcrack_152_0", (float) pet.getLocation().getX(), (float) pet.getLocation().getY(), (float) pet.getLocation().getZ(), 100, health * 0.10, health * 0.04, health * 0.10, 0);
                world.a("blockcrack_35_6", (float) pet.getLocation().getX(), (float) pet.getLocation().getY(), (float) pet.getLocation().getZ(), 100, health * 0.08, health * 0.06, health * 0.08, 0);
                plugin.manager.removePet(player, id);
            } catch (NumberFormatException e) {
                plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
            }
        } else {
            plugin.message(sender, "You must be a player to run this command.");
        }
        return true;
    }

    public String getName() {
        return "explode";
    }

    @Override
    public String getDefaultUsage() {
        return "<id>";
    }
    
}
