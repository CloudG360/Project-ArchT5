package net.cloudsbots.archseriest.archt5.plugin;

import java.util.Map;
import java.util.UUID;

public final class PluginPackaging {

    private String id;
    private String description;
    private String authors;
    private String version;
    private int struct_target;

    private Plugin plugin;

    public PluginPackaging(Plugin plugin, String id, String description, String authors, String version, int structureTarget){
        this.id = id;
        this.description = description;
        this.authors = authors;
        this.version = version;
        this.struct_target = structureTarget;

        this.plugin = plugin;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public String getAuthors() { return authors; }
    public String getVersion() { return version; }
    public int getStructureTarget() { return struct_target; }
    public Plugin getPlugin() { return plugin; }
}
