package net.cloudsbots.archseriest.archt5.plugin;

import net.cloudsbots.archseriest.archt5.Bot;

public class Plugin {

    private static Plugin plugin = new Plugin();

    private PluginPackaging packaging;
    private boolean hasPackagingSet = false;

    protected void onEnable(){ }
    protected void onDisable(DisableReason cause){ }

    public final void enablePlugin(){
        Bot.getBot().getPluginManager().setPluginState(this, PluginState.ENABLED);
        onEnable();
    }

    public final void disablePlugin(DisableReason cause){
        //TODO: Unregistering commands + Behaviors
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



}
