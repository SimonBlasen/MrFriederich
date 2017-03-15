package com.sapp.glet.database.games.general;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Simon on 15.03.2017.
 */

public class News {

    private int m_authorId = -1;
    private String m_message;
    private Calendar m_timeStamp;

    public News(int authorId, String message, Calendar timeStamp)
    {
        m_authorId = authorId;
        m_message = message;
        m_timeStamp = timeStamp;
    }

    public News(int authorId, String message, int year, int month, int day, int hour, int minute, int second)
    {
        new News(authorId, message, new GregorianCalendar(year, month, day, hour, minute, second));
    }


    public int getAuthorId()
    {
        return m_authorId;
    }

    public String getMessage()
    {
        return m_message;
    }

    public Calendar getTimeStamp()
    {
        return m_timeStamp;
    }
}
