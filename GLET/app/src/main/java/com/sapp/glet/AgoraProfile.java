package com.sapp.glet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sapp.glet.database.Player;
import com.sapp.glet.database.stats.StatsParagon;

import static com.sapp.glet.R.id.player_name;

public class AgoraProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agora_profile);

    }

    TextView text_player_name = (TextView) findViewById(player_name);
    TextView text_player_score = (TextView) findViewById(R.id.player_score);
    TextView text_player_league = (TextView) findViewById(R.id.player_leauge);

    Player tom = new Player(1, "Tom");
    StatsParagon tom_paragon = new StatsParagon(tom);


    //player_name.setText("Test");


}
