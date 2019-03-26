package net.cloudsbots.archseriest.archt5.plugin;

import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.behaviorchunks.BehaviorManager;
import net.cloudsbots.archseriest.archt5.behaviorchunks.CoreBehaviors.BehaviorCommandProcess;
import net.cloudsbots.archseriest.archt5.commands.CommandManager;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandBotStats;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandHelp;
import net.cloudsbots.archseriest.archt5.commands.Core.CommandPlugins;

public final class SystemPlugin extends Plugin{

    @Override
    protected void onEnable() {
        Bot.getBot().getLogger().logInfo("SysPlugin", "Dummy Plugin enabled for main system. Unloading it will unload all system commands and behaviors so be careful.");

        BehaviorManager.getBehaviorManager().registerBehavior("CMD_PCS", new BehaviorCommandProcess(), this.getPackaging());

        CommandManager.getCommandManager().registerCommand("help", new CommandHelp().setDescription("Lists all commands registered with the bot on one page. Placeholder Command").setUsage("help"), this.getPackaging());
        CommandManager.getCommandManager().registerCommand("botstats", new CommandBotStats().setDescription("Provides all the logged bot stats. No user stats.").setUsage("botstats"), this.getPackaging());
        CommandManager.getCommandManager().registerCommand("plugins", new CommandPlugins().setDescription("Provides a list of plugins").setUsage("plugins"), this.getPackaging());
    }

    @Override
    protected void onDisable(DisableReason cause) {
        if(cause == DisableReason.SECURITY){
            Bot.getBot().getLogger().logRuntimeError("SysPlugin", "Security disabled SysPlugin. Something's very wrong.", "Safeguards have been broken.", " **Please Report This @ https://github.com/CloudGamer360/Project-ArchT5/issues**", "Try to figure out which plugin broke these safeguards and report it in the issue.");
            Bot.shutdown(1024);
        }
    }
}
