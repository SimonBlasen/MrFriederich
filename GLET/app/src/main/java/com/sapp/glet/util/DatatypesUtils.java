package com.sapp.glet.util;

/**
 * Created by Simon on 12.04.2017.
 */

public class DatatypesUtils {

    public static byte[] IntegerToBytes(int i)
    {
        return new byte[] {(byte)(i >> 24), (byte)(i >> 16), (byte)(i >> 8), (byte)(i)};
    }

    public static int BytesToInteger(byte[] bytes)
    {
        return BytesToInteger(bytes, 0);
    }

    public static int BytesToInteger(byte[] bytes, int index)
    {
        if (bytes.length - index >= 4)
        {
            return (bytes[index] << 24) + (bytes[index + 1] << 16) + (bytes[index + 2] << 8) + (int)(bytes[index + 3]);
        }
        return -1;
    }
}
