package net.cloudsbots.archseriest.archt5.events;

import javafx.util.Pair;

import java.util.Map;

public class CallableEvent extends Event{
    public CallableEvent(String plugin, String id, Pair<String, String>... tags){ super(plugin, id, tags); }
}
