package com.sapp.glet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sapp.glet.database.Player;


public class FirstStart extends AppCompatActivity {

    Context theContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_start);


        //Player Namensfeld
        final EditText text_player_name = (EditText) findViewById(R.id.text_player_name);


        Button b_to_main = (Button) findViewById(R.id.button_to_main);
        b_to_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Toast "Spieler Erstellt"
                Toast.makeText(getApplicationContext(),"Player erstellt", Toast.LENGTH_SHORT).show();

                //Starte Main Activity
                Intent launch_main_activity = new Intent(theContext, MainActivity.class);
                theContext.startActivity(launch_main_activity);
            }
        });


    }
}
