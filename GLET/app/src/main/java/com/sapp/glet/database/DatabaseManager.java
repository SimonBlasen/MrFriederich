package com.sapp.glet.database;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.sapp.glet.database.stats.Stats;
import com.sapp.glet.database.stats.StatsCsGo;
import com.sapp.glet.database.stats.StatsParagon;
import com.sapp.glet.database.stats.StatsProjectCars;
import com.sapp.glet.database.stats.StatsType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 20.03.2017.
 */


public class DatabaseManager {
    private static final String PLAYERS_CACHE_FILENAME = "players_cache";




//    public static Database loadDatabase(Context context){
//        String[] data = Database.loadPlayersCache(context);
//        List<Player> playerList = PlayersCacheToPlayerList(data);
//        for(int i = 0; i < playerList.size(); i++){
//            Log.w("TEST", "Name des Spielers = " + playerList.get(i).getName());
//            StatsParagon paragonStats = (StatsParagon) playerList.get(i).getStats(StatsType.PARAGON);
//            Log.w("TEST", "Elo des Spielers = " + paragonStats.getScore());
//        }
//        Database database = new Database(playerList, null);
//        return database;
//    }

//    public static List<Player> PlayersCacheToPlayerList(String[] playerCache){
//        Log.w("TEST", "PlayersCacheToPlayerList");
//        List<Player> playerList= new ArrayList<Player>();
//        int startPos = 0;
//        int playersAmount = getPlayersAmount(playerCache);
//        for(int i =0; i < playersAmount; i++){
//            Log.w("TEST","for-schleife runde = " + i);
//            int playerStartPos = findString("<Player", startPos, playerCache);
//            int playerEndPos = findString("<\\Player>",playerStartPos,playerCache);
//
//            int playerNamePos = playerStartPos+1;
//            int playerOnline = playerStartPos+2;
//            int playerIdPos = playerStartPos+3;
//            Player player = new Player(playerCache[playerNamePos]);
//            Log.w("TEST", "PlayerNamePos = " + playerCache[playerNamePos]);
//            Log.w("TEST", "PlayerOnlinePos = " + playerCache[playerOnline]);
//            Log.w("TEST", "PlayerID = " + playerCache[playerIdPos]);
//            player.setId(Integer.parseInt(playerCache[playerIdPos]));
//            Log.w("TEST", "Solomon i am in");
//            player.setIsOnline(Boolean.parseBoolean(playerCache[playerOnline]));
//            Log.w("TEST","Player erstellt");
//            //Generate Stats of Player
//
//            int paragonStartPos = findString("<Paragon",playerStartPos,playerCache);
//            if(paragonStartPos != -1){
//                //Generate Paragon stats
//                StatsParagon paragonStats = new StatsParagon(playerCache[paragonStartPos+1]);
//                player.addStats(paragonStats);
//            }
//            int csGoStartPos = findString("<Cs",playerStartPos,playerCache);
//
//            if(csGoStartPos != -1){
//                //Generate CsGo stats
//                StatsCsGo csGoStats = new StatsCsGo();
//                player.addStats(csGoStats);
//            }
//
//            int projectCarsStartPos = findString("<ProjectCars",playerStartPos,playerCache);
//            if(projectCarsStartPos != -1){
//                //Generate ProjectCars stats
//                StatsProjectCars prCarsStats = new StatsProjectCars();
//                player.addStats(prCarsStats);
//            }
//            playerList.add(player);
//
//            //nächster Player
//            startPos = playerEndPos+1;
//
//        }
//        Log.w("TEST", "returning playerList");
//        return playerList;
//    }
//
//    //Berechne Anzahl der Player Einträge in PlayerCacheFile
//    public static int getPlayersAmount(String[] data){
//        int amount = 0;
//        for(int i = 0; i < data.length; i++){
//            if(data[i].equals("<Player")){
//                amount++;
//            }
//        }
//
//
//        return amount;
//    }
//
//
//    //Suche target String in Array, wenn nicht vorhanden return -1
//    public static int findString(String target, int startPos, String[] array){
//        int i = startPos;
//        for(;i < array.length; i++){
//            if(array[i].equals(target)){
//                return i;
//            }
//        }
//        return -1;
//    }




}
