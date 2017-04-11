package com.sapp.glet;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sapp.glet.GameRequests.Time;
import com.sapp.glet.database.Database;
import com.sapp.glet.database.Player;
import com.sapp.glet.database.games.Paragon;
import com.sapp.glet.filesystem.FilerDatabase;
import com.sapp.glet.GameRequests.GameRequest;
import com.sapp.glet.GameRequests.GameRequestHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentParagon.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentParagon#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentParagon extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static final Paragon GAME_TYPE = new Paragon();
    private static List<Player> invitedPlayers = new ArrayList<Player>();

    //UhrzeitWidget
    private AlertDialog dialogIn;
    private AlertDialog dialogAt;
    int inTime;
    int atHour;
    int atMinute;
    EditText paragonInTime;
    EditText paragonAtTime;
    Bundle savedInstanceState;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentParagon() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentParagon.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentParagon newInstance(String param1, String param2) {
        FragmentParagon fragment = new FragmentParagon();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Clock Widget stuff
        this.savedInstanceState = savedInstanceState;

        View view =  inflater.inflate(R.layout.fragment_paragon, container, false);

        paragonInTime = (EditText) view.findViewById(R.id.eT_paragon_input_in_time);
        paragonAtTime = (EditText) view.findViewById(R.id.eT_paragon_input_at_time);


        // Alert Popup InTime
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Get the layout inflater
        LayoutInflater inflater_paragon = this.getLayoutInflater(savedInstanceState);
        // Inflate and set the layout for the popup
        // Pass null as the parent view because it's going in the popup layout
        View view_popup1 = inflater.inflate(R.layout.start_time_popup1, null);
        builder.setView(view_popup1);
        builder.setTitle("Set the start time");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                paragonInTime.setText(inTime + " Minuten");
                // Hier noch das andere Feld ändern
                Date currentTime = new Date();
                int hours = currentTime.getHours() + 2;
                int mins = currentTime.getMinutes();
                mins = mins + inTime;
                if(mins >= 60){
                    mins = mins - 60;
                    hours++;
                }
                atHour = hours;
                atMinute = mins;
                paragonAtTime.setText(hours + ":" + mins);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        });


        final SeekBar inputTime = (SeekBar) view_popup1.findViewById(R.id.sB_input_time);
        inputTime.setMax(60);
        final TextView displayInputTime = (TextView) view_popup1.findViewById(R.id.tV_display_input_time);
        displayInputTime.setText("Jetzt");
        inputTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                inTime = inputTime.getProgress();
                if(inTime == 0){
                    displayInputTime.setText("Jetzt");
                }else{
                    displayInputTime.setText("In " + inTime + " min");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialogIn = builder.create();


        // Popup AtTime
        View view_popup2 = inflater.inflate(R.layout.start_time_popup2, null);
        builder.setView(view_popup2);
        builder.setTitle("Set the start time");

        final TimePicker timePick = (TimePicker) view_popup2.findViewById(R.id.tP_input_time);
        timePick.setIs24HourView(true);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                atHour = timePick.getCurrentHour();
                atMinute = timePick.getCurrentMinute();
                paragonAtTime.setText(atHour + ":" + atMinute);

                int timeDif = Time.calculateTimeDifference(atHour, atMinute);

                paragonInTime.setText(timeDif + " Minuten");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        });




        dialogAt = builder.create();


        paragonInTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogIn.show();
            }
        });
        paragonAtTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialogAt.show();
            }
        });


        //Dynamic Player List
        Database.loadDatabase(getContext());
        //TODO Remove List with Auto players
        for(int i = 0; i < 10; i++){
            Player player = new Player("Autoplayer " + i);
            Database.addPlayer(player);
        }
        List<Player> playerList = Database.getPlayers();

        String[] data = new String[playerList.size()];
        for(int i = 0; i < playerList.size(); i++){
            data[i] = playerList.get(i).getName();
        }
        PlayerListAdapter playerListAdapter = new PlayerListAdapter(getContext(),data);
        ListView list = (ListView) view.findViewById(R.id.list_ListView);
        list.setAdapter(playerListAdapter);



        playerListAdapter.setOnCheckBoxChangeListener(new CheckBoxChangeListener() {
            @Override
            public void OnCheckBoxChecked(int position) {
                Player player = Database.getPlayers().get(position);
                Log.w("bug5", "added Player = " + player.getName());
                FragmentParagon.invitedPlayers.add(player);

            }

            @Override
            public void OnCheckBoxUnChecked(int position) {
                Log.w("bug5", "unchecked");
                Player player = Database.getPlayers().get(position);
                invitedPlayers.remove(Player.getPlayerIndexInList(invitedPlayers,player));
            }

        });



        //Debug TODO Remove
        final TextView debug = (TextView) view.findViewById(R.id.debug);

        ImageButton debug2 = (ImageButton) view.findViewById(R.id.debug2);
        debug2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameRequest request = new GameRequest(invitedPlayers,GAME_TYPE,Database.getPlayer(0), atHour, atMinute); //TODO Time
                GameRequestHandler.addGameRequest(request);
                GameRequestHandler.writeGameRequestList(getContext());
                Log.w("bug7", "request written");
            }
        });

        GameRequestHandler.loadGameRequests(getContext());
        Log.w("bug7", "GameRequestsLoaded");

        debug.setText("");
        for(int i = 0; i < GameRequestHandler.getGameRequests().size(); i++){
            debug.setText(debug.getText() + "\n" + GameRequestHandler.getGameRequests().get(i).getRequestHost().getName());
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
