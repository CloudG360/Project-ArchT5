package io.cloudsbots.arch.archt5.components;

import io.cloudsbots.arch.archt5.Bot;

import java.time.LocalDateTime;

public class Statistics {

    public Statistics (){
        dateTime = LocalDateTime.now();
    }

    private int events_fired = 0;
    private int behaviors_ran = 0;
    private int commands_ran = 0;
    private int messages_processed = 0;
    private LocalDateTime dateTime;

    public static Statistics getStatisticsHub() { return Bot.getBot().getStatisticsHub(); }

    public int getEventsFired() { return events_fired; }
    public int getBehaviorsRan() { return behaviors_ran; }
    public int getCommandsRan() { return commands_ran; }
    public int getMessagesProcessed() { return messages_processed; }
    public String getStartTime() { return dateTime.toString(); }

    public void incrementEventsFired(){ events_fired += 1;}
    public void incrementCommandsRan(){ commands_ran += 1;}
    public void incrementBehaviorsRan(){ behaviors_ran += 1;}
    public void incrementMessagesProcessed(){ messages_processed += 1;}
}
