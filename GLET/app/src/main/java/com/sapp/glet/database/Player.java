package com.sapp.glet.database;

import com.sapp.glet.database.stats.Stats;

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

    public List<Stats> getM_stats(){
        return m_stats;
    }


    @Override
    public String toString()
    {
        return m_name;
    }

}
