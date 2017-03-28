package com.sapp.glet;

/**
 * Created by Simon on 27.03.2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 27.03.2017.
 */

public class PlayerListAdapter extends ArrayAdapter<String> {
    private List<CheckBoxChangeListener> cbcl;

    PlayerListAdapter(Context context, String[] data) {
        super(context, R.layout.player_list_item, data);
        cbcl = new ArrayList<CheckBoxChangeListener>();

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.player_list_item, parent, false);


        String playerName = getItem(position);
        final CheckBox playerButton = (CheckBox) view.findViewById(R.id.list_player_name);
        playerButton.setText(playerName);
        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < cbcl.size(); i++)
                {
                    if(playerButton.isChecked()){
                        cbcl.get(i).OnCheckBoxChecked(position);
                    }
                    else{
                        cbcl.get(i).OnCheckBoxUnChecked(position);
                    }
                }
            }
        });

        return view;

    }


    public void setOnCheckBoxChangeListener(CheckBoxChangeListener cbc)
    {
        cbcl.add(cbc);
    }
}