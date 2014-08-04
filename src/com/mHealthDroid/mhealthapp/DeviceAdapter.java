package com.mHealthDroid.mhealthapp;

import java.util.ArrayList;
import java.util.Vector;

import com.mHealthDroid.mhealthapp.Device.State;
import com.mHealthDroid.mhealthapp.Device.TypeDevice;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DeviceAdapter extends BaseAdapter{

	
	private ArrayList<Device> devices;
	
	
	public DeviceAdapter(){
		devices = new ArrayList<Device>();
	}
	
	public DeviceAdapter(Device device){
		devices = new ArrayList<Device>();
		devices.add(device);
		notifyDataSetChanged();
	}
	
	public void add(Device device){
		if(devices.size()==1 && devices.get(0).name.equals("No device connected"))
			devices.remove(0);
		devices.add(device);
		notifyDataSetChanged();
	}
	
	public void remove(int position){
		devices.remove(position);
		if(devices.size()==0){
			Device emptyDevice = new Device("No device connected",null,State.NONE);
			devices.add(emptyDevice);
		}
	}
	
	public void remove(String deviceName){
		
		for(Device d: devices){
			if(d.name.equals(deviceName)){
				devices.remove(d);
				notifyDataSetChanged();
				return;
			}
		}
	}
	
	public void clear(){
		devices.clear();
	}
	
	public void setStatus(String nameDevice, State state){
		
		for(Device d: devices){
			if(d.name.equals(nameDevice)){
				d.state = state;
				notifyDataSetChanged();
				return;
			}
		}
	}
	
	public void setAddress(String nameDevice, String address){
		
		for(Device d: devices){
			if(d.name.equals(nameDevice)){
				d.macAddress = address;
				return;
			}
		}
	}
	
	public boolean remove(Device d){
		return devices.remove(d);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(devices.size()==1)
			if(devices.get(0).name.equals("No device connected"))
				return 0;
		
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DeviceView view;
		if(convertView==null)
			view = new DeviceView(parent.getContext());
		else
			view = (DeviceView) convertView;
		
		view.setDevice(devices.get(position));
		return view;
	}
	
	public boolean existName(String nameDevice){
		
		for(Device d: devices)
			if(d.name.equals(nameDevice))
				return true;
		
		return false;
	}
	
	public boolean existDeviceMobile(){
		
		for(Device d: devices){
			if(d.typeDevice == TypeDevice.Mobile)
				return true;
		}
		return false;
	}

}
