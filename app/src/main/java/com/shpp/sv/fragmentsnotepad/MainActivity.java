package com.shpp.sv.fragmentsnotepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements onEditRequestListener{
    public final static String NOTE_ID = "noteid";
    private final static int REQUEST_CODE_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });
    }

    @Override
    public void editRequest(int id) {
        editNote(id);
    }

    @Override
    public void editFinish() {
        updateList(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT){
            updateList(false);
        }
    }

    private void openEditActivity(int id) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_ID, id);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    private void updateList(boolean activateLast){
        NotesListFragment notesListFragment = (NotesListFragment)getFragmentManager()
                .findFragmentById(R.id.frgNotesList);

        if (notesListFragment != null && notesListFragment.isVisible()) {
            notesListFragment.updateList(activateLast);
        }
    }

    private void editNote(int id){
        EditNoteFragment editFragment = (EditNoteFragment)getFragmentManager()
                .findFragmentById(R.id.frgEditeNote);

        if (editFragment != null && editFragment.isVisible()) {
            editFragment.editNote(id);
        } else {
            openEditActivity(id);
        }
    }

    private void addNote(){
        NotesDbHelper dbHelper = NotesDbHelper.getInstance(this);
        int newID = dbHelper.addNote("");
        updateList(true);
        editNote(newID);
    }
}
