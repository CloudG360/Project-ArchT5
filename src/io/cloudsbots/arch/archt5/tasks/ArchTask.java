package io.cloudsbots.arch.archt5.tasks;

import io.cloudsbots.arch.archt5.Bot;

import java.util.Map;

/**
 * <h2>WARNING: Development preview system.</h2>
 *
 * This system is ported from another plugin which used it as a development preview system.
 *
 * An ArchTask is a replacement structure for Behaviors & Commands. Behaviors can still be
 * based off of Behavior object but the Behavior object will be tied to ArchTasks and TaskManager.
 *
 * [!] BREAKING_CHANGES
 */
public abstract class ArchTask {

    public final void execute(Map<String, Object> vars){
        Bot.getBot().getTaskManager().processTask(this, vars);
    }

    protected abstract void run(String id);

    public abstract void cleanUp(String dataID);

    @Override
    public String toString() {
        return getClass().getTypeName();
    }
}
