package com.michaelelin.Barnyard.commands;

import com.michaelelin.Barnyard.BarnyardPlugin;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

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
                LivingEntity victim = plugin.manager.getPet(player, id - 1);
                if (victim == null) {
                    plugin.message(sender, "You don't have a pet with ID '" + args[0] + "'.");
                    return true;
                }

                World world = victim.getWorld();
                world.playSound(victim.getEyeLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                // float health = (float) victim.getMaxHealth();
                world.spawnParticle(Particle.EXPLOSION_LARGE, victim.getEyeLocation(), 4, 1.0, 2.0, 1.0);
                world.spawnParticle(Particle.BLOCK_DUST, victim.getEyeLocation(), 250, 0.2, 0.08, 0.2, 0.3,
                        new MaterialData(Material.REDSTONE_BLOCK));
                world.spawnParticle(Particle.ITEM_CRACK, victim.getEyeLocation(), 200, 0.16, 0.12, 0.16, 0.2,
                        new ItemStack(Material.PORK));

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
