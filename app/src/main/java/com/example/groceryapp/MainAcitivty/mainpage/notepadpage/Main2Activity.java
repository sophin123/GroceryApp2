package com.example.groceryapp.MainAcitivty.mainpage.notepadpage;

import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.adapters.NotesAdapter;
import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.callbacks.MainActionModeCallback;
import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.callbacks.NoteEventListener;
import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.db.NotesDB;
import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.db.NotesDao;
import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.model.Note;
import com.example.groceryapp.MainAcitivty.mainpage.notepadpage.utils.NoteUtils;
import com.example.groceryapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.groceryapp.MainAcitivty.mainpage.notepadpage.EditNoteActivity.NOTE_EXTRA_Key;

public class Main2Activity extends AppCompatActivity implements NoteEventListener, Drawer.OnDrawerItemClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NotesAdapter adapter;
    private NotesDao dao;
    private MainActionModeCallback actionModeCallback;
    private int chackedCount = 0;
    private FloatingActionButton fab;
    private SharedPreferences settings;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES = "notepad_settings";
    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme = settings.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigation(savedInstanceState, toolbar);
        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNewNote();
            }
        });

        dao = NotesDB.getInstance(this).notesDao();
    }
    
    private void setupNavigation(Bundle savedInstanceState, Toolbar toolbar) {

        List<IDrawerItem> iDrawerItems = new ArrayList<>();
        iDrawerItems.add(new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.ic_home_black_24dp));
        iDrawerItems.add(new PrimaryDrawerItem().withName("Notes").withIcon(R.drawable.ic_note_black_24dp));

        List<IDrawerItem> stockyItems = new ArrayList<>();

        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem()
                .withName("Dark Theme")
                .withChecked(theme == R.style.AppTheme_Dark)
                .withIcon(R.drawable.ic_dark_theme)
                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme_Dark).apply();
                        } else {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme).apply();
                        }

                        TaskStackBuilder.create(Main2Activity.this)
                                .addNextIntent(new Intent(Main2Activity.this, Main2Activity.class))
                                .addNextIntent(getIntent()).startActivities();
                    }
                });

        stockyItems.add(new PrimaryDrawerItem().withName("Setting").withIcon(R.drawable.ic_settings_black_24dp));
        stockyItems.add(switchDrawerItem);


        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                .withEmail("sophinstha123@gmail.com")
                .withName("sophin123")
                .withIcon(R.mipmap.ic_launcher_round))
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.ic_launcher_background)
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSavedInstance(savedInstanceState)
                .withDrawerItems(iDrawerItems)
                .withTranslucentNavigationBar(true)
                .withStickyDrawerItems(stockyItems)
                .withAccountHeader(header)
                .withOnDrawerItemClickListener(this)
                .build();

    }

    private void loadNotes(){
        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotes();
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();

        swipeToDeleteHelper.attachToRecyclerView(recyclerView);
    }



    private void showEmptyView() {
        if(notes.size() == 0){
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        }else{
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }

    }

    private void onAddNewNote(){
        startActivity(new Intent(this, EditNoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(Note note) {
        note.setChecked(true);
        chackedCount = 1;
        adapter.setMultiCheckMode(true);

        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked());
                if(note.isChecked())
                    chackedCount++;
                else chackedCount--;

                if(chackedCount > 1) {
                    actionModeCallback.changeShareItemVisible(false);
                }else actionModeCallback.changeShareItemVisible(true);

                if (chackedCount == 0){
                    actionModeCallback.getAction().finish();
                }

                actionModeCallback.setCount(chackedCount + " /" + notes.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Note note) {


            }
        });

        actionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionmode, MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_delete_notes)
                    onDeleteMultiNotes();
                else if (menuItem.getItemId() == R.id.action_share_note)
                    onShareNote();

                actionmode.finish();
                return false;
            }
        };

    }

    private void onShareNote() {
        Note note = adapter.getCheckedNotes().get(0);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getNoteText() + "\n\n Create on : " +
                NoteUtils.dateFromLong(note.getNoteDate()) + "\n By :" + "Simple Notepad";
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);
    }

    private void onDeleteMultiNotes() {
        List<Note> chackedNotes = adapter.getCheckedNotes();
        if(chackedNotes.size() !=0){
            for (Note note : chackedNotes) {
                dao.deleteNote(note);
            }
            loadNotes();
            Toast.makeText(this,chackedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this,"No Note(s) selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {

        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false);
        adapter.setListener(this);
        fab.show();
    }

    private ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    if(notes != null) {
                        Note swipedNote = notes.get(viewHolder.getAdapterPosition());
                        if (swipedNote != null){
                            swipeToDelete(swipedNote, viewHolder);
                        }
                    }
                }
            });

    private void swipeToDelete(final Note swipeNote, final RecyclerView.ViewHolder viewHolder){
        new AlertDialog.Builder(Main2Activity.this)
                .setMessage("Delete Note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dao.deleteNote(swipeNote);
                        notes.remove(swipeNote);
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        showEmptyView();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());

            }
        }).setCancelable(false)
                .create().show();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        return false;
    }

}

    
