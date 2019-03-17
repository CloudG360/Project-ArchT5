package net.cloudsbots.archseriest.archt5.database.textdb;

import net.cloudsbots.archseriest.archt5.components.Validator;
import net.cloudsbots.archseriest.archt5.database.textdb.exceptions.DatabaseForbiddenActionException;
import net.cloudsbots.archseriest.archt5.database.textdb.exceptions.DatabasePermissionException;
import net.cloudsbots.archseriest.archt5.exceptions.InvalidFormatException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class TextDBFactory {

    private HashMap<String, String> dbTable;
    private File dbLoc;
    private boolean canOverwrite;

    public TextDBFactory(File dbLocation) throws IOException{ this(dbLocation, false); }

    public TextDBFactory(File dbLocation, boolean overwrite) throws IOException{
        Validator.notNull(dbLocation, "(File dbLocation, ...) @TextDBFactory Constructor");
        Validator.notNull(overwrite, "(..., boolean overwrite) @TextDBFactory Constructor");
        dbTable = new HashMap<>();
        dbLoc = dbLocation;
        canOverwrite = overwrite;
        dbTable.put("id", "string");
    }

    public void addField(String fieldName, String fieldType){ dbTable.replace(fieldName, fieldType); }
    public void replaceField(String fieldName, String fieldType){ dbTable.replace(fieldName, fieldType); }
    public void removeField(String fieldName){ dbTable.remove(fieldName); }
    public void clear(String fieldName){ dbTable.clear();}

    public TextDBAdapter build(){
        if(dbTable.size() < 1){ throw new DatabaseForbiddenActionException("Database must have at least 1 field."); }
        if(dbLoc.exists()){
            if(canOverwrite){
                boolean delete = dbLoc.delete();
                if(!delete){ throw new UncheckedIOException(new IOException("Unable to delete old DB to overwrite.")); }
            } else { throw new DatabasePermissionException("Unable to overwrite old DB. Method does not provide permission."); }
        }
        try {
            boolean created = dbLoc.createNewFile();
            if(!created){ throw new IOException("Unable to create DB"); }
        } catch (IOException err) { throw new UncheckedIOException(err); }
        Iterator<Map.Entry<String, String>> i = dbTable.entrySet().iterator();
        String writeable = "";
        while(i.hasNext()){
            Map.Entry<String, String> entry = i.next();

            if(!Pattern.matches("[a-zA-z]+", entry.getKey())){ throw new InvalidFormatException("");}

            Validator.validateString(entry.getValue(), "DB Type must be one of the following: string,int,float", "string", "int", "float");
            writeable=writeable.concat(entry.getKey()+"#"+entry.getValue());
            if(i.hasNext()){ writeable=writeable.concat("|"); }
        }
        try {
            Writer writer = new FileWriter(dbLoc);
            BufferedWriter write = new BufferedWriter(writer);
            write.write(writeable);
        } catch (IOException err){ throw new UncheckedIOException(err); }

        //Create Adapter

        return null;
    }

    /**
     * Same as the regular build method {@see TextDBFactory#build()} but it has
     * a few less checks. Can be useful when implementing custom data types as you
     * would be able to bypass the built in checks and add your own. You can override
     * the {@see TextDBFactory#build()} method with it. It specifically bypasses the
     * alpha checks for field names and type checks. It still checks that the fields do
     * not contain '#' or '|', however.
     *
     * It's deprecated to suggest it shouldn't be used regularly. Along with that, it does
     * not return a TextDBAdapter as that would create errors from unsupported field types
     * if this method is used correctly. A modified TextDBAdapter would be required in order
     * to use the constructed DB. BuildUnsafe also bypasses .lock checking so it's a good
     * idea to manually check before hand or disable overwriting
     *
     * @deprecated
     */
    public void buildUnsafe() {
        if (dbTable.size() < 1) { throw new DatabaseForbiddenActionException("Database must have at least 1 field."); }
        if (dbLoc.exists()) {
            if (canOverwrite) {
                boolean delete = dbLoc.delete();
                if (!delete) { throw new UncheckedIOException(new IOException("Unable to delete old DB to overwrite.")); }
            } else { throw new DatabasePermissionException("Unable to overwrite old DB. Method does not provide permission."); }
        }
        try {
            boolean created = dbLoc.createNewFile();
            if (!created) { throw new IOException("Unable to create DB"); }
        } catch (IOException err) { throw new UncheckedIOException(err); }
        Iterator<Map.Entry<String, String>> i = dbTable.entrySet().iterator();
        String writeable = "";
        while (i.hasNext()) {
            Map.Entry<String, String> entry = i.next();
            if (entry.getKey().contains("#")) { throw new DatabaseForbiddenActionException("Forbidden name - Database Fields can't contain the character '#'."); }
            if (entry.getKey().contains("|")) { throw new DatabaseForbiddenActionException("Forbidden name -  can't contain the character '|'."); }
            Validator.validateString(entry.getValue(), "DB Type must be one of the following: string,int,float", "string", "int", "float");
            writeable = writeable.concat(entry.getKey() + "#" + entry.getValue());
            if (i.hasNext()) { writeable = writeable.concat("|"); }
        }
        try {
            Writer writer = new FileWriter(dbLoc);
            BufferedWriter write = new BufferedWriter(writer);
            write.write(writeable);
        } catch (IOException err) { throw new UncheckedIOException(err); }

    }

}
