package io.cloudsbots.arch.archt5.commands.debug;

import io.cloudsbots.arch.archt5.commands.Command;
import io.cloudsbots.arch.archt5.Bot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * <h2>WARNING: Test Component for preview system.</h2>
 *
 * This system is purley for testing other Extension packages and will be removed once those packages get
 * full implementations in the development branch of ArchT5 with refined testing components.
 *
 * Target Package: Task Manager (tasks)
 * Version Aim: 1.0.0 (No build ID specified)
 * [!] REMOVE_ON_DEV_MERGE
 */
public class CommandListTasks extends Command {
    @Override
    protected void run(Message message, String[] strings) {
        int length = 0;
        String text = "";
        for(String process: Bot.getBot().getTaskManager().getRunningTasks()){
            String task = process+"\n";
            if(task.length() > 128){
                task = "TASK_ID_TOO_LONG - Blocked (Limited to 128 Characters)\n";
            }

            if(length + task.length() > 2048){
                message.getChannel().sendMessage(new EmbedBuilder().setTitle("Tasks").setDescription(text).build()).complete();
                length = 0;
                text = "";
            }
            text = text.concat(task);
            length += task.length();
        }
    }
}
