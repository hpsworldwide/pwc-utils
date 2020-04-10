package com.hpsworldwide.powercard.utils.socket;

import com.hpsworldwide.powercard.utils.ByteArrayUtils;
import com.hpsworldwide.powercard.utils.OperationReferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author (c) HPS Solutions
 */
public class TestSocketListener implements ISocketListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestSocketListener.class);

    @Override
    public void onConnected(SslSocketServer sslSocketServer, long socketId, Socket socket, InputStream inputStream, OutputStream outputStream) throws IOException {
        LOG.info("socket onConnected event socketId=" + OperationReferenceManager.toBase32(socketId) + "; remoteSocketAddress=" + socket.getRemoteSocketAddress());

        final int HEADER_LENGTH = 4;
        byte[] baRequestFileLength = new byte[HEADER_LENGTH];
        inputStream.read(baRequestFileLength, 0, HEADER_LENGTH);
        LOG.info("read request file length: 0x" + ByteArrayUtils.toHexString(baRequestFileLength));
        int requestFileLength = ByteArrayUtils.byteArrayToInteger(baRequestFileLength);
        LOG.info("request file length: " + requestFileLength + " bytes; reading file...");
        byte[] requestData = new byte[requestFileLength];
        inputStream.read(requestData, 0, requestFileLength);
        LOG.info("request file read\nfile content:\n" + new String(requestData));
        LOG.info("writing response...");
        byte[] responseData = ("ok for " + requestFileLength + " bytes").getBytes();
        outputStream.write(ByteArrayUtils.integerToByteArray(responseData.length));
        outputStream.write(responseData, 0, responseData.length);
        LOG.info("response written ; closing socket " + OperationReferenceManager.toBase32(socketId) + "...");
        sslSocketServer.closeSocket(socketId);
        LOG.info("socket " + OperationReferenceManager.toBase32(socketId) + " closed.");
    }

}
