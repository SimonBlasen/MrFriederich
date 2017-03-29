package com.sapp.glet.gamelauncher;

import android.content.Context;
import android.util.Log;

import com.sapp.glet.database.Player;
import com.sapp.glet.database.games.Game;
import com.sapp.glet.filesystem.FilerDatabase;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 28.03.2017.
 */

public class GameRequestHandler {
    private static final String GAME_FILENAME = "game_requests";
    private static List<GameRequest> gameRequestsList = new ArrayList<GameRequest>();


    public static void addGameRequest(GameRequest gameRequest){
        gameRequestsList.add(gameRequest);
    }

    public static void removeGameListRequest(GameRequest gameRequest){
        gameRequestsList.remove(findGameRequestIntex(gameRequest));
    }

    public static void writeGameRequestList(Context context){
        String[] data = FilerDatabase.ListToArray(gameRequestsToList());
        FilerDatabase.writeFile(context,GAME_FILENAME,data);
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

    public static List<String> gameRequestsToList(){
        List<String> data = new ArrayList<String>();
        for(int i = 0; i < gameRequestsList.size(); i++){
            GameRequest gameRequest = gameRequestsList.get(i);
            data.add("<GameRequest");
            data.add("<Game");
            data.add(gameRequest.getGame().getGameName());
            data.add("<\\Game");
            data.add("<Host");
            data.add(gameRequest.getRequestHost().getName()); //Todo trage ID nicht Name ein.
            data.add("<\\Host>");
            data.add("<Invited");
            List<Player> playerList = gameRequest.getInvitedPlayers();
            for(int j = 0; j < playerList.size(); j++){
                data.add(playerList.get(j).getName());
            }
            data.add("<\\Invited>");
        }
        return data;
    }
}
