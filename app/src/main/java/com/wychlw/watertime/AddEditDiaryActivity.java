package com.wychlw.watertime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddEditDiaryActivity extends AppCompatActivity {

    public static final String EXTRA_DATE = "com.wychlw.watertime.EXTRA_DATE";
    public static final String EXTRA_CONTENT = "com.wychlw.watertime.EXTRA_CONTENT";

    private LocalDate date;
    private EditText editTextContent;
    private TextView textViewDate;
    private File diaryFile;
    private Map<LocalDate, String> diaryMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_diary);

        initToolbar();
        initViews();
        setupListeners();

        openFile();
        loadIntentData();
        loadDiary();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        textViewDate = findViewById(R.id.textViewDate);
        editTextContent = findViewById(R.id.editTextContent);editTextContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editTextContent, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void setupListeners() {
        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                // Optionally handle text changes
            }
        });
    }

    private void openFile() {
        String filename = "DiaryData";
        File dir = getFilesDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        diaryFile = file;
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        String dateString = intent.getStringExtra(EXTRA_DATE);
        Log.d("AddEditDiaryActivity", "EXTRA_DATE: " + dateString); // 打印EXTRA_DATE的值
        date = LocalDate.parse(dateString);
        textViewDate.setText(date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE")).trim());
    }

    private void loadDiary() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(diaryFile);
            ois = new ObjectInputStream(fis);
            diaryMap = (Map<LocalDate, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            diaryMap = new HashMap<>();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String content = diaryMap.getOrDefault(date, "");
        editTextContent.setText(content.trim());
    }

    private void saveDiary() {
        String content = editTextContent.getText().toString().trim();
        diaryMap.put(date, content);

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(diaryFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(diaryMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_DATE, date.toString());
        data.putExtra(EXTRA_CONTENT, content);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            saveDiary();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
