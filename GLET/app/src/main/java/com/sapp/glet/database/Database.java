package com.sapp.glet.database;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sapp.glet.MainActivity;
import com.sapp.glet.database.games.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 04.03.2017.
 */

public class Database {

    private static List<Player> m_players = new ArrayList<Player>();
    private static List<Game> m_games = new ArrayList<Game>();
    private static final String PLAYERS_CACHE_FILENAME = "players_cache";
    //TODO Array in File schreiben

    public static void createPlayersCache(Context context){
        File file = new File(context.getFilesDir(), PLAYERS_CACHE_FILENAME);
    }

    //Writes the Players List to a file
    public static void writePlayersCache(Context context){
        try{
            m_players.add(new Player("Thomas"));

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(PLAYERS_CACHE_FILENAME,Context.MODE_PRIVATE));
            for(int i = 0; i < m_players.size(); i++){
                Player player = m_players.get(i);
                outputStreamWriter.write(player.getName());
                Log.w("ASDF","" + "Writing = " + player.getName());
                outputStreamWriter.append("\n\r");
            }
            outputStreamWriter.close();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Loads players from players_cache file
    public static String getPlayersCache(Context context){
        String content = "";
        {
            try {
                InputStream inputStream = context.openFileInput(PLAYERS_CACHE_FILENAME);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                if ( inputStream != null ) {
                    String line = "";
                    do{
                        Log.w("ASDF", "Reading Line = " + line);
                        content = content + line;
                        Log.w("ASDF", "Content = " + content);
                        line = bufferedReader.readLine();
                    }while(line !=null);
                    Log.w("ASDF", content + "ENDE");
                    inputStream.close();

                }
            }
            catch (FileNotFoundException e) {

            } catch (IOException e) {

            }

            return content;
        }
    }

    /**
     * Returns list of current registered Players
     * @return List of all players
     */
    public static List<Player> getPlayers(){
        return m_players;
    }

    public static void addPlayer(Player player){
        m_players.add(player);
    }



    /**
     * Returns the current amount of players available in total
     * @return The amount of players
     */
    public static int getPlayersAmount() {
        return m_players.size();
    }

    /**
     * Returns the requested player or null, if the index is out of the lists range
     * @param index The index of the player to return
     * @return The requested player
     */
    @Nullable
    public static Player getPlayer(int index) {
        if (index >= 0 && index < m_players.size())
            return m_players.get(index);
        else
            return null;
    }
}
