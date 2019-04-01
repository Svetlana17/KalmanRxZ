package net.kibotu.kalmanrx.app.ui;

import android.hardware.SensorEvent;

public class EventWripper {
    SensorEvent sensorEvent;
    float x,y,z;
    long date;
    public  EventWripper(SensorEvent event, float x, float y, float z,long date){
        this.sensorEvent=event;
        this.x=x;
        this.y=y;
        this.z=z;
        this.date=date;

    }
}
