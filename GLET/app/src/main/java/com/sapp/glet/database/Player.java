package com.sapp.glet.database;

import android.util.Log;

import com.sapp.glet.database.stats.Stats;
import com.sapp.glet.database.stats.StatsCsGo;
import com.sapp.glet.database.stats.StatsParagon;
import com.sapp.glet.database.stats.StatsProjectCars;
import com.sapp.glet.database.stats.StatsType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 04.03.2017.
 */

//Jeder Player hat Liste mit Stats?!

public class Player {

    private String m_name;
    private boolean m_isOnline = false;

    private List<Stats> m_stats = new ArrayList<Stats>();
    /**
     * Eindeutige, immer konstante id jedes Spielers
     */
    private int m_id;

    public Player(int id)
    {
        m_id = id;
    }
    public Player(int id, String name)
    {
        m_id = id;
        m_name = name;

    }

    public void setM_name(String name){this.m_name = name;}

    public String getM_name(){return m_name;}

    public int getId() {
        return m_id;
    }

    public boolean isOnline() {
        return m_isOnline;
    }

    public String getName() {
        return m_name;
    }

    public void addM_stats(Stats stats){
        m_stats.add(stats);
    }


    public Stats getStats(StatsType type) {
       if(m_stats != null){

           for(int i = 0; i < m_stats.size(); i++){
               switch (type){
                   case PARAGON:
                       if(m_stats.get(i) instanceof StatsParagon){
                           return m_stats.get(i);
                       }
                   case CSGO:
                       if(m_stats.get(i) instanceof StatsCsGo){
                           return m_stats.get(i);
                       }
                   case PROJECTCARS:
                       if(m_stats.get(i) instanceof StatsProjectCars){
                           return m_stats.get(i);
                       }
               }

           }


           if(type == StatsType.PARAGON){
               for(int i = 0; i < m_stats.size(); i++){
                   if(m_stats.get(i) instanceof StatsParagon){
                       return m_stats.get(i);
                   }
               }
           }





           return null;
       }


       else{
           return null;
       }
    }


    @Override
    public String toString()
    {
        return m_name;
    }


}
