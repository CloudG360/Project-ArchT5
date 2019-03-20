package net.cloudsbots.archseriest.archt5.events;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;

import java.util.Map;

public class CallableEvent extends Event{
    public CallableEvent(PluginPackaging plugin, String id, Pair<String, String>... tags){ super(plugin, id, tags); }
}
