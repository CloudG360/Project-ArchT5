package io.cloudsbots.arch.archt5.components;

import java.time.LocalDateTime;
import java.util.Map;

public class UtilityFunctions {

    /**
     * Merges the entries of two maps together, overriding any duplicates from the merging map
     * to the original
     *
     * @param original - The original map.
     * @param merge - The map to merge with it (Overrides entries in orignal if duplicates)
     * @return - Returns the new merged map.
     */
    public static Map<String, Object> mergeMaps(Map<String, Object> original, Map<String, Object> merge){
        Map<String, Object> merged = original;
        for(String key:merge.keySet()){
            if(merged.containsKey(key)){
                merged.remove(key);
            }
            merge.put(key, merge.get(key));
        }
        return merged;
    }

    public static String dateString(){
        return String.valueOf(LocalDateTime.now().getDayOfMonth())+"-"+String.valueOf(LocalDateTime.now().getMonthValue())+"-"+String.valueOf(LocalDateTime.now().getYear()+"_"+String.valueOf(LocalDateTime.now().getHour())+String.valueOf(LocalDateTime.now().getMinute()));
    }

}
