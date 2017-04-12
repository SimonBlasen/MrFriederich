package com.sapp.glet.service;

import com.sapp.glet.connection.Client;
import com.sapp.glet.database.Database;
import com.sapp.glet.util.DatatypesUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Simon on 12.04.2017.
 */

public class Synchronisator {

    private static List<DataUpdate> dataUpdates;

    public static void addDataUpdateListener(DataUpdate dataUpdate)
    {
        if (dataUpdate != null)
        {
            dataUpdates.add(dataUpdate);
        }
    }

    public static boolean sendMessage(TcpMessage message)
    {
        Client client = new Client("m.m-core.eu", 24400);
        try {
            client.Connect();
            byte[] bytes = new byte[message.getMessage().length + 2];
            bytes[0] = (byte)(message.getId() >> 8);
            bytes[1] = (byte)(message.getId());
            for (int i = 0; i < message.getMessage().length; i++)
            {
                bytes[i + 2] = message.getMessage()[i];
            }
            client.send(bytes);

            int owndId = Database.getOwndId();
            byte[] ownIdBytes = DatatypesUtils.IntegerToBytes(owndId);

            client.send(new byte[] {0, 8, ownIdBytes[0], ownIdBytes[1],ownIdBytes[2],ownIdBytes[3]});

            client.send(new byte[] {0, 6, ownIdBytes[0], ownIdBytes[1],ownIdBytes[2],ownIdBytes[3]});

            return true;
        } catch (IOException e) {
            return false;
        }
        //return true;
    }

    public static boolean sendFile(String filename)
    {
        /*Client client = new Client("m.m-core.eu", 24400);
        try {
            client.Connect();
            byte[] bytes = new byte[message.getMessage().length + 2];
            bytes[0] = (byte)(message.getId() >> 8);
            bytes[1] = (byte)(message.getId());
            for (int i = 0; i < message.getMessage().length; i++)
            {
                bytes[i + 2] = message.getMessage()[i];
            }
            client.send(bytes);

            int owndId = Database.getOwndId();
            byte[] ownIdBytes = DatatypesUtils.IntegerToBytes(owndId);

            client.send(new byte[] {0, 8, ownIdBytes[0], ownIdBytes[1],ownIdBytes[2],ownIdBytes[3]});

            client.send(new byte[] {0, 6, ownIdBytes[0], ownIdBytes[1],ownIdBytes[2],ownIdBytes[3]});

            return true;
        } catch (IOException e) {
            return false;
        }*/

        return true;
    }
}
