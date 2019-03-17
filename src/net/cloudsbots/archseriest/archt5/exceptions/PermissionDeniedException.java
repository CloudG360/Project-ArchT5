package net.cloudsbots.archseriest.archt5.exceptions;

import net.cloudsbots.archseriest.archt5.Bot;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(String message){
        super(message);
    }

}
