package com.sapp.glet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sapp.glet.database.Database;
import com.sapp.glet.database.Player;
import com.sapp.glet.gamelauncher.GameRequestHandler;

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
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_paragon, container, false);


        //Dynamic Player List
        //List with players
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
                GameRequestHandler.addInvitedPlayer(player);
            }

            @Override
            public void OnCheckBoxUnChecked(int position) {
                Log.w("bug5", "unchecked");
                Player player = GameRequestHandler.getInvitedPlayers().get(position);
                Log.w("bug5", "removed Player = " + player.getName());
                GameRequestHandler.removeInvitedPlayer(position);
            }
        });



        //Debug TODO Remove
        final TextView debug = (TextView) view.findViewById(R.id.debug);

        Button debug2 = (Button) view.findViewById(R.id.debug2);
        debug2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> debugData = GameRequestHandler.invitedPlayersToString();
                debug.setText("");
                for(int i =0; i < debugData.size(); i++){
                    debug.setText(debug.getText() + "\n" +  debugData.get(i));
                }
            }
        });

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
