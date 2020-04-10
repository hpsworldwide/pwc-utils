package com.hpsworldwide.powercard.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 *
 * @author (c) HPS Solutions
 */
public class DurationUtils {

    public static String format(long durationMillis) {
        final boolean padWithZeros = true;
        return DurationFormatUtils.formatDuration(durationMillis, "dd'd'HH'h'mm'm'ss's'", padWithZeros);
    }
}
