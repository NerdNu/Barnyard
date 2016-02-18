package com.michaelelin.Barnyard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class PetRegistry {

    private BarnyardPlugin plugin;

    private Map<Player, List<PetData>> onlinePets;
    private Set<LivingEntity> loadedPets;

    public PetRegistry(BarnyardPlugin plugin) {
        this.plugin = plugin;
        onlinePets = new HashMap<Player, List<PetData>>();
        loadedPets = new HashSet<LivingEntity>();
    }

    public PetData createDataForPet(OfflinePlayer owner, LivingEntity pet) {
        PetData data = new PetData();
        data.setOwner(owner.getUniqueId().toString());
        data.setUuid(pet.getUniqueId().toString());
        data.setChunkX(pet.getLocation().getChunk().getX());
        data.setChunkZ(pet.getLocation().getChunk().getZ());
        data.setWorld(pet.getWorld().getUID().toString());
        data.setType(pet.getType().ordinal());
        pet.setMetadata("petdata", new FixedMetadataValue(plugin, data));
        return data;
    }

    public PetData getDataFromPet(LivingEntity pet) {
        if (pet.hasMetadata("petdata")) {
            List<MetadataValue> meta = pet.getMetadata("petdata");
            for (MetadataValue value : meta) {
                if (value.getOwningPlugin() == plugin && value.value() instanceof PetData) {
                    return (PetData) value.value();
                }
            }
        }
        return plugin.getDatabase().find(PetData.class).where().eq("uuid", pet.getUniqueId().toString()).query().findUnique();
    }

    public LivingEntity getPetFromData(PetData data) {
        if (data == null) return null;
        for (LivingEntity entity : loadedPets) {
            if (!entity.isDead() && data.getUuid().toString().equals(entity.getUniqueId().toString())) {
                return entity;
            }
        }
        World world = plugin.getServer().getWorld(UUID.fromString(data.getWorld()));
        if (world != null) {
            Chunk chunk = world.getChunkAt(data.getChunkX(), data.getChunkZ());
            Entity[] entities = chunk.getEntities();
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && data.getUuid().equals(entity.getUniqueId().toString())) {
                    return (LivingEntity) entity;
                }
            }
        }
        plugin.getDatabase().delete(plugin.getDatabase().find(PetData.class).where().eq("uuid", data.getUuid()).query().findUnique());
        Player player = plugin.getServer().getPlayer(UUID.fromString(data.getOwner()));
        if (onlinePets.containsKey(player)) {
            onlinePets.get(player).remove(data);
        }
        return null;
    }

    public PetData registerPet(OfflinePlayer player, LivingEntity pet) {
        PetData data = createDataForPet(player, pet);
        if (player.isOnline()) {
            onlinePets.get(player).add(data);
        }
        loadedPets.add(pet);
        plugin.getDatabase().save(data);
        return data;
    }

    public LivingEntity unregisterPet(PetData pet) {
        if (pet == null) return null;
        Player owner = plugin.getServer().getPlayer(UUID.fromString(pet.getOwner()));
        if (owner!= null) {
            onlinePets.get(owner).remove(pet);
        }
        LivingEntity entity = getPetFromData(pet);
        loadedPets.remove(entity);
        plugin.getDatabase().delete(pet);
        return entity;
    }

    public void updatePet(PetData pet) {
        plugin.getDatabase().save(pet);
        Player owner = plugin.getServer().getPlayer(UUID.fromString(pet.getOwner()));
        if (owner != null) {
            List<PetData> pets = onlinePets.get(owner);
            if (!pets.contains(pet)) {
                for (int i = 0; i < pets.size(); i++) {
                    if (pets.get(i).getUuid().equals(pet.getUuid())) {
                        pets.set(i, pet);
                        break;
                    }
                }
            }
        }
    }

    public List<PetData> getPetsFromPlayer(OfflinePlayer player) {
        if (player.isOnline()) {
            return onlinePets.get(player);
        } else {
            return plugin.getDatabase().find(PetData.class).where().eq("owner", player.getUniqueId().toString()).query().findList();
        }
    }

    public void loadPetsForPlayer(Player player) {
        onlinePets.put(player, plugin.getDatabase().find(PetData.class).where().eq("owner", player.getUniqueId().toString()).query().findList());
    }

    public void unloadPetsForPlayer(Player player) {
        onlinePets.remove(player);
    }

    public void loadChunk(Chunk chunk) {
        List<PetData> pets = plugin.getDatabase().find(PetData.class).where().eq("world", chunk.getWorld().getUID().toString()).eq("chunkX", chunk.getX()).eq("chunkZ", chunk.getZ()).query().findList();
        for (PetData pet : pets) {
            LivingEntity entity = getPetFromData(pet);
            if (entity != null && !loadedPets.contains(entity)) {
                entity.setMetadata("petdata", new FixedMetadataValue(plugin, pet));
                loadedPets.add(getPetFromData(pet));
            }
        }
    }

    public void unloadChunk(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof LivingEntity && hasPet(entity)) {
                PetData pet = getDataFromPet((LivingEntity) entity);
                if (pet != null) {
                    pet.setChunkX(chunk.getX());
                    pet.setChunkZ(chunk.getZ());
                    pet.setWorld(chunk.getWorld().getUID().toString());
                    updatePet(pet);
                    loadedPets.remove(entity);
                }
            }
        }
    }

    public boolean hasPet(Entity pet) {
        return pet.hasMetadata("petdata");
    }

}
