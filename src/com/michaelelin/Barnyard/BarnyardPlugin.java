package com.michaelelin.Barnyard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.michaelelin.Barnyard.commands.*;

public class BarnyardPlugin extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");

    public int MAXIMUM_PETS;
    public List<EntityType> ALLOWED_TYPES;

    public PetManager manager;

    public ProtocolManager protocolManager;

    private Map<String, BarnyardCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (name.equalsIgnoreCase("pet")) {
            if (args.length >= 1 && commands.containsKey(args[0].toLowerCase())) {
                BarnyardCommand cmd = commands.get(args[0].toLowerCase());
                if (!cmd.execute(sender, Arrays.copyOfRange(args, 1, args.length))) {
                    cmd.sendUsage(sender);
                }
            } else {
                message(sender, ChatColor.UNDERLINE + "Barnyard Commands");
                message(sender, "Max pets: " + MAXIMUM_PETS);
                message(sender, "/pet spawn <type>");
                message(sender, "/pet remove <id>");
                message(sender, "/pet list");
                message(sender, "/pet wear [id]");
                message(sender, "/pet ride <id>");
                message(sender, "/pet stack <id> <id> [id...]");
                message(sender, "/pet name <id> [name]");
                message(sender, "/pet explode <id>");
            }
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
        try {
            protocolManager = ProtocolLibrary.getProtocolManager();
        } catch (Exception e) {
            log.warning("[Barnyard] ProtocolLib could not be loaded.");
        }
        setupDatabase();
        commands = new HashMap<String, BarnyardCommand>();
        commands.put("spawn", new SpawnCommand(this));
        commands.put("remove", new RemoveCommand(this));
        commands.put("list", new ListCommand(this));
        commands.put("wear", new WearCommand(this));
        commands.put("ride", new RideCommand(this));
        commands.put("stack", new StackCommand(this));
        commands.put("name", new NameCommand(this));
        commands.put("explode", new ExplodeCommand(this));
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
