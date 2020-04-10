package com.hpsworldwide.powercard.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.HexDump;

/**
 * Byte Array framework : various useful functions working on byte arrays
 *
 * @author (c) Mobitrans / PowerCARD-Mobile team, HPS Solutions
 */
public class ByteArrayUtils {

    /**
     * result is upper case
     */
    public static String toHexString(byte[] byteArray) {
        return toHexString(byteArray, 0, byteArray.length);
    }

    /**
     * result is upper case
     */
    public static String toHexString(byte[] byteArray, int offset, int length) {
        StringBuilder hexString = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            hexString.append(Integer.toHexString(0x0100 + (byteArray[i] & 0xFF)).substring(1));
        }
        return hexString.toString().toUpperCase();
    }

    public static String hexDump(byte[] byteArray, long offset, int index) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HexDump.dump(byteArray, offset, bos, index);
        return new String(bos.toByteArray(), StandardCharsets.US_ASCII);
    }

    public static String hexDump(byte[] byteArray) throws IOException {
        return hexDump(byteArray, 0, 0);
    }

    public static void replaceAll(byte[] src, byte toChange, byte replacement) {
        for (int i = 0; i < src.length; i++) {
            if (src[i] == toChange) {
                src[i] = replacement;
            }
        }
    }

    public static boolean isAllPrintableAscii(byte[] text) {
        for (byte b : text) {
            if (b < 0x20 || b > 0x7E) {
                return false;
            }
        }
        return true;
    }

    public static byte[] integerToByteArray(int i) {
        byte[] ba = new byte[4];
        ba[0] = (byte) (i >> 24);
        ba[1] = (byte) (i >> 16);
        ba[2] = (byte) (i >> 8);
        ba[3] = (byte) (i);
        return ba;
    }

    public static byte[] longToByteArray(long l) {
        byte[] ba = new byte[8];
        for (int i = 7; i >= 0; i--) {
            ba[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return ba;
    }

    public static int byteArrayToInteger(byte[] ba) {
        return ByteBuffer.wrap(ba).getInt();
    }
}
