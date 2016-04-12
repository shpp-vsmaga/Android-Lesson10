package com.shpp.sv.fragmentsnotepad;

import android.content.Intent;
import android.content.res.Configuration;
//import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.util.Log;
import android.view.View;
//import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements onEditRequestListener {
    public final static String NOTE_ID = "noteid";
    private final static int REQUEST_CODE_EDIT = 1;
    private static int currentNoteID = -1;
    private static final int EMPTY_NOTE_ID = -1;
    private static final String LOG_TAG = "svcom";
    private static int currentOrientation = Configuration.ORIENTATION_PORTRAIT;
    private static boolean isTablet = false;

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
        checkDevice();
        checkOrientation();

    }

    private void checkDevice() {
        isTablet = getResources().getBoolean(R.bool.isTablet);
    }


    private void checkOrientation() {
        currentOrientation = getResources().getConfiguration().orientation;
        //currentOrientation = orientation;
        Log.d(LOG_TAG, "current check orient id - " + currentNoteID);
//        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT && currentNoteID >= 0 && !isTablet){
//            editNote(currentNoteID);
//        } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE && currentNoteID >= 0 && isTablet){
//            editNote(currentNoteID);
//            Log.d(LOG_TAG, "lanscape and tablet id - " + currentNoteID);
//        }
//        if (currentNoteID >= 0){
//            editNote(currentNoteID);
//        }
//        switch (orientation) {
//            case Configuration.ORIENTATION_PORTRAIT:
//                Log.d("svcom", "PORTRAIT " + currentNoteID);
//                break;
//            case Configuration.ORIENTATION_LANDSCAPE:
//                Log.d("svcom", "LANDSCAPE");
//                break;
//            default:
//                Log.d("svcom", "UNKNOWN ORIENTATION");
//                break;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentNoteID >= 0){
            editNote(currentNoteID);
        }
    }

    @Override
    public void editRequest(int id) {
        editNote(id);
    }

    @Override
    public void editFinish() {
        updateList(false);
        currentNoteID = EMPTY_NOTE_ID;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                currentNoteID = EMPTY_NOTE_ID;
                updateList(false);
            } else if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
                editNote(currentNoteID);
            } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT){
                currentNoteID = EMPTY_NOTE_ID;
                updateList(false);
            } else {
                updateList(false);
            }

        }
    }

//    private void disableEditFragmentControls(){
//        EditNoteFragment editFragment = (EditNoteFragment)getFragmentManager()
//                .findFragmentById(R.id.frgEditeNote);
//        if (editFragment != null){
//            editFragment.setControlsActive(false);
//        }
//    }

    private void openEditActivity(int id) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_ID, id);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    private void updateList(boolean activateLastItem) {
        NotesListFragment notesListFragment = (NotesListFragment) getFragmentManager()
                .findFragmentById(R.id.frgNotesList);

        if (notesListFragment != null) {
            notesListFragment.updateList(activateLastItem);
        }
    }

    private void editNote(int id) {
        EditNoteFragment editFragment = (EditNoteFragment) getFragmentManager()
                .findFragmentById(R.id.frgEditeNote);
        currentNoteID = id;
        //Log.d("svcom", "editeNote id " + currentNoteID);
        //&& editFragment.isVisible()

        if (editFragment != null && isTablet) {
            editFragment.editNote(id);
            //Log.d(LOG_TAG, "edit fragment is visible, edit - " + id);
        } else if (editFragment != null && !isTablet && currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            //Log.d(LOG_TAG, "edit fragment LANDSCAPE, edit - " + id);
            editFragment.editNote(id);
        } else if (!isTablet && currentOrientation == Configuration.ORIENTATION_PORTRAIT){
            //Log.d(LOG_TAG, "open activity portrait, edit - " + id);
            openEditActivity(id);
        }
    }

//    private boolean editFragmentIsVisible() {
//        EditNoteFragment editFragment = (EditNoteFragment) getFragmentManager()
//                .findFragmentById(R.id.frgEditeNote);
//        boolean result = false;
//        if (editFragment != null) {
//            result = editFragment.isVisible();
//        }
//        return result;
//    }

    private void addNote() {
        NotesDbHelper dbHelper = NotesDbHelper.getInstance(this);
        int newID = dbHelper.addNote("");
        updateList(true);
        editNote(newID);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        int orientation = newConfig.orientation;
//        //Log.d("svcom", "configuration changed");
//        switch (orientation){
//            case Configuration.ORIENTATION_PORTRAIT:
//                Log.d("svcom", "PORTRAIT");
//                break;
//            case Configuration.ORIENTATION_LANDSCAPE:
//                Log.d("svcom", "LANDSCAPE");
//                break;
//            default:
//                Log.d("svcom", "UNKNOWN ORIENTATION");
//                break;
//        }
//    }
}
