package net.cloudsbots.archseriest.archt5.plugin;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.Main;
import net.cloudsbots.archseriest.archt5.components.Logger;
import net.cloudsbots.archseriest.archt5.config.ConfigurationFile;
import net.cloudsbots.archseriest.archt5.exceptions.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public final class PluginManager extends ClassLoader{

    private HashMap<String, PluginPackaging> plgs;
    private HashMap<Plugin, PluginState> states;

    private PluginPackaging sysPlugin;

    public PluginManager(PluginPackaging systemPlugin ){
        this.sysPlugin = systemPlugin;
        this.plgs = new HashMap<>();
        this.states = new HashMap<>();
    }


    public boolean checkSystemPlugin(PluginPackaging pluginPackaging){ return pluginPackaging == sysPlugin; }

    public PluginState getPluginState(PluginPackaging plugin){ return states.getOrDefault(plugin.getPlugin(), PluginState.UNLOADED); }
    public PluginState getPluginState(Plugin plugin){ return states.getOrDefault(plugin, PluginState.UNLOADED); }
    public void setPluginState(Plugin plugin, PluginState state){
        if(states.containsKey(plugin)){
            states.remove(plugin);
        }
        states.put(plugin, state);
    }

    /**
     * Checks if a plugin is <b>loaded</b> rather than if it's enabled or not.
     *
     * @param name The name of the plugin
     * @return If it exists, true.
     */
    public boolean doesPluginExist(String name) { return plgs.containsKey(name.toLowerCase()); }

    public PluginPackaging getPlugin(String name) {
        if(doesPluginExist(name)){
            return plgs.get(name.toLowerCase());
        } else {
            throw new PluginNotFoundException("Plugin '"+name.toLowerCase()+"' has not been registered on the bot.");
        }
    }

    public HashMap<String, PluginPackaging> getPlugins(){
        return plgs;
    }

    public void loadPlugin(String pathToJar) throws Exception{
        try {
            PluginLoadData plgloaddat = loadPlgClasses(pathToJar);
            Class plgclass = plgloaddat.getMain();
            ConfigurationFile configurationFile = plgloaddat.getConfigurationFile();
            Object plg = plgclass.newInstance();
            if(!(plg instanceof Plugin)){
                plg = null;
                throw new InvalidPluginException("Main Class of plugin did not inherit 'net.cloudsbots.archseriest.archt5.plugin.Plugin'");
            }
            if(plg instanceof SystemPlugin){
                plg = null;
                throw new PermissionDeniedException("Main Class of plugin is forbidden from inheriting 'net.cloudsbots.archseriest.archt5.plugin.SystemPlugin' (Should be impossible???)");
            }
            Plugin plugin = (Plugin) plg;
            plugin.setBot(Bot.getBot());
            String id = (String) configurationFile.getConfig().get("id");
            id = id.toLowerCase();
            String desc = (String) configurationFile.getConfig().getOrDefault("description", "Unspecified. But the developer that they aren't explaining their plugin. CG360 was here <o/");
            String authors = (String) configurationFile.getConfig().getOrDefault("authors", "Literally no one made this it appears...");
            String version = (String) configurationFile.getConfig().getOrDefault("version", "Unspecified (1.0)");
            Integer struct = (Integer) configurationFile.getConfig().get("structure");
            PluginPackaging packaging = new PluginPackaging(plugin, id, "Test Description", authors, version, struct);
            plugin.setPackaging(packaging);
            plgs.put(id, packaging);
            Logger.getLogger().logInfo("PluginManager/Enable", "Enabling plugin '"+id+"' ");
            if(struct != Bot.BUILD_STRUCTURE){
                Logger.getLogger().logWarn("PluginManager/Enable", "Plugin '"+id+"' is not made for the latest build structure (Latest: "+Bot.BUILD_STRUCTURE+" - Plugin: "+struct+")");
            }
            plugin.enablePlugin();
        } catch (InstantiationException err){
            try {
                throw new InstantiationException("An InstantiationException was thrown whilst loading '" + pathToJar + "' (" + err.getMessage() + ")");
            } catch (InstantiationException ee){
                throw new RethrownException(ee, false);
            }
        } catch (IllegalAccessException err) {
            try {
                throw new IllegalAccessException("An IllegalAccessException was thrown whilst loading '" + pathToJar + "' (" + err.getMessage() + ")");
            } catch (IllegalAccessException ee){
                throw new RethrownException(ee, false);
            }

        }
    }

    private PluginLoadData loadPlgClasses(String pathToJar) throws Exception {
        //TODO: Implement Plugin Loader

        if(!new File(pathToJar).exists()){ throw new PluginNotFoundException("The plugin jar at '"+pathToJar+"' doesn't exist. Aborting load."); }
        try{
            Logger.getLogger().logInfo("PluginManager/Load", "Begging Load of plugin '"+pathToJar+"'");
            Logger.getLogger().logInfo("PluginManager/Load", "Locating plugin info ("+pathToJar+")");
            JarFile jar = new JarFile(new File(pathToJar));
            int entrieslist = 0;
            Enumeration<JarEntry> jarentries = jar.entries();
            InputStream plugincfg = null;
            List<String> jarlisting = new ArrayList<>();
            Logger.getLogger().logInfo("PluginManager/Load","Jar Entry Structure:");
            while(jarentries.hasMoreElements()){
                entrieslist++;
                JarEntry entry = jarentries.nextElement();
                jarlisting.add(entry.getName());
                Logger.getLogger().logAppend("at -> ", entry.getName());
                if(entry.getName().contains("plugin.cfgp")){
                    plugincfg = jar.getInputStream(entry);
                }
            }
            if(plugincfg == null){ throw new InvalidPluginException("The jar at '"+pathToJar+"' is invalid as it does not contain a plugin.cfgp file."); }
            Logger.getLogger().logInfo("PluginManager/Load", "Located plugin info! Loading info... ("+pathToJar+")");
            BufferedReader r = new BufferedReader(new InputStreamReader(plugincfg));
            ConfigurationFile config = new ConfigurationFile(r, "id", "mainclass", "structure");
            Logger.getLogger().logInfo("PluginManager/Load", "Loaded info. Checks passed. Info is valid. ("+pathToJar+")");
            String mainpath = (String) config.getConfig().get("mainclass");
            String[] mainsplit = mainpath.split(Pattern.quote("."));
            String reformatmainpath = "";
            for(String spt: mainsplit){
                reformatmainpath = reformatmainpath.concat("/"+spt);
            }
            reformatmainpath = reformatmainpath.substring(1);
            reformatmainpath = reformatmainpath.concat(".class");
            if(!jarlisting.contains(reformatmainpath)) { throw new InvalidConfigException("Property 'mainclass' must point to a class - '" + reformatmainpath+ "' is not present."); }
            Logger.getLogger().logInfo("PluginManager/Load", "Beginning load of classes ("+pathToJar+")");
            // Loading of Classes is below -------------------------------------------------------------------------------------------------------------------------------------------
            Class plgmain = null;
            List<String> additionals = new ArrayList<>();
            Enumeration<JarEntry> classentries = jar.entries();
            while(classentries.hasMoreElements()){
                JarEntry entry = classentries.nextElement();
                if(entry.getName().endsWith(".class")){
                    Logger.getLogger().logInfo("PluginManager/Load","5");
                    InputStream classInput = jar.getInputStream(entry);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    Logger.getLogger().logInfo("PluginManager/Load","6");
                    int data = classInput.read();
                    Logger.getLogger().logInfo("PluginManager/Load","7");
                    while(data != -1){
                        buffer.write(data);
                        data = classInput.read();
                    }
                    Logger.getLogger().logInfo("PluginManager/Load","8");
                    classInput.close();
                    Logger.getLogger().logInfo("PluginManager/Load","3");
                    byte[] classdat = buffer.toByteArray();
                    String p = entry.getName().replace('/', '.');
                    String path = p.substring(0, p.length()-6);
                    Logger.getLogger().logInfo("PluginManager/Load","4");
                    if(path.equals(mainpath)){
                        Logger.getLogger().logInfo("PluginManager/Load","2");
                        Logger.getLogger().logInfo("PluginManager/Load","Main Class registering @ "+path);
                        plgmain = defineClass(path, classdat, 0, classdat.length);
                    } else {
                        Logger.getLogger().logInfo("PluginManager/Load","1");
                        Logger.getLogger().logInfo("PluginManager/Load","Additional Class registering @ "+path);
                        defineClass(path, classdat, 0, classdat.length);
                        additionals.add(path);
                    }
                    classdat = null;
                }
            }
            Logger.getLogger().logInfo("PluginManager/Load", "Cleaning Up. ("+pathToJar+")");
            classentries = null;
            jarentries = null;
            jar.close();
            Logger.getLogger().logInfo("PluginManager/Load", "Loaded classes. Beginning Enable process. ("+pathToJar+")");

            return new PluginLoadData(plgmain, additionals, config);


        } catch (IOException e) {
            try {
                throw new IOException("An IOException was thrown whilst loading '" + pathToJar + "' (" + e.getMessage() + ")");
            } catch (IOException ee){
                throw new RethrownException(ee, false);
            }
        } catch (InvalidConfigException e){
            try {
                throw new InvalidConfigException("An InvalidConfigException was thrown whilst loading'" + pathToJar + "' (" + e.getMessage() + ")");
            } catch (InvalidConfigException ee){
                throw new RethrownException(ee, false);
            }
        } catch (InvalidFormatException e){
            throw new InvalidFormatException("An InvalidFormatException was thrown whilst loading'"+pathToJar+"' ("+e.getMessage()+")");
        }
    }

    private class PluginLoadData {

        private Class main;
        private List<String> classes;
        private ConfigurationFile configurationFile;

        public PluginLoadData(Class main, List<String> classes, ConfigurationFile configurationFile){
            this.main = main;
            this.classes = classes;
            this.configurationFile = configurationFile;
        }

        public Class getMain() { return main; }
        public List<String> getClasses() { return classes; }
        public ConfigurationFile getConfigurationFile() { return configurationFile; }
    }

}
