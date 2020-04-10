package com.hpsworldwide.powercard.utils.socket;

import com.hpsworldwide.powercard.utils.ByteArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SslSocketClientTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(SslSocketClientTest.class);
    private static final String REMOTE_ADDRESS = "127.0.0.1";
    private static final int REMOTE_PORT = 11111;
    private static final String LOCAL_ADDRESS = "127.0.0.1";
    private static final int LOCAL_PORT = 12345;
    private static final boolean SSL = false;
    private static final String REQUEST_FILE_PATH = "D:\\data\\ISO20022\\caaa.003.AccptrAuthstnReq_1.xml";
    
    public static void main(String[] args) throws Exception {
        // test
        try (SslSocketClient client = new SslSocketClient(LOCAL_PORT, LOCAL_ADDRESS, REMOTE_PORT, REMOTE_ADDRESS, SSL)) {
            Pair<InputStream, OutputStream> streams = client.start();
            InputStream inputStream = streams.getLeft();
            OutputStream outputStream = streams.getRight();
            File requestFile = new File(REQUEST_FILE_PATH);
            long lRequestFileLength = requestFile.length();
            LOG.info("request file length: " + lRequestFileLength + " bytes");
            byte[] baOutputFileLength = ByteArrayUtils.integerToByteArray((int) lRequestFileLength);
            outputStream.write(baOutputFileLength);
            try (FileInputStream requestFileInputStream = new FileInputStream(requestFile)) {
                IOUtils.copy(requestFileInputStream, outputStream);
            }
            LOG.info("reading answer...");

            final int HEADER_LENGTH = 4;
            byte[] baResponseFileLength = new byte[HEADER_LENGTH];
            inputStream.read(baResponseFileLength, 0, HEADER_LENGTH);
            LOG.info("read response file length: 0x" + ByteArrayUtils.toHexString(baResponseFileLength));
            int inputFileLength = ByteArrayUtils.byteArrayToInteger(baResponseFileLength);
            LOG.info("response file length: " + inputFileLength + " bytes; reading response file...");
            byte[] responseData = new byte[inputFileLength];
            inputStream.read(responseData, 0, inputFileLength);
            LOG.info("response file read:\n" + new String(responseData));
        }
    }
}
