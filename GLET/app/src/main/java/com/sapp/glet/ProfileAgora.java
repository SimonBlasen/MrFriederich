package com.sapp.glet;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sapp.glet.database.Database;
import com.sapp.glet.database.Player;
import com.sapp.glet.database.stats.StatsParagon;
import com.sapp.glet.database.stats.StatsType;

import org.w3c.dom.Text;

public class ProfileAgora extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_agora);

        TextView text_player_name = (TextView) findViewById(R.id.text_player_name);
        TextView text_player_score = (TextView) findViewById(R.id.text_player_score);
        TextView text_player_league = (TextView) findViewById(R.id.text_player_leauge);
        ImageView score_logo = (ImageView) findViewById(R.id.score_logo);

        Player test = Database.getPlayer(0);


        StatsParagon test_stats = (StatsParagon) test.getStats(StatsType.PARAGON);

       String league = test_stats.getLeague();


        switch (league){
            case StatsParagon.BRONCE:
                text_player_league.setTextColor(Color.parseColor("#cd7f32"));
                text_player_score.setTextColor(Color.parseColor("#cd7f32"));
                score_logo.setImageResource(R.mipmap.ic_bronce);

                break;
            case StatsParagon.SILVER:
                text_player_league.setTextColor(Color.parseColor("#c0c0c0"));
                text_player_score.setTextColor(Color.parseColor("#c0c0c0"));
                score_logo.setImageResource(R.mipmap.ic_silver);
                break;
            case StatsParagon.GOLD:
                text_player_league.setTextColor(Color.parseColor("#ffd700"));
                text_player_score.setTextColor(Color.parseColor("#ffd700"));
                score_logo.setImageResource(R.mipmap.ic_gold);
                break;
            case StatsParagon.PLATIN:
                text_player_league.setTextColor(Color.parseColor("#478F63"));
                text_player_score.setTextColor(Color.parseColor("#478F63"));
                score_logo.setImageResource(R.mipmap.ic_platin);
                break;
            case StatsParagon.DIAMOND:
                text_player_league.setTextColor(Color.parseColor("#0198E1"));
                text_player_score.setTextColor(Color.parseColor("#0198E1"));
                score_logo.setImageResource(R.mipmap.ic_diamond);
                break;
        }
        text_player_name.setText(test.getName());
        text_player_score.setText("" + ((StatsParagon) test.getStats(StatsType.PARAGON)).getScore());
        text_player_league.setText(league);


    }

}
