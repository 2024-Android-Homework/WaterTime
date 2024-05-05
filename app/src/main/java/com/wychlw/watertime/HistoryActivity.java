package com.wychlw.watertime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class HistoryActivity extends Activity {
    private List<Record> recordList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        recordList = (List<Record>)intent.getSerializableExtra("recordList");
        Collections.reverse(recordList);
        initRecylerView();
    }

    private void initRecylerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.history_recylerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }
}