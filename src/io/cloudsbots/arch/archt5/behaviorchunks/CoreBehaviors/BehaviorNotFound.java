package io.cloudsbots.arch.archt5.behaviorchunks.CoreBehaviors;

import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.behaviorchunks.Behavior;

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
