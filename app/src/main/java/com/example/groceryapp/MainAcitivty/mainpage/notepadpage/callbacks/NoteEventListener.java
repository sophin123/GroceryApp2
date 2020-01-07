package com.example.groceryapp.MainAcitivty.mainpage.notepadpage.callbacks;

import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.model.Note;

public interface NoteEventListener {
    /**
     * call wen note clicked.
     *
     * @param note: note item
     */
    void onNoteClick(Note note);

    /**
     * call wen long Click to note.
     *
     * @param note : item
     */
    void onNoteLongClick(Note note);

}
