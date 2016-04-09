package com.shpp.sv.fragmentsnotepad;

import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.app.ListFragment;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by SV on 07.04.2016.
 */
public class NotesListFragment extends ListFragment {
    private ArrayAdapter<String> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NotesDbHelper dbHelper = NotesDbHelper.getInstance(getActivity());

        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, dbHelper.getDbArray());

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ((onEditRequestListener)getActivity()).editRequest(position);
    }


    public void updateList(boolean activateLastItem) {
        adapter.notifyDataSetChanged();

        ListView listView = getListView();
        if (activateLastItem){
            listView.setItemChecked(listView.getCount() - 1, true);
        } else {
            listView.setItemChecked(-1, true);
        }
    }
}
