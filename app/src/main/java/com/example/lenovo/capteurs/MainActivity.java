package com.example.lenovo.capteurs;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView sensorLV;
    private List<Sensor> sensorList;
    private List<String> sensorListName;
    private Button launchActivity;
    private Button launchFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorLV = findViewById(R.id.sensorList);
        launchActivity = findViewById(R.id.btnSensor);
        launchFlash = findViewById(R.id.btnFlash);

        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorListName = new ArrayList<>();
        for (Sensor sensor : sensorList) {
            sensorListName.add(sensor.getName());
        }
        Sensor temp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(temp == null)
        {
            Toast.makeText(this, "Pas de capteur de température", Toast.LENGTH_SHORT).show();
        }
        sensorLV.setAdapter(new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, sensorListName));

        launchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Accelerometer.class));
            }
        });

        launchFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Flash.class));
            }
        });
    }
}
