package io.cloudsbots.arch.archt5.commands.Core;

import io.cloudsbots.arch.archt5.commands.Command;
import net.dv8tion.jda.core.entities.Message;

//TODO: Replace with TriArch as it's better designed for side scripts.
public class CommandTasks extends Command {

    /**
     * Runs the command !tasks. This command allows you to interface with TaskManager and control
     * the bot's core processes. You can either kill or start a process with looping behaviors being
     * added just for this command.
     *
     * Version Target: 0.3.0
     *
     * @param message - Original message
     * @param args - Split command
     */
    @Override
    protected void run(Message message, String[] args) {
        message.getChannel().sendMessage("Hey! This command is not finished. If I don't register it myself, it's not ready.").complete();
        /*
        if(args.length < 3){
            message.getChannel().sendMessage(new EmbedBuilder().setTitle("Error: Incorrect Usage").setDescription("Usage:\n!tasks kill <process> OR\n!tasks start <'Behavior'/'Looped Behavior'> <Behavior Name> [Loop interval in seconds (For Looped) ]").build()).complete();
            return;
        }

        if(args[1].equals("kill")){

            return;
        }
        if(args[1].equals("start")){

            return;
        }
        */
    }
}
