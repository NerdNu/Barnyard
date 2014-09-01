package com.michaelelin.Barnyard;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import net.minecraft.server.v1_7_R3.WorldServer;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BarnyardPlugin extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    
    public int MAXIMUM_PETS;
    public List<EntityType> ALLOWED_TYPES;
    
    public PetManager manager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (name.equalsIgnoreCase("pet")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (manager.canSpawnPet(player)) {
                            if (args.length == 2) {
                                try {
                                    EntityType type = EntityType.valueOf(args[1].toUpperCase());
                                    if (ALLOWED_TYPES.contains(type)) {
                                        manager.spawnPet(type, player);
                                        return true;
                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                message(sender, "Usage: /pet spawn <type>");
                            }
                            message(sender, "Allowed types: " + ALLOWED_TYPES.toString());
                        } else {
                            message(sender, "You can't spawn any more pets.");
                        }
                    } else {
                        message(sender, "You must be a player to run this command.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (args.length == 2) {
                            try {
                                int id = Integer.parseInt(args[1]);
                                manager.removePet(player, id);
                            } catch (Exception e) {
                                message(sender, "You don't have a pet with ID '" + args[1] + "'.");
                            }
                        } else {
                            message(sender, "Usage: /pet remove <id>");
                        }
                    } else {
                        message(sender, "You must be a player to run this command.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (args.length == 1) {
                            List<PetData> pets = manager.listPets(player);
                            if (pets.isEmpty()) {
                                message(sender, "You don't have any pets.");
                            } else {
                                message(sender, "Your pets:");
                                for (int i = 0; i < pets.size(); i++) {
                                    message(sender, "  [" + (i + 1) + "] : " + EntityType.values()[pets.get(i).getType()]);
                                }
                            }
                        } else {
                            message(sender, "Usage: /pet list");
                        }
                    } else {
                        message(sender, "You must be a player to run this command.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("wear")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (args.length == 1) {
                            Entity passenger = player.getPassenger();
                            if (passenger != null) {
                                passenger.leaveVehicle();
                            }
                        }
                        else if (args.length == 2) {
                            try {
                                int id = Integer.parseInt(args[1]);
                                LivingEntity pet = manager.getPet(player, id);
                                // This is kind of necessary for some reason.
                                pet.getLocation().getChunk().load();
                                pet.teleport(player);
                                pet.leaveVehicle();
                                player.eject();
                                player.setPassenger(pet);
                            } catch (Exception e) {
                                message(sender, "You don't have a pet with ID '" + args[1] + "'.");
                            }
                        } else {
                            message(sender, "Usage: /pet wear [id]");
                        }
                    } else {
                        message(sender, "You must be a player to run this command.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("ride")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (args.length == 2) {
                            try {
                                int id = Integer.parseInt(args[1]);
                                LivingEntity pet = manager.getPet(player, id);
                                if (pet.getVehicle() == player) {
                                    pet.leaveVehicle();
                                }
                                pet.eject();
                                pet.setPassenger(player);
                            } catch (Exception e) {
                                message(player, "You don't have a pet with ID '" + args[1] + "'.");
                            }
                        } else {
                            message(sender, "Usage: /pet ride <id>");
                        }
                    } else {
                        message(sender, "You must be a player to run this command.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("stack")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (args.length >= 3) {
                            LivingEntity[] stack = new LivingEntity[args.length - 1];
                            for (int i = 1; i < args.length; i++) {
                                try {
                                    int id = Integer.parseInt(args[i]);
                                    stack[i - 1] = manager.getPet(player, id);
                                    
                                } catch (Exception e) {
                                    message(sender, "You don't have a pet with ID '" + args[1] + "'.");
                                    return true;
                                }
                            }
                            for (int i = 1; i < stack.length; i++) {
                                stack[i - 1].setPassenger(stack[i]);
                            }
                        } else {
                            message(sender, "Usage: /pet stack <id> <id> [id...]");
                        }
                    } else {
                        message(sender, "You must be a player to run this command.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("explode")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (args.length == 2) {
                            try {
                                int id = Integer.parseInt(args[1]);
                                LivingEntity pet = manager.getPet(player, id);
                                pet.getWorld().playSound(pet.getEyeLocation(), Sound.EXPLODE, 1, 1);
                                WorldServer world = ((CraftWorld) pet.getWorld()).getHandle();
                                float health = (float) pet.getMaxHealth();
                                world.a("largeexplode", (float) pet.getLocation().getX(), (float) pet.getLocation().getY(), (float) pet.getLocation().getZ(), 10, 1, 2, 1, 0);
                                world.a("blockcrack_152_0", (float) pet.getLocation().getX(), (float) pet.getLocation().getY(), (float) pet.getLocation().getZ(), 100, health * 0.10, health * 0.04, health * 0.10, 0);
                                world.a("blockcrack_35_6", (float) pet.getLocation().getX(), (float) pet.getLocation().getY(), (float) pet.getLocation().getZ(), 100, health * 0.08, health * 0.06, health * 0.08, 0);
                                manager.removePet(player, id);
                            } catch (Exception e) {
                                message(sender, "You don't have a pet with ID '" + args[1] + "'.");
                            }
                        }
                    }
                    return true;
                }
            }
            message(sender, ChatColor.UNDERLINE + "Barnyard Commands");
            message(sender, "Max pets: " + MAXIMUM_PETS);
            message(sender, "/pet spawn <type>");
            message(sender, "/pet remove <id>");
            message(sender, "/pet list");
            message(sender, "/pet wear [id]");
            message(sender, "/pet ride <id>");
            message(sender, "/pet stack <id> <id> [id...]");
            return true;
        }
        return false;
    }

    public void message(CommandSender sender, String msg) {
        sender.sendMessage(ChatColor.GREEN + msg);
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        MAXIMUM_PETS = getConfig().getInt("maximum-pets");
        List<String> types = getConfig().getStringList("allowed-types");
        ALLOWED_TYPES = new ArrayList<EntityType>();
        for (String s : types) {
            try {
                ALLOWED_TYPES.add(EntityType.valueOf(s.toUpperCase()));
            } catch (IllegalArgumentException e) {
                log.warning("[Barnyard] Unrecognized creature type '" + s + "'. Check config.yml.");
            }
        }
        getServer().getPluginManager().registerEvents(new BarnyardListener(this), this);
        this.manager = new PetManager(this);
        setupDatabase();
        for (Player player : getServer().getOnlinePlayers()) {
            manager.registry.loadPetsForPlayer(player);
        }
        for (World world : getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                manager.registry.loadChunk(chunk);
            }
        }
        log.info("[Barnyard] " + getDescription().getVersion() + " enabled.");
    }

    @Override
    public void onDisable() {
        log.info("[Barnyard] " + getDescription().getVersion() + " disabled.");
    }

    public void setupDatabase() {
        try {
            getDatabase().find(PetData.class).findRowCount();
        } catch (PersistenceException e) {
            log.info("Installing " + getDescription().getName() + " database.");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(PetData.class);
        return list;
    }

}
