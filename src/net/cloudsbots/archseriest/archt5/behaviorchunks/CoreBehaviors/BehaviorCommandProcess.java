package net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors;

import net.cloudsbots.archseriest.archt5.commands.CommandManager;
import net.cloudsbots.archseriest.archt5.commands.CommandPackaging;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.behaviorchunks.Behavior;
import net.dv8tion.jda.core.entities.Message;

import java.util.UUID;

public class BehaviorCommandProcess extends Behavior {

    @Override
    protected void run(Object[] parameters) {
        if(parameters.length > 1) {
            Validator.verifyType(parameters[0], String.class); //Prefix
            Validator.verifyType(parameters[1], Message.class); //Message

            String text = ((Message) parameters[1]).getContentRaw();
            String prefix = (String) parameters[0];



            String[] command = text.substring(prefix.length()).split(" ");

            if(CommandManager.getCommandManager().getCmdmap().keySet().contains(command[0].toLowerCase())){
                CommandPackaging cmdpkg = new CommandPackaging((Message) parameters[1], command, CommandManager.getCommandManager().getCmdmap().get(command[0].toLowerCase()), UUID.randomUUID());
                cmdpkg.start();
            }

        }
    }

    @Override
    public void clean() {

    }
}
