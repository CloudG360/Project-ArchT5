package net.cloudsbots.archseriest.archt5.database.textdb;

import net.cloudsbots.archseriest.archt5.exceptions.PermissionDeniedException;
import net.cloudsbots.archseriest.archt5.exceptions.RethrownException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TextDBAdapter {

    protected UUID uuid;
    protected File location;
    protected boolean s_instance;
    protected boolean reading_lock;

    public TextDBAdapter (File location, boolean singleInstance, boolean readOnly){
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.s_instance = singleInstance;
        this.reading_lock = readOnly;

        if(singleInstance && readOnly){

        }

        if(!this.location.exists()) { try { throw new FileNotFoundException("DB File specified does not exist: "+this.location.getPath()); } catch (FileNotFoundException err){ throw new RethrownException(err, false); } }
        if(location.isDirectory()){ throw new InvalidParameterException("TextDB Adapter cannot have a directory as the DB."); }

        if(s_instance){
            if(new File(this.location.getPath()+".lock").exists()) {
                throw new PermissionDeniedException("Lock File already exists for TextDB " + location.getName() + "And only a single instance is allowed");
            }
        }

        List<String> file = new ArrayList<>();

        try {
            FileReader r = new FileReader(this.location.getPath() + ".lock");
            BufferedReader read = new BufferedReader(r);
            Iterator<String> i = read.lines().iterator();
            while(i.hasNext()){
                String s = i.next();
                file.add(s);
            }
        } catch (FileNotFoundException err) {
            throw new RethrownException(err, false);
        }

        String ownerUUID = "";
        String tags = "";
        String editorUUIDs = ""; //For temp files








        // Temp DBs for editors should have timestamps for each logged change so when merging changes, newer ones can overwrite older entries.
        // Temp DBs for adapters should have a temporary directiory with the name "commits-[UUID]". Each commit should have the timestamp as the name in their own file.

    }

    public TextDBAdapter (File location){ this(location, false, false); }
    public TextDBAdapter (File location, boolean singleInstance){ this(location, singleInstance, false); }

    /*

        This class interacts will already created Databases.. If a specified database cannot be found, a
        DatabaseUnreachableException is thrown.

        == File Format ==

        1 idfield:string|field1:int|field2:float|field_with_cool_name:string            //These are a Table's fields. You can target data from specific ones. Each one has a paired type.
        3 @12345678|72|1.5|BeepBoop                             // This is an entry
        4
        5
        6

        Once you create a connection by instantiating the adapter, the database becomes uneditable by other programs (With lockfile support)
        to ensure data doesn't get overritten, skipped over, etc. Adapters automatically sleep after 10 minutes, unlocking the database for
        other adapters. All you need to do to wake it back up is wake it back up by using the #wake(); method. You should use it every time
        you want to interact with the database to ensure it's up. You can also force the adapter to be awake all the time in the constructor
        but this is not advised.

        Every time an adapter sleeps or closes, the database text file is updated with all the actions which took place since the last save.
        If the adapter is asleep, the Ram copy of the DB is also cleared to save ram. Once you wake up the DB again, the RAM copy is restored
        with up to data data. Due to this functionality, it's advised you split up databases into nodes so you don't get one massive database.
        If you're storing user data, store high priority users in one DB and lower ones in another.

        You would have to program that approach yourself in the current release. Soon there will be an update which allows multiple Adapters
        to read the same database as long as they share the same DBMaster. The DBmaster would handle edits to the DB, making it safe.
     */

    public void commit(){

    }

}
