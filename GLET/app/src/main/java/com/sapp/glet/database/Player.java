package com.sapp.glet.database;

import android.provider.ContactsContract;
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


public class Player {

    private String m_name;
    private boolean m_isOnline = false;
    //Generiere Id mit id_count und clacId()
    private static int id_count = 0;

    private List<Stats> m_stats = new ArrayList<Stats>();
    /**
     * Eindeutige, immer konstante id jedes Spielers
     */
    private int m_id;

    public Player(String name){
        m_id = calcId();
        m_name = name;
    }

    public void setName(String name){this.m_name = name;}


    public void setId(int id){m_id = id;}

    public int getId() {
        return m_id;
    }

    public boolean isOnline() {
        return m_isOnline;
    }

    public void setIsOnline(boolean online){
        m_isOnline = online;
    }

    public String getName() {
        return m_name;
    }

    public void addStats(Stats stats){
        m_stats.add(stats);
    }

    //Gebe Stats für bestimmtes Spiel zurück
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
           return null;
       }
       else{
           return null;
       }
    }
    //Füge Stats hinzu
    public void setStats(Stats stats){
        m_stats.add(stats);
    }




    //Prüft ob Name nicht leer ist und ob Name existiert
    public static boolean validName(String name){
        if(name.equals("")){
            return false;
        }
        //TODO prüfe ob Spieler in Datenbank

        return true;
    }

    public int calcId(){
        int id = id_count;

        id_count++;
        return id;
    }


    @Override
    public String toString()
    {
        return m_name;
    }


}
