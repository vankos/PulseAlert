package android.example.pulsealert;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends WearableActivity implements SensorEventListener
{

    private TextView mTextView;
    SensorManager sensorManager;
    Sensor heartRateSensor;
    boolean isOn;
    private View view;
    Vibrator vibrator;
    int limitHeartRate;
    float defaultTextSize;
    Button plusButton;
    Button minusButton;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
            heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        editor = sharedPref.edit();
        limitHeartRate = sharedPref.getInt("LimitValue", 170);
        mTextView.setText(limitHeartRate+"");

        defaultTextSize=mTextView.getTextSize();


        // Enables Always-on
        setAmbientEnabled();
    }

    public void ButtonClick(View view)
    {


        if(isOn)
        {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,defaultTextSize);
            mTextView.setText(String.format(limitHeartRate+"" ));
            plusButton.setVisibility(View.VISIBLE);
            minusButton.setVisibility(View.VISIBLE);
            sensorManager.unregisterListener(this);
            isOn=false;
        }
        else
        {
            mTextView.setTextSize(45);
            mTextView.setText("...");
            plusButton.setVisibility(View.INVISIBLE);
            minusButton.setVisibility(View.INVISIBLE);
            sensorManager.registerListener( this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            isOn=true;
        }
    }

    private void DisplayAndSave(int limit)
    {
        mTextView.setText(limit+"");
        editor.putInt("LimitValue",limit);
        editor.apply();
    }

    public void onSensorChanged(SensorEvent event)
    {
        float rate = event.values[0];
        mTextView.setText(String.format("%.0f",rate ));
        if(rate>limitHeartRate)
            vibrator.vibrate(VibrationEffect.createOneShot(100,10));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void MinusClick(View view)
    {
        limitHeartRate-=5;
        DisplayAndSave(limitHeartRate);
    }

    public void PlusClick(View view) {
        limitHeartRate+=5;
        DisplayAndSave(limitHeartRate);
    }
}
