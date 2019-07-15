package io.cloudsbots.arch.archt5.components;

import io.cloudsbots.arch.archt5.Bot;
import io.cloudsbots.arch.archt5.exceptions.RethrownException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Logger implements Thread.UncaughtExceptionHandler{

    public Logger(){
        log = new ArrayList<>();
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logRuntimeError("Exception/"+e.getClass().getTypeName(), e.getMessage());
        for (StackTraceElement element: e.getStackTrace()) {
            logAppend(element.getClassName()+"."+element.getMethodName()+":Line "+element.getLineNumber(), " >> ");
        }

        List<Throwable> causes = new ArrayList<>();
        Throwable last = e;
        while (last != null){
            causes.add(last);
            last = e.getCause();
        }

        for(Throwable th:causes) {
            if (th instanceof RethrownException) {
                logAppend("@Rethrown", " Exception was caught and rethrown.");
                if (((RethrownException) th).shouldExit()) {

                    Bot.shutdown(2);
                }
            }
        }

    }

    public static Logger getLogger(){ return Bot.getBot().getLogger(); }


    private List<String> log;

    public static void logPoint(String loc){
        Logger.getLogger().logInfo("DEBUG/Point", "Reached location code: "+loc);
    }

    public void logInfo(String location, String... message) {
        log.add("[" + LocalDateTime.now().toString() + "][INFO][" + location + "]: " + message[0]);
        System.out.println("[" + LocalDateTime.now().toString() + "][INFO][" + location + "]: " + message[0]);
        if (message.length > 1){
            for (int i = 1; i < message.length; i++) {
                log.add(" ->  "+message[i]);
                System.out.println(" ->  "+message[i]);
            }
        }
        if(log.size() >= 1000){
            dumpLog("LOG-", UtilityFunctions.dateString());
        }
    }

    public void logAppend(String location, String... message) {
        if (message.length > 0){
            for (int i = 0; i < message.length; i++) {
                log.add(" ->  @"+location+" "+message[i]);
                System.out.println(" ->  @"+location+" "+message[i]);
            }
        }
        if(log.size() >= 1000){
            dumpLog("LOG-", UtilityFunctions.dateString());
        }
    }

    public void logWarn(String location, String... message) {
        log.add("[" + LocalDateTime.now().toString() + "][WARN][" + location + "]: " + message[0]);
        System.out.println("[" + LocalDateTime.now().toString() + "][WARN][" + location + "]: " + message[0]);
        if (message.length > 1){
            for (int i = 1; i < message.length; i++) {
                log.add(" ->  "+message[i]);
                System.out.println(" ->  "+message[i]);
            }
        }
        if(log.size() >= 1000){
            dumpLog("LOG-", UtilityFunctions.dateString());
        }
    }

    public void logRuntimeError(String location, String... message) {
        log.add("---");
        System.out.println("---");
        log.add("[" + LocalDateTime.now().toString() + "][ERROR][" + location + "]: " + message[0]);
        System.out.println("[" + LocalDateTime.now().toString() + "][ERROR][" + location + "]: " + message[0]);
        if (message.length > 1){
            for (int i = 1; i < message.length; i++) {
                log.add(" ->  "+message[i]);
                System.out.println(" ->  "+message[i]);
            }
        }
        if(log.size() >= 1000){
            dumpLog("LOG-", UtilityFunctions.dateString());
        }
    }

    public void logFatal(String location, String... message){
        log.add("==========");
        System.out.println("==========");
        log.add("[" + LocalDateTime.now().toString() + "][FATAL][" + location + "]: " + message[0]);
        System.out.println("[" + LocalDateTime.now().toString() + "][FATAL][" + location + "]: " + message[0]);
        if (message.length > 1){
            for (int i = 1; i < message.length; i++) {
                log.add(" ->  "+message[i]);
                System.out.println(" ->  "+message[i]);
            }
        }

        Bot.shutdown(2);
    }

    public void dumpLog(String id, String name){
        List<String> clone = new ArrayList<>(log);
        log.clear();
        String logstr = "";
        for(String line:clone){
            logstr = logstr.concat(line+"\n");
        }
        if(Bot.getBot().isDiscordActive()){
            int pointer = 0;
            while(pointer < logstr.length()) {
                String buff = "";
                for (int i = pointer; i < pointer + 2048; i++) {
                    buff = buff.concat(String.valueOf(logstr.toCharArray()[i]));
                    if(i == logstr.length()-1){
                        break;
                    }
                }
                pointer += 2048;
                MessageEmbed embed = new EmbedBuilder().setTitle("Log Dump ("+String.valueOf(pointer)+"/"+String.valueOf(logstr.length())+")").setDescription(buff).setColor(Color.ORANGE).build();
                Bot.getBot().getJDA().getTextChannelById("546319924599980044").sendMessage(embed).queue();
            }

        }
        if(!(new File("./logs").exists())){ new File("./logs").mkdir(); }
        File logout = new File("./logs/"+id+name+"-"+UUID.randomUUID().toString().substring(0, 2)+".txt");
        try{
            logout.createNewFile();
            FileWriter writer = new FileWriter(logout);
            BufferedWriter write = new BufferedWriter(writer);
            write.write(logstr);
            write.close();
        } catch (IOException ioe){ }
    }

}


