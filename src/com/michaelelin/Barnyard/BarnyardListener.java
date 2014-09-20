package com.michaelelin.Barnyard;

import java.util.UUID;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class BarnyardListener implements Listener {
    
    private BarnyardPlugin plugin;
    
    public BarnyardListener(BarnyardPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.manager.registry.loadPetsForPlayer(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.manager.registry.unloadPetsForPlayer(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        plugin.manager.registry.loadChunk(event.getChunk());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkUnload(ChunkUnloadEvent event) {
        plugin.manager.registry.unloadChunk(event.getChunk());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity instanceof LivingEntity && plugin.manager.registry.hasPet(entity)) {
            LivingEntity pet = (LivingEntity) entity;
            if (plugin.manager.registry.getDataFromPet(pet).getOwner().equals(event.getPlayer().getUniqueId().toString())) {
                if (pet instanceof Ageable) {
                    ((Ageable) pet).setBreed(false);
                }
            } else {
                plugin.message(event.getPlayer(), "This pet belongs to " + plugin.getServer().getOfflinePlayer(UUID.fromString(plugin.manager.registry.getDataFromPet((LivingEntity) entity).getOwner())).getName());
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity && plugin.manager.registry.hasPet(event.getEntity())) {
            event.setCancelled(true);
        }
    }
    
}
