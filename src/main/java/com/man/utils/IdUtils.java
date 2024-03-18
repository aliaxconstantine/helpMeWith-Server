package com.man.utils;

import cn.hutool.core.lang.Snowflake;

import java.time.Instant;
import java.util.Random;

/**
 * @author 艾莉希雅
 */
public class IdUtils {

    public static final Snowflake snowflake = new Snowflake();
    private static final int ACCOUNT_LENGTH = 7;
    private static final String DIGITS = "0123456789";

    public static String  generate() {
        StringBuilder sb = new StringBuilder(ACCOUNT_LENGTH);
        Random random = new Random();

        for (int i = 0; i < ACCOUNT_LENGTH; i++) {
            int index = random.nextInt(DIGITS.length());
            char digit = DIGITS.charAt(index);
            sb.append(digit);
        }

        return sb.toString();
    }
}
