package com.sapp.glet.filesystem;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Simon on 17.03.2017.
 */

public class Filer {

    private static byte[] lastHash;

    public static byte[] getHash(Context context)
    {
        /*String fileContent = readFile(context, "test.txt");
        if (fileContent.length() == 0)
        {
            writeFile(context, "test.txt", "123456");
        }*/


        byte[] hash = new byte[] {};
        try {
            hash = java.security.MessageDigest.getInstance("MD5").digest(readFileBytes(context, "test.txt"));
            //hash = java.security.MessageDigest.getInstance("MD5").digest(new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x07});
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        lastHash = hash;

        writeFile(context, "test.txt", "123457");

        return hash;
    }



    public static void writeFile(Context context, String filename, String content)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        }
        catch (IOException e) {

        }
    }

    public static void writeFileBytes(Context context, String filename, byte[] content)
    {

    }

    public static byte[] readFileBytes(Context context, String filename)
    {
        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes, 0, bytes.length);


                inputStream.close();
                return bytes;
            }
        }
        catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return new byte[] {};
    }

    public static String readFile(Context context, String filename)
    {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return ret;
    }
}
