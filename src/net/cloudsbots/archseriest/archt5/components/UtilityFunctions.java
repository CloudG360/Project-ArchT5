package net.cloudsbots.archseriest.archt5.components;

import java.time.LocalDateTime;

public class UtilityFunctions {

    public static String dateString(){
        return String.valueOf(LocalDateTime.now().getDayOfMonth())+"-"+String.valueOf(LocalDateTime.now().getMonthValue())+"-"+String.valueOf(LocalDateTime.now().getYear()+"_"+String.valueOf(LocalDateTime.now().getHour())+String.valueOf(LocalDateTime.now().getMinute()));
    }

}
