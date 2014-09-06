package com.michaelelin.Barnyard.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.michaelelin.Barnyard.BarnyardPlugin;

public abstract class BarnyardCommand {
    
    protected BarnyardPlugin plugin;
    
    private int minArgs;
    private int maxArgs;
    
    public BarnyardCommand(BarnyardPlugin plugin, int minArgs, int maxArgs) {
        this.plugin = plugin;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }
    
    public BarnyardCommand(BarnyardPlugin plugin, int argCount) {
        this.plugin = plugin;
        this.minArgs = argCount;
        this.maxArgs = argCount;
    }
    
    public boolean checkArgs(String[] args) {
        return args.length >= minArgs && args.length <= maxArgs;
    }
    
    public boolean checkPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission for that command.");
            return false;
        }
        return true;
    }
    
    public void sendUsage(CommandSender sender) {
        plugin.message(sender, "Usage: /pet " + getName() + " " + getUsage(sender));
    }
    
    public String getAdditionalInfo() {
        return null;
    }
    
    public abstract boolean execute(CommandSender sender, String[] args);
    
    public abstract String getName();
    
    public abstract String getDefaultUsage();
    
    public String getUsage(CommandSender sender) {
        return getDefaultUsage();
    }
    
}
