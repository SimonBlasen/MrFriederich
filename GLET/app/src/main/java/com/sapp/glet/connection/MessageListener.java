package com.sapp.glet.connection;


/**
 * Created by Simon on 07.01.2015.
 */
public interface MessageListener {
    public void recieveMessage(String message, byte[] bytes);
}
