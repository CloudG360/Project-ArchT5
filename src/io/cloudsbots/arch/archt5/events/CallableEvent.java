package io.cloudsbots.arch.archt5.events;

import io.cloudsbots.arch.archt5.plugin.PluginPackaging;
import javafx.util.Pair;

public class CallableEvent extends Event{
    public CallableEvent(PluginPackaging plugin, String id, Pair<String, String>... tags){ super(plugin, id, tags); }
}
