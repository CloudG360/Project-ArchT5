package io.cloudsbots.arch.archt5.components;

import io.cloudsbots.arch.archt5.exceptions.InvalidFormatException;
import io.cloudsbots.arch.archt5.events.Event;

public final class Validator {

    /**
     *
     *
     * @param var Var to check.
     * @param info Useful error information!
     */
    public static void notNull(Object var, String info){
        if(var == null){
            throw new NullPointerException("(Data Validator) Variable cannot be null - "+info);
        }
    }

    public static void validateString(String string, String info, String... validationTerms){
        boolean c = false;
        for(String t: validationTerms){ if(string.toLowerCase().equals(t.toLowerCase())){ c=true; } }
        if(!c){throw new InvalidFormatException("String does not match criteria. - "+info); }
    }

    public static <T> T verifyType(Object object, Class<T> type, String info) {
        Validator.notNull(object, info);
        String t = type.getTypeName();
        if(type.isInstance(object)){
            return (T) object;
        } else {
            throw new InvalidFormatException("Type does not match required type: "+info+" ("+object.getClass().getTypeName()+" != "+t+")");
        }
    }

    @Deprecated
    public static <T> T verifyType(Object object, Class<T> type) {
        return verifyType(object, type, "A plugin used a Deprecated Validator function (Therefore no return info)");
    }

    public static boolean verifyTypeBool(Object object, Class type) {
        if(object == null){ return false; }
        String t = type.getTypeName();
        if(type.isInstance(object)){
            return true;
        } else {
            return false;
        }
    }



    public static void checkNoSpaceString(String string, String info){
        if(string.contains(" ")) {throw new InvalidFormatException("String should not have spaces. Please fix your plugin and use underscores instead - "+info); }
    }

    public static boolean isEventType(Event event, String id){
        if(event.getEventTypeid().equals(id) ){
            return true;
        } else {
            return false;
        }
    }

}
