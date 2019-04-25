package net.cloudsbots.archseriest.archt5.commands.Core;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.commands.Command;
import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.cloudsbots.archseriest.archt5.plugin.PluginPackaging;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandPlugins extends Command {

    /**
     * Runs the command !plugins. This command shows the user all potentially useful data including statistics and core info.
     * This includes loaded plugins, IDs, Session Info, etc.
     *
     * Version Target: ADP19 Q1 Major
     *
     * @param message - Original message
     * @param args - Split command
     */
    @Override
    protected void run(Message message, String[] args) {
        EmbedBuilder e = new EmbedBuilder().setTitle("Plugins").setDescription("Here are all the plugins registered currently.").setColor(Color.green);
        e.addField("Core Plugin", "Private Plugin - No details are specified", true);
        for(PluginPackaging plg:Bot.getBot().getPluginManager().getPlugins().values()){
            e.addField(plg.getId(), "Description: "+plg.getDescription()+"\nVersion: "+plg.getVersion()+"\nAuthors: "+plg.getAuthors(), false);
        }
        message.getChannel().sendMessage(e.build()).queue();
    }
}
