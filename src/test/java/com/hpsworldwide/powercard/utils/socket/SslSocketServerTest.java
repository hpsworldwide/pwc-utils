package com.hpsworldwide.powercard.utils.socket;

public class SslSocketServerTest {

    /**
     * backlog : how many connections are queued
     */
    private static final int BACK_LOG = 20;
    private static final String LOCAL_ADDRESS = "127.0.0.1";
    private static final int LOCAL_PORT = 11111;
    private static final boolean SSL = false;
    private static final boolean PERPETUAL_LOOP = true;

    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        TestSocketListener testSocketListener = new TestSocketListener();
        try (SslSocketServer sslSocketServer = new SslSocketServer(LOCAL_PORT, LOCAL_ADDRESS, BACK_LOG, SSL, testSocketListener)) {
            sslSocketServer.start(PERPETUAL_LOOP);
        }
    }

}
