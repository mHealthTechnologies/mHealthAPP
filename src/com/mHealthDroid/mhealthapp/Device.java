package com.mHealthDroid.mhealthapp;

public class Device {

	public String name;
	public TypeDevice typeDevice;
	public State state;
	public String macAddress;
	
	
	
	public Device(String name, TypeDevice typeDevice, State state) {
		super();
		this.name = name;
		if(typeDevice!=null)
			this.typeDevice = typeDevice;
		if(state!=null)
			this.state = state;
		macAddress="";
	}


	public Device(String name, TypeDevice typeDevice, State state,
			String macAddress) {
		super();
		this.name = name;
		this.typeDevice = typeDevice;
		this.state = state;
		this.macAddress = macAddress;
	}


	public void setAddress(String address){
		this.macAddress = address;
	}
	
	enum TypeDevice{Shimmer, Mobile};
	enum State {CONNECTING, CONNECTED, DISCONNECTED, STREAMING, NONE};
}
