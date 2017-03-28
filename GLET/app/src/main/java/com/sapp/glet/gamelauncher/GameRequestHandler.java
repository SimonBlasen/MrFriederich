package com.sapp.glet.gamelauncher;

import android.content.Context;

import com.sapp.glet.database.Player;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 28.03.2017.
 */

public class GameRequestHandler {
    private static String GAME_FILENAME = "game_request";
    private static List<GameRequest> gameRequestsList;

    public static void addGameRequest(GameRequest gameRequest){
        gameRequestsList.add(gameRequest);
    }

    public static void removeGameListRequest(GameRequest gameRequest){
        gameRequestsList.remove(42);
    }

    //Returns -1 if GameRequest is not in List
    public static int findGameRequestIntex(GameRequest gameRequest){
        if (isInList(gameRequest)){
            for(int i = 0; i <  gameRequestsList.size(); i++){
                if(gameRequest.equals(gameRequestsList.get(i))){
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean isInList(GameRequest gameRequest){
        for(int i = 0; i < gameRequestsList.size(); i++){
            if(gameRequest.equals(gameRequestsList.get(i))){
                return true;
            }
        }
        return false;
    }

    public static void writeGameRequests(){

    }
}
