package com.mHealthDroid.mhealthapp;

import com.mHealthDroid.mhealthpp.R;

import communicationManager.dataStructure.ObjectData.SensorType;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DeviceView extends LinearLayout{

	
	private TextView name;
	private TextView type;
	private ImageView imageStatus;
	
	
	public DeviceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.device_connectivity, this);
		 name = (TextView) findViewById(R.id.name_device);
		 type = (TextView) findViewById(R.id.type_device);
		 imageStatus = (ImageView) findViewById(R.id.status_device);
	}
	
	public void setDevice(Device device){
		name.setText(device.name);
		
		if(device.typeDevice!=null)
			type.setText(device.typeDevice.toString());
		else
			type.setText("");
		
		if(device.state!=null){
			switch(device.state){
				case CONNECTING:
					imageStatus.setVisibility(View.VISIBLE);
					imageStatus.setImageResource(R.drawable.yellowcircle24);
				break;
				case CONNECTED:
					imageStatus.setVisibility(View.VISIBLE);
					imageStatus.setImageResource(R.drawable.circuloverde);
					break;
				case DISCONNECTED:
					imageStatus.setVisibility(View.VISIBLE);
					imageStatus.setImageResource(R.drawable.circulorojo);
					break;
				case STREAMING:
					imageStatus.setVisibility(View.VISIBLE);
					imageStatus.setImageResource(R.drawable.circulonaranja);
					break;
				case NONE:
					//imageStatus.setImageResource(R.drawable.whitecircle24);
					imageStatus.setVisibility(View.INVISIBLE);
					break;
			}
		}
	}
	
}
