package com.simplex.utbathroomservices.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.simplex.utbathroomservices.cloudfirestore.Rating;

import java.util.UUID;

/**
 * Created by dchun on 11/30/17.
 */

@Table(database = AppDatabase.class)
public class Rating_Item extends BaseModel{
    @PrimaryKey
    private String location;

    @Column
    private UUID uuid;

    @Column
    private String type;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
