package com.sapp.glet.filesystem;

import android.content.Context;

import com.sapp.glet.database.Player;
import com.sapp.glet.database.stats.StatsParagon;
import com.sapp.glet.database.stats.StatsType;

import java.io.OutputStreamWriter;

/**
 * Created by Simon on 28.03.2017.
 */

public class FilerDatabase {

    //Writes a String Array to a file, each index of array is one line
    public static void writeFile(Context context, String filename, String[] data){
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename,Context.MODE_PRIVATE));
            //iterates over all players
            for(int i = 0; i < data.length; i++){
                outputStreamWriter.write(data[i]);
                if(i < data.length-1){
                    outputStreamWriter.append("\n");
                }
            }
            outputStreamWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
