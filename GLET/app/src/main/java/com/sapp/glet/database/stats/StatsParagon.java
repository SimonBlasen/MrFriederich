package com.sapp.glet.database.stats;

import com.sapp.glet.database.Player;

/**
 * Created by Simon on 15.03.2017.
 */

public class StatsParagon extends Stats {

    private static final int BRONCE = 1099;
    private static final int SILVER = 1299;
    private static final int GOLD = 1499;
    private static final int PLAIN = 1699;
    private static final int DIAMOND = Integer.MAX_VALUE;

    private int score = 0;
    private String league;

    public StatsParagon(Player player){
        score = getScore(player);
    };


    public int getScore(Player p){
        //TODO
        // Score wird von Agora geladen
        return 42;
    }



    public void calcLeague(Player player){
        if (score <= BRONCE){
            league = "Bronze";
        }
        else if (score <= SILVER){
            league = "Silber";
        }
        else if (score <= GOLD){
            league = "Gold";
        }
        else if (score <= PLAIN){
            league = "Platin";
        }
        else{
            league = "Diamant";
        }
    }


}
