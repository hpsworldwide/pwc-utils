package com.hpsworldwide.powercard.utils.socket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SslSocketServer implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(SslSocketServer.class);
    private final ServerSocket serverSocket;
    private final Map<Long, Socket> sockets;
    private final Random random;
    private final ISocketListener socketListener;

    /**
     * @param backlog how many connections are queued
     */
    public SslSocketServer(int localPort, String localHost, int backlog, boolean ssl, ISocketListener socketListener) throws IOException {
        this.socketListener = socketListener;
        serverSocket = generateServerSocket(localPort, localHost, backlog, ssl);
        if (serverSocket instanceof SSLServerSocket) {
            String[] cipherSuites = ((SSLServerSocket) serverSocket).getSupportedCipherSuites();
            LOG.debug(cipherSuites.length + " cipher suites supported");
            for (String cipherSuite : cipherSuites) {
                LOG.debug("supported cipher suite : " + cipherSuite);
            }
        }
        LOG.info("socket ready on " + localHost + ":" + localPort + " backlog[" + backlog + "] ssl?" + ssl);
        sockets = new HashMap<>();
        random = new Random();
    }

    /**
     * @param loop if true, will listen perpetually for a connexion ; if false,
     * will listen only to the first connexion
     */
    public void start(boolean loop) throws IOException {
        do {
            LOG.info("waiting for socket connexion...");
            final Socket socket = serverSocket.accept();
            final SslSocketServer sslSocketServer = this;
            new Thread() {
                @Override
                public void run() {
                    boolean isSSL = socket instanceof SSLSocket;
                    LOG.info("socket connected; ssl=" + isSSL);
                    long socketId = random.nextLong();
                    sockets.put(socketId, socket);
                    if (isSSL) {
                        String[] enabledProtocols = ((SSLSocket) socket).getEnabledProtocols();
                        LOG.debug(enabledProtocols.length + " enabled protocols");
                        for (String enabledProtocol : enabledProtocols) {
                            LOG.debug("enabled protocol : " + enabledProtocol);
                        }
                    }
                    try {
                        OutputStream outputStream = socket.getOutputStream();
                        InputStream inputStream = socket.getInputStream();
                        LOG.info("action delegated to socket listener");
                        socketListener.onConnected(sslSocketServer, socketId, socket, inputStream, outputStream);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }.start();
        } while (loop);
    }

    /**
     * @param backlog how many connections are queued
     */
    private static ServerSocket generateServerSocket(int localPort, String localHost, int backlog, boolean ssl) throws IOException {
        ServerSocketFactory serverSocketFactory = generateServerSocketFactory(ssl);
        InetAddress localHostAddress = InetAddress.getByName(localHost);
        return serverSocketFactory.createServerSocket(localPort, backlog, localHostAddress);
    }

    private static ServerSocketFactory generateServerSocketFactory(boolean ssl) {
        if (!ssl) {
            return ServerSocketFactory.getDefault();
        } else {
            return SSLServerSocketFactory.getDefault();
        }
    }

    public void closeSocket(long socketId) throws IOException {
        Socket socket = sockets.get(socketId);
        socket.close();
        sockets.remove(socketId);
    }

    @Override
    public void close() throws IOException {
        for (Socket socket : sockets.values()) {
            socket.close();
        }
        sockets.clear();
    }
}
