package net.cloudsbots.archseriest.archt5.events;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class DataEvent extends Event{

    public DataEvent(String plugin, String id, Pair<String, String>... tags){ super(plugin, id, tags); }

    private Object data = new Object();
    private Map<String, Object> datacollection = new HashMap<>();

    public Object getPrimaryData() {
        return data;
    }
    public Map<String, Object> getDataCollection() {
        return datacollection;
    }
    public void setPrimaryData(Object object){
        data = object;
    }
    public void packageExtraData(String id, Object object){
        datacollection.put(id, object);
    }
}
