package com.wychlw.watertime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wychlw.watertime.DataHandeler.RecordHandeler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {
    private static final int ADD_DIARY_REQUEST = 1;
    private static final int EDIT_DIARY_REQUEST = 2;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private List<Record> recordList;
    private Map<LocalDate, String> diaryMap;
    private RecordHandeler recordHandeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initToolbar();
        Context context = getApplicationContext();
        recordHandeler = new RecordHandeler(context);
        recordList = recordHandeler.loadRecord();
        loadDiary();
        processRecords();
        initRecyclerView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.note_recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new SpaceItemDecoration(30));

        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                String dateString = data.getStringExtra(AddEditDiaryActivity.EXTRA_DATE);
                String content = data.getStringExtra(AddEditDiaryActivity.EXTRA_CONTENT);
                LocalDate date = LocalDate.parse(dateString);

                for (Note note : noteList) {
                    if (note.getDate().equals(date)) {
                        note.setHasDiary(true);
                        note.setContent(content);
                        noteAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }


    private void loadDiary() {
        String filename = "DiaryData";
        File file = new File(getFilesDir(), filename);

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            diaryMap = (Map<LocalDate, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            diaryMap = new HashMap<>();
        }
    }

    private void processRecords() {
        Map<LocalDate, Integer> dateToVolumeMap = new HashMap<>();

        for (Record record : recordList) {
            LocalDate date = record.getCurrent().toLocalDate();
            dateToVolumeMap.put(date, dateToVolumeMap.getOrDefault(date, 0) + record.getVolume());
        }

        noteList = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : dateToVolumeMap.entrySet()) {
            String content = diaryMap.getOrDefault(entry.getKey(), "");
            Note note = new Note(entry.getKey(), entry.getValue(), !content.isEmpty(), content);
            noteList.add(note);
        }
    }
}
