package net.cloudsbots.archseriest.archt5.commands;

import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.dv8tion.jda.core.entities.Message;

public abstract class Command {

    private String description = "No description provided. Test Command?";
    private String usage = "(No usage specified)";

    /**
     * Runs the command and increments the statistics. Can be used to add pre-command processes which either prepare a command
     * more or run a behavior first. Currently, none of them can be added without modifying the source.
     *
     * Version Target: ADP19 Q1 Major
     *
     * @param message - Original message
     * @param args - Split command
     */
    public final void execute(Message message, String[] args){
        Statistics.getStatisticsHub().incrementCommandsRan();
        run(message, args);
    }

    /**
     * Runs the command which inherits the class. Inheritors are required to modify this to add functionality to their commands.
     *
     * @param message - Original message
     * @param args - Split command
     */
    protected abstract void run(Message message, String[] args);

    /**
     * Sets tbe description of a command for registering.
     * @param desc - What you want the description to be.
     * @return Returns command object so more modifiers can be applied.
     */
    public final Command setDescription(String desc){
        description = desc;
        return this;
    }

    /**
     * Sets tbe usage of a command for registering.
     * @param usage - What you want the usage description to be.
     * @return Returns command object so more modifiers can be applied.
     */
    public final Command setUsage(String usage){
        this.usage = usage;
        return this;
    }

    /**
     * @return Returns the description of the command object.
     */
    public String getDescription() { return description; }

    /**
     * @return Returns the usage of the command object.
     */
    public String getUsage() { return usage; }
}
