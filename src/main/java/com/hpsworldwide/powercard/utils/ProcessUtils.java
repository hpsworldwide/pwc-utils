package com.hpsworldwide.powercard.utils;

import java.lang.management.ManagementFactory;

/**
 *
 * @author (c) HPS Solutions
 */
public class ProcessUtils {

    public static String getProcessID() {
        String processID = ManagementFactory.getRuntimeMXBean().getName();
        if (processID.contains("@")) {
            processID = processID.substring(0, processID.indexOf("@"));
        }
        return processID;
    }
}
