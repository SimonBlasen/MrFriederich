package com.sapp.glet;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sapp.glet.database.Database;
import com.sapp.glet.database.Player;
import com.sapp.glet.database.stats.StatsParagon;


public class FirstStart extends AppCompatActivity {

    Context theContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_start);
        Log.w("TEST", "First Launch.class");
        //Player Namensfeld
        final EditText text_player_name = (EditText) findViewById(R.id.text_player_name);


        Button b_to_main = (Button) findViewById(R.id.button_to_main);
        b_to_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast "Spieler prüfen"
                Toast.makeText(getApplicationContext(),"Player wird geprüft", Toast.LENGTH_SHORT).show();

                //Player in Database eintragen
                Player player_me = new Player("");
                String name_input = text_player_name.getText().toString();
                player_me.setName(name_input);
                //TODO Download Elo from Agora
                StatsParagon paragon = new StatsParagon(player_me.getName());
                paragon.loadScore();
                player_me.addStats(paragon);
                player_me.setIsOnline(true);
                Database.addPlayer(player_me);

                //Starte Main Activity
                Intent launch_main_activity = new Intent(theContext, MainActivity.class);
                theContext.startActivity(launch_main_activity);
            }
        });


    }
}
