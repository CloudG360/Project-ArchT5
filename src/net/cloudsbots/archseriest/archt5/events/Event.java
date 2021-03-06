package net.cloudsbots.archseriest.archt5.events;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.exceptions.PermissionDeniedException;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;

import java.util.*;

public abstract class Event {

    protected String id;
    protected String pluginid;
    protected String eventid;

    private PluginPackaging plg;
    private UUID uuid;
    private String returnaddress;
    private List<Pair<String, String>> tags;

    public Event(PluginPackaging plugin, String id, Pair<String, String>... tags){
        Validator.notNull(plugin, "Events cannot be initialised with a null plugin id.");
        Validator.notNull(plugin, "Events cannot be initialised with a null event id.");

        //Replace plugin id string with plugin class getting name.
        this.id = "event."+plugin.getId()+"."+id;
        this.pluginid = plugin.getId();
        this.eventid = id;
        this.tags = Arrays.asList(tags);
        this.plg = plugin;

        uuid = UUID.randomUUID();

        this.returnaddress = this.id+"#"+uuid.toString();
    }

    public final void call(EventChannel channel, boolean sendOnLateChannel){
        boolean late = sendOnLateChannel;
        for (EventListener listener: Bot.getBot().getEventManager().getListeners()) {
            switch (channel) {
                case GLOBAL:
                    listener.globalChannelEvent(this, id);
                    break;
                case PLUGIN:
                    listener.globalChannelEvent(this, id);
                    listener.pluginChannelEvent(this, id);
                    break;
                case SYSTEM:
                    int verifcheck = containsTag("VERIF");
                    if(verifcheck < 0){ throw new PermissionDeniedException("System events require verification to be called."); }
                    if(tags.get(verifcheck).getValue().equals(Bot.getBot().getSession().toString())) {
                        listener.globalChannelEvent(this, id);
                        listener.systemChannelEvent(this, id);
                    } else { throw new PermissionDeniedException("Plugins should not call system events"); }
                    break;
                case BEHAVIORS:
                    listener.globalChannelEvent(this, id);
                    listener.behaviorChannelEvent(this, id);
                    break;
                case INFO:
                    listener.globalChannelEvent(this, id);
                    listener.infoChannelEvent(this, id);
                    break;
                case ERROR:
                    listener.globalChannelEvent(this, id);
                    listener.errorChannelEvent(this, id);
                    break;
                case CHECKPOINT:
                    listener.globalChannelEvent(this, id);
                    listener.checkpointChannelEvent(this, id);
                    break;
                case DATA:
                    if(this instanceof DataEvent){
                        listener.dataChannelEvent(this, id);
                        late=false;
                    } else {throw new PermissionDeniedException("Only DataEvent types can use this channel."); }
                    break;
            }
        }
        for (EventListener listener: Bot.getBot().getEventManager().getListeners()) { if (late) { listener.finalChannelEvent(this, id); } }
        Bot.getBot().getLogger().logInfo("Event Dispatcher", "Dispacted Event of type "+this.id, "@ Channel "+channel.toString(), "Return Address = "+this.returnaddress, "UUID: "+this.uuid);
        uuid=UUID.randomUUID();
        returnaddress=id+"#"+uuid;
        Statistics.getStatisticsHub().incrementEventsFired();
    }

    public UUID getEventUUID(){
        return uuid;
    }
    public String getReturnaddress() {
        return returnaddress;
    }
    public List<Pair<String, String>> getTags() {
        return tags;
    }
    public String getEventid() {
        return id;
    }
    public String getEventTypeid() { return eventid; }
    public PluginPackaging getPlugin(){ return plg; }

    public int containsTag(String id){
        int i = 0;
        for (Pair<String, String> tag: tags) {
            Validator.checkNoSpaceString(tag.getKey(), "Event tags");
            if(tag.getKey().toUpperCase().equals(id.toUpperCase())) { return i; }
            i++;
        }
        return -1;
    }
}
