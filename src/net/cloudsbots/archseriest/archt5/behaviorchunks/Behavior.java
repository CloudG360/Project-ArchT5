package net.cloudsbots.archseriest.archt5.behaviorchunks;

import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.cloudsbots.archseriest.archt5.exceptions.RethrownException;

import java.util.concurrent.TimeUnit;

public abstract class Behavior extends Bh {

    /**
     * Called to start a Behavior
     */
    public final Behavior beginBehavior(Object[] parameters){
        Statistics.getStatisticsHub().incrementBehaviorsRan();
        run(parameters);
        return this;
    }

    protected abstract void run(Object[] parameters);

    /**
     * Calls the clean() function and interrupts the thread.
     */
    public final void cleanUp(){
        clean();
    }

    /**
     * It should clear all variables to save memory or should clean up text
     * channels on Discord. Should be implemented using thread safe means.
     */
    public abstract void clean();

}
