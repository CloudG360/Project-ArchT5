package net.cloudsbots.archseriest.archt5.plugin;

import net.cloudsbots.archseriest.archt5.exceptions.PluginNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PluginManager extends ClassLoader{

    private HashMap<String, PluginPackaging> plgs;
    private HashMap<Plugin, PluginState> states;

    private PluginPackaging sysPlugin;

    public PluginManager(PluginPackaging systemPlugin){
        this.sysPlugin = systemPlugin;
        this.plgs = new HashMap<>();
        this.states = new HashMap<>();
    }


    public boolean checkSystemPlugin(PluginPackaging pluginPackaging){ return pluginPackaging == sysPlugin; }

    public PluginState getPluginState(PluginPackaging plugin){ return states.getOrDefault(plugin.getPlugin(), PluginState.UNLOADED); }
    public PluginState getPluginState(Plugin plugin){ return states.getOrDefault(plugin, PluginState.UNLOADED); }
    public void setPluginState(Plugin plugin, PluginState state){
        if(states.containsKey(plugin)){
            states.remove(plugin);
        }
        states.put(plugin, state);
    }

    /**
     * Checks if a plugin is <b>loaded</b> rather than if it's enabled or not.
     *
     * @param name The name of the plugin
     * @return If it exists, true.
     */
    public boolean doesPluginExist(String name) { return plgs.containsKey(name.toLowerCase()); }

    public PluginPackaging getPlugin(String name) {
        if(doesPluginExist(name)){
            return plgs.get(name.toLowerCase());
        } else {
            throw new PluginNotFoundException("Plugin '"+name.toLowerCase()+"' has not been registered on the bot.");
        }
    }

    public HashMap<String, PluginPackaging> getPlugins(){
        return plgs;
    }

    public void loadPlugin(){
        //TODO: Implement Plugin Loader

        //Adding Plugins to list, ID should be lowercase
    }


}
