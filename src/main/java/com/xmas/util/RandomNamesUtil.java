package com.xmas.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RandomNamesUtil {

    private static final Random RANDOM = new Random();

    public static String getRandomName(){
        LocalDateTime dateTime = LocalDateTime.now();
        String timePart = dateTime.format(DateTimeFormatter.ofPattern("YYMMddhhmmss"));
        Integer randomPart = RANDOM.nextInt(10000000);
        return toHex(timePart + randomPart);
    }

    private static String toHex(String digits){
        return Long.toHexString(Long.valueOf(digits));
    }
}
