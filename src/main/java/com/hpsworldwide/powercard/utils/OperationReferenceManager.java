package com.hpsworldwide.powercard.utils;

import java.util.Random;
import org.apache.commons.codec.binary.Base32;

/**
 * @author (c) HPS Solutions
 */
public class OperationReferenceManager {

    private static final Random RANDOM = new Random();
    /**
     * 128 bits
     */
    private static final int LENGTH = 16;
    private static final Base32 BASE_32 = new Base32(-1);
    public static final String OPERATION_REFERENCE = "OPERATION_REFERENCE";

    public static String getNextOperationReference() {
        byte[] baOperationRef = new byte[LENGTH];
        RANDOM.nextBytes(baOperationRef);
        return BASE_32.encodeAsString(baOperationRef).replaceAll("=", "");
    }

    public static String toBase32(long l) {
        return BASE_32.encodeAsString(ByteArrayUtil.longToByteArray(l)).replaceAll("=", "");
    }

}
