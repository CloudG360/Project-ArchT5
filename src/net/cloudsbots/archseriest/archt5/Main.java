package net.cloudsbots.archseriest.archt5;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.commands.CommandManager;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandBotStats;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandHelp;
import net.cloudsbots.archseriest.archt5.behaviorchunks.BehaviorManager;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorCommandProcess;
import net.cloudsbots.archseriest.archt5.components.BotEventsProcessor;
import net.cloudsbots.archseriest.archt5.components.CoreEventProcessor;
import net.cloudsbots.archseriest.archt5.components.Logger;
import net.cloudsbots.archseriest.archt5.config.ConfigurationFile;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;
import net.cloudsbots.archseriest.archt5.exceptions.PermissionDeniedException;
import net.cloudsbots.archseriest.archt5.exceptions.RethrownException;
import net.cloudsbots.archseriest.archt5.plugin.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.io.File;
import java.util.UUID;

public class Main {

    private static PluginPackaging plugin;
    private static String pass = UUID.randomUUID().toString();

    public static void main(String[] args){
        //-- Initialize system --

        Bot.setBot(new Bot(pass));

        Plugin plg = new SystemPlugin();
        plugin = new PluginPackaging(plg, "sys", "Core System Plugin", "CloudGamer360", "B1", 1);
        plugin.getPlugin().setPackaging(plugin);

        PluginManager plgManager = new PluginManager(plugin);
        Bot.getBot().setPluginManager(plgManager);

        plugin.getPlugin().enablePlugin();


        Bot.getBot().getEventManager().registerEventListeners(new CoreEventProcessor());


        //-- Load Client --
        new CallableEvent(plugin, "prebotconnect.config", new Pair<>("VERIF", Bot.getBot().getSession().toString()), new Pair<>("AVOID_LOGS", "")).call(EventChannel.SYSTEM, false);

        ConfigurationFile cfg;
        try{
            cfg = new ConfigurationFile(new File("./bot.cfgp"), "token");
        } catch (Exception err){
            Bot.getBot().getLogger().logRuntimeError("Bootloader/Config", "Unable to load required config file. Exiting process.");
            throw new RethrownException(err, true);
        }
        Bot.getBot().setSessionConfig(cfg.getConfig());

        Object t = Bot.getBot().getSessionConfig().get("token");
        if(!(t instanceof String)){
            Bot.getBot().getLogger().logFatal("Bootloader/Config", "Token Config entry was not of type String", "Tokens are required to be String types for bot creation.", "In the config, use '[string]' before 'token' to cast the type.");
        }
        String token = (String) t;

        new CallableEvent(plugin, "prebotconnect.build", new Pair<>("VERIF", Bot.getBot().getSession().toString()), new Pair<>("AVOID_LOGS", "")).call(EventChannel.SYSTEM, false);
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        try {
            Bot.getBot().setJDA(jdaBuilder.setAutoReconnect(true).setStatus(OnlineStatus.IDLE).setGame(Game.listening("Ver. " + Bot.BUILD_VERSION + "/" + Bot.BRANCH_NAME)).setToken(token).buildBlocking());
        } catch (Exception err) {
            throw new RethrownException(err, true);
        }
        Bot.getBot().setDiscord_active(true);


        //-- Bot Client Online After Here --
        new CallableEvent(plugin, "bot.discordonline", new Pair<>("VERIF", Bot.getBot().getSession().toString())).call(EventChannel.SYSTEM, false);
        Bot.getBot().getJDA().addEventListener(new BotEventsProcessor());

        new CallableEvent(plugin, "bot.ready", new Pair<>("VERIF", Bot.getBot().getSession().toString())).call(EventChannel.SYSTEM, false);


    }

    public static PluginPackaging getSysPlugin(String password) {
        if(password.toLowerCase().equals(pass.toLowerCase())) {
            return plugin;
        } else {
            throw new PermissionDeniedException("SysPlugin password denied. (Potentially malicious plugin?) ");
        }
    }
}

