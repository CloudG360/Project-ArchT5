package io.cloudsbots.arch.archt5.tasks;

import io.cloudsbots.arch.archt5.components.UtilityFunctions;

import java.util.Map;

public class PreTaskScheduler {

    private ArchTask task;
    private Map<String, Object> args;

    public PreTaskScheduler(ArchTask task, Map<String, Object> args){
        this.task = task;
        this.args = args;
    }

    public void runtask(Map<String, Object> runtimeargs){ task.execute(UtilityFunctions.mergeMaps(args, runtimeargs)); }
    public void runtask(){ task.execute(args); }

}
