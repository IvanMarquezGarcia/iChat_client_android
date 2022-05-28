package com.ivan.iChat.ichat_android.model;

import java.io.IOException;
import java.net.Socket;

public class SingletonSocket {
    private static Socket INSTANCE = null;

    /*// Private constructor suppresses
    private SingletonSocket(String host, int port){
        try {
            INSTANCE = new Socket(host, port);
        } catch(IOException ioe) {
            // ioe.printStackTrace();
            System.out.println("Error al crear socket");
        }
    }

    // Synchronized creator to prevent posibles multi-thread problems
    private synchronized static boolean createInstance(String host, int port) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Socket(host, port);
                return true;
            } catch(IOException ioe) {
                // ioe.printStackTrace();
                System.out.println("Error al crear socket");
            }
        }
        return false;
    }*/

    // Synchronized creator to prevent posibles multi-thread problems
    public synchronized static void setInstance(Socket socket) {
        INSTANCE = socket;
    }

    public static Socket getInstance() {
        return INSTANCE;
    }
}