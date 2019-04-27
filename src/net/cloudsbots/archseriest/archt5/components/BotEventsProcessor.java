package net.cloudsbots.archseriest.archt5.components;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.behaviorchunks.BehaviorManager;
import net.cloudsbots.archseriest.archt5.components.UtilityFunctions;
import net.cloudsbots.archseriest.archt5.components.Validator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class BotEventsProcessor extends ListenerAdapter {

    @Override
    public void onDisconnect(DisconnectEvent event) {
        Bot.getBot().setDiscord_active(false);
        Bot.getBot().getLogger().logWarn("APIEvents", "Bot Client API Disconnect detected.", " Any services requiring a bot account should have been halted", "There will be an log entry after a bot reconnect.", "Any errors after this could be due to plugins not integrating checks properly.");
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        Bot.getBot().setDiscord_active(true);
        Bot.getBot().setJDA(event.getJDA());
        Bot.getBot().getLogger().logWarn("APIEvents", "Bot Client API Reconnect detected.", "The Bot Client API Service has been restored.");

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Statistics.getStatisticsHub().incrementMessagesProcessed();

        //Hard Coded to stop Plugins from deleting it.
        if(event.getMessage().getContentRaw().equals("!xstop")){
            if(event.getAuthor().getId().equals("167516428767854592")) {
                Bot.getBot().getLogger().logInfo("Commands/Hardcoded", "Stopping bot via hardcoded command.", "Shutdown initiated by <@"+event.getAuthor().getId()+">");
                Bot.shutdown(1000);
            }
            return;
        }
        if(event.getMessage().getContentRaw().equals("!xinfo")){
            if(event.getAuthor().getId().equals("167516428767854592")) {
                Bot.getBot().getLogger().logInfo("Commands/Hardcoded", "Showing bot info via hardcoded command. ", "@NOTICE: Contains sensitive info - Shouldn't be public. (Token)");
                EmbedBuilder e = new EmbedBuilder().setTitle("Bot Info (Development)").setDescription("Do not use for regular use. Only use when modifying the bot's source code as it contains sensitive components.");
                e.addField("Client Info", "=======================", false);
                e.addField("Bot ID", Bot.getBot().getJDA().getSelfUser().getId(), true);
                e.addField("Branch/Version", Bot.BRANCH_NAME+"/"+Bot.BUILD_VERSION, true);
                e.addField("API Structure", String.valueOf(Bot.BUILD_STRUCTURE), true);
                e.addField("Event Listeners", String.valueOf(Bot.getBot().getEventManager().getListeners().size()), true);
                e.addField("Plugins", "Disabled", true);
                e.addField("Misc", "=======================", false);
                e.addField("Config", Bot.getBot().getSessionConfig().toString(), true);
                e.addField("Session UUID", Bot.getBot().getSession().toString(), true);
                event.getChannel().sendMessage(e.setColor(Color.RED).build()).complete();
            }
            return;
        }
        if(event.getMessage().getContentRaw().equals("!xdump")){
            Bot.getBot().getLogger().logInfo("Commands/Hardcoded", "Dumping log on request.", "@NOTICE: May contain sensitive info.");
            Bot.getBot().getLogger().dumpLog("FORCE", UtilityFunctions.dateString());
            return;
        }

        if(event.getMessage().getContentRaw().startsWith(Validator.verifyType(Bot.getBot().getSessionConfig().get("prefix"), String.class))){
            String prefix = (String) Bot.getBot().getSessionConfig().get("prefix");
            BehaviorManager.getBehaviorManager().runBehavior("CMD_PCS", prefix, event.getMessage());
        }
    }
}
