package net.kibotu.kalmanrx.app;

import android.content.Intent;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.common.android.utils.logging.Logger;

import net.kibotu.android.deviceinfo.library.Device;
import net.kibotu.kalmanrx.app.Math.MathActivity;
import net.kibotu.kalmanrx.app.ui.AccelerationSensorFragment;
import net.kibotu.kalmanrx.app.ui.AccelerationSensorKalmanFragment;
import net.kibotu.kalmanrx.app.ui.AccelerationSensorLowPassFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.common.android.utils.extensions.FragmentExtensions.replace;
import static com.common.android.utils.extensions.FragmentExtensions.setFragmentContainerId;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {


    Button buttonStart, buttonStop, buttonSend;
    private static final String TAG = MainActivity.class.getSimpleName();
    private AccelerationSensorKalmanFragment kalmanFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         buttonStop = (Button) findViewById(R.id.stop);
         buttonStart = (Button) findViewById(R.id.start);
         buttonSend = (Button) findViewById(R.id.send);

        buttonStop.setOnClickListener((View.OnClickListener) this);
        buttonStart.setOnClickListener((View.OnClickListener) this);
        buttonSend.setOnClickListener((View.OnClickListener) this);



        List<Sensor> sensorList = Device.getSensorList();
        for (Sensor sensor : sensorList)
            Logger.v(TAG, "Sensor: " + sensor.getName() + " [" + sensor.getType() + "]");
        Sensor accelerometer = stream(sensorList).firstOrNull(sensor
                -> sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null)
            return;
       replace(new AccelerationSensorFragment());

        setFragmentContainerId(R.id.fragment_container3);
        kalmanFragment=new AccelerationSensorKalmanFragment();

        replace(kalmanFragment);
    }
    public void onClick(View v) {
       // Intent intent = new Intent(MainActivity.this, MathActivity.class);
        //startActivity(intent);
        switch (v.getId()){
            case R.id.stop:
                this.kalmanFragment.stop();
                break;
            case R.id.send:
                this.share();

                break;
            case R.id.start:
                this.kalmanFragment.start();
                break;
        }
    }
    private void share() {
        File dir = getExternalFilesDir(null);
        File zipFile = new File(dir, "accel.zip");
        if (zipFile.exists()) {
            zipFile.delete();
        }
        File[] fileList = dir.listFiles();
        try {
            zipFile.createNewFile();
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File file : fileList) {
                zipFile(out, file);
            }
            out.close();
            sendBundleInfo(zipFile);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Can't send file!", Toast.LENGTH_LONG).show();
        }
    }

    private static void zipFile(ZipOutputStream zos, File file) throws IOException {
        zos.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[10000];
        int byteCount = 0;
        try {
            while ((byteCount = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, byteCount);
            }
        } finally {
            safeClose(fis);
        }
        zos.closeEntry();
    }

    private static void safeClose(FileInputStream fis) {
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendBundleInfo(File file) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
        startActivity(Intent.createChooser(emailIntent, "Send data"));
    }
    }


