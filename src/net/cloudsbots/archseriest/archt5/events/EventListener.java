package net.cloudsbots.archseriest.archt5.events;

public class EventListener {

    protected void globalChannelEvent(Event event, String id){

    }

    /**
     * This channel is for plugins to call on when they have an event which could be cancelled
     * or modified.
     *
     * {@see #globalChannelEvent} for information on event channels.
     *
     * @param event Event Object
     * @param id The id of the event. Should be covered by the event's call method.
     */
    protected void pluginChannelEvent(Event event, String id){

    }


    /**
     * This channel shouldn't be called on as it's for core bot events. You can listen
     * for those kinds events on this channel.
     *
     * {@see #globalChannelEvent} for information on event channels.
     *
     * @param event Event Object
     * @param id The id of the event. Should be covered by the event's call method.
     */
    protected void systemChannelEvent(Event event, String id){

    }

    protected void behaviorChannelEvent(Event event, String id){

    }

    protected void infoChannelEvent(Event event, String id){

    }

    protected void errorChannelEvent(Event event, String id){

    }

    /**
     * This event channel should be broadcasted to for events which indicate the
     * bot or a process has reached a certain point. This could be the end of
     * a thread or a command processing beggining.
     *
     * {@see #globalChannelEvent} for information on event channels.
     *
     * @param event Event Object
     * @param id The id of the event. Should be covered by the event's call method.
     */
    protected void checkpointChannelEvent(Event event, String id){

    }

    /**
     * This event channel is automatically broadcasted to if an event is called after
     * it has been processed on all the other channels. Useful
     *
     * {@see #globalChannelEvent} for information on event channels.
     *
     * @param event Event Object
     * @param id The id of the event. Should be covered by the event's call method.
     */
    protected void finalChannelEvent(Event event, String id){

    }

    protected void dataChannelEvent(Event event, String id){

    }



}
