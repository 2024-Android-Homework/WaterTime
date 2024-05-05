package com.wychlw.watertime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.lzyzsd.circleprogress.CircleProgress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    final private List<Drink> drinkList = new ArrayList<Drink>();
    private List<Record> recordList;
    private final int OBJECT = 2600;
    private TextView textViewTopLeft;
    private TextView textViewTopRight;
    private TextView textViewCenter;
    private CircleProgress circleProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTopLeft = (TextView) findViewById(R.id.textViewTopLeft);
        textViewTopRight = (TextView) findViewById(R.id.textViewTopRight);
        textViewCenter = (TextView) findViewById(R.id.textViewCenter);
        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        initRecylerView();
        initTextViewTopLeft();
        initTextViewTopRight();
        File file = new File("data");
        if (file.exists()) {
            recordList = (ArrayList<Record>) load();
            if(recordList == null)
                recordList = new ArrayList<Record>();
        }
        restartProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save((Serializable) recordList);
    }
    public void save(Serializable data) {
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        try {
            fileOut = openFileOutput("data", Context.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOut != null) {
                    objectOut.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Serializable load() {
        FileInputStream fileIn = null;
        ObjectInputStream objectIn = null;
        Serializable data = null;
        try {
            fileIn = openFileInput("data");
            objectIn = new ObjectInputStream(fileIn);
            data = (Serializable) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectIn != null) {
                    objectIn.close();
                }
                if (fileIn != null) {
                    fileIn.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
    private void initTextViewTopRight(){
        textViewTopRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTopRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                        intent.putExtra("recordList", (Serializable) recordList);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    private void initTextViewTopLeft(){
        LocalTime currentTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
            int hour = currentTime.getHour();
            if (hour >= 6 && hour < 12) {
                textViewTopLeft.setText("上午好(^U^)");
            } else if (hour >= 12 && hour < 14) {
                textViewTopLeft.setText("中午好(*^_^*)");
            } else if (hour >= 14 && hour < 18) {
                textViewTopLeft.setText("下午好(≧▽≦)");
            } else {
                textViewTopLeft.setText("晚上好(～﹃～)");
            }
        }
    }

    private void initRecylerView(){
        initDrinks();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DrinkAdapter adapter = new DrinkAdapter(drinkList);
        recyclerView.setAdapter(adapter);
        adapter.setOnButtonClickListener(new DrinkAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(Record record) {
                recordList.add(record);
                restartProgress();
            }
        });
    }
    private void restartProgress(){
        int total = 0;
        for (int i = recordList.size() - 1; i >= 0; i--){
            Record tmp = recordList.get(i);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate tmpDate = tmp.getCurrent().toLocalDate();
                LocalDate currentDate = LocalDate.now();
                if (!tmpDate.equals(currentDate))
                    break;
            }
            total += tmp.getVolume();
        }
        textViewCenter.setText("今日已摄入: "+ total +" ml");
        circleProgress.setProgress(100 * total / OBJECT);
        if (total >= OBJECT){
            circleProgress.setProgress(100);
        }
    }
    private void initDrinks() {
        Drink water = new Drink("Water", R.drawable.water_pic);
        drinkList.add(water);
        Drink coffee = new Drink("Coffee", R.drawable.coffee_pic);
        drinkList.add(coffee);
        Drink milk = new Drink("Milk", R.drawable.milk_pic);
        drinkList.add(milk);
        Drink tea = new Drink("Tea", R.drawable.tea_pic);
        drinkList.add(tea);
        Drink yogurt = new Drink("Yogurt", R.drawable.yogurt_pic);
        drinkList.add(yogurt);
        Drink juice = new Drink("Juice", R.drawable.juice_pic);
        drinkList.add(juice);
        Drink carbonatedDrink = new Drink("Carbonated Drink", R.drawable.carbonated_drink_pic);
        drinkList.add(carbonatedDrink);
        Drink milkTea = new Drink("Milk Tea", R.drawable.milk_tea_pic);
        drinkList.add(milkTea);
    }
}