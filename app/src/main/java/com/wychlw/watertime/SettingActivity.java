package com.wychlw.watertime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    private EditText objectEdit;
    private int targetWater;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar();
        Intent intent = getIntent();
        targetWater = intent.getIntExtra("OBJECT", 0);
        objectEdit = findViewById(R.id.object_edit);
        objectEdit.setText(String.valueOf(targetWater));

    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("TARGET_WATER", targetWater);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onAffirmButtonClick(View view) {
        String targetWaterString = objectEdit.getText().toString();

        if (targetWaterString.isEmpty() || !targetWaterString.matches("\\d+")) {
            Toast.makeText(this, "请输入正确的目标喝水量", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "已修改每日喝水目标", Toast.LENGTH_SHORT).show();
        targetWater = Integer.parseInt(targetWaterString);
    }
}
