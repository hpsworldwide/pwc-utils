package com.hpsworldwide.powercard.utils.socket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SslSocketClient implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(SslSocketClient.class);
    private final Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public SslSocketClient(int localPort, String localHost, int remotePort, String remoteHost, boolean ssl) throws IOException {
        socket = generateClientSocket(localPort, localHost, remotePort, remoteHost, ssl);
    }

    public Pair<InputStream, OutputStream> start() throws IOException {
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        if (socket instanceof SSLSocket) {
            String[] cipherSuites = ((SSLSocket) socket).getSupportedCipherSuites();
            LOG.debug(cipherSuites.length + " cipher suites supported");
            for (String cipherSuite : cipherSuites) {
                LOG.debug("supported cipher suite : " + cipherSuite);
            }
        }
        return new ImmutablePair<>(inputStream, outputStream);
        // not yet connected, SSL might still fail at this point
        // send event ready
//        outputStream.write("hello, world!\n".getBytes());
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//            LOG.info(line);
//        }
    }

    private static Socket generateClientSocket(int localPort, String localHost, int remotePort, String remoteHost, boolean ssl) throws IOException {
        SocketFactory socketFactory = generateClientSocketFactory(ssl);
        InetAddress localHostAddress = InetAddress.getByName(localHost);
        InetAddress remoteAddress = InetAddress.getByName(remoteHost);
        return socketFactory.createSocket(remoteAddress, remotePort, localHostAddress, localPort);
    }

    private static SocketFactory generateClientSocketFactory(boolean ssl) {
        if (!ssl) {
            return SocketFactory.getDefault();
        } else {
            return SSLSocketFactory.getDefault();
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
