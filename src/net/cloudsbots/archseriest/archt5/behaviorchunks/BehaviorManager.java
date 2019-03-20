package net.cloudsbots.archseriest.archt5.behaviorchunks;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.Main;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorNotFound;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;
import net.cloudsbots.archseriest.archt5.exceptions.PermissionDeniedException;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;
import net.cloudsbots.archseriest.archt5.plugin.PluginState;

import java.util.HashMap;
import java.util.List;

public final class BehaviorManager {

    private HashMap<String, Behavior> behaviors = new HashMap<>();
    private HashMap<String, PluginPackaging> behaviorParents = new HashMap<>();

    private String pass;

    public BehaviorManager(String pass){ this.pass = pass; }

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

    public static BehaviorManager getBehaviorManager(){
        return Bot.getBot().getBehaviorManager();
    }

}
