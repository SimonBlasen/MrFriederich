package com.sapp.glet.GameRequests;

import android.content.Context;
import android.provider.ContactsContract;

import com.sapp.glet.database.Database;
import com.sapp.glet.database.Player;
import com.sapp.glet.database.games.CsGo;
import com.sapp.glet.database.games.Game;
import com.sapp.glet.database.games.Paragon;
import com.sapp.glet.database.games.ProjectCars;
import com.sapp.glet.filesystem.FilerDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 28.03.2017.
 */

public class GameRequestHandler {
    private static final String REQUESTS_FILE_NAME = "game_requests";
    private static List<GameRequest> gameRequestsList = new ArrayList<GameRequest>();


    public static void addGameRequest(GameRequest gameRequest){
        gameRequestsList.add(gameRequest);
    }

    public static void removeGameListRequest(GameRequest gameRequest){
        gameRequestsList.remove(findGameRequestIndex(gameRequest));
    }

    public static List<GameRequest> getGameRequests(){
        return gameRequestsList;
    }

    public static void writeGameRequestList(Context context){
        String[] data = FilerDatabase.ListToArray(gameRequestsToList());
        FilerDatabase.writeFile(context, REQUESTS_FILE_NAME,data);
    }

    public static void loadGameRequests(Context context){
        String[] data = FilerDatabase.readFileToStringArray(context, REQUESTS_FILE_NAME);
        int gameRequestConter = FilerDatabase.countTargetString("<GameRequest", data);
        List<GameRequest> gameRequests = new ArrayList<GameRequest>();
        int pos = 0;
        //Führe für jeden GameRequest aus
        for(int i = 0; i < gameRequestConter; i++){
            pos++; //<GameRequest
            pos++; //<Time
            int timeHour = Integer.parseInt(data[pos]);
            pos++;
            int timeMinute = Integer.parseInt(data[pos]);
            pos++;
            pos++; //<\\Time>
            pos++; //<Game
            Game game = null;
            switch (data[pos]){
                case "Paragon" :
                    game = new Paragon();
                    break;
                case "Project Cars" :
                    game = new ProjectCars();
                    break;
                case "Counter Strike - Global Offensive" :
                    game = new CsGo();
                    break;
            }
            pos++;
            pos++; //<\\Game>
            pos++; //<Host
            Player host = Database.getPlayerById(Integer.parseInt(data[pos]));
            pos++;
            pos++; //<\\Host>
            pos++; //<Invited
            //Generiere Liste eingeladener Spieler
            List<Player> invitedPlayers = new ArrayList<Player>();
            while (!data[pos].equals("<\\Invited>")){
                Player player = Database.getPlayerById(Integer.parseInt(data[pos]));
                invitedPlayers.add(player);
                pos++;
            }
            pos++; //<\\Invited
            pos++; //<\\GameRequest
            GameRequest gameRequest = new GameRequest(invitedPlayers,game,host,timeHour,timeMinute);
            gameRequests.add(gameRequest);
        }
        gameRequestsList = gameRequests;
    }



    //Helper Methods Stuff
    //Returns -1 if GameRequest is not in List
    public static int findGameRequestIndex(GameRequest gameRequest){
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
            data.add("<Time");
            data.add("" + gameRequest.getRequestHour());
            data.add("" + gameRequest.getRequestMinute());
            data.add("<\\Time>");
            data.add("<Game");
            data.add(gameRequest.getGame().getGameName());
            data.add("<\\Game");
            data.add("<Host");
            data.add("" + gameRequest.getRequestHost().getId()); //Todo trage ID nicht Name ein.
            data.add("<\\Host>");
            data.add("<Invited");
            List<Player> playerList = gameRequest.getInvitedPlayers();
            for(int j = 0; j < playerList.size(); j++){
                data.add("" + playerList.get(j).getId()); //Todo trage ID nicht Name ein
            }
            data.add("<\\Invited>");
            data.add("<\\GameRequest>");
        }
        return data;
    }

}
