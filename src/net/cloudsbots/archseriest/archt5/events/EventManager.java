package net.cloudsbots.archseriest.archt5.events;

import net.cloudsbots.archseriest.archt5.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventManager {

    private List<EventListener> eventListeners = new ArrayList<>();

    public void registerEventListener(EventListener listener){
        eventListeners.add(listener);
    }

    public void registerEventListeners(EventListener... listeners){
        eventListeners.addAll(Arrays.asList(listeners));
    }

    public void unregisterEventListener(EventListener listener){
        eventListeners.remove(listener);
    }

    public List<EventListener> getListeners(){
        return new ArrayList<>(eventListeners);
    }

    public void callAsync(Event event, EventChannel channel){
        new Thread(){
            @Override
            public void run() {
                Bot.getBot().getLogger().logInfo("Event Dispatcher", "Created Async Dispatcher for Event Logged below.");
                event.call(channel, false);
                this.interrupt();
            }
        }.start();
    }

    /**
     * METHOD SHOULD NOT BE USED -
     *
     * This method can be really useful but it comes with a cost of breaking
     * other plugins for the bot. It should only be used in a situation where
     * other plugins are breaking events.
     *
     */

    @Deprecated
    public void clearAllEventListeners(){
        eventListeners.clear();
    }

}
