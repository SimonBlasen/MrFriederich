package com.sapp.glet.gamelauncher;

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
    private Player mRequestPlayer;

    public GameRequest(List<Player> playerList, Game game, Player requestPlayer){
        mInvitedPlayers = playerList;
        mGame = game;
        mRequestPlayer = requestPlayer;
    }

    public List<Player> getInvitedPlayers() {
        return mInvitedPlayers;
    }




}
