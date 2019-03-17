package net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.behaviorchunks.Behavior;

public class BehaviorNotFound extends Behavior {

    @Override
    protected void run(Object[] parameters) {
        if(parameters.length > 0) {
            Bot.getBot().getLogger().logRuntimeError("Behaviors", "Behavior " + parameters[0].toString() + "was not found.");
        }
    }

    @Override
    public void clean() {

    }
}
