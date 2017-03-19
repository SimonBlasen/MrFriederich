package com.sapp.glet.database.stats;

import com.sapp.glet.database.Player;

/**
 * Created by Simon on 15.03.2017.
 */

public class StatsParagon extends Stats {


    private static final int BRONCE_VAL = 1099;
    private static final int SILVER_VAL = 1299;
    private static final int GOLD_VAL = 1499;
    private static final int PLAIN_VAL = 1699;

    public static final String BRONCE = "Bronze";
    public static final String SILVER = "Silber";
    public static final String GOLD = "Gold";
    public static final String PLATIN = "Platin";
    public static final String DIAMOND = "Diamant";



    private int score = 0;
    private String league;

    public StatsParagon(Player player){
        score = getScore(player);
        calcLeague(player);
    };


    public int getScore(Player p){
        //TODO
        // Score wird von Agora geladen
        return 900;
    }

    public String getLeague(){
        return league;
    }



    public void calcLeague(Player player){
        if (score <= BRONCE_VAL){
            league = BRONCE;
        }
        else if (score <= SILVER_VAL){
            league = SILVER;
        }
        else if (score <= GOLD_VAL){
            league = GOLD;
        }
        else if (score <= PLAIN_VAL){
            league = PLATIN;
        }
        else{
            league = DIAMOND;
        }
    }



}
