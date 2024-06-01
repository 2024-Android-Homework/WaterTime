package com.wychlw.watertime;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wychlw.watertime.DataHandeler.SyncHandeler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SettingActivity extends AppCompatActivity {
    private EditText objectEdit;
    private EditText uuidEdit;
    private Button objectButton;
    private Button uuidPullButton;
    private Button uuidPushButton;
    private UUID uuid;
    private int targetWater;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar();
        initObjectButton();
        initUuidButton();
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
    private void initObjectButton(){
        objectButton = (Button)findViewById(R.id.object_button);
        objectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String targetWaterString = objectEdit.getText().toString();

                if (targetWaterString.isEmpty() || !targetWaterString.matches("\\d+")) {
                    Toast.makeText(SettingActivity.this, "请输入正确的目标喝水量", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(SettingActivity.this, "已修改每日喝水目标", Toast.LENGTH_SHORT).show();
                targetWater = Integer.parseInt(targetWaterString);
            }
        });
    }
    private void initUuidButton(){
        uuidEdit = findViewById(R.id.uuid_edit);
        uuidPullButton = findViewById(R.id.uuid_pull);
        uuidPushButton = findViewById(R.id.uuid_push);
        Context context = getApplicationContext();
        SyncHandeler sh = new SyncHandeler(context);
        // 设置默认显示远程同步的UUID
        UUID remoteUUID = sh.getUUID();
        uuidEdit.setText(remoteUUID.toString());
        // 设置默认显示远程同步的UUID
        uuidPushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditText中的UUID
                final String newUUIDString = uuidEdit.getText().toString();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            UUID newUUID = UUID.fromString(newUUIDString);
                            // 更新本地UUID并同步到远程
                            sh.putUUID(context, newUUID);
                            sh.syncToRemote(context);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        // 更新成功后提示用户
                        Toast.makeText(context, "UUID已更新并同步到远程", Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }
        });
        // 点击pullButton复制UUID
        uuidPullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取EditText中的UUID
                String remoteUUIDString = uuidEdit.getText().toString();

                // 复制UUID到剪贴板
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("UUID", remoteUUIDString);
                clipboard.setPrimaryClip(clip);

                // 提示用户已复制UUID
                Toast.makeText(context, "UUID已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
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
}
