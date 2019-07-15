package io.cloudsbots.arch.archt5.plugin;

import io.cloudsbots.arch.archt5.components.Logger;
import io.cloudsbots.arch.archt5.exceptions.*;
import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.config.ConfigurationFile;
import io.cloudsbots.arch.archt5.exceptions.*;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class PluginManager extends ClassLoader{

    //TODO: Deprecate the whole loader.
    // Replacing the Plugin manager should not break old plugins. Old functions will direct plugins
    // to newer functions and provide a deprecation warning.

    //TODO: Implement a security manager and remove the use of the SysPlugin.

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

    private String convertClassPath(String path, convertiontype outputtype){

        String fpath = path;

        switch(outputtype){
            case PATH:
                fpath = path.replace('.', '/')+".class";
                break;
            case CLASS:
                fpath = path.replace('/', '.');
                if(fpath.endsWith(".class")) {
                    fpath = fpath.substring(0, fpath.length() - 6);
                }
                break;
        }
        return fpath;
    }

    public HashMap<String, PluginPackaging> getPlugins(){
        return plgs;
    }

    public void loadPlugin(String pathToJar) throws Exception{
        try {
            PluginLoadData plgloaddat = loadPluginClassesWithQueue(pathToJar);
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

    private PluginJarData loadPlgConfig(JarFile jar, String pathToJar) throws IOException, InvalidConfigException, InvalidFormatException {

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
        return new PluginJarData(jarlisting, new ConfigurationFile(r, "id", "mainclass", "structure"));
    }

    private PluginLoadData loadPluginClassesWithQueue(String pathToJar){
        if(!new File(pathToJar).exists()){ throw new PluginNotFoundException("The plugin jar at '"+pathToJar+"' doesn't exist. Aborting load."); }
        try {
            JarFile jar = new JarFile(new File(pathToJar));
            Logger.getLogger().logInfo("PluginManager/Load", "Begging Load of plugin '" + pathToJar + "'");
            Logger.getLogger().logInfo("PluginManager/Load", "Locating plugin info (" + pathToJar + ")");
            PluginJarData jardat = loadPlgConfig(jar, pathToJar);
            ConfigurationFile config = jardat.getPlgconfig();
            List<String> jarlisting = jardat.getJarlisting();
            Logger.getLogger().logInfo("PluginManager/Load", "Loaded info. Checks passed. Info is valid. ("+pathToJar+")");
            String mainclasspath = (String) config.getConfig().get("mainclass");
            String mainfilepath = convertClassPath(mainclasspath, convertiontype.PATH);
            Logger.getLogger().logInfo("PluginManager/Load", "MainClassPath - "+mainclasspath+" MainFilePath - "+mainfilepath);
            if(!jarlisting.contains(mainfilepath)) { throw new InvalidConfigException("Property 'mainclass' must point to a class - '" + mainfilepath+ "' is not present."); }
            Logger.getLogger().logInfo("PluginManager/Load", "Beginning load of classes ("+pathToJar+")");

            Class plgmain = null;
            List<String> additionals = new ArrayList<>();

            List<String> queue = new ArrayList<>();

            for(String jarlisti:jarlisting){
                if(jarlisti.endsWith(".class")){
                    queue.add(jarlisti);
                }
            }

            while(queue.size() > 0){
                JarEntry entry = jar.getJarEntry(queue.get(0));
                Logger.getLogger().logInfo("PluginManager/Load","Loading Class from queue '"+entry.getName()+"' ("+String.valueOf(queue.size()-1)+" left...");
                InputStream classInput = jar.getInputStream(entry);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int data = classInput.read();
                while(data != -1){
                    buffer.write(data);
                    data = classInput.read();
                }
                classInput.close();
                byte[] classdat = buffer.toByteArray();
                String location = entry.getName();
                String path = convertClassPath(entry.getName(), convertiontype.CLASS);
                queue.remove(0);
                if(path.equals(mainclasspath)){
                    Logger.getLogger().logInfo("PluginManager/Load","Main Class registering @ "+path);
                    try {
                        plgmain = defineClass(path, classdat, 0, classdat.length);
                    } catch (NoClassDefFoundError err){
                        if(queue.contains(err.getMessage()+".class")){
                            queue.remove(err.getMessage()+".class");
                            queue.add(0, err.getMessage()+".class");
                            queue.add(1, location);
                        } else {
                            Logger.getLogger().logInfo("PluginManager/Load","Clearing queue. Required dependancy Main Class for @ "+path+" was not present. Aborting load.");
                            queue.clear();
                        }
                    }
                } else {
                    Logger.getLogger().logInfo("PluginManager/Load","Additional Class registering @ "+path);
                    try {
                        defineClass(path, classdat, 0, classdat.length);
                        additionals.add(path);
                    } catch (NoClassDefFoundError err) {
                        if(queue.contains(err.getMessage()+".class")){
                            queue.remove(err.getMessage()+".class");
                            queue.add(0, err.getMessage()+".class");
                            queue.add(1, location);
                        } else {
                            Logger.getLogger().logInfo("PluginManager/Load","Clearing queue. Required dependancy for @ "+path+" was not present. Aborting load.");
                            queue.clear();
                        }
                    }

                }
                classdat = null;
            }

            Logger.getLogger().logInfo("PluginManager/Load", "Cleaning Up. ("+pathToJar+")");
            jar.close();
            Logger.getLogger().logInfo("PluginManager/Load", "Loaded classes. Beginning Enable process. ("+pathToJar+")");
            return new PluginLoadData(plgmain, additionals, config);
        } catch (IOException err) {
            throw new RethrownException(err, false);
        } catch (InvalidConfigException err) {
            throw new RethrownException(err, false);
        } catch (InvalidFormatException err) {
            throw new RethrownException(err, false);
        }

    }

    // -------------------------------------------------------

    private enum convertiontype{
        CLASS, PATH
    }

    private class PluginJarData {
        private List<String> jarlisting;
        private ConfigurationFile plgconfig;
        public PluginJarData(List<String> jarlisting, ConfigurationFile plgconfig){
            this.jarlisting = jarlisting;
            this.plgconfig = plgconfig;
        }
        public ConfigurationFile getPlgconfig() { return plgconfig; }
        public List<String> getJarlisting() { return jarlisting; }
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
