package org.example;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.LocalTime;
public class Checks {


    public static boolean isValidSymbol(String str){
        if (str.length() != 4){
            return false;
        }

        String numbers = "0123456789";
        String symbols = "0123456789ACDFGHJKLMNPRSTUWXY";

        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);

            if(i == 0 || i == 2){
                if(numbers.indexOf(c) == -1){
                    return false;
                }
            } else {
                if(symbols.indexOf(c) == -1){
                    return false;
                }
            }
        }
        return true;
    }



    public static boolean isTradingHours(LocalDateTime dateTime) {
        if (dateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return false;
        }
        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }

        LocalTime time = dateTime.toLocalTime();
        if(!time.isBefore(LocalTime.of(9, 0)) &&
                (!time.isAfter(LocalTime.of(11,30)))){
            return true;
        }
        if(!time.isBefore(LocalTime.of(12, 30)) &&
                (!time.isAfter(LocalTime.of(15,30)))){
            return true;
        }

        return false;
    }


}
