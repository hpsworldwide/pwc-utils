package com.hpsworldwide.powercard.utils.zip;

import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author (c) HPS Solutions
 */
public class TestCompressString {

    public static void main(String[] args) throws Exception {
        String sSrc = "upi://pay?pa=zeeshan@npci&pn=Zeeshan%Khan&mc=0000&tid=cxnkjcnkjdfdvjndkjfvn&tr=4894398cndhcd23&tn=Pay%to%rohit%stores&am=1010&cu=INR&refUrl=https://rohit.com/orderid=9298yw89e8973e87389e78923ue892";
        byte[] baSrc = sSrc.getBytes("UTF-8");
        System.out.println("src char length: " + sSrc.length());
        System.out.println("src byte array length: " + baSrc.length);
        byte[] baCompressed = Compress.compress(baSrc);
        System.out.println("compressed byte array length: " + baCompressed.length);
        String base64Compressed = Base64.encodeBase64String(baCompressed);
        System.out.println("compressed base64 byte array length: " + base64Compressed.length());
        byte[] uncompressedResult = Compress.uncompress(baCompressed);
        boolean equal = Arrays.equals(baSrc, uncompressedResult);
        System.out.println("are equal? " + equal);
    }

}
