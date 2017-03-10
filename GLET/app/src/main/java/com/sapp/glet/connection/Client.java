package com.sapp.glet.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 07.01.2015.
 */
public class Client implements MessageListener {

    private List<MessageListener> listeners = new ArrayList<MessageListener>();
    private Receiver receiver;
    private String adress;
    private int port;

    private static InputStream is;
    public static PrintWriter out;
    private static Socket socket;

    public Client(String adress, int port) {
        this.adress = adress;
        this.port = port;
    }

    public void send(String message) {
        out.printf(message);
    }

    public void send(byte[] bytes) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(bytes);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean Connect() throws UnknownHostException, IOException {
        socket = new Socket(adress, port);
        is = socket.getInputStream();
        out = new PrintWriter(socket.getOutputStream(), true);

        //Admit, that you are Java:
        out.printf("IAmJava");
        //Done admitting

        receiver = new Receiver(is);
        receiver.addListener(this);
        receiver.start();
        return true;
    }

    public void Disconnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }

    public void addListener(MessageListener toAdd) {
        listeners.add(toAdd);
    }

    @Override
    public void recieveMessage(String message) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).recieveMessage(message);
        }
    }
}

