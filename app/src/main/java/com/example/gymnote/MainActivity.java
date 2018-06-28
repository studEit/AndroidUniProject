package com.example.gymnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListViewNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewNotes = (ListView) findViewById(R.id.main_list_view_notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_main_new_note:
                //start NoteActivity in NewNote mode
                Intent newNoteActivity = new Intent(this, NoteActivity.class);
                startActivity(new Intent(this, NoteActivity.class));
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        //zaladuj cwiczenia z listview
        //wyzeruj listview
        mListViewNotes.setAdapter(null);
        ArrayList<Note> notes = Utilities.getAllSavedNotes(this);

        //sprawdz, czy sa jakiekolwiek zapisane cwiczenia
        //nie ma
        if(notes == null || notes.size() ==0) {
            Toast.makeText(this, "Nie podales zadnych cwiczen", Toast.LENGTH_SHORT).show();
        }
        //sa
        else {
            NoteAdapter na = new NoteAdapter(this, R.layout.item_note, notes);
            mListViewNotes.setAdapter(na);

            mListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String fileName = ((Note)mListViewNotes.getItemAtPosition(position)).getmDateTime()
                            +Utilities.FILE_EXTENSION;

                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });
        }

    }
}
