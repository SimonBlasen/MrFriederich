package com.sapp.glet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sapp.glet.connection.Client;
import com.sapp.glet.connection.MessageListener;
import com.sapp.glet.database.Database;
import com.sapp.glet.database.DatabaseManager;
import com.sapp.glet.database.Player;
import com.sapp.glet.filesystem.Filer;
import com.sapp.glet.service.HelloService;
import com.sapp.glet.service.MessengerService;
import com.sapp.glet.service.PullService;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MessageListener {

    private Client client;
    private Intent intentService;
    private Context theContext;
    private DrawerLayout drawer;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        theContext = this;

        //Prüfe ob erster Start - wenn ja first_launch, sonst main.
        boolean isFirstTime = LaunchControl.isFirst(MainActivity.this);
        if(isFirstTime){
            Log.w("TEST", "erster Start!");
            Intent intent_firsttime = new Intent(theContext, FirstStart.class);
            startActivity(intent_firsttime);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavDrawer Content
        expandableListView = (ExpandableListView) findViewById(R.id.exp_listview);
        List<String> Headings = new ArrayList<String>();
        List<String> L1 = new ArrayList<String>();
        List<String> L2 = new ArrayList<String>();
        List<String> L3 = new ArrayList<String>();
        HashMap<String,List<String>> ChildList = new HashMap<String,List<String>>();

        String heading_items[] = getResources().getStringArray(R.array.header_titles);
        String l1[] = getResources().getStringArray(R.array.h1_items);
        String l2[] = getResources().getStringArray(R.array.h2_items);
        String l3[] = getResources().getStringArray(R.array.h3_items);

        for(String title : heading_items){
            Headings.add(title);
        }

        for(String title : l1){
            L1.add(title);
        }

        for(String title : l2){
            L2.add(title);
        }

        for(String title : l3){
            L3.add(title);
        }


        ChildList.put(Headings.get(0), L1);
        ChildList.put(Headings.get(1), L2);
        ChildList.put(Headings.get(2), L3);


        final NavDrawerAdapter navDrawerAdapter = new NavDrawerAdapter(this, Headings, ChildList);


        expandableListView.setAdapter(navDrawerAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Intent intent;
                switch (groupPosition){
                    case 0:
                        intent = new Intent(getApplicationContext(),StartGame.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(),ProfileAgora.class);
                        startActivity(intent);
                        drawer.closeDrawer(Gravity.LEFT);
                }
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition){
                    case 0:
                    case 1:
                        switch (childPosition){
                            case 0:
                                //TODO getself methode benutzen.
                                Database.getPlayer(0).setIsOnline(true);
                                Toast.makeText(getApplicationContext(), "Online", Toast.LENGTH_SHORT).show();
                                expandableListView.collapseGroup(1);
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "Beschäftigt", Toast.LENGTH_SHORT).show();
                                Database.getPlayer(0).setIsOnline(false);
                                expandableListView.collapseGroup(1);
                                break;
                        }
                }
                return false;
            }
        });






// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        final CheckBox cB_accept = (CheckBox) findViewById(R.id.cB_accept);
        final CheckBox cB_decline = (CheckBox) findViewById(R.id.cB_decline);
        final Animation animationFadeAccept = AnimationUtils.loadAnimation(this, R.anim.fade_transition);
        final Animation animationFadeDecline = AnimationUtils.loadAnimation(this, R.anim.fade_transition);
        cB_accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                if(cB_decline.isChecked()){
                    cB_accept.startAnimation(animationFadeAccept);
                    cB_decline.setChecked(false);
                    cB_decline.startAnimation(animationFadeDecline);
                }else{
                    cB_accept.startAnimation(animationFadeAccept);
                }

            }
        });
        cB_decline.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v){
                if(cB_accept.isChecked()){
                    cB_decline.startAnimation(animationFadeDecline);
                    cB_accept.setChecked(false);
                    cB_accept.startAnimation(animationFadeAccept);
                }else{
                    cB_decline.startAnimation(animationFadeDecline);
                }

            }
        });*/
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(theContext, StartGame.class);
                theContext.startActivity(intent);
            }
        });


        intentService = new Intent(this, HelloService.class);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void recieveMessage(String message, byte[] bytes) {
        //TextView tv = (TextView) findViewById(R.id.textViewMsg);
        //tv.setText(message);
    }


}
