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



    private int m_score = 0;
    private String m_league;

    public StatsParagon(String name){
        loadScore();
        calcLeague(m_score);
    }

    public StatsParagon(int score){
        m_score = score;
        calcLeague(score);
    };

    public int getScore(){
        return m_score;
    }


    public void loadScore(){
        //TODO
        // Score wird von Agora geladen
         m_score = 1700;
    }

    public String getLeague(){
        return m_league;
    }



    public void calcLeague(int score){
        if (score <= BRONCE_VAL){
            m_league = BRONCE;
        }
        else if (score <= SILVER_VAL){
            m_league = SILVER;
        }
        else if (score <= GOLD_VAL){
            m_league = GOLD;
        }
        else if (score <= PLAIN_VAL){
            m_league = PLATIN;
        }
        else{
            m_league = DIAMOND;
        }
    }

}
