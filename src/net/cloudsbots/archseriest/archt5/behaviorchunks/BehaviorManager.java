package net.cloudsbots.archseriest.archt5.behaviorchunks;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.Main;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorCommandProcess;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorNotFound;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;
import net.cloudsbots.archseriest.archt5.exceptions.BehaviorNotFoundException;
import net.cloudsbots.archseriest.archt5.exceptions.PermissionDeniedException;
import net.cloudsbots.archseriest.archt5.plugin.Plugin;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;
import net.cloudsbots.archseriest.archt5.plugin.PluginState;
import net.cloudsbots.archseriest.archt5.plugin.SystemPlugin;

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
            behaviors.put(name.toLowerCase(), new BehaviorCommandProcess());
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

    public void registerBehavior(String name, Behavior behavior, PluginPackaging plugin){ registerBehavior(name, behavior, plugin, ""); }

    public boolean registerBehavior(String name, Behavior behavior, PluginPackaging plugin, String password){
        if(Bot.getBot().getPluginManager().getPluginState(plugin) != PluginState.ENABLED){
            //TODO: #BanSpot
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
