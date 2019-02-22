package com.example.lenovo.capteurs;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Accelerometer extends AppCompatActivity {
    private TextView direction;
    private SensorManager sensorManager;
    private SensorEventListener listener;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        direction = findViewById(R.id.txtDirection);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null){
            Toast.makeText(this, "Pas d'accelerometre", Toast.LENGTH_SHORT).show();
        }
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];
                float x1 = x * x;
                float y1 = y * y;
                float z1 = z * z;
                float acceleration =(x1+y1+z1);
                acceleration = (float)Math.sqrt(acceleration);

                if(acceleration < 10){
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                if(acceleration>10 && acceleration<12){
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                }
                if(acceleration > 12){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }

                if(x>3){
                    direction.setText("gauche");
                }
                else if(x<-3){
                    direction.setText("droite");
                }
                else if(y<-3){
                    direction.setText("haut");
                }
                else if(y>3){
                    direction.setText("bas");
                }
                else{
                    direction.setText("");
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }
}
