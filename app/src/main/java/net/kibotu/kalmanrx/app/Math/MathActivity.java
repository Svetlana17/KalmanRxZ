package net.kibotu.kalmanrx.app.Math;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.kibotu.kalmanrx.app.MainActivity;
import net.kibotu.kalmanrx.app.R;

public class MathActivity extends AppCompatActivity {
    public String state = "DEFAULT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.line_gyroscope:
//                state = "gyroscope";
//                Intent intent = new Intent(MathActivity.this,GyroscopeActivity.class);
//                startActivity(intent);
//                return true;
//
//            case R.id.line_accelerometr:
//                state = "accelerometr";
//                return true;

            case R.id.line_accelerometr_geroscope:
                Intent i = new Intent(MathActivity.this, MainActivity.class);
                startActivity(i);
                return true;
//            case R.id.record:
//                state="record";
//                Intent is = new Intent(MainActivity.this,RecordActivity.class);
//                startActivity(is);
//                return true;
//            case R.id.main:
//                Intent main=new Intent(MainActivity.this, ActivityView.class);
//                startActivity(main);


            default:
                return true;
        }
    }
}
