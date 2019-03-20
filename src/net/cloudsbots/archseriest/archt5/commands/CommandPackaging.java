package net.cloudsbots.archseriest.archt5.commands;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;
import net.dv8tion.jda.core.entities.Message;

import java.util.UUID;

public final class CommandPackaging extends Thread{

    private UUID uuid;
    private Message msg;
    private Command cmd;

    public CommandPackaging(Message message, Command command, UUID uuid){
        super();
        Validator.notNull(message, "Command Packaging [message] cannot be null");
        Validator.notNull(command, "Command Packaging [command] cannot be null");
        Validator.notNull(uuid, "Command Packaging [uuid] cannot be null");
        this.uuid = uuid;
        this.msg = message;
        this.cmd = command;
    }

    @Override
    public void run() {
        CommandManager.getCommandManager().registerCommandPackage(this);
        cmd.execute(msg);
        CommandManager.getCommandManager().removeCommandPackage(this);
    }

    public UUID getUuid() { return uuid; }
    public Message getMessage() { return msg; }
}
