package net.cloudsbots.archseriest.archt5.commands.Core;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.commands.Command;
import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandBotStats extends Command {

    /**
     * Runs the command !botstats. This command shows the statistics the bot measures via the Statistics
     * Manager. It just tallies actions and is included with the new !coreinfo so it's only really useful
     * if you set the permissions of !coreinfo to admins only.
     *
     * Version Target: ADP19 Q1 Major
     *
     * @param message - Original message
     * @param args - Split command
     */
    @Override
    protected void run(Message message, String[] args) {
        EmbedBuilder e = new EmbedBuilder().setTitle("Statistics").setDescription("Here are some statistics the bot logs while operating.").setColor(Color.yellow);
        e.addField("Running since:", Statistics.getStatisticsHub().getStartTime(), true);
        e.addField("Behaviors Ran:", String.valueOf(Statistics.getStatisticsHub().getBehaviorsRan()), true);
        e.addField("Commands Ran:", String.valueOf(Statistics.getStatisticsHub().getCommandsRan()), true);
        e.addField("Events Fired:", String.valueOf(Statistics.getStatisticsHub().getEventsFired()), true);
        e.addField("Messages Processed:", String.valueOf(Statistics.getStatisticsHub().getMessagesProcessed()) + " (Not Stored)", true);
        e.addField("Servers", String.valueOf(Bot.getBot().getJDA().getGuilds().size()), true);
        int total = 0;
        for(Guild g: Bot.getBot().getJDA().getGuilds()){ total += g.getMembers().size(); }
        e.addField("Total Members:", String.valueOf(total), true);
        message.getChannel().sendMessage(e.build()).queue();
    }
}
