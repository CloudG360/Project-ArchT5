package io.cloudsbots.arch.archt5.plugin;

import io.cloudsbots.arch.archt5.commands.CommandManager;
import io.cloudsbots.arch.archt5.commands.Core.CommandBotStats;
import io.cloudsbots.arch.archt5.commands.Core.CommandCoreInfo;
import io.cloudsbots.arch.archt5.commands.Core.CommandHelp;
import io.cloudsbots.arch.archt5.commands.Core.CommandPlugins;
import io.cloudsbots.arch.archt5.Bot;

public final class SystemPlugin extends Plugin{

    @Override
    protected void onEnable() {
        Bot.getBot().getLogger().logInfo("SysPlugin", "Dummy Plugin enabled for main system. Unloading it will unload all system commands and behaviors so be careful.");

        CommandManager.getCommandManager().registerCommand("help", new CommandHelp().setDescription("Lists all commands registered with the bot on one page. Placeholder Command").setUsage("help"), this.getPackaging());
        CommandManager.getCommandManager().registerCommand("botstats", new CommandBotStats().setDescription("Provides all the logged bot stats. No user stats.").setUsage("botstats"), this.getPackaging());
        CommandManager.getCommandManager().registerCommand("plugins", new CommandPlugins().setDescription("Provides a list of plugins").setUsage("plugins"), this.getPackaging());
        CommandManager.getCommandManager().registerCommand("core", new CommandCoreInfo().setDescription("Lists the bot's core info").setUsage("core"), this.getPackaging());
        //CommandManager.getCommandManager().registerCommand("tasks", new CommandTasks().setDescription("Allows the manipulation of tasks").setUsage("tasks"), this.getPackaging());


    }

    @Override
    protected void onDisable(DisableReason cause) {
        if(cause == DisableReason.SECURITY){
            Bot.getBot().getLogger().logRuntimeError("SysPlugin", "Security disabled SysPlugin. Something's very wrong.", "Safeguards have been broken.", " **Please Report This @ https://github.com/CloudGamer360/Project-ArchT5/issues**", "Try to figure out which plugin broke these safeguards and report it in the issue.");
            Bot.shutdown(1024);
        }
    }
}
