package com.shpp.sv.fragmentsnotepad;

import android.content.Intent;
import android.content.res.Configuration;
//import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
//import android.view.Menu;
import android.view.MenuItem;

public class EditNoteActivity extends AppCompatActivity implements onEditRequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        displayHomeButton();
        checkOrientation();
    }

    private void checkOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        //Log.d("svcom", "configuration changed");
        switch (orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                //finish();
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                //Log.d("svcom", "LANDSCAPE");
                finish();
                break;
            default:
                //Log.d("svcom", "UNKNOWN ORIENTATION");
                break;
        }
    }

    private void displayHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        int id = intent.getIntExtra(MainActivity.NOTE_ID, 0);

        EditNoteFragment edtFragment = (EditNoteFragment)getFragmentManager()
                .findFragmentById(R.id.frgEditeNote);

        if (edtFragment != null) {
            edtFragment.editNote(id);
        }
    }

    @Override
    public void editRequest(int id) {

    }

    @Override
    public void editFinish() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
