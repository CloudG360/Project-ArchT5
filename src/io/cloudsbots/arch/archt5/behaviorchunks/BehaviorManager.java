package io.cloudsbots.arch.archt5.behaviorchunks;

import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.Main;
import io.cloudsbots.arch.archt5.behaviorchunks.CoreBehaviors.BehaviorNotFound;
import io.cloudsbots.arch.archt5.components.Validator;
import io.cloudsbots.arch.archt5.events.CallableEvent;
import io.cloudsbots.arch.archt5.events.EventChannel;
import io.cloudsbots.arch.archt5.exceptions.PermissionDeniedException;
import io.cloudsbots.arch.archt5.plugin.Plugin;
import io.cloudsbots.arch.archt5.plugin.PluginPackaging;
import io.cloudsbots.arch.archt5.plugin.PluginState;
import io.cloudsbots.arch.archt5.plugin.SystemPlugin;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BehaviorManager {

    private HashMap<String, Behavior> behaviors = new HashMap<>();
    private HashMap<String, PluginPackaging> behaviorParents = new HashMap<>();

    private String pass;

    public BehaviorManager(String pass){ this.pass = pass; }

    public void defaultBehavior(Plugin plugin, String name, String pass){
        if(name.toLowerCase().equals("cmd_pcs")){
            behaviors.put(name.toLowerCase(), new BehaviorNotFound());
        } else {
            if(behaviors.containsKey(name.toLowerCase())){
                Behavior b = behaviors.get(name.toLowerCase());
                if(b.isProtected()){
                    if(plugin instanceof SystemPlugin){
                        behaviors.put(name.toLowerCase(), new BehaviorNotFound());
                    } else if(plugin == behaviorParents.get(name.toLowerCase()).getPlugin()) {
                        behaviors.put(name.toLowerCase(), new BehaviorNotFound());
                    } else {
                        if(b.unlock(pass)){
                            behaviors.put(name.toLowerCase(), new BehaviorNotFound());
                        } else {
                            throw new PermissionDeniedException("You cannot default a protected behavior.");
                        }
                    }
                } else {
                    behaviors.put(name.toLowerCase(), new BehaviorNotFound());
                }
            }
        }
    }

    /**
     * Registers a modular behavior without any password protection.
     *
     * Deprecation - Replaced by ArchTasks and TriArch (Scripting) as
     * they are designed better and can tie into the security manager better.
     */
    @Deprecated
    public void registerBehavior(String name, Behavior behavior, PluginPackaging plugin){ registerBehavior(name, behavior, plugin, ""); }

    /**
     * Registers a modular behavior which is password protected. Behaviors can
     * be ran with the run Behavior function.
     *
     * Deprecation - Replaced by ArchTasks and TriArch (Scripting) as
     * they are designed better and can tie into the security manager better.
     */
    @Deprecated
    public boolean registerBehavior(String name, Behavior behavior, PluginPackaging plugin, String password){
        if(Bot.getBot().getPluginManager().getPluginState(plugin) != PluginState.ENABLED){
            throw new PermissionDeniedException("Plugin State Error: Disabled/Banned/Unloaded plugins cannot register behaviors");
        }
        if(behaviors.containsKey(name.toLowerCase())){
            Behavior b = behaviors.get(name.toLowerCase());
            if (b.isProtected()){
                if(b.unlock(password)){
                    behaviors.remove(name.toLowerCase());
                    behaviors.put(name.toLowerCase(), behavior);
                    behaviorParents.put(name.toLowerCase(), plugin);
                    return true;
                } else {
                    return false;
                }
            } else {
                behaviors.remove(name.toLowerCase());
                behaviors.put(name.toLowerCase(), behavior);
                behaviorParents.put(name.toLowerCase(), plugin);
                return true;
            }
        } else {
            behaviors.put(name.toLowerCase(), behavior);
            behaviorParents.put(name.toLowerCase(), plugin);
            return true;
        }
    }

    /**
     * Runs a registered behavior.
     *
     * Deprecation - Replaced by ArchTasks and TriArch (Scripting) as
     * they are designed better and can tie into the security manager better.
     */
    @Deprecated
    public Behavior runBehavior(String name, Object... params){
        Validator.notNull(name, "Running Behavior");
        new CallableEvent(Main.getSysPlugin(pass), "behaviors.run."+name, new Pair<>("VERIF", Bot.getBot().getSession().toString())).call(EventChannel.SYSTEM, true);
        if(behaviors.keySet().contains(name.toLowerCase())){
            return behaviors.get(name.toLowerCase()).beginBehavior(params);
        } else {
            Object[] a = {name};
            return new BehaviorNotFound().beginBehavior(a);
        }
    }

    public List<String> getPluginBehaviors(Plugin plugin){
        List<String> list = new ArrayList<>();
        for(Map.Entry<String, PluginPackaging> entry:behaviorParents.entrySet()){
            if(entry.getValue().getPlugin() == plugin){
                list.add(entry.getKey());
            }
        }
        return list;
    }

    public static BehaviorManager getBehaviorManager(){
        return Bot.getBot().getBehaviorManager();
    }

}
