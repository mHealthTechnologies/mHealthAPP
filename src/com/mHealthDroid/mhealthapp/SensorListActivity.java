package com.mHealthDroid.mhealthapp;

import java.util.ArrayList;

import com.mHealthDroid.mhealthpp.R;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData.SensorType;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SensorListActivity extends Activity{

	
	public CommunicationManager cm;
	public int typeDevice;
	public String nameDevice;
	public ListView listSensors;
	
	MyCustomAdapter dataAdapter = null;
	ArrayList<SensorDevice> sensorList = new ArrayList<SensorDevice>();
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cm = CommunicationManager.getInstance();
//		sensors = new SensorAdapter();
		sensorList = new ArrayList<SensorDevice>();
		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.sensors_list);
		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nameDevice = extras.getString(ConnectFragment.NAME_DEVICE);
		    typeDevice = extras.getInt(ConnectFragment.TYPE_DEVICE);
		}
		
		Button okButton = (Button) findViewById(R.id.button_ok_sensors);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<SensorType> sensorsToEnable = new ArrayList<SensorType>();
				for(int i=0; i<sensorList.size(); i++)
					if(sensorList.get(i).selected)
						sensorsToEnable.add(sensorList.get(i).sensorType);
				
				cm.setEnabledSensors(nameDevice, sensorsToEnable);
				setResult(RESULT_OK);
				finish();
			}
		});
		
		if(typeDevice == ConnectFragment.DEVICE_SHIMMER)
			initShimmerSensors();
		else
			initMobileSensors();
		
		setEnabledSensors(cm.getEnabledSensors(nameDevice));
		//create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.sensor_checkbox, sensorList);
		ListView listView = (ListView) findViewById(R.id.list_sensors);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);
	}
	
	
	
//	public class MyCustomAdapter extends ArrayAdapter<SensorDevice> {
//		
//		private ArrayList<SensorDevice> sensorList;
//		
//		public MyCustomAdapter(Context context, int textViewResourceId,
//			    ArrayList<SensorDevice> sensorList) {
//			   super(context, textViewResourceId, sensorList);
//			   this.sensorList = new ArrayList<SensorDevice>();
//			   this.sensorList.addAll(sensorList);
//		}
//		
//		 public class ViewHolder{
//			 TextView text;
//			 CheckBox check;
//		 }
//		 
//		 @Override
//		  public View getView(int position, View convertView, ViewGroup parent) {
//			 
//			 ViewHolder holder = null;
//			 Log.v("ConvertView", String.valueOf(position));
//			 
//			 if (convertView == null) {
//				   LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				   convertView = vi.inflate(R.layout.sensor_checkbox, null);
//				 
//				   holder = new ViewHolder();
//				   holder.text = (TextView) convertView.findViewById(R.id.text_sensor);
//				   holder.check = (CheckBox) convertView.findViewById(R.id.checkBox_sensor);
//				   convertView.setTag(holder);
//				   holder.check.setOnClickListener(new OnClickListener() {
//					
//					   @Override
//					   public void onClick(View v) {
//						   // TODO Auto-generated method stub
//						   CheckBox cb = (CheckBox) v ; 
//						   SensorDevice sensor = (SensorDevice) cb.getTag(); 
//						   sensor.setSelected(cb.isChecked());
//					   }
//				   });
//			 }
//			 else 
//				 holder = (ViewHolder) convertView.getTag();
//			 
//			 SensorDevice sensor = sensorList.get(position);
//			 holder.text.setText(sensor.sensorType.toString());
//			 holder.check.setChecked(sensor.selected);
//			 holder.check.setTag(sensor);
//			 
//			 return convertView;
//		 }
//	}
	
	
	public void initShimmerSensors(){
		  sensorList.add(new SensorDevice(SensorType.ACCELEROMETER, false));
		  sensorList.add(new SensorDevice(SensorType.MAGNETOMETER, false));
		  sensorList.add( new SensorDevice(SensorType.GYROSCOPE, false));
		  sensorList.add(new SensorDevice(SensorType.ECG, false));
		  sensorList.add(new SensorDevice(SensorType.EMG, false));
		  sensorList.add(new SensorDevice(SensorType.GSR, false));
		  sensorList.add(new SensorDevice(SensorType.STRAIN, false));
		  sensorList.add(new SensorDevice(SensorType.HEART_RATE, false));
		  sensorList.add(new SensorDevice(SensorType.EXP_BOARDA0, false));
		  sensorList.add(new SensorDevice(SensorType.EXP_BOARDA7, false));
	}
	
	public void initMobileSensors(){
		  sensorList.add(new SensorDevice(SensorType.ACCELEROMETER, false));
		  sensorList.add(new SensorDevice(SensorType.AMBIENT_TEMPERATURE, false));
		  sensorList.add(new SensorDevice(SensorType.GYROSCOPE, false));
		  sensorList.add(new SensorDevice(SensorType.GRAVITY, false));
		  sensorList.add(new SensorDevice(SensorType.LIGHT, false));
		  sensorList.add(new SensorDevice(SensorType.LINEAR_ACCELERATION, false));
		  sensorList.add(new SensorDevice(SensorType.PRESSURE, false));
		  sensorList.add(new SensorDevice(SensorType.PROXIMITY, false));
		  sensorList.add(new SensorDevice(SensorType.HUMIDITY, false));
		  sensorList.add(new SensorDevice(SensorType.ROTATION_VECTOR, false));
	}
	
	public void setEnabledSensors(ArrayList<SensorType> enabledSensors){
		
		for(int i=0; i<enabledSensors.size(); i++)
			for(int j=0; j<sensorList.size(); j++)
				if(sensorList.get(j).sensorType == enabledSensors.get(i)){
					sensorList.get(j).selected = true;
					break;
				}
	}

}
