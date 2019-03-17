package net.cloudsbots.archseriest.archt5.commands;

import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.dv8tion.jda.core.entities.Message;

public abstract class Command {

    private String description = "No description provided. Test Command?";
    private String usage = "(No usage specified)";

    public void execute(Message message){
        Statistics.getStatisticsHub().incrementCommandsRan();
        run(message);
    }

    protected abstract void run(Message message);

    public final Command setDescription(String desc){
        description = desc;
        return this;
    }
    public final Command setUsage(String usage){
        this.usage = usage;
        return this;
    }

    public String getDescription() { return description; }
    public String getUsage() { return usage; }
}
