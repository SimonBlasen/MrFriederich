package com.sapp.glet.filesystem;

import android.content.Context;
import android.util.Log;

import com.sapp.glet.database.Player;
import com.sapp.glet.database.stats.StatsParagon;
import com.sapp.glet.database.stats.StatsType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Simon on 28.03.2017.
 */

public class FilerDatabase {

    //Writes a String Array to a file, each index of array is one line
    public static void writeFile(Context context, String filename, String[] data){
        //Log.w("bug8", "filepath=" + context.getFilesDir().toString());

        File file = new File(context.getFilesDir(), filename);
        if(file.exists()){
            file.delete();

            Log.w("bug8", "datei gelöscht");
            file = new File(context.getFilesDir(), filename);

            Log.w("bug8", "datei neu erstellt");
        }

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

    public static String[] ListToArray(List<String> list){
        String[] data = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            data[i] = list.get(i);
        }
        return data;
    }



    public static String[] readFileToStringArray(Context context,String fileName){
        try {
            InputStream inputStreamCount = context.openFileInput(fileName);
            InputStreamReader inputStreamReaderCount = new InputStreamReader(inputStreamCount);

            BufferedReader lineCounter = new BufferedReader(inputStreamReaderCount);
            int linesAmount = 0;

            if ( inputStreamCount != null ) {
                String read  = "";
                while(read != null){
                    read = lineCounter.readLine();
                    if(read == null){
                        break;
                    }
                    linesAmount++;
                };
                inputStreamCount.close();

                String[] data = new String[linesAmount];

                InputStream inputStreamRead =  context.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStreamRead);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                int position = 0;
                //Lese alle Zeilen der Datei in text[] ein.
                for(int i = 0; i < linesAmount; i++){
                    line = bufferedReader.readLine();
                    data[position] = line;
                    position++;
                }
                inputStreamRead.close();
                return data;

            }
        }
        catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return new String[]{};
    }
}
