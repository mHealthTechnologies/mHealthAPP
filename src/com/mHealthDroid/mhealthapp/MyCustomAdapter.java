package com.mHealthDroid.mhealthapp;

import java.util.ArrayList;

import com.mHealthDroid.mhealthpp.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyCustomAdapter extends ArrayAdapter<SensorDevice> {
	
	private ArrayList<SensorDevice> sensorList;
	private Context context;
	
	public MyCustomAdapter(Context context, int textViewResourceId,
		    ArrayList<SensorDevice> sensorList) {
		   super(context, textViewResourceId, sensorList);
		   this.sensorList = new ArrayList<SensorDevice>();
		   this.sensorList.addAll(sensorList);
		   this.context = context;
	}
	
	 public class ViewHolder{
		 TextView text;
		 CheckBox check;
	 }
	 
	 @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		 
		 ViewHolder holder = null;
		 Log.v("ConvertView", String.valueOf(position));
		 
		 if (convertView == null) {
			   LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = vi.inflate(R.layout.sensor_checkbox, null);
			 
			   holder = new ViewHolder();
			   holder.text = (TextView) convertView.findViewById(R.id.text_sensor);
			   holder.check = (CheckBox) convertView.findViewById(R.id.checkBox_sensor);
			   convertView.setTag(holder);
			   holder.check.setOnClickListener(new OnClickListener() {
				
				   @Override
				   public void onClick(View v) {
					   // TODO Auto-generated method stub
					   CheckBox cb = (CheckBox) v ; 
					   SensorDevice sensor = (SensorDevice) cb.getTag(); 
					   sensor.setSelected(cb.isChecked());
				   }
			   });
		 }
		 else 
			 holder = (ViewHolder) convertView.getTag();
		 
		 SensorDevice sensor = sensorList.get(position);
		 holder.text.setText(sensor.sensorType.toString());
		 holder.check.setChecked(sensor.selected);
		 holder.check.setTag(sensor);
		 
		 return convertView;
	 }
}
