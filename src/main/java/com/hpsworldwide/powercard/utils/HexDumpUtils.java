package com.hpsworldwide.powercard.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.io.HexDump;

/**
 * useful operations for dumping hexadecimal data
 * @author PowerCARD-Mobile team - HPS Solutions - HPS (Hightech Payment
 * Systems)
 */
public class HexDumpUtils {

    public static String dump(byte[] data, long offset, int index) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HexDump.dump(data, offset, outputStream, index);
        return outputStream.toString();
    }

    public static String dump(byte[] data) throws IOException {
        return dump(data, 0, 0);
    }

}
