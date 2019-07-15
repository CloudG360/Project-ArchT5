package io.cloudsbots.arch.archt5.commands;

import javafx.util.Pair;
import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.Main;
import io.cloudsbots.arch.archt5.events.CallableEvent;
import io.cloudsbots.arch.archt5.events.EventChannel;
import io.cloudsbots.arch.archt5.exceptions.CommandClashException;
import io.cloudsbots.arch.archt5.plugin.Plugin;
import io.cloudsbots.arch.archt5.plugin.PluginPackaging;

import java.util.*;

public class CommandManager {

    private HashMap<String, Command> cmdmap = new HashMap<>();
    private HashMap<String, PluginPackaging> commandOwnership = new HashMap<>();

    private HashMap<UUID, CommandPackaging> commandPackages = new HashMap<>();

    private String pass;
    public CommandManager(String pass){ this.pass = pass; }

    /**
     * Unregisters all commands owned by a specified plugin. You need the plugin itself to
     * unregister them.
     *
     * @param plugin - The plugin to have commands unregistered
     */
    public void registerCommand(String name, Command command, PluginPackaging plugin){
        /*TODO: Regarding clashes:
         * Idea: Give each clashing command a new identity like "testplugin:help" and "core:help"
         * There should still be an option to remove the clashing command. Like it has the option to overwrite.
         */
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
     *
     * @param name - Name of the command.
     */
    @Deprecated
    public void unregisterCommand(String name){
        if(cmdmap.containsKey(name.toLowerCase())){
            cmdmap.remove(name.toLowerCase());
            commandOwnership.remove(name.toLowerCase());
        }
    }

    /**
     * Unregisters all commands owned by a specified plugin. You need the plugin itself to
     * unregister them.
     *
     * @param plugin - The plugin to have commands unregistered
     */
    public void unregisterAll(Plugin plugin){
        Iterator<Map.Entry<String, PluginPackaging>> i = commandOwnership.entrySet().iterator();
        List<String> keys = new ArrayList<>();
        while(i.hasNext()) {
            Map.Entry<String, PluginPackaging> pair = i.next();
            if (pair.getValue().getPlugin() == plugin) {
                keys.add(pair.getKey());
            }
        }
        for(String key:keys){
            cmdmap.remove(key.toLowerCase());
            commandOwnership.remove(key);
        }

    }

    //TODO: Add a plugin based unregister which can unregister if you supply the plugin (Protects System)
    //Do not remove #unregisterCommand(String name); as that's useful for deeper plugins and isn't too harmful due to hardcodeds

    //TODO: Add a plugin #unregisterAll(Plugin plugin);

    public static CommandManager getCommandManager(){ return Bot.getBot().getCommandManager(); }
    public HashMap<String, Command> getCmdmap() { return cmdmap; }
    public HashMap<UUID, CommandPackaging> getCommandPackages() { return commandPackages; }

    /**
     * Registers a command package onto process list.
     *
     * @param packaging - The packaging to register
     */
    public void registerCommandPackage(CommandPackaging packaging){ commandPackages.put(packaging.getUuid(), packaging); }
    public void removeCommandPackage(CommandPackaging packaging){ commandPackages.remove(packaging.getUuid()); }
}
