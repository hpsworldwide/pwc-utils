package com.hpsworldwide.powercard.utils.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author (c) HPS Solutions
 */
public class Compress {

    /**
     * ZIP (with file names)
     */
    public static void compress(Map<String, InputStream> content, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            // zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
            // zipOutputStream.setComment("HPS Solutions");
            for (String entryName : content.keySet()) {
                ZipEntry ze = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(ze);
                try (InputStream entryContent = content.get(entryName)) {
                    IOUtils.copy(entryContent, zipOutputStream);
                }
                zipOutputStream.closeEntry();
            }
        }
    }

    /**
     * ZIP (with file names)
     */
    public static Map<String, byte[]> uncompress(InputStream inputStream) throws IOException {
        Map<String, byte[]> content = new HashMap<>();
        try (ZipInputStream zipInputstream = new ZipInputStream(inputStream)) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputstream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    IOUtils.copy(zipInputstream, bos);
                    content.put(entryName, bos.toByteArray());
                }
                zipInputstream.closeEntry();
            }
        }
        return content;
    }

    /**
     * GZIP
     */
    public static byte[] compress(byte[] src) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(src);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * GZIP
     */
    public static byte[] uncompress(byte[] src) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(src)) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
                return IOUtils.toByteArray(gzipInputStream);
            }
        }
    }
}
