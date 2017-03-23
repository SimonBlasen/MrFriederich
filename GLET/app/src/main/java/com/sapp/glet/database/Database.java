package com.sapp.glet.database;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sapp.glet.MainActivity;
import com.sapp.glet.database.games.Game;
import com.sapp.glet.database.stats.StatsCsGo;
import com.sapp.glet.database.stats.StatsParagon;
import com.sapp.glet.database.stats.StatsProjectCars;
import com.sapp.glet.database.stats.StatsType;
import com.sapp.glet.filesystem.Filer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 04.03.2017.
 */

public class Database {

    private static List<Player> m_players = new ArrayList<Player>();
    private static List<Game> m_games = new ArrayList<Game>();
    private static final String PLAYERS_CACHE_FILENAME = "players_cache";
    private static final String PLAYER_ME_ID = "player_own_id";

    private static int m_own_id = -1;

    public static void loadDatabase(Context context){
        String[] data = loadPlayersCache(context);
        m_players = playersCacheToPlayerList(data);
    }

    public static void createPlayersCache(Context context){
        File file = new File(context.getFilesDir(), PLAYERS_CACHE_FILENAME);
    }

    //Writes the Players List to a file
    public static void writePlayersCache(Context context){
        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(PLAYERS_CACHE_FILENAME,Context.MODE_PRIVATE));
            //iterates over all players
            for(int i = 0; i < m_players.size(); i++){
                Player player = m_players.get(i);
                outputStreamWriter.write("<Player");
                outputStreamWriter.append("\n");
                outputStreamWriter.write(player.getName());
                outputStreamWriter.append("\n");
                outputStreamWriter.write("" + player.isOnline());
                outputStreamWriter.append("\n");
                outputStreamWriter.write("" + player.getId());
                outputStreamWriter.append("\n");
                outputStreamWriter.write("<Paragon");
                outputStreamWriter.append("\n");
                if(player.getStats(StatsType.PARAGON) != null){
                    StatsParagon player_paragon = (StatsParagon) player.getStats(StatsType.PARAGON);
                    outputStreamWriter.write("" +player_paragon.getScore());
                    outputStreamWriter.append("\n");
                }
                outputStreamWriter.write("<\\Paragon>");
                outputStreamWriter.append("\n");
                outputStreamWriter.write("<\\Player>");
                if(i < m_players.size()-1){
                    outputStreamWriter.append("\n");
                }
            }
            outputStreamWriter.close();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Loads players from players_cache file
    public static String[] loadPlayersCache(Context context){
        {
            try {
                InputStream inputStreamCount = context.openFileInput(PLAYERS_CACHE_FILENAME);
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
                    Log.w("ASDF", "Amount = " +linesAmount);
                    InputStream inputStreamRead =  context.openFileInput(PLAYERS_CACHE_FILENAME);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStreamRead);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String[] text = new String[linesAmount];
                    String line = "";
                    int position = 0;
                    //Lese alle Zeilen der Datei in text[] ein.
                    for(int i = 0; i < linesAmount; i++){
                        line = bufferedReader.readLine();
                        text[position] = line;
                        position++;
                    }
                    inputStreamRead.close();
                    return text;

                }
            }
            catch (FileNotFoundException e) {

            } catch (IOException e) {

            }

            return new String[]{};
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

    public static void setOwnId(Context context, int id)
    {
        m_own_id = id;

        Filer.writeFile(context, PLAYER_ME_ID, String.valueOf(m_own_id));
    }

    public static int getOwnId(Context context)
    {
        if (m_own_id == -1)
        {
            String fileContent = Filer.readFile(context, PLAYER_ME_ID);
            Log.w("SEND", "FileContent: " + fileContent);
            if (fileContent != null && fileContent.length() > 0)
            {
                m_own_id = Integer.valueOf(fileContent);
            }
        }
        return m_own_id;
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

    @Nullable
    public static Player getPlayerById(int id)
    {
        for (int i = 0; i < m_players.size(); i++)
        {
            if (m_players.get(i).getId() == id)
            {
                return m_players.get(i);
            }
        }

        return null;
    }

    public static Player getSelf(Context context)
    {
        return getPlayerById(getOwnId(context));
    }


    public static List<Player> playersCacheToPlayerList(String[] playerCache){
        Log.w("TEST", "PlayersCacheToPlayerList");
        List<Player> playerList= new ArrayList<Player>();
        int startPos = 0;
        int playersAmount = getPlayersAmount(playerCache);
        for(int i =0; i < playersAmount; i++){
            Log.w("TEST","for-schleife runde = " + i);
            int playerStartPos = findString("<Player", startPos, playerCache);
            int playerEndPos = findString("<\\Player>",playerStartPos,playerCache);

            int playerNamePos = playerStartPos+1;
            int playerOnline = playerStartPos+2;
            int playerIdPos = playerStartPos+3;
            Player player = new Player(playerCache[playerNamePos]);
            Log.w("TEST", "PlayerNamePos = " + playerCache[playerNamePos]);
            Log.w("TEST", "PlayerOnlinePos = " + playerCache[playerOnline]);
            Log.w("TEST", "PlayerID = " + playerCache[playerIdPos]);
            player.setId(Integer.parseInt(playerCache[playerIdPos]));
            Log.w("TEST", "Solomon i am in");
            player.setIsOnline(Boolean.parseBoolean(playerCache[playerOnline]));
            Log.w("TEST","Player erstellt");
            //Generate Stats of Player

            int paragonStartPos = findString("<Paragon",playerStartPos,playerCache);
            if(paragonStartPos != -1){
                //Generate Paragon stats
                StatsParagon paragonStats = new StatsParagon(playerCache[paragonStartPos+1]);
                player.addStats(paragonStats);
            }
            int csGoStartPos = findString("<Cs",playerStartPos,playerCache);

            if(csGoStartPos != -1){
                //Generate CsGo stats
                StatsCsGo csGoStats = new StatsCsGo();
                player.addStats(csGoStats);
            }

            int projectCarsStartPos = findString("<ProjectCars",playerStartPos,playerCache);
            if(projectCarsStartPos != -1){
                //Generate ProjectCars stats
                StatsProjectCars prCarsStats = new StatsProjectCars();
                player.addStats(prCarsStats);
            }
            playerList.add(player);

            //nächster Player
            startPos = playerEndPos+1;

        }
        Log.w("TEST", "returning playerList");
        return playerList;
    }

    //Berechne Anzahl der Player Einträge in PlayerCacheFile
    public static int getPlayersAmount(String[] data){
        int amount = 0;
        for(int i = 0; i < data.length; i++){
            if(data[i].equals("<Player")){
                amount++;
            }
        }


        return amount;
    }


    //Suche target String in Array, wenn nicht vorhanden return -1
    public static int findString(String target, int startPos, String[] array){
        int i = startPos;
        for(;i < array.length; i++){
            if(array[i].equals(target)){
                return i;
            }
        }
        return -1;
    }
}
