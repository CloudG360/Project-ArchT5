package io.cloudsbots.arch.archt5.commands;

import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.components.Validator;
import io.cloudsbots.arch.archt5.tasks.ArchTask;
import net.dv8tion.jda.core.entities.Message;

import java.util.UUID;

public class CommandProcessThread extends ArchTask {


    @Override
    protected void run(String id) {
        Object[] parameters = (Object[]) Bot.getBot().getTaskManager().getThreadData(id).get("objects");
        if(parameters.length > 1) {
            Validator.verifyType(parameters[0], String.class, "@CmdProcess prefix is not of type 'string' - fix this!"); //Prefix
            Validator.verifyType(parameters[1], Message.class, "@CmdProcess message provided is not of type 'message'"); //Message

            Message msg = (Message) parameters[1];
            String text = ((Message) parameters[1]).getContentRaw();
            String prefix = (String) parameters[0];



            String[] command = text.substring(prefix.length()).split(" ");

            if(CommandManager.getCommandManager().getCmdmap().keySet().contains(command[0].toLowerCase())){
                CommandPackaging cmdpkg = new CommandPackaging(msg, command, this, CommandManager.getCommandManager().getCmdmap().get(command[0].toLowerCase()), UUID.randomUUID());
                CommandManager.getCommandManager().registerCommandPackage(cmdpkg);
                cmdpkg.getCmd().execute(msg, cmdpkg.getArgs());
                CommandManager.getCommandManager().removeCommandPackage(cmdpkg);
            }

        }
    }

    @Override
    public void cleanUp(String dataID) {

    }
}
