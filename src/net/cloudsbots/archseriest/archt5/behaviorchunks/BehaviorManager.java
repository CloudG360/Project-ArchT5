package net.cloudsbots.archseriest.archt5.behaviorchunks;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorNotFound;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;

import java.util.HashMap;
import java.util.List;

public final class BehaviorManager {

    private HashMap<String, Behavior> behaviors = new HashMap<>();

    public void registerBehavior(String name, Behavior behavior){
        registerBehavior(name, behavior,"");
    }

    public boolean registerBehavior(String name, Behavior behavior, String password){
        if(behaviors.containsKey(name.toLowerCase())){
            Behavior b = behaviors.get(name.toLowerCase());
            if (b.isProtected()){
                if(b.unlock(password)){
                    behaviors.remove(name.toLowerCase());
                    behaviors.put(name.toLowerCase(), behavior);
                    return true;
                } else {
                    return false;
                }
            } else {
                behaviors.remove(name.toLowerCase());
                behaviors.put(name.toLowerCase(), behavior);
                return true;
            }
        } else {
            behaviors.put(name.toLowerCase(), behavior);
            return true;
        }
    }

    public Behavior runBehavior(String name, Object... params){
        Validator.notNull(name, "Running Behavior");
        new CallableEvent("__SYS", "behaviors.run."+name, new Pair<>("VERIF", Bot.getBot().getSession().toString())).call(EventChannel.SYSTEM, true);
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
