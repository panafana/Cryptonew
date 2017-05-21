package com.example.panafana.cryptonew;

/**
 * Created by panafana on 22-Apr-17.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyList2 extends ListActivity {

    private TextView text;
    private List<String> listValues;
    SharedPreferences SP;
    SharedPreferences.Editor SPE;
    private Toolbar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keylist2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        SP = this.getSharedPreferences("KeyChain", MODE_PRIVATE);

        Map<String, ?> keys = SP.getAll();

        text = (TextView) findViewById(R.id.mainText);

        listValues = new ArrayList<String>();


        for (Map.Entry<String, ?> entry : keys.entrySet()) {

            listValues.add(entry.getKey().toString());
            //Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());

        }


        // initiate the listadapter
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                R.layout.rowlayout, R.id.listText, listValues);

        // assign the list adapter
        setListAdapter(myAdapter);

    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);
        //String selectedItem = (String) getListAdapter().getItem(position);

        text.setText("You clicked " + selectedItem + " at position " + position);
        Intent i = new Intent(getApplicationContext(), SignedMessage.class);
        i.putExtra("publicK", SP.getString(selectedItem,""));
        startActivity(i);
    }


}
