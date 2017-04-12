package com.sapp.glet.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by Simon on 12.04.2017.
 */

public class TcpMessage {

    private char m_id;
    private byte[] msg;

    public TcpMessage()
    {
        m_id = 0;
        msg = new byte[] {};
    }

    public TcpMessage(char id, byte[] message)
    {
        m_id = id;
        msg = message;
    }

    public TcpMessage(char id, String message)
    {
        m_id = id;
        msg = message.getBytes(Charset.forName("UTF-8"));
    }

    public void setId(char id)
    {
        m_id = id;
    }

    public void setMessage(byte[] message)
    {
        msg = message;
    }

    public void setMessage(String message)
    {
        msg = message.getBytes(Charset.forName("UTF-8"));
    }

    public char getId()
    {
        return m_id;
    }

    public byte[] getMessage()
    {
        return msg;
    }
}
