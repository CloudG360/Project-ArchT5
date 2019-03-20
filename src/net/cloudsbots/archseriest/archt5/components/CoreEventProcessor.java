package net.cloudsbots.archseriest.archt5.components;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.events.Event;
import net.cloudsbots.archseriest.archt5.events.EventListener;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class CoreEventProcessor extends EventListener{

    @Override
    protected void globalChannelEvent(Event event, String id) {
        if(event.containsTag("AVOID_LOGS") < 0){
            MessageEmbed embed =  new EmbedBuilder().setTitle("EVENT FIRED").setDescription("The event '"+event.getEventid()+"' was fired.\nReturn: "+event.getReturnaddress()).addField("Tags", ". "+event.getTags().toString(), true).setColor(Color.orange).build();
            Bot.getBot().getJDA().getTextChannelById("546319924599980044").sendMessage(embed).queue();
        }
    }
}
