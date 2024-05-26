package com.wychlw.watertime;

import java.io.Serializable;
import java.time.LocalDate;

public class Note implements Serializable {
    private LocalDate date;
    private int waterAmount;
    private boolean hasDiary;
    private String content;

    public Note(LocalDate date, int waterAmount, boolean hasDiary, String content) {
        this.date = date;
        this.waterAmount = waterAmount;
        this.hasDiary = hasDiary;
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public boolean hasDiary() {
        return hasDiary;
    }

    public String getContent() {
        return content;
    }

    public void setHasDiary(boolean hasDiary) {
        this.hasDiary = hasDiary;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
