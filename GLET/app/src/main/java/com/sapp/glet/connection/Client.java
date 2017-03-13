package com.sapp.glet.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
        String newMsg = ((char)((byte)255)) + ((char)((byte)255)) + message;
        out.printf(newMsg);
    }

    public void send(byte[] bytes) {
        if (true)
        {
            OutputStream os = null;
            try {
                byte[] newBytes = new byte[bytes.length + 2];
                newBytes[0] = (byte)(bytes.length >> 8);
                newBytes[1] = (byte)(bytes.length);
                for (int i = 0; i < bytes.length; i++)
                {
                    newBytes[i + 2] = bytes[i];
                }
                os = socket.getOutputStream();
                os.write(newBytes, 0, newBytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                byte[] newBytes = new byte[bytes.length + 2];
                newBytes[0] = (byte)(bytes.length >> 8);
                newBytes[1] = (byte)(bytes.length);
                for (int i = 0; i < bytes.length; i++)
                {
                    newBytes[i + 2] = bytes[i];
                }
                DataOutputStream dos = new DataOutputStream(oos);
                dos.write(bytes, 0, bytes.length);

                //oos.writeObject(newBytes);
                //oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean Connect() throws UnknownHostException, IOException {
        socket = new Socket(adress, port);
        is = socket.getInputStream();
        out = new PrintWriter(socket.getOutputStream(), true);

        //Admit, that you are Java:
        //out.printf("IAmJava");
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
    public void recieveMessage(String message, byte[] bytes) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).recieveMessage(message, bytes);
        }
    }
}

