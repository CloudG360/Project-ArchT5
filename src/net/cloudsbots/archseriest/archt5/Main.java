package net.cloudsbots.archseriest.archt5;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.commands.CommandManager;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandBotStats;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandHelp;
import net.cloudsbots.archseriest.archt5.behaviorchunks.BehaviorManager;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorCommandProcess;
import net.cloudsbots.archseriest.archt5.components.BotEventsProcessor;
import net.cloudsbots.archseriest.archt5.components.CoreEventProcessor;
import net.cloudsbots.archseriest.archt5.config.ConfigurationFile;
import net.cloudsbots.archseriest.archt5.events.CallableEvent;
import net.cloudsbots.archseriest.archt5.events.EventChannel;
import net.cloudsbots.archseriest.archt5.exceptions.RethrownException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.io.File;

public class Main extends Bot{


    public static void main(String[] args){
        //-- Register Event Processor --
        Bot.getBot().getEventManager().registerEventListeners(new CoreEventProcessor());

        //-- Bot Client Online After Here --

        new CallableEvent("__SYSBOOT", "prebotconnect.config", new Pair<>("VERIF", Bot.getBot().getSession().toString()), new Pair<>("AVOID_LOGS", "")).call(EventChannel.SYSTEM, false);
        ConfigurationFile cfg;
        try{
            cfg = new ConfigurationFile(new File("./bot.cfgp"), "token");
        } catch (Exception err){
            Bot.getBot().getLogger().logRuntimeError("Bootloader/Config", "Unable to load required config file. Exiting process.");
            throw new RethrownException(err, true);
        }
        Bot.getBot().setSessionConfig(cfg.getConfig());
        Object t = Bot.getBot().getSessionConfig().get("token");
        if(!(t instanceof String)){ Bot.getBot().getLogger().logFatal("Bootloader/Config", "Token Config entry was not of type String", "Tokens are required to be String types for bot creation.", "In the config, use '[string]' before 'token' to cast the type."); }
        String token = (String) t;
        new CallableEvent("__SYSBOOT", "prebotconnect.build", new Pair<>("VERIF", Bot.getBot().getSession().toString()), new Pair<>("AVOID_LOGS", "")).call(EventChannel.SYSTEM, false);
        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);
        try { Bot.getBot().setJDA(jdaBuilder.setAutoReconnect(true).setStatus(OnlineStatus.IDLE).setGame(Game.listening("Ver. " + Bot.BUILD_VERSION + "/" + Bot.BRANCH_NAME)).setToken(token).buildBlocking()); } catch (Exception err) { throw new RethrownException(err, true); }
        Bot.getBot().setDiscord_active(true);


        //-- Bot Client Online After Here --


        new CallableEvent("__SYSBOOT", "bot.discordonline", new Pair<>("VERIF", Bot.getBot().getSession().toString())).call(EventChannel.SYSTEM, false);

        //Behaviors
        BehaviorManager.getBehaviorManager().registerBehavior("CMD_PCS", new BehaviorCommandProcess());


        //Commands
        CommandManager.getCommandManager().registerCommand("help", new CommandHelp().setDescription("Lists all commands registered with the bot on one page. Placeholder Command").setUsage("help"));
        CommandManager.getCommandManager().registerCommand("botstats", new CommandBotStats().setDescription("Provides all the logged bot stats. No user stats.").setUsage("botstats"));


        Bot.getBot().getJDA().addEventListener(new BotEventsProcessor());


        new CallableEvent("__SYS", "bot.ready", new Pair<>("VERIF", Bot.getBot().getSession().toString())).call(EventChannel.SYSTEM, false);


    }




}
