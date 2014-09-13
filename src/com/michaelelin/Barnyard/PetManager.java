package com.michaelelin.Barnyard;

import java.util.List;

import org.bukkit.OfflinePlayer;
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
    
    public void spawnPet(EntityType type, OfflinePlayer owner, Player player) {
        registry.registerPet(owner, (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), type));
    }
    
    public boolean removePet(OfflinePlayer player, int id) {
        LivingEntity pet = registry.unregisterPet(getPetData(player, id));
        if (pet != null) {
            pet.remove();
            return true;
        }
        return false;
    }
    
    public List<PetData> listPets(OfflinePlayer player) {
        return registry.getPetsFromPlayer(player);
    }
    
    public LivingEntity getPet(OfflinePlayer player, int id) {
        return registry.getPetFromData(getPetData(player, id));
    }
    
    public PetData getPetData(OfflinePlayer player, int id) {
        List<PetData> pets = listPets(player);
        return id >= 0 && id < pets.size() ? pets.get(id) : null;
    }
    
}
