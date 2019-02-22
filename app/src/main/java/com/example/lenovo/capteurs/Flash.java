package com.example.lenovo.capteurs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Flash extends AppCompatActivity {

    private SensorManager sensorManager;
    private SensorEventListener listener;
    private Sensor linearSensor;
    private TextView txtOnOff;
    private CameraManager cameraManager;
    private String flashID;
    private boolean torcheOnOff = false;
    private long torcheTemp;
    private long ViewTorcheTemp;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        txtOnOff = findViewById(R.id.textTorch);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        linearSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            flashID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (linearSensor == null){
            Toast.makeText(this, "Pas d'accelerometre", Toast.LENGTH_SHORT).show();
        }
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                torcheTemp = System.currentTimeMillis();
                float[] values = sensorEvent.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];
                float x1 = x * x;
                float y2 = y * y;
                float z3 = z * z;
                double acceleration =(x1+y2+z3);
                acceleration = Math.sqrt(acceleration);
                if(acceleration > 2){
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    SwitchLight();
                }

            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
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
        sensorManager.registerListener(listener,linearSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SwitchLight(){
        torcheTemp = System.currentTimeMillis();
        if((torcheTemp-ViewTorcheTemp) > 1000) {
            if (torcheOnOff) {
                try {
                    cameraManager.setTorchMode(flashID, false);
                    torcheOnOff = false;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    cameraManager.setTorchMode(flashID, true);
                    torcheOnOff = true;
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            ViewTorcheTemp = System.currentTimeMillis();
        }

    }
}
