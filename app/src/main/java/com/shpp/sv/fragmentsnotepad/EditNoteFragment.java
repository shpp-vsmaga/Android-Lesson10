package com.shpp.sv.fragmentsnotepad;

import android.app.Fragment;
import android.os.Bundle;
//import android.R;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class EditNoteFragment extends Fragment {
    private EditText edtNoteEditor;
    private static final int SAVE_BUTTON_ID = 101;
    private static final int DELETE_BUTTON_ID = 102;
    private static final String SAVED_BUTTONS_STATE = "buttonsState";
    private static final String SAVED_CURRENT_ID = "savedID";
    private boolean showButtons = false;
    private int currentId;
    private MenuItem saveButton;
    private MenuItem deleteButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        edtNoteEditor = (EditText) view.findViewById(R.id.edtEditeNote);
        setHasOptionsMenu(true);
        return view;
    }

    public void editNote(int id) {
        currentId = id;
        NotesDbHelper dbHelper = NotesDbHelper.getInstance(getActivity());
        edtNoteEditor.setText(dbHelper.getNote(id));
        edtNoteEditor.requestFocus();
        setControlsActive(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        saveButton = menu.add(0, SAVE_BUTTON_ID, 1, getResources().getString(R.string.save));
        saveButton.setIcon(android.R.drawable.ic_menu_save);
        saveButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        deleteButton = menu.add(0, DELETE_BUTTON_ID, 0, getResources().getString(R.string.delete));
        deleteButton.setIcon(android.R.drawable.ic_menu_delete);
        deleteButton.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        setButtonsVisible(showButtons);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_BUTTONS_STATE, showButtons);
        outState.putInt(SAVED_CURRENT_ID, currentId);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            showButtons = savedInstanceState.getBoolean(SAVED_BUTTONS_STATE);
            currentId = savedInstanceState.getInt(SAVED_CURRENT_ID);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case SAVE_BUTTON_ID:
                saveText();
                return true;
            case DELETE_BUTTON_ID:
                deleteText();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setControlsActive(boolean active) {
        if (!active) {
            edtNoteEditor.setText("");
        }
        edtNoteEditor.setEnabled(active);
        showButtons = active;
        getActivity().invalidateOptionsMenu();
    }

    private void saveText() {
        /*Save to DB*/
        NotesDbHelper dbHelper = NotesDbHelper.getInstance(getActivity());
        dbHelper.updateNote(currentId, edtNoteEditor.getText().toString());

        editFinish();
    }

    private void deleteText() {
        /*Delete from DB*/
        NotesDbHelper dbHelper = NotesDbHelper.getInstance(getActivity());
        dbHelper.deleteNote(currentId);

        editFinish();
    }

    private void editFinish() {
        /*Disable buttons*/
        setControlsActive(false);

        /*Say activity all is done*/
        ((onEditRequestListener) getActivity()).editFinish();
    }

    private void setButtonsVisible(boolean visible) {
        edtNoteEditor.setEnabled(visible);
        if (visible) {
            deleteButton.setVisible(true);
            saveButton.setVisible(true);
        } else {
            deleteButton.setVisible(false);
            saveButton.setVisible(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setControlsActive(false);
    }
}
