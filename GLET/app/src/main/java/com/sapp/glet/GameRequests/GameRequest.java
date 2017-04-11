package com.sapp.glet.GameRequests;

import com.sapp.glet.database.Player;
import com.sapp.glet.database.games.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 28.03.2017.
 */

public class GameRequest {
    private List<Player> mInvitedPlayers = new ArrayList<Player>();
    private Game mGame;
    private Player mRequestHost;
    private int mStartHour = 0;
    private int mStartMinute = 0;


    public GameRequest(List<Player> playerList, Game game, Player requestHost, int hour, int minute){
        mInvitedPlayers = playerList;
        mGame = game;
        mRequestHost = requestHost;
        mStartHour = hour;
        mStartMinute = minute;
    }

    public List<Player> getInvitedPlayers() {
        return mInvitedPlayers;
    }

    public Game getGame(){
        return mGame;
    }

    public Player getRequestHost(){
        return mRequestHost;
    }

    public int getRequestHour(){
        return mStartHour;
    }

    public int getRequestMinute(){
        return mStartMinute;
    }

}
