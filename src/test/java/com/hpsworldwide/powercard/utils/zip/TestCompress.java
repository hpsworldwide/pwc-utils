package com.hpsworldwide.powercard.utils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author (c) HPS Solutions
 */
public class TestCompress {

    private static final File[] FILES = new File[]{
        new File("D:\\data\\zip\\caaa.003.AccptrAuthstnReq_2.xml"),
        new File("D:\\data\\zip\\caaa.003.AccptrAuthstnRspn_1.xml"),
        new File("D:\\data\\zip\\caaa.003.AccptrAuthstnRspn_2.xml"),
        new File("D:\\data\\zip\\caaa.003.AccptrCmpltnAdvc.xml")
    };

    public static void main(String[] args) throws Exception {
        Map<String, InputStream> content = new HashMap<>();
        for (File file : FILES) {
            content.put(file.getName(), new FileInputStream(file));
        }
        try (FileOutputStream zipFileOutputStream = new FileOutputStream("D:\\data\\zip\\compressed.zip")) {
            Compress.compress(content, zipFileOutputStream);
        }
        System.out.println("ok");
        Map<String, byte[]> uncompressed;
        try (FileInputStream zipFileInputStream = new FileInputStream("D:\\data\\zip\\compressed.zip")) {
            uncompressed = Compress.uncompress(zipFileInputStream);
        }
        for (String fileName : uncompressed.keySet()) {
            FileUtils.writeByteArrayToFile(new File("D:\\data\\zip\\uncompressed\\" + fileName), uncompressed.get(fileName));
        }
    }

}
