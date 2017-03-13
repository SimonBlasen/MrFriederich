package com.sapp.glet.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Receiver extends Thread {

    private List<MessageListener> listeners = new ArrayList<MessageListener>();
    private InputStream is;

    public Receiver(InputStream is) {
        this.is = is;
    }

    public void addListener(MessageListener toAdd) {
        listeners.add(toAdd);
    }

    public void run() {
        int length = -1;
        boolean nextIsLength = false;

        while (true) {
            try {
                int read = is.read();
                length = 0 + ((int)(((byte)(read)) << 8));
                length += ((int)(((byte)(is.read()))));
                if (length > 0) {
                    //length = read;
                    byte[] bytes = new byte[length];
                    for (int i = 0; i < length; i++) {
                        bytes[i] = (byte)is.read();
                    }
                    String message = new String(bytes);
                    nextIsLength = false;
                    length = -1;
                    for (int i = 0; i < listeners.size(); i++) {
                        listeners.get(i).recieveMessage(message, bytes);
                    }
                }
            } catch (IOException e) {

            }
        }
    }
}
