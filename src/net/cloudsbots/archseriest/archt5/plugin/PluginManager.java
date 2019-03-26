package net.cloudsbots.archseriest.archt5.plugin;

import javafx.util.Pair;
import net.cloudsbots.archseriest.archt5.Bot;
import net.cloudsbots.archseriest.archt5.Main;
import net.cloudsbots.archseriest.archt5.components.Logger;
import net.cloudsbots.archseriest.archt5.config.ConfigurationFile;
import net.cloudsbots.archseriest.archt5.exceptions.InvalidConfigException;
import net.cloudsbots.archseriest.archt5.exceptions.InvalidFormatException;
import net.cloudsbots.archseriest.archt5.exceptions.PluginNotFoundException;
import net.cloudsbots.archseriest.archt5.exceptions.RethrownException;

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

    public void loadPlugin(String pathToJar){
        try {
            Pair<Class, ConfigurationFile> plgpair = loadPlgClasses(pathToJar);
            Class plgclass = plgpair.getKey();
            ConfigurationFile configurationFile = plgpair.getValue();
            Plugin plugin = (Plugin) plgclass.newInstance();
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
            Logger.getLogger().logInfo("PluginManager/Enable", "Enabling '"+id+"'", "Version: "+version+"(For API Structure "+struct+")", "Authors: "+authors, "Description: "+desc);
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

    private Pair<Class, ConfigurationFile> loadPlgClasses(String pathToJar) throws PluginNotFoundException, RethrownException {
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
            while(jarentries.hasMoreElements()){
                entrieslist++;
                JarEntry entry = jarentries.nextElement();
                jarlisting.add(entry.getName());
                if(entry.getName().contains("plugin.cfgp")){
                    plugincfg = jar.getInputStream(entry);
                }
            }
            if(plugincfg == null){ throw new PluginNotFoundException("The jar at '"+pathToJar+"' is invalid as it does not contain a plugin.cfgp file."); }
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

            if(!jarlisting.contains(reformatmainpath)) {
                throw new InvalidConfigException("Property 'mainclass' must point to a class - '" + reformatmainpath+ "' is not present.");

            }

            Logger.getLogger().logInfo("PluginManager/Load", "Beginning load of classes ("+pathToJar+")");

            // Loading of Classes is below

            Class plgmain = null;

            Enumeration<JarEntry> classentries = jar.entries();
            while(classentries.hasMoreElements()){
                JarEntry entry = classentries.nextElement();
                if(entry.getName().endsWith(".class")){
                    InputStream classInput = jar.getInputStream(entry);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int data = classInput.read();

                    while(data != -1){
                        buffer.write(data);
                        data = classInput.read();
                    }

                    classInput.close();

                    byte[] classdat = buffer.toByteArray();

                    String p = entry.getName().replace('/', '.');
                    String path = p.substring(0, p.length()-6);
                    if(path.equals(mainpath)){
                        plgmain = defineClass(path, classdat, 0, classdat.length);
                    } else {
                        defineClass(path, classdat, 0, classdat.length);
                    }
                    classdat = null;
                }
            }

            Pair<Class, ConfigurationFile> pair = new Pair<>(plgmain, config);
            Logger.getLogger().logInfo("PluginManager/Load", "Cleaning Up. ("+pathToJar+")");

            classentries = null;
            jarentries = null;
            jar.close();

            Logger.getLogger().logInfo("PluginManager/Load", "Loaded classes. Beginning Enable process. ("+pathToJar+")");

            return pair;


        } catch (IOException e) {
            try {
                throw new InvalidConfigException("An IOException was thrown whilst loading '" + pathToJar + "' (" + e.getMessage() + ")");
            } catch (InvalidConfigException ee){
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



}
