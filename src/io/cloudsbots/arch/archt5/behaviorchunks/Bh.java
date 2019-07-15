package io.cloudsbots.arch.archt5.behaviorchunks;

public abstract class Bh {

    private boolean isProtected = false;
    private String pass;

    public Bh(String protection){
        super();
        this.isProtected = true;
        this.pass = pass;
    }

    public Bh(){ }

    public final boolean unlock(String password){
        return this.pass.equals(password);
    }

    public boolean isProtected(){ return isProtected; }

}
