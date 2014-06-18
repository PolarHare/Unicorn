package com.polarnick.unicorn.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Nickolay Polyarnyi
 */
public class Utils {

    public static String readString(InputStream is) {
        java.util.Scanner s = new Scanner(is, StandardCharsets.UTF_8.name()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

}
