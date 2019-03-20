package net.cloudsbots.archseriest.archt5.commands;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.Main;
import net.cloudsbots.archseriest.archt5.behaviorchunks.BehaviorManager;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;
import net.cloudsbots.archseriest.archt5.exceptions.CommandClashException;
import net.cloudsbots.archseriest.archt5.plugin.Plugin;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CommandManager {

    private HashMap<String, Command> cmdmap = new HashMap<>();
    private HashMap<String, PluginPackaging> commandOwnership = new HashMap<>();

    private HashMap<UUID, CommandPackaging> commandPackages = new HashMap<>();

    private String pass;
    public CommandManager(String pass){ this.pass = pass; }

    public void registerCommand(String name, Command command, PluginPackaging plugin){
        if(cmdmap.keySet().contains(name.toLowerCase())){
            cmdmap.remove(name.toLowerCase());
            commandOwnership.remove(name.toLowerCase());
            new CallableEvent(Main.getSysPlugin(pass), "commands.unregister", new Pair<>("COMMAND", name)).call(EventChannel.BEHAVIORS, true);
            throw new CommandClashException("Command "+name+" was registered and clashed with a command of the same name. Removing both from command map.");
        } else {
            new CallableEvent(Main.getSysPlugin(pass), "commands.register", new Pair<>("COMMAND", name)).call(EventChannel.BEHAVIORS, true);
            cmdmap.put(name.toLowerCase(), command);
            commandOwnership.put(name.toLowerCase(), plugin);
        }
    }


    /**
     * Commands really shouldn't be unregisted this way as it allows for system commands to be removed. It's
     * only here for people who want to remove commands part of the core system or remove another plugin's command
     * without a clash. Otherwise, don't use this. Deprecated functions suggest that they shouldn't be used or
     * there's a better way.
     */
    @Deprecated
    public void unregisterCommand(String name){
        if(cmdmap.containsKey(name.toLowerCase())){
            cmdmap.remove(name.toLowerCase());
            commandOwnership.remove(name.toLowerCase());
        }
    }

    //TODO: Add a plugin based unregister which can unregister if you supply the plugin (Protects System)
    //Do not remove #unregisterCommand(String name); as that's useful for deeper plugins and isn't too harmful due to hardcodeds

    //TODO: Add a plugin #unregisterAll(Plugin plugin);

    public static CommandManager getCommandManager(){ return Bot.getBot().getCommandManager(); }
    public HashMap<String, Command> getCmdmap() { return cmdmap; }
    public HashMap<UUID, CommandPackaging> getCommandPackages() { return commandPackages; }

    public void registerCommandPackage(CommandPackaging packaging){ commandPackages.put(packaging.getUuid(), packaging); }
    public void removeCommandPackage(CommandPackaging packaging){ commandPackages.remove(packaging.getUuid()); }
}
