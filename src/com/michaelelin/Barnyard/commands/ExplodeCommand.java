package com.michaelelin.Barnyard.commands;



import java.lang.reflect.InvocationTargetException;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
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
                LivingEntity victim;
                int id = -1;
                if (args[0].equalsIgnoreCase("self")) {
                    victim = player;
                } else {
                    id = Integer.parseInt(args[0]);
                    victim = plugin.manager.getPet(player, id - 1);
                    if (victim == null) {
                        plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                        return true;
                    }
                }
                victim.getWorld().playSound(victim.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                float health = (float) victim.getMaxHealth();
                Class<Enum> particleEnum = (Class<Enum>) MinecraftReflection.getMinecraftClass("EnumParticle");
                PacketContainer[] packets = new PacketContainer[3];
                packets[0] = plugin.protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packets[0].getSpecificModifier(particleEnum).write(0, Enum.valueOf(particleEnum, "EXPLOSION_LARGE"));
                packets[0].getBooleans().write(0, false);
                packets[0].getFloat().
                    write(0, (float) victim.getLocation().getX()).
                    write(1, (float) victim.getEyeLocation().getY()).
                    write(2, (float) victim.getLocation().getZ()).
                    write(3, 1.0F).
                    write(4, 2.0F).
                    write(5, 1.0F).
                    write(6, 0.0F);
                packets[0].getIntegers().write(0, 4);
                packets[0].getIntegerArrays().write(0, new int[0]);
                packets[1] = plugin.protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packets[1].getSpecificModifier(particleEnum).write(0, Enum.valueOf(particleEnum, "BLOCK_DUST"));
                packets[1].getBooleans().write(0, false);
                packets[1].getFloat().
                    write(0, (float) victim.getLocation().getX()).
                    write(1, (float) victim.getEyeLocation().getY()).
                    write(2, (float) victim.getLocation().getZ()).
                    write(3, 0.20F).
                    write(4, 0.08F).
                    write(5, 0.20F).
                    write(6, 0.30F);
                packets[1].getIntegers().write(0, 250);
                packets[1].getIntegerArrays().write(0, new int[]{152, 0});
                packets[2] = plugin.protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);
                packets[2].getSpecificModifier(particleEnum).write(0, Enum.valueOf(particleEnum, "ITEM_CRACK"));
                packets[2].getBooleans().write(0, false);
                packets[2].getFloat().
                    write(0, (float) victim.getLocation().getX()).
                    write(1, (float) victim.getEyeLocation().getY()).
                    write(2, (float) victim.getLocation().getZ()).
                    write(3, 0.16F).
                    write(4, 0.12F).
                    write(5, 0.16F).
                    write(6, 0.20F);
                packets[2].getIntegers().write(0, 200);
                packets[2].getIntegerArrays().write(0, new int[]{319, 0});
                for (Entity entity : victim.getNearbyEntities(64, 64, 64)) {
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
                if (victim == player){
                    victim.setLastDamageCause(new EntityDamageEvent(victim, EntityDamageEvent.DamageCause.CUSTOM, victim.getHealth()));
                    victim.setHealth(0);
                } else {
                    plugin.manager.removePet(player, id - 1);
                }
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
