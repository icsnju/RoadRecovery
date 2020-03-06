package nju.ics.Backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Backend {

    ServerSocket serverSkt = null;
    int defaultPort = 4444;
    long conCount = 0;

    public void listenPort() {
        try {
            serverSkt = new ServerSocket(defaultPort);
            while (true) {
                System.out.println("Wait for the " + (++conCount) + " client request.");
                Socket cppSocket = serverSkt.accept();
                BackendThread backendThread = new BackendThread(cppSocket);
                backendThread.start();
            }
        } catch (IOException e) {
            System.err.println("cpp client's connection failed.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.listenPort();
    }
}
