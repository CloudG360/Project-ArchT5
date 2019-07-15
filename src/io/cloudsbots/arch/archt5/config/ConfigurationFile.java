package io.cloudsbots.arch.archt5.config;

import io.cloudsbots.arch.archt5.exceptions.InvalidFormatException;
import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.exceptions.InvalidConfigException;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ConfigurationFile {

    private HashMap<String, Object> config = new HashMap<>();
    private List<String> comments = new ArrayList<>();
    private File cfgfile;

    /**
     * Reads a config under the ConfigPlus format, creating a map
     * of keys and object values. These values can be converted by
     * the specified format in the config.
     *
     * @param file File Object which points towards config
     *
     * @throws IOException - Errors regarding read.
     * @throws InvalidFormatException - Config Has a format error
    */
    public ConfigurationFile(File file) throws IOException, InvalidFormatException{
        cfgfile = file;
        if(!cfgfile.exists()){ cfgfile.createNewFile(); }
        if(!cfgfile.getName().endsWith(".cfgp")){ throw new InvalidFormatException("Invalid file format. Please select a .cfgp file."); }
        Reader reader = new FileReader(file);
        BufferedReader read = new BufferedReader(reader);
        readFile(read);
    }

    /**
     * Reads a config under the ConfigPlus format, creating a map
     * of keys and object values. These values can be converted by
     * the specified format in the config.
     *
     * @param read Reader Object which points towards config
     * @throws InvalidFormatException - Config Has a format error
     */
    public ConfigurationFile(BufferedReader read) throws IOException, InvalidFormatException{
        readFile(read);
    }

    /**
     * Just like {@see #Constructer(File, String)} but
     * it does a few checks to ensure that some fields are
     * present in the config. Else it returns a InvalidConfigException
     *
     * @param file File Object which points towards config
     * @param requiredfields List of fields which are required in the config. Throws an error if one or more aren't present
     * @throws IOException
     * @throws InvalidFormatException
     * @throws InvalidConfigException Thrown if a required field is not present in the loaded config.
     */
    public ConfigurationFile(File file, String ... requiredfields) throws IOException, InvalidFormatException, InvalidConfigException{
        this(file);
        for (String r:requiredfields) { if(!getConfig().containsKey(r)){ throw new InvalidConfigException("Missing Field Error - Required Field '"+r+"' was not found in config '"+getConfigFile().getName()+"' ("+getConfig().keySet().toString()+")"); } }
    }

    public ConfigurationFile(BufferedReader reader, String ... requiredfields) throws IOException, InvalidFormatException, InvalidConfigException{
        this(reader);
        for (String r:requiredfields) { if(!getConfig().containsKey(r)){ throw new InvalidConfigException("Missing Field Error - Required Field '"+r+"' was not found in config '"+getConfigFile().getName()+"' ("+getConfig().keySet().toString()+")"); } }
    }

    private void readFile(BufferedReader read) throws IOException, InvalidFormatException{
        Iterator<String> cfglines = read.lines().iterator();
        int linen = 0;
        while (cfglines.hasNext()){
            String line = cfglines.next();
            linen++;
            if(line.startsWith("//")){
                comments.add(line);
            } else {
                if(line.contains("=")){
                    String[] components = line.split(Pattern.quote("="), 2);
                    Bot.getBot().getLogger().logInfo("Debug/Config", "Whole = "+line, "Components = "+Arrays.asList(components).toString());

                    if(components[0].length() == 0) {
                        read.close();
                        throw new InvalidFormatException("Syntax Violation Error - The pair key cannot be null. [Line "+String.valueOf(linen)+"]");
                    }
                    if(components[1].length() == 0) {
                        read.close();
                        throw new InvalidFormatException("Syntax Violation Error - The pair value cannot be null. [Line "+String.valueOf(linen)+"]");
                    }
                    if(components[0].startsWith("[")){
                        if (!components[0].contains("]")) {
                            read.close();
                            throw new InvalidFormatException("Syntax Error - Expected ']' [Line "+String.valueOf(linen)+"]");
                        }
                        String type = components[0].substring(1).split(Pattern.quote("]"))[0].toLowerCase();
                        String prefix = "[" + type + "]";
                        if(components[0].substring(prefix.length()).length() == 0){
                            read.close();
                            throw new InvalidFormatException("Syntax Violation Error - The pair value cannot be null. [Line "+String.valueOf(linen)+"]");
                        }
                        String content = components[0].substring(prefix.length());
                        if (type.equals("string")){
                            config.put(content, components[1]);
                        } else if(type.equals("int")){
                            config.put(content, Integer.parseInt(components[1]));
                        } else if(type.equals("integer")){
                            config.put(content, Integer.parseInt(components[1]));
                        } else if(type.equals("uint")){
                            config.put(content, Integer.parseUnsignedInt(components[1]));
                        } else if(type.equals("array")){
                            config.put(content, components[1].split(Pattern.quote(",")));
                        } else if(type.equals("map")){
                            String[] pairs = components[1].split(Pattern.quote(","));
                            Map<String, String> map = new HashMap<>();
                            for(String pair: pairs){
                                if(!pair.contains(":")){
                                    read.close();
                                    throw new InvalidFormatException("Syntax Error - Expected ':' (Map Type requires the format 'key1:value1,key2:value2,key3:value3' [Line "+String.valueOf(linen)+"]");
                                }
                                String[] keyval = pair.split(Pattern.quote(":"), 2);
                                if(keyval[0].length() == 0){
                                    read.close();
                                    throw new InvalidFormatException("Syntax Violation Error - A map key cannot be null. [Line "+String.valueOf(linen)+"]");
                                }
                                if(keyval[1].length() == 0){
                                    read.close();
                                    throw new InvalidFormatException("Syntax Violation Error - A map value cannot be null. [Line "+String.valueOf(linen)+"]");
                                }
                                map.put(keyval[0], keyval[1]);
                            }
                            config.put(components[0], map);
                        } else if(type.equals("json")){
                            read.close();
                            throw new InvalidFormatException("Type Error - The JSON format is not yet supported in version "+ Bot.MILESTONE_VERSION+" (Build "+ Bot.BUILD_VERSION+") [Line "+String.valueOf(linen)+"]");
                        }  else {
                            read.close();
                            throw new InvalidFormatException("Type Error - '"+type+"' is not a supported config entry conversion type. [Line "+String.valueOf(linen)+"]");
                        }
                    } else { config.put(components[0], components[1]); }
                } else {
                    continue;
                }
            }
        }

        read.close();

    }


    public HashMap<String, Object> getConfig() { return config; }
    public List<String> getConfigComments() { return comments; }
    public File getConfigFile() { return cfgfile; }
}
