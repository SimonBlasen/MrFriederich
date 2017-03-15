package com.sapp.glet.database;

import com.sapp.glet.database.stats.Stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 04.03.2017.
 */

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

    public int getId() {
        return m_id;
    }

    public boolean isOnline() {
        return m_isOnline;
    }

    public String getName() {
        return m_name;
    }


    @Override
    public String toString()
    {
        return m_name;
    }
}
