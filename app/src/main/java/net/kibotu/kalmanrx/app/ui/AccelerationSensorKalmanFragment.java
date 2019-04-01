package net.kibotu.kalmanrx.app.ui;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import net.kibotu.kalmanrx.KalmanRx;
import net.kibotu.kalmanrx.app.misc.SensorEventObservableFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by jan.rabe on 11/10/16.
 */

public class AccelerationSensorKalmanFragment extends AccelerationSensorFragment {
    private FileWriter writer;
    private SensorEventObservableFactory factory;
    private  EventWripper firstEvent, lastEvent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File file = new File(getStorageDir(), "sensors.csv");
        if (file.exists())
            file.delete();

      
        try {
            writer = new FileWriter(file);

            //writer.write("TIME;ACC X;ACC Y;ACC Z;ACC XF;ACC YF;ACC ZF;GYR X; GYR Y; GYR Z; GYR XF; GYR YF; GYR ZF;\n");
            //    writer.write("TIME;ACC X;ACC Y;ACC Z;ACC XF;ACC YF;ACC ZF;GYR X; GYR Y; GYR Z; GYR XF; GYR YF; GYR ZF;  VX);
            writer.write("TIME; dT; ACC X;ACC Y;ACC Z;ACC XF;ACC YF;ACC ZF;GYR X; GYR Y; GYR Z; GYR XF; GYR YF; GYR ZF;  VX; VY; VZ; VxFiltr;  VyFiltr; VzFiltr; Sx; Sy; Sz; SxF; SyF; SzF;" +
                    "XGirQ; UGirQ; ZGirQ; WGirQ f\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private String getStorageDir() {
        return getContext().getExternalFilesDir(null).getAbsolutePath();
    }


    @Override
    protected Subscription createSensorSubscription() {
       factory=new SensorEventObservableFactory();

        // 1) float stream
        Observable<float[]> floatStream = factory
                .createSensorEventObservable(sensorType(), sensorDelay())
                .map(e -> e.values);

        // 2) apply kalman filter
        Observable<float[]> kalmanFilterStream = KalmanRx.createFrom3D(floatStream);

        // (optional) apply low pass filter
        Observable<float[]> lowPassFilter = KalmanRx.createLowPassFilter(kalmanFilterStream);

        return lowPassFilter.subscribe(this::process, Throwable::printStackTrace);
    }

    @Override
    protected void process(float x, float y, float z) {
        addToGraph(x, y, z);
        try {
            SensorEvent event=this.factory.lastevent;
            EventWripper eventWripper=new EventWripper(event, x,y,z,event.timestamp);
            if(firstEvent==null){
                this.firstEvent=eventWripper;
            }
            long durition=eventWripper.date-firstEvent.date;
            long duritionS=durition/1000;
            long dT= lastEvent==null?0:eventWripper.date-this.lastEvent.date;
            long dTS= dT/1000;
            float d=2*event.values[1];
            writer.write(String.format(" %d; %d; %d; %f; %f; %f; %f; %f; %f;\n",duritionS, dT, dTS, event.values[0], event.values[1], event.values[2], x,y,z, d));
            this.lastEvent=eventWripper;

      // xa=event.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}