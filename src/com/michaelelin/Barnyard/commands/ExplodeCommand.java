package com.michaelelin.Barnyard.commands;



import java.lang.reflect.InvocationTargetException;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
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
                LivingEntity pet = plugin.manager.getPet(player, id - 1);
                if (pet == null) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                    return true;
                }
                pet.getWorld().playSound(pet.getEyeLocation(), Sound.EXPLODE, 1, 1);
                float health = (float) pet.getMaxHealth();
                PacketContainer[] packets = new PacketContainer[3];
                packets[0] = plugin.protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packets[0].getStrings().write(0, "largeexplode");
                packets[0].getFloat().
                    write(0, (float) pet.getLocation().getX()).
                    write(1, (float) pet.getLocation().getY()).
                    write(2, (float) pet.getLocation().getZ()).
                    write(3, 1.0F).
                    write(4, 2.0F).
                    write(5, 1.0F).
                    write(6, 0.0F);
                packets[0].getIntegers().write(0, 10);
                packets[1] = plugin.protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packets[1].getStrings().write(0, "blockcrack_152_0");
                packets[1].getFloat().
                    write(0, (float) pet.getLocation().getX()).
                    write(1, (float) pet.getLocation().getY()).
                    write(2, (float) pet.getLocation().getZ()).
                    write(3, health * 0.10F).
                    write(4, health * 0.04F).
                    write(5, health * 0.10F).
                    write(6, 0.0F);
                packets[1].getIntegers().write(0, 100);
                packets[2] = plugin.protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packets[2].getStrings().write(0, "blockcrack_35_6");
                packets[2].getFloat().
                    write(0, (float) pet.getLocation().getX()).
                    write(1, (float) pet.getLocation().getY()).
                    write(2, (float) pet.getLocation().getZ()).
                    write(3, health * 0.08F).
                    write(4, health * 0.06F).
                    write(5, health * 0.08F).
                    write(6, 0.0F);
                packets[2].getIntegers().write(0, 100);
                for (Entity entity : pet.getNearbyEntities(64, 64, 64)) {
                    if (entity instanceof Player) {
                        Player witness = (Player) entity;
                        try {
                            plugin.protocolManager.sendServerPacket(witness, packets[0]);
                            plugin.protocolManager.sendServerPacket(witness, packets[1]);
                            plugin.protocolManager.sendServerPacket(witness, packets[2]);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                plugin.manager.removePet(player, id - 1);
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
