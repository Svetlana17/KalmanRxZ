package net.kibotu.kalmanrx.app.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.kibotu.kalmanrx.app.R;

public class LowPassFiltrActivity extends AppCompatActivity {
/// Здесь должна быть страница из основного проекта с ФНЧ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_pass_filtr);
    }
}
