package io.cloudsbots.arch.archt5.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * <h2>WARNING: Development preview system.</h2>
 *
 * This system is ported from another plugin which used it as a development preview system.
 *
 * The TaskManager is aimed to be a replacement for the command systems and behavior systems
 * which should make modifying them easier and should extend it's abilities, as well as
 * providing more powerful tools for management
 *
 * Version Goal: 1.0.0 (No Build ID determined)
 */
public class TaskManager {

    private Map<String, ArchTask> archTasks;
    private Map<String, Thread> threads;

    private Map<String, Map<String, Object>> threadData;

    public TaskManager(){
        archTasks = new HashMap<>();
        threads = new HashMap<>();
        threadData = new HashMap<>();
    }

    public String processTask(ArchTask archTask){
        return processTask(archTask, new HashMap<>());
    }

    public String processTask(ArchTask task, Map<String, Object> vars){

        String id = task.toString()+"-"+UUID.randomUUID().toString();

        new Thread(){

            @Override
            public void run() {
                threads.put(id, this);
                archTasks.put(id, task);
                threadData.put(id, vars);
                task.run(id);
                archTasks.remove(id);
                threads.remove(id);
                // Thread data can be cleaned up by the task if not necessary.
            }
        }.start();
        return id;



    }

    public void cancelTask(String id){

        if(threads.containsKey(id)) {
            threads.get(id).interrupt();
            threads.remove(id);
        }
        if(archTasks.containsKey(id)) {
            archTasks.remove(id);
        }
    }

    public void cancelTaskAsyncronously(String id){
        new Thread() {
            @Override
            public void run() {
                if (threads.containsKey(id)) {
                    threads.get(id).interrupt();
                    threads.remove(id);
                }
                if (archTasks.containsKey(id)) {
                    archTasks.get(id).cleanUp(id);
                    archTasks.remove(id);
                }
            }
        }.start();
    }

    public Set<String> getRunningTasks(){
        return threads.keySet();
    }

    public void setThreadData(String id, Map<String, Object> data){
        threadData.put(id, data);
    }
    public void putThreadData(String id, String string, Object obj){
        Map<String, Object> data = getThreadData(id);
        data.put(string, obj);
        threadData.put(id, data);
    }
    public void removeThreadData(String id, String string){
        Map<String, Object> data = getThreadData(id);
        data.remove(string);
        threadData.put(id, data);
    }

    public Map<String, Object> getThreadData(String id){
        return threadData.get(id);
    }

}
