package net.cloudsbots.archseriest.archt5.commands.Core;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.commands.Command;
import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandBotStats extends Command {

    @Override
    protected void run(Message message) {
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
