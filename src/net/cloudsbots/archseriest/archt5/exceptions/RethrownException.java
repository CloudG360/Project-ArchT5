package net.cloudsbots.archseriest.archt5.exceptions;

public class RethrownException extends RuntimeException {

    private boolean exit;

    public RethrownException(Exception err, boolean exit){
        super(err.getMessage());
        this.exit = exit;
    }

    public boolean shouldExit(){
        return exit;
    }

}
