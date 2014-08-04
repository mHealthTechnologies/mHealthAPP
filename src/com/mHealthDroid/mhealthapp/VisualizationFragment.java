package com.mHealthDroid.mhealthapp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.mHealthDroid.mhealthpp.R;
import com.jjoe64.graphview.GraphView.LegendAlign;

import visualizationManager.VisualizationManager;
import visualizationManager.Plot.GraphType;
import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData.SensorType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;

public class VisualizationFragment extends Fragment{

	
	public VisualizationManager vm;
	public CommunicationManager cm;
	
	public static final String nameGraph = "Visualization Online";
	public double viewPort;
	public double maxY;
	public double minY;
	public boolean setViewPort;
	public boolean setYValues;
	public boolean showLegend;
	public boolean isOnline;
	public boolean isCustomized;
	public int align;
	
	LinearLayout graphLayout; 
	
	public static String deviceSelected;
	public ArrayList<String> sensorsSelected;
	public ArrayList<SensorType> sensorsSelectedInType;
	public int numSensors;
	
	static final int REQUEST_GRAPH_CONFIGURATION = 1;
	static final int REQUEST_ONLINE_VISUALIZATION = 2;
	static final String IS_ONLINE = "online";
	static final String SET_VIEW_PORT = "setViewPort";
	static final String VIEW_PORT = "viewPort";
	static final String SET_COORDINATES = "setCoordinates";
	static final String Y_MAX = "yMax";
	static final String Y_MIN = "yMin";
	static final String SET_LEGEND = "setLegend";
	static final String ALIGN = "align";
	static final String IS_CUSTOMIZED = "customized";
	static final String NUM_SENSORS = "numSensors";
	static final String SENSOR = "sensor";
	static final String DEVICE_SELECTED = "deviceSelected";
	static final String SENSORS_SELECTED = "sensorsSelected";
	
	public SharedPreferences myprefs;
	
	Button configurationButton;
	
	public VisualizationFragment() {
		super();
		// TODO Auto-generated constructor stub
		vm = VisualizationManager.getInstance();
		cm = CommunicationManager.getInstance();
		sensorsSelected = new ArrayList<String>();
		sensorsSelectedInType = new ArrayList<SensorType>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		vm.addGraph(nameGraph, GraphType.LINE, getActivity());
		vm.setScalable(nameGraph, true);
		vm.setScrollable(nameGraph, true);
		vm.getGraphStyle(nameGraph).setNumHorizontalLabels(3);
		vm.getGraphStyle(nameGraph).setNumVerticalLabels(4);
		vm.getGraphStyle(nameGraph).setVerticalLabelsWidth(50);
		vm.getGraphStyle(nameGraph).setTextSize(20f);
		viewPort=200;
		vm.setViewPort(nameGraph, 0, viewPort);
		isOnline = false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		myprefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
//		isOnline = myprefs.getBoolean(IS_ONLINE, false);
		isCustomized = myprefs.getBoolean(IS_CUSTOMIZED, false);
		setViewPort = myprefs.getBoolean(SET_VIEW_PORT, false);
		setYValues = myprefs.getBoolean(SET_COORDINATES, false);
		showLegend = myprefs.getBoolean(SET_LEGEND, false);
		viewPort = myprefs.getFloat(VIEW_PORT, 200);
		maxY = myprefs.getFloat(Y_MAX, 10);
		minY = myprefs.getFloat(Y_MIN, -10);
		align = myprefs.getInt(ALIGN, 1);
		
		Log.d("viewport", "viewport = "+viewPort);
		
//		numSensors = myprefs.getInt(NUM_SENSORS, 4);
//		for(int i=0; i<numSensors; i++){
//			Log.d("sensores", "sensor añadido: "+myprefs.getString(SENSOR+i, ""));
//			sensorsSelected.add(myprefs.getString(SENSOR+i, ""));
//		}
		

		vm.setViewPort(nameGraph, 0, viewPort);
		if(setYValues){
			vm.setManualYAxis(nameGraph, setYValues);
			vm.setManualYAxisBounds(nameGraph, maxY, minY);
		}
		if(showLegend){
			vm.setShowLegend(nameGraph, showLegend);
			switch(align){
			case 1:
				vm.setLegendAlign(nameGraph, LegendAlign.TOP);
				break;
			case 2:
				vm.setLegendAlign(nameGraph, LegendAlign.MIDDLE);
				break;
			case 3:
				vm.setLegendAlign(nameGraph, LegendAlign.BOTTOM);
				break;
			}
			
		}
		
		
		LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.visualization, null);
		graphLayout = (LinearLayout) mLinearLayout.findViewById(R.id.layout_graph);
		
		Button visualizationButton = (Button) mLinearLayout.findViewById(R.id.button_visualization);
//		if(isOnline){
//			deviceSelected = myprefs.getString(DEVICE_SELECTED, "");
//			if(cm.getDevice(deviceSelected)==null){
//				isOnline = false;
//				sendErrorDialog("The device "+deviceSelected+" does not exist anymore");
//				visualizationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_start));
//				visualizationButton.setText(R.string.start);
//			}
//			else
//				if(!cm.isStreaming(deviceSelected)){
//					isOnline = false;
//					sendErrorDialog("The device "+deviceSelected+" is not streaming anymore");
//					visualizationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_start));
//					visualizationButton.setText(R.string.start);
//				}
//				else{
//					visualizationButton.setBackgroundColor(getResources().getColor(R.color.Red));
//					visualizationButton.setText(R.string.stop);
//					numSensors = myprefs.getInt(NUM_SENSORS, 0);
//					Log.d("visualization", "numero sensores = "+numSensors);
//					Log.d("visualization", "nombre device = "+ deviceSelected);
//					for(int i=0; i<numSensors; i++){
//						sensorsSelectedInType.add(fromStringToSensorType(sensorsSelected.get(i)));
//						Log.d("visualization", "nombre sebsor = "+ myprefs.getString(SENSOR+i, ""));
//					}
//					Log.d("visualization", "numero sensores again = "+ sensorsSelected.size());
//					vm.visualizationOnline(nameGraph, deviceSelected, sensorsSelectedInType);
//				}
//		}
//		else{
//			visualizationButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_start));
//			visualizationButton.setText(R.string.start);
//		}
		vm.paint(nameGraph, graphLayout);
			
		visualizationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isOnline==false){
					Hashtable<String, Integer> states = cm.getCurrentDevicesState();
					Set<String> keys = states.keySet();
					int cont = 0;
					for(String k: keys)
						if(states.get(k)==2)
							cont++;
					
					if(cont==0){
						sendErrorDialog("There are no devices streaming");
						return;
					}
					Intent intent = new Intent(getActivity(), StartVisualizationActivity.class);
//					intent.putExtra(IS_CUSTOMIZED, isCustomized);
//					if(isCustomized){
						intent.putExtra(SET_VIEW_PORT, setViewPort);
//						if(setViewPort)
							intent.putExtra(VIEW_PORT, viewPort);
						intent.putExtra(SET_COORDINATES, setYValues);
//						if(setYValues){
							intent.putExtra(Y_MAX, maxY);
							intent.putExtra(Y_MIN, minY);
//						}
						intent.putExtra(SET_LEGEND, showLegend);
//						if(showLegend)
							intent.putExtra(ALIGN, align);
//					}
					startActivityForResult(intent, REQUEST_ONLINE_VISUALIZATION);
				}
				else{
					isOnline = false;
					Button b = (Button) v;
//					b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_start));
					b.setBackgroundResource(R.drawable.start_selector);
					b.setText(R.string.start);
					vm.stopVisualizationOnline(nameGraph, deviceSelected);
					Fragment visualizationFragment = new VisualizationFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, visualizationFragment).commit();
				}
			}
		});
		
		
		configurationButton = (Button) mLinearLayout.findViewById(R.id.button_graph_configuration);
		configurationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent serverIntent = new Intent(getActivity().getApplicationContext(), GraphConfigurationActivity.class);
				serverIntent.putExtra(IS_CUSTOMIZED, isCustomized);
				serverIntent.putExtra(IS_ONLINE, isOnline);
				serverIntent.putExtra(SET_VIEW_PORT, setViewPort);
//				if(!isOnline && setViewPort)
					serverIntent.putExtra(VIEW_PORT, viewPort);
				
				serverIntent.putExtra(SET_COORDINATES, setYValues);
//				if(!isOnline && setYValues){
					
					serverIntent.putExtra(Y_MAX, maxY);
					serverIntent.putExtra(Y_MIN, minY);
//				}
				serverIntent.putExtra(SET_LEGEND, showLegend);
				serverIntent.putExtra(ALIGN, align);
				startActivityForResult(serverIntent, REQUEST_GRAPH_CONFIGURATION);
			}
		});
		
		
		return mLinearLayout;
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
			case REQUEST_GRAPH_CONFIGURATION:
				if(resultCode==Activity.RESULT_OK){
					isCustomized = true;
//					Bundle extras = data.getExtras();
					setViewPort = data.getExtras().getBoolean(SET_VIEW_PORT);
					if(setViewPort)
						viewPort = data.getExtras().getDouble(VIEW_PORT);
					setYValues = data.getExtras().getBoolean(SET_COORDINATES);
					if(setYValues){
						maxY = data.getExtras().getDouble(Y_MAX);
						minY = data.getExtras().getDouble(Y_MIN);
					}
					showLegend = data.getExtras().getBoolean(SET_LEGEND);
					if(showLegend)
						align = data.getExtras().getInt(ALIGN);			
					if(isOnline){
						if(showLegend){
							switch(align){
								case 1:
									vm.setLegendAlign(nameGraph, LegendAlign.TOP);
								break;
								case 2:
									vm.setLegendAlign(nameGraph, LegendAlign.MIDDLE);
								break;
								case 3:
									vm.setLegendAlign(nameGraph, LegendAlign.BOTTOM);
								break;
							}
						}
						vm.setShowLegend(nameGraph, showLegend);
					}
				}
			break;
			case REQUEST_ONLINE_VISUALIZATION:
				if(resultCode==Activity.RESULT_OK){
					isOnline = true;
					Button b = (Button) getView().findViewById(R.id.button_visualization);
//					b.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_stop));
					b.setBackgroundResource(R.drawable.stop_selector);
					b.setText(R.string.stop);
					if(isOnline)
						configurationButton.setVisibility(View.GONE);
				}
			break;
		}
	}
	
	public void sendErrorDialog(String message){
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		// set title
		alertDialogBuilder.setTitle("Visualization not possible");
		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						dialog.cancel();
					}
				  });
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
	@Override
	public void onPause(){
		super.onPause();
					
		Editor mEditor = myprefs.edit();
//		mEditor.putBoolean(IS_ONLINE, isOnline);		
		mEditor.putBoolean(IS_CUSTOMIZED, isCustomized);
		Log.d("viewport", "viewport = "+viewPort);
		mEditor.putBoolean(SET_VIEW_PORT, setViewPort);
		mEditor.putBoolean(SET_COORDINATES, setYValues);
		mEditor.putBoolean(SET_LEGEND, showLegend);
		mEditor.putFloat(VIEW_PORT, (float) viewPort);
		mEditor.putFloat(Y_MAX, (float) maxY);
		mEditor.putFloat(Y_MIN, (float) minY);
		mEditor.putInt(ALIGN, align);
		
//		mEditor.putInt(NUM_SENSORS, sensorsSelected.size());
//		
//		if(isOnline){
//			vm.stopVisualizationOnline(nameGraph, deviceSelected);
//			Log.d("guardado", "nombre device: "+deviceSelected);
//			mEditor.putString(DEVICE_SELECTED, deviceSelected);
//			mEditor.putInt(NUM_SENSORS, sensorsSelected.size());
//			Log.d("guardado", "numero sensores: "+sensorsSelected.size());
//			for(int i=0; i<sensorsSelected.size(); i++){
//				Log.d("guardado", "numero sensores: "+sensorsSelected.get(i).toString());
//				mEditor.putString(SENSOR+i, sensorsSelected.get(i));
//			}
//		}
	
		mEditor.commit();
		
		if(isOnline)
			vm.stopVisualizationOnline(nameGraph, deviceSelected);
		
	}
	
	
public SensorType fromStringToSensorType(String sensor){
		
		
		if(sensor.equals(SensorType.ACCELEROMETER_X.toString()))
			return SensorType.ACCELEROMETER_X;
		if(sensor.equals(SensorType.ACCELEROMETER_Y.toString()))
			return SensorType.ACCELEROMETER_Y;
		if(sensor.equals(SensorType.ACCELEROMETER_Z.toString()))
			return SensorType.ACCELEROMETER_Z;
		
		if(sensor.equals(SensorType.AMBIENT_TEMPERATURE.toString()))
			return SensorType.AMBIENT_TEMPERATURE;
		
		if(sensor.equals(SensorType.ECG_LALL.toString()))
			return SensorType.ECG_LALL;
		if(sensor.equals(SensorType.ECG_RALL.toString()))
			return SensorType.ECG_RALL;
			
		if(sensor.equals(SensorType.EMG.toString()))
			return SensorType.EMG;
		if(sensor.equals(SensorType.EXP_BOARDA0.toString()))
			return SensorType.EXP_BOARDA0;
		if(sensor.equals(SensorType.EXP_BOARDA7.toString()))
			return SensorType.EXP_BOARDA7;
		
		if(sensor.equals(SensorType.GRAVITY_X.toString()))
			return SensorType.GRAVITY_X;
		if(sensor.equals(SensorType.GRAVITY_Y.toString()))
			return SensorType.GRAVITY_Y;
		if(sensor.equals(SensorType.GRAVITY_Z.toString()))
			return SensorType.GRAVITY_Z;
		
		if(sensor.equals(SensorType.GSR.toString()))
			return SensorType.GSR;
		
		if(sensor.equals(SensorType.GYROSCOPE_X.toString()))
			return SensorType.GYROSCOPE_X;
		if(sensor.equals(SensorType.GYROSCOPE_Y.toString()))
			return SensorType.GYROSCOPE_Y;
		if(sensor.equals(SensorType.GYROSCOPE_Z.toString()))
			return SensorType.GYROSCOPE_Z;
		
		if(sensor.equals(SensorType.HEART_RATE.toString()))
			return SensorType.HEART_RATE;
		if(sensor.equals(SensorType.HUMIDITY.toString()))
			return SensorType.HUMIDITY;
		if(sensor.equals(SensorType.LIGHT.toString()))
			return SensorType.LIGHT;
		
		if(sensor.equals(SensorType.LINEAR_ACCELERATION_X.toString()))
			return SensorType.LINEAR_ACCELERATION_X;
		if(sensor.equals(SensorType.LINEAR_ACCELERATION_Y.toString()))
			return SensorType.LINEAR_ACCELERATION_Y;
		if(sensor.equals(SensorType.LINEAR_ACCELERATION_Z.toString()))
			return SensorType.LINEAR_ACCELERATION_Z;
		
		if(sensor.equals(SensorType.MAGNETOMETER_X.toString()))
			return SensorType.MAGNETOMETER_X;
		if(sensor.equals(SensorType.MAGNETOMETER_Y.toString()))
			return SensorType.MAGNETOMETER_Y;
		if(sensor.equals(SensorType.MAGNETOMETER_Z.toString()))
			return SensorType.MAGNETOMETER_Z;
		
		if(sensor.equals(SensorType.ORIENTATION_X.toString()))
			return SensorType.ORIENTATION_X;
		if(sensor.equals(SensorType.ORIENTATION_Y.toString()))
			return SensorType.ORIENTATION_Y;
		if(sensor.equals(SensorType.ORIENTATION_Z.toString()))
			return SensorType.ORIENTATION_Z;
		
		if(sensor.equals(SensorType.PRESSURE.toString()))
			return SensorType.PRESSURE;
		if(sensor.equals(SensorType.PROXIMITY.toString()))
			return SensorType.PROXIMITY;
		
		if(sensor.equals(SensorType.ROTATION_VECTOR_X.toString()))
			return SensorType.ROTATION_VECTOR_X;
		if(sensor.equals(SensorType.ROTATION_VECTOR_Y.toString()))
			return SensorType.ROTATION_VECTOR_Y;
		if(sensor.equals(SensorType.ROTATION_VECTOR_Z.toString()))
			return SensorType.ROTATION_VECTOR_Z;
		
		if(sensor.equals(SensorType.STRAIN_GAUGE_HIGH.toString()))
			return SensorType.STRAIN_GAUGE_HIGH;
		if(sensor.equals(SensorType.STRAIN_GAUGE_LOW.toString()))
			return SensorType.STRAIN_GAUGE_LOW;
		
		return SensorType.ACCELEROMETER_X;
	}
}
