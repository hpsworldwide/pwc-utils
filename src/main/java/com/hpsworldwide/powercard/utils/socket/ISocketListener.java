package com.hpsworldwide.powercard.utils.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author (c) HPS Solutions
 */
public interface ISocketListener {

    public void onConnected(SslSocketServer sslSocketServer, long socketId, Socket socket, InputStream inputStream, OutputStream outputStream) throws IOException;
}
