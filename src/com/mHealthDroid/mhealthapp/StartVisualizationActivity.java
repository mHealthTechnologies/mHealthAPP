package com.mHealthDroid.mhealthapp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.mHealthDroid.mhealthpp.R;
import com.jjoe64.graphview.GraphView.LegendAlign;

import visualizationManager.VisualizationManager;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData.SensorType;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StartVisualizationActivity extends Activity{

	public CommunicationManager cm;
	public VisualizationManager vm;
	public ArrayList<String> devicesStreaming;
	public MyCustomAdapter dataAdapter = null;
	public ArrayList<SensorDevice> signalsList = new ArrayList<SensorDevice>();
	private ArrayAdapter<String> mDevicesStreamingArrayAdapter;
	public ArrayList<String> sensorsSelected;
	public ListView devicesListView;
	public ListView signalsListView;
	public String deviceSelected;
	public Button startButton;
	public LinearLayout layoutStartButton;
	public LinearLayout layoutSignalsList;
	
	public double viewPort;
	public double maxY;
	public double minY;
	public boolean setViewPort;
	public boolean setYValues;
	public boolean showLegend;
	public boolean isOnline;
	public boolean isCustomized;
	public int align;
	
	public StartVisualizationActivity() {
		super();
		// TODO Auto-generated constructor stub
		cm = CommunicationManager.getInstance();
		vm = VisualizationManager.getInstance();
		devicesStreaming = new ArrayList<String>();
		sensorsSelected = new ArrayList<String>();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.devices_visualization);

		// Set result CANCELED in case the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		//load graph configuration
		Bundle extras = getIntent().getExtras();
//		isCustomized = extras.getBoolean(VisualizationFragment.IS_CUSTOMIZED);
//		if(isCustomized){
			setViewPort = extras.getBoolean(VisualizationFragment.SET_VIEW_PORT);
			setYValues = extras.getBoolean(VisualizationFragment.SET_COORDINATES);
			showLegend = extras.getBoolean(VisualizationFragment.SET_LEGEND);
//			if(setViewPort)
				viewPort = extras.getDouble(VisualizationFragment.VIEW_PORT);
//			if(setYValues){
				maxY = extras.getDouble(VisualizationFragment.Y_MAX);
				minY = extras.getDouble(VisualizationFragment.Y_MIN);
//			}
//			if(showLegend)
				align = extras.getInt(VisualizationFragment.ALIGN);
//		}
		
		mDevicesStreamingArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
		Hashtable<String, Integer> states = cm.getCurrentDevicesState();
		Set<String> keys = states.keySet();
		for(String k: keys)
			if(states.get(k)==2)
				mDevicesStreamingArrayAdapter.add(k);
		
		startButton = (Button) findViewById(R.id.okButtonVisulization);
		layoutStartButton = (LinearLayout) findViewById(R.id.layoutButtonVisulization);
		layoutSignalsList = (LinearLayout) findViewById(R.id.layout_list_sensors);
		devicesListView = (ListView) findViewById(R.id.list_devices);
		devicesListView.setAdapter(mDevicesStreamingArrayAdapter);
		devicesListView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
				
				deviceSelected = ((TextView) v).getText().toString();
				VisualizationFragment.deviceSelected = deviceSelected;
				ArrayList<SensorType> sensors = cm.getEnabledSensors(deviceSelected);
				fromSensorsToSignals(sensors);
				
				dataAdapter = new MyCustomAdapter(getApplicationContext(), R.layout.sensor_checkbox, signalsList);
				signalsListView = (ListView) findViewById(R.id.list_sensors);
				layoutSignalsList.setVisibility(View.VISIBLE);
				startButton.setVisibility(View.VISIBLE);
				layoutStartButton.setVisibility(View.VISIBLE);
				// Assign adapter to ListView
				signalsListView.setAdapter(dataAdapter);
			}
		});
		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<SensorType> sensorsToVisualize = new ArrayList<SensorType>();
				for(int i=0; i<signalsList.size(); i++)
					if(signalsList.get(i).selected)
						sensorsToVisualize.add(signalsList.get(i).sensorType);
				
				for(int i=0; i<sensorsToVisualize.size(); i++)
					sensorsSelected.add(sensorsToVisualize.get(i).toString());
				


				vm.setViewPort(VisualizationFragment.nameGraph, 0, viewPort);
				vm.setManualYAxis(VisualizationFragment.nameGraph, setYValues);
				if(setYValues)
					vm.setManualYAxisBounds(VisualizationFragment.nameGraph, maxY, minY);
				
				vm.setShowLegend(VisualizationFragment.nameGraph, showLegend);
				if(showLegend){	
					switch(align){
						case 1:
							vm.setLegendAlign(VisualizationFragment.nameGraph, LegendAlign.TOP);
							break;
						case 2:
							vm.setLegendAlign(VisualizationFragment.nameGraph, LegendAlign.MIDDLE);
							break;
						case 3:
							vm.setLegendAlign(VisualizationFragment.nameGraph, LegendAlign.BOTTOM);
							break;
					}
				}			
				
				vm.visualizationOnline(VisualizationFragment.nameGraph, deviceSelected, sensorsToVisualize, (int) (2*viewPort));
//				Intent intent = getIntent();
//				intent.putExtra(VisualizationFragment.IS_ONLINE, true);
//				intent.putStringArrayListExtra(VisualizationFragment.SENSORS_SELECTED, sensorsSelected);
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	public void fromSensorsToSignals(ArrayList<SensorType> sensors){
		
		signalsList.clear();
		for(int i=0; i<sensors.size(); i++){
			switch(sensors.get(i)){
				case ACCELEROMETER:
					signalsList.add(new SensorDevice(SensorType.ACCELEROMETER_X, false));
					signalsList.add(new SensorDevice(SensorType.ACCELEROMETER_Y, false));
					signalsList.add(new SensorDevice(SensorType.ACCELEROMETER_Z, false));
				break;
				case AMBIENT_TEMPERATURE:
					signalsList.add(new SensorDevice(SensorType.AMBIENT_TEMPERATURE, false));
				break;
				case ECG:
					signalsList.add(new SensorDevice(SensorType.ECG_LALL, false));
					signalsList.add(new SensorDevice(SensorType.ECG_RALL, false));
				break;
				case EMG:
					signalsList.add(new SensorDevice(SensorType.EMG, false));
				break;
				case EXP_BOARDA0:
					signalsList.add(new SensorDevice(SensorType.EXP_BOARDA0, false));
				break;
				case EXP_BOARDA7:
					signalsList.add(new SensorDevice(SensorType.EXP_BOARDA7, false));
				break;
				case GRAVITY:
					signalsList.add(new SensorDevice(SensorType.GRAVITY_X, false));
					signalsList.add(new SensorDevice(SensorType.GRAVITY_Y, false));
					signalsList.add(new SensorDevice(SensorType.GRAVITY_Z, false));
				break;
				case GSR:
					signalsList.add(new SensorDevice(SensorType.GSR, false));
				break;
				case GYROSCOPE:
					signalsList.add(new SensorDevice(SensorType.GYROSCOPE_X, false));
					signalsList.add(new SensorDevice(SensorType.GYROSCOPE_Y, false));
					signalsList.add(new SensorDevice(SensorType.GYROSCOPE_Z, false));
				break;
				case HEART_RATE:
					signalsList.add(new SensorDevice(SensorType.HEART_RATE, false));
				break;
				case HUMIDITY:
					signalsList.add(new SensorDevice(SensorType.HUMIDITY, false));
				break;
				case LIGHT:
					signalsList.add(new SensorDevice(SensorType.LIGHT, false));
				break;
				case LINEAR_ACCELERATION:
					signalsList.add(new SensorDevice(SensorType.LINEAR_ACCELERATION_X, false));
					signalsList.add(new SensorDevice(SensorType.LINEAR_ACCELERATION_Y, false));
					signalsList.add(new SensorDevice(SensorType.LINEAR_ACCELERATION_Z, false));
				break;
				case MAGNETOMETER:
					signalsList.add(new SensorDevice(SensorType.MAGNETOMETER_X, false));
					signalsList.add(new SensorDevice(SensorType.MAGNETOMETER_Y, false));
					signalsList.add(new SensorDevice(SensorType.MAGNETOMETER_Z, false));
				break;
				case ORIENTATION:
					signalsList.add(new SensorDevice(SensorType.ORIENTATION_X, false));
					signalsList.add(new SensorDevice(SensorType.ORIENTATION_Y, false));
					signalsList.add(new SensorDevice(SensorType.ORIENTATION_Z, false));
				break;
				case PRESSURE:
					signalsList.add(new SensorDevice(SensorType.PRESSURE, false));
				break;
				case PROXIMITY:
					signalsList.add(new SensorDevice(SensorType.PROXIMITY, false));
				break;
				case ROTATION_VECTOR:
					signalsList.add(new SensorDevice(SensorType.ROTATION_VECTOR_X, false));
					signalsList.add(new SensorDevice(SensorType.ROTATION_VECTOR_Y, false));
					signalsList.add(new SensorDevice(SensorType.ROTATION_VECTOR_Z, false));
				break;
				case STRAIN:
					signalsList.add(new SensorDevice(SensorType.STRAIN_GAUGE_HIGH, false));
					signalsList.add(new SensorDevice(SensorType.STRAIN_GAUGE_LOW, false));
				break;
			}
		}
	}
}
