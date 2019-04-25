package net.cloudsbots.archseriest.archt5.commands.Core;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.commands.Command;
import net.cloudsbots.archseriest.archt5.commands.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CommandHelp extends Command {

    //TODO: Replace this command with a Form UI to shorten list or add categories.

    /**
     * Runs the command !help. It lists all the commands with their descriptios and usages and it lists all of them in
     * one page Requires updating at a later point and only stores commands registered with the central command manager.
     *
     * Version Target: ADP19 Q1 Major
     *
     * @param message - Original message
     * @param args - Split command
     */
    @Override
    protected void run(Message message, String[] args) {
        HashMap<String, Command> cmdmap = CommandManager.getCommandManager().getCmdmap();

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Commands").setDescription("Here's a list of all the currently registered commands with descriptions and usages.\n\n------------\n\n------------").setAuthor("System Command");

        for(Map.Entry<String, Command> set: cmdmap.entrySet()){
            embedBuilder.addField(set.getKey(), set.getValue().getDescription() + "\n\n*Usage:* "+ Bot.getBot().getSessionConfig().get("prefix").toString() + set.getValue().getUsage(), false);
        }

        embedBuilder.setColor(Color.magenta);
        message.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
