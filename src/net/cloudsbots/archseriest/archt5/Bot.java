package net.cloudsbots.archseriest.archt5;

import net.cloudsbots.archseriest.archt5.commands.CommandManager;
import net.cloudsbots.archseriest.archt5.behaviorchunks.BehaviorManager;
import net.cloudsbots.archseriest.archt5.components.Logger;
import net.cloudsbots.archseriest.archt5.components.Statistics;
import net.cloudsbots.archseriest.archt5.components.UtilityFunctions;
import net.cloudsbots.archseriest.archt5.events.EventManager;
import net.dv8tion.jda.core.JDA;

import java.util.HashMap;
import java.util.UUID;

public class Bot {

    private static Bot bot;
    private static Logger log;

    static {
        log = new Logger();
        Thread.setDefaultUncaughtExceptionHandler(log);
        bot = new Bot();
    }

    public Bot(){
        logger = log;
        statistics = new Statistics();
        eventManager = new EventManager();
        behaviorManager = new BehaviorManager();
        commandManager = new CommandManager();
    }

    public static final String BRANCH_NAME = "DEV5";
    public static final String MILESTONE_VERSION = "0.1.0";
    public static final String BUILD_VERSION = "ADP190901"; //Example - Alpha Version, Development Build, Private Beta, 2019, Week 9, Build 01 of that week = ADP185201
    public static final Integer BUILD_STRUCTURE = 1; // If there's a major code change which is breaking of old features, increment this.
    public static final String BUILD_DATE = "17/03/19"; // dd/mm/yy

    public static final String AUTHOR = "CloudG360";
    public static final String WEBSITE = "https://discordapp.com/";
    public static final String PREFIX = "Hey Arch! ";

    private EventManager eventManager;
    private Logger logger;
    private BehaviorManager behaviorManager;
    private CommandManager commandManager;
    private Statistics statistics;

    //Session Data
    private UUID session_uuid = UUID.randomUUID();
    private boolean discord_active = false;
    private JDA jda = null;
    private HashMap<String, Object> session_config = null;

    //Behavior Manager

    public EventManager getEventManager(){ return getBot().eventManager; }
    public UUID getSession(){ return getBot().session_uuid; }
    public JDA getJDA(){ return getBot().jda; }
    public Logger getLogger() { return getBot().logger; }
    public HashMap<String, Object> getSessionConfig() { return getBot().session_config; }
    public boolean isDiscordActive() { return discord_active; }
    public BehaviorManager getBehaviorManager() { return behaviorManager; }
    public CommandManager getCommandManager() { return commandManager; }
    public Statistics getStatisticsHub() { return statistics; }

    public void setJDA(JDA jda) { this.jda = jda; }
    public void setSession_uuid(UUID session_uuid) { this.session_uuid = session_uuid; }
    public void setDiscord_active(boolean discord_active) { this.discord_active = discord_active; }
    public void setSessionConfig(HashMap<String, Object> session_config) { getBot().session_config = session_config; }

    public static Bot getBot(){ return bot; }
    public static void shutdown(int code){

        getBot().getLogger().logInfo("CoreOp/Shutdown", String.valueOf(code));

        //Shutdown

        String id = (code==0) ? "LOG": "CRASH";

        getBot().getLogger().dumpLog(id, UtilityFunctions.dateString());
        System.exit(code);
    }
}
