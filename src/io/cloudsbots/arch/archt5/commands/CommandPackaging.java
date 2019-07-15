package io.cloudsbots.arch.archt5.commands;

import io.cloudsbots.arch.archt5.components.Validator;
import io.cloudsbots.arch.archt5.tasks.ArchTask;
import net.dv8tion.jda.core.entities.Message;

import java.util.UUID;

public final class CommandPackaging {

    private UUID uuid;
    private Message msg;
    private Command cmd;
    private String[] args;
    private ArchTask parent;

    public CommandPackaging(Message message, String[] args, ArchTask parent, Command command, UUID uuid){
        super();
        Validator.notNull(message, "Command Packaging [message] cannot be null");
        Validator.notNull(args, "Command Packaging [args] cannot be null");
        Validator.notNull(command, "Command Packaging [command] cannot be null");
        Validator.notNull(uuid, "Command Packaging [uuid] cannot be null");
        this.uuid = uuid;
        this.msg = message;
        this.parent = parent;
        this.cmd = command;
        this.args = args;
    }

    public UUID getUuid() { return uuid; }
    public Message getMessage() { return msg; }
    public Command getCmd() { return cmd; }
    public String[] getArgs() { return args; }
    public ArchTask getParentTask() { return parent; }
}
