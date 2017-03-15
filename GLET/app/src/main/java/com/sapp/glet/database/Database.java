package com.sapp.glet.database;

import android.support.annotation.Nullable;

import com.sapp.glet.database.games.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 04.03.2017.
 */

public class Database {

    private static List<Player> m_players = new ArrayList<Player>();
    private static List<Game> m_games = new ArrayList<Game>();

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
