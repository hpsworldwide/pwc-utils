package com.hpsworldwide.powercard.utils;

import java.text.ParseException;

/**
 * @author (c) HPS Solutions
 */
public class StringUtil {

    /**
     * transforms "0123456789ABCDEF" in byte[]{0x01, 0x23, 0x45, 0x67, 0x89,
     * 0xAB, 0xCD, 0xEF}
     *
     * @param hexaString must only contain characters O-9 A-F (or a-f) ; its
     * length must be even
     * @throws Exception when the input string length isn't even
     */
    public static byte[] stringToByteArray(String hexaString) throws ParseException {
        // the padding shouldn't be used, so a random one was chosen
        return stringToByteArray(hexaString, hexaString.length() / 2, (byte) 0xFF);
    }

    /**
     * /!\ the byte array format is not standard example : "O123456789ABCDEF"
     * becomes Ox01, 0x23, 0x45, 0x67, 0x89, 0xAB, 0xCD, 0xEF
     *
     * @param str the string to be converted
     * @param resultArraySize the expected result size of the array
     * @param padding the value of the padding to be added on the right
     * @throws Exception if the input string length isn't even or if the input
     * string length / 2 is superior to the resultArraySize
     * @throws NumberFormatException if str contains a non 0-9 A-F character
     */
    public static byte[] stringToByteArray(String hexaString, int resultArraySize, byte padding) throws ParseException {
        final int HEXA_RADIX = 16;
        int length = hexaString.length();
        if (length % 2 == 0) {
            length /= 2;
            if (length <= resultArraySize) {
                byte[] numbers = new byte[resultArraySize];
                int i;
                // filling the array
                for (i = 0; i < length; i++) {
                    // the following line will trigger a NumberFormatException if str contains a non 0-9 A-F character
                    try {
                        int j = Integer.parseInt(hexaString.substring(2 * i, 2 * i + 2), HEXA_RADIX);
                        numbers[i] = (byte) (j & 0xFF);
                    } catch (NumberFormatException ex) {
                        throw new ParseException(ex.getMessage(), i);
                    }
                }
                // padding
                for (; i < resultArraySize; i++) {
                    numbers[i] = padding;
                }
                return numbers;
            } else {
                throw new ParseException("the resulting array size is too big compared to the min size specified in the parameters", 0);
            }
        } else {
            throw new ParseException("string length must be even", 0);
        }
    }
}
