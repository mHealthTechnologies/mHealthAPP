package com.mHealthDroid.mhealthapp;

import communicationManager.dataStructure.ObjectData.SensorType;

public class SensorDevice {

	
	public SensorType sensorType;
	public boolean selected;
	
	
	public SensorDevice(SensorType sensorType, boolean selected) {
		super();
		this.sensorType = sensorType;
		this.selected = selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
