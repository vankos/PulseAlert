package android.example.pulsealert;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        // Enables Always-on
        setAmbientEnabled();
    }

    public void ButtonClick(View view)
    {


        if(isOn)
        {
            mTextView.setText("Отключено");
            sensorManager.unregisterListener((SensorEventListener)this);
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
        mTextView.setText(String.format("%.0f",event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
