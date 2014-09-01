package com.michaelelin.Barnyard;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PetManager {
    // TODO: Either get rid of this class, or make it more useful.
    
    private BarnyardPlugin plugin;
    public PetRegistry registry;
    
    public PetManager(BarnyardPlugin plugin) {
        this.plugin = plugin;
        registry = new PetRegistry(plugin);
    }
    
    public boolean canSpawnPet(Player player) {
        return listPets(player).size() < plugin.MAXIMUM_PETS;
    }
    
    public void spawnPet(EntityType type, Player player) {
        registry.registerPet(player, (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), type));
    }
    
    public void removePet(Player player, int id) {
        registry.unregisterPet(registry.getPetsFromPlayer(player).get(id)).remove();
    }
    
    public List<PetData> listPets(Player player) {
        return registry.getPetsFromPlayer(player);
    }
    
    public LivingEntity getPet(Player player, int id) {
        return registry.getPetFromData(registry.getPetsFromPlayer(player).get(id));
    }
    
}
