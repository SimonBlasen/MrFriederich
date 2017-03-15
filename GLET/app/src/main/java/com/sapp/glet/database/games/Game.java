package com.sapp.glet.database.games;

import com.sapp.glet.database.games.general.News;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Simon on 15.03.2017.
 */

public abstract class Game {

    protected String m_gameName;
    /**
     * Sind im Grunde sowas wie ein fortlaufender News/Kommentare Feed, mit Infors über Patches, geile Plays oder so Zeugs. Kann jeder reinschreiben
     */
    protected List<News> m_news = new ArrayList<News>();

    public String getGameName() {
        return m_gameName;
    }

    public int getNewsAmount()
    {
        return m_news.size();
    }

    public News getNews(int index)
    {
        if (index >= 0 && index < m_news.size())
            return m_news.get(index);
        else
            return null;
    }

    public void AddNews(News news)
    {
        m_news.add(news);
    }

    public void AddNews(int authorId, String message, Calendar timeStamp)
    {
        m_news.add(new News(authorId, message, timeStamp));
    }

    public void AddNews(int authorId, String message, int year, int month, int day, int hour, int minute, int second)
    {
        AddNews(authorId, message, new GregorianCalendar(year, month, day, hour, minute, second));
    }




    @Override
    public String toString()
    {
        return m_gameName;
    }
}
