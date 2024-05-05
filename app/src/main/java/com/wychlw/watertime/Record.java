package com.wychlw.watertime;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Record implements Serializable {
    final private String drinkName;
    final private int imageId;
    final private int volume;
    final private LocalDateTime current;

    public Record(String drinkName, int imageId, int volume, LocalDateTime current) {
        this.drinkName = drinkName;
        this.imageId = imageId;
        this.volume = volume;
        this.current = current;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public int getImageId() {
        return imageId;
    }

    public int getVolume() {
        return volume;
    }

    public LocalDateTime getCurrent() {
        return current;
    }
}
