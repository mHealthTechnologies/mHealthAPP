package com.mHealthDroid.mhealthapp;

import com.mHealthDroid.mhealthpp.R;

import communicationManager.dataStructure.ObjectData.SensorType;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SensorView extends LinearLayout{

	
	public CheckBox checkbox;
	public TextView txt;
	
	public SensorView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.sensor_checkbox, this);
		checkbox = (CheckBox) findViewById(R.id.checkBox_sensor);
		txt = (TextView) findViewById(R.id.text_sensor);
	}
	
	public void setSensor(SensorDevice sensor){
		checkbox.setText(sensor.sensorType.toString());
		checkbox.setSelected(sensor.selected);
	}

	
	public String nameSensor(SensorType sensor){
		
		String name = "";
		
		switch(sensor){
		case ACCELEROMETER:
			name ="Acc";
			break;
		case ACCELEROMETER_X:
			name ="Acc X";
			break;
		case ACCELEROMETER_Y:
			name ="Acc Y";
			break;
		case ACCELEROMETER_Z:
			name ="Acc Y";
			break;
		case AMBIENT_TEMPERATURE:
			name ="Temperature";
			break;
		case ECG:
			name ="Ecg";
			break;
		case ECG_LALL:
			name ="Ecg LALL";
		break;
		case ECG_RALL:
			name ="Ecg RALL";
			break;
		case EMG:
			name ="Emg";
			break;
		case EXP_BOARDA0:
			name ="Exp Board A0";
			break;
		case EXP_BOARDA7:
			name ="Exp Board A7";
			break;
		case GRAVITY:
			name ="Gravity";
			break;
		case GRAVITY_X:
			name ="Gravity X";
			break;
		case GRAVITY_Y:
			name ="Gravity Y";
			break;
		case GRAVITY_Z:
			name ="Gravity Z";
			break;
		case GSR:
			name ="Gsr";
			break;
		case GYROSCOPE:
			name ="Gyroscope";
			break;
		case GYROSCOPE_X:
			name ="Gyr X";
			break;
		case GYROSCOPE_Y:
			name ="Gyr Y";
			break;
		case GYROSCOPE_Z:
			name ="Gyr Z";
			break;
		case HEART_RATE:
			name ="Heart Rate";
			break;
		case HUMIDITY:
			name ="Humidity";
			break;
		case LIGHT:
			name ="Light";
			break;
		case LINEAR_ACCELERATION:
			name ="Acceleration";
			break;
		case LINEAR_ACCELERATION_X:
			name ="Acceleration X";
			break;
		case LINEAR_ACCELERATION_Y:
			name ="Acceleration Y";
			break;
		case LINEAR_ACCELERATION_Z:
			name ="Acceleration Z";
			break;
		case MAGNETOMETER:
			name ="Magn";
			break;
		case MAGNETOMETER_X:
			name ="Mag X";
		break;
		case MAGNETOMETER_Y:
			name ="Mag Y";
			break;
		case MAGNETOMETER_Z:
			name ="Mag Z";
			break;
		case ORIENTATION:
			name ="Orientation";
			break;
		case ORIENTATION_X:
			name ="Ori X";
			break;
		case ORIENTATION_Y:
			name ="Ori Y";
			break;
		case ORIENTATION_Z:
			name ="Ori Z";
			break;
		case PRESSURE:
			name ="Pressure";
			break;
		case PROXIMITY:
			name ="Proximity";
			break;
		case ROTATION_VECTOR:
			name ="Rotation V.";
			break;
		case ROTATION_VECTOR_X:
			name ="R.V X";
			break;
		case ROTATION_VECTOR_Y:
			name ="R.V Y";
			break;
		case ROTATION_VECTOR_Z:
			name ="R.V Z";
			break;
		case STRAIN:
			name ="Strain G.";
			break;
		case STRAIN_GAUGE_HIGH:
			name ="SG High";
		break;
		case STRAIN_GAUGE_LOW:
			name ="SG Low";
			break;
			
		}
		
		return name;
	}
}
