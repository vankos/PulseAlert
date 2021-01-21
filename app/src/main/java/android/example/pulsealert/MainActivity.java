package android.example.pulsealert;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity implements SensorEventListener
{

    private TextView mTextView;
    SensorManager sensorManager;
    Sensor heartRateSensor;
    boolean isOn;
    private View view;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
            heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void ButtonClick(View view)
    {


        if(isOn)
        {
            mTextView.setText("Отключено");
            sensorManager.unregisterListener(this);
            isOn=false;
        }
        else
        {
            mTextView.setText("Идет измерение...");
            sensorManager.registerListener( this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            isOn=true;
        }
    }

    public void onSensorChanged(SensorEvent event)
    {
        float rate = event.values[0];
        mTextView.setText(String.format("%.0f",rate ));
        if(rate>170)
            vibrator.vibrate(VibrationEffect.createOneShot(100,10));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
