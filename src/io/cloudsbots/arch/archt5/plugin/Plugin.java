package io.cloudsbots.arch.archt5.plugin;

import io.cloudsbots.arch.archt5.Bot;

import java.util.List;

public class Plugin {

    private static Plugin plugin = new Plugin();

    private PluginPackaging packaging;
    private boolean hasPackagingSet = false;

    private Bot bot;
    private boolean hasBotSet = false;

    protected void onEnable(){ }
    protected void onDisable(DisableReason cause){ }

    public final void enablePlugin(){
        Bot.getBot().getPluginManager().setPluginState(this, PluginState.ENABLED);
        onEnable();
    }

    public final void disablePlugin(DisableReason cause){
        //TODO: Unregistering commands + Behaviors
        List<String> behaviorids = Bot.getBot().getBehaviorManager().getPluginBehaviors(this);
        for(String id: behaviorids){
            Bot.getBot().getBehaviorManager().defaultBehavior(this, id, "");
        }
        Bot.getBot().getCommandManager().unregisterAll(this);

        Bot.getBot().getPluginManager().setPluginState(this, PluginState.DISABLED);
        onDisable(cause);
    }

    public static Plugin getPlugin(){ return plugin; }
    public PluginPackaging getPackaging(){ return packaging; }

    public void setPackaging(PluginPackaging packaging){
        if(!hasPackagingSet) {
            this.packaging = packaging;
            hasPackagingSet = true;
        }
    }

    public void setBot(Bot bot){
        if(!hasBotSet) {
            this.bot = bot;
            hasBotSet = true;
        }
    }

}
