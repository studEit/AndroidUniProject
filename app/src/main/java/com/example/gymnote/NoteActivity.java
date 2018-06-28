package com.example.gymnote;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private EditText mEditTitle;
    private EditText mEtContent;
    private String mNoteFilename;
    private Note mLoadedNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEditTitle = (EditText) findViewById(R.id.note_et_title);
        mEtContent = (EditText) findViewById(R.id.note_et_content);

        mNoteFilename = getIntent().getStringExtra("NOTE_FILE");
        if(mNoteFilename != null && !mNoteFilename.isEmpty()){
            mLoadedNote = Utilities.getNoteByName(getApplicationContext(),mNoteFilename);

            if(mLoadedNote != null){
                mEditTitle.setText(mLoadedNote.getmTitle());
                mEtContent.setText(mLoadedNote.getmContent());
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_new, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_note_save:
                saveNote();
            case R.id.action_note_delete:
                deleteNote();

                break;
        }
        return true;
    }


    private void saveNote(){

        Note note;

        if(mLoadedNote ==null){
            note = new Note(System.currentTimeMillis(), mEditTitle.getText().toString(),
                    mEtContent.getText().toString());
        } else {
            note = new Note(mLoadedNote.getmDateTime(), mEditTitle.getText().toString(),
                    mEtContent.getText().toString());
        }



                if (Utilities.saveNote(this,note)){
                    Toast.makeText(this, "Twoje cwiczenie zostalo zapisane", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Brak miejsca w pamieci",
                            Toast.LENGTH_SHORT).show();
                }
                finish();

    }

    private void deleteNote() {

        //Sprawdz, czy zapisane cwiczenie w ogole istnieje
        if(mLoadedNote == null){
            finish();
        } else {

            // upewnij sie, ze uzytkownik na pewno chce skasowac wpis
            AlertDialog.Builder dialog = new AlertDialog.Builder((this))
                    .setTitle("Usun cwiczenie")
                    .setMessage("Zaraz usuniesz " + mEditTitle.getText().toString() + " - jestes pewien?")
                    .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Utilities.deleteNote(getApplicationContext(),
                                    mLoadedNote.getmDateTime() + Utilities.FILE_EXTENSION);
                            Toast.makeText(getApplicationContext(), mEditTitle.getText().toString()
                                    + " - usunieto", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })

                    .setNegativeButton("Nie", null) //nic sie nie dzieje
                    .setCancelable(false);
            dialog.show();
        }
    }
}
