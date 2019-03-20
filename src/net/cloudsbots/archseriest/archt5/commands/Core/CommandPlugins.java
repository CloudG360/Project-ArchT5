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

    @Override
    protected void run(Message message) {
        EmbedBuilder e = new EmbedBuilder().setTitle("Plugins").setDescription("Here are all the plugins registered currently.").setColor(Color.green);
        e.addField("Core Plugin", "Private Plugin - No details are specified", true);
        for(PluginPackaging plg:Bot.getBot().getPluginManager().getPlugins().values()){
            e.addField(plg.getId(), "Description: "+plg.getDescription()+"\nVersion: "+plg.getVersion()+"\nAuthors: "+plg.getAuthors(), false);
        }
        message.getChannel().sendMessage(e.build()).queue();
    }
}
