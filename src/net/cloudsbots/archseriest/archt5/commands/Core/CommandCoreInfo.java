package net.cloudsbots.archseriest.archt5.commands.Core;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.commands.Command;
import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandCoreInfo extends Command {

    /**
     * Runs the command !coreinfo. This command shows the user all potentially useful data including statistics and core info.
     * This includes loaded plugins, IDs, Session Info, etc.
     *
     * Version Target: 0.2.2
     *
     * @param message - Original message
     * @param args - Split command
     */
    @Override
    protected void run(Message message, String[] args) {
        if(message.getAuthor().getId().equals("167516428767854592")) {
            Bot.getBot().getLogger().logInfo("Commands/Hardcoded", "Showing bot info via hardcoded command. ");
            EmbedBuilder e = new EmbedBuilder().setTitle("Bot Info (Development)").setDescription("Here's a bunch of useful bot info todo with it's core processes.");
            e.addField("Client Info", "=======================", false);
            e.addField("Bot ID", Bot.getBot().getJDA().getSelfUser().getId(), true);
            e.addField("Branch/Version", Bot.BRANCH_NAME+"/"+Bot.BUILD_VERSION, true);
            e.addField("API Structure", String.valueOf(Bot.BUILD_STRUCTURE), true);
            e.addField("Event Listeners", String.valueOf(Bot.getBot().getEventManager().getListeners().size()), true);
            String plgs = "";
            for(String id:Bot.getBot().getPluginManager().getPlugins().keySet()){ plgs = plgs.concat(id+"\n"); }
            e.addField("Plugins", plgs, true);
            e.addField("Misc", "=======================", false);
            e.addField("Session UUID", Bot.getBot().getSession().toString(), true);
            message.getChannel().sendMessage(e.setColor(Color.RED).build()).complete();
            EmbedBuilder eb = new EmbedBuilder().setTitle("Statistics").setDescription("Here are some statistics the bot logs while operating.").setColor(Color.yellow);
            eb.addField("Running since:", Statistics.getStatisticsHub().getStartTime(), true);
            eb.addField("Behaviors Ran:", String.valueOf(Statistics.getStatisticsHub().getBehaviorsRan()), true);
            eb.addField("Commands Ran:", String.valueOf(Statistics.getStatisticsHub().getCommandsRan()), true);
            eb.addField("Events Fired:", String.valueOf(Statistics.getStatisticsHub().getEventsFired()), true);
            eb.addField("Messages Processed:", String.valueOf(Statistics.getStatisticsHub().getMessagesProcessed()) + " (Not Stored)", true);
            eb.addField("Servers", String.valueOf(Bot.getBot().getJDA().getGuilds().size()), true);
            int total = 0;
            for(Guild g: Bot.getBot().getJDA().getGuilds()){ total += g.getMembers().size(); }
            eb.addField("Total Members:", String.valueOf(total), true);
            message.getChannel().sendMessage(eb.build()).queue();
        }
        return;
    }
}
