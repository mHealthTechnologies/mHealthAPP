package com.mHealthDroid.mhealthapp;


import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import weka.classifiers.Classifier;

import communicationManager.CommunicationManager;

import com.mHealthDroid.mhealthapp.Device.State;
import com.mHealthDroid.mhealthapp.Device.TypeDevice;
import com.mHealthDroid.mhealthpp.R;

import dataprocessingManager.DataProcessingManager;



import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ConnectFragment extends Fragment{

	public CommunicationManager cm;
	private BluetoothAdapter myBluetoothAdapter = null;
	public ListView listDevices;
	public DeviceAdapter devices;
	static final int REQUEST_ENABLE_BT = 1;
	static final int REQUEST_CONNECT_DEVICE = 2;
	static final int REQUEST_CONNECT_DEVICE_SHIMMER = 3;
	static final int REQUEST_DEVICE_MENU = 4;
	static final int DEVICE_MOBILE = 1;
	static final int DEVICE_SHIMMER = 2;
	public static final String TYPE_DEVICE = "type";
	public static final String NAME_DEVICE = "nameDevice";
	public String nameDeviceIntroduced;	
	public String nameDeviceSelected;
	public TypeDevice typeDeviceSelected;
	public Device noneDevice;
	
	public SharedPreferences myprefs;
	public int numDevices;
	public static final String NUM_DEVICES = "numDevices";
	public static final String NAME_STORED = "device";
	public static final String TYPE = "type";
	public static final String ADDRESS = "address";
	public ArrayList<String> names = new ArrayList<String>();
	public ArrayList<String> addressess = new ArrayList<String>();
	public ArrayList<String> types = new ArrayList<String>();
	public boolean created;
	public Hashtable<String, Integer> statesStored;
	
	public ConnectFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cm = CommunicationManager.getInstance();
		cm.CreateStorage(getActivity());
		names = new ArrayList<String>();
		cm.setHandlerApp(mHanlder);
		addressess = new ArrayList<String>();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		
		myprefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        numDevices = myprefs.getInt(NUM_DEVICES, 0);
        
        devices = new DeviceAdapter();
        noneDevice = new Device("No device connected", null, State.NONE);
        
        if(numDevices!=0){
        	
        	if(cm.getDevice(myprefs.getString(NAME_STORED+"0", ""))==null)
        		created = false;
        	else{
        		created = true;
        		statesStored = cm.getCurrentDevicesState();
        	}
        	
        	for(int i=0; i<numDevices;i++){
        		names.add(myprefs.getString(NAME_STORED+i, ""));
        		types.add(myprefs.getString(TYPE+i, ""));
        		addressess.add(myprefs.getString(ADDRESS+i, ""));
        		if(myprefs.getString(TYPE+i, "").equals(TypeDevice.Shimmer.toString())){
        			Device d;
        			if(created){
        				 d = new Device(myprefs.getString(NAME_STORED+i, ""), TypeDevice.Shimmer, 
        						 	convertState(statesStored.get(myprefs.getString(NAME_STORED+i, ""))),
        						 		myprefs.getString(ADDRESS+i, ""));
        			}
        			else{
        				d = new Device(myprefs.getString(NAME_STORED+i, ""), TypeDevice.Shimmer, 
        						State.DISCONNECTED, myprefs.getString(ADDRESS+i, ""));
        			}
        				
        			devices.add(d);	
        		}
        		else{
        			Device d;
        			if(created){
        				d = new Device(myprefs.getString(NAME_STORED+i, ""), TypeDevice.Mobile, 
            						convertState(statesStored.get(myprefs.getString(NAME_STORED+i, ""))),
            							myprefs.getString(ADDRESS+i, ""));
        			}
        			else{
        				d = new Device(myprefs.getString(NAME_STORED+i, ""), TypeDevice.Mobile, 
            					State.CONNECTED, myprefs.getString(ADDRESS+i, ""));
        			}
        			devices.add(d);	
        		}
        		
        	}

        	if(cm.getDevice(names.get(0))==null){ 
        		for(int j=0; j<names.size();j++){
        			if(types.get(j).equals(TypeDevice.Shimmer.toString())){
        				cm.addDeviceShimmer(getActivity(), names.get(j), true);
        				cm.setMacAddres(names.get(j), addressess.get(j));
        			}
        			else{
        				cm.addDeviceMobile(getActivity(), names.get(j));
        			}
        		}
        	}        			
        }
        else
        	devices.add(noneDevice);
        
		
		LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.connect, null);		
		listDevices = (ListView) mLinearLayout.findViewById(R.id.list_devices);		
	    listDevices.setAdapter(devices);	    
	    listDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				nameDeviceSelected = ((Device) devices.getItem(position)).name;
				typeDeviceSelected = ((Device) devices.getItem(position)).typeDevice;
				if(!nameDeviceSelected.equals("No device connected")){
					Intent serverIntent = new Intent(getActivity().getApplicationContext(), DeviceMenuActivity.class);
					serverIntent.putExtra(NAME_DEVICE, nameDeviceSelected);
					if(typeDeviceSelected == TypeDevice.Shimmer)
						serverIntent.putExtra(TYPE_DEVICE, DEVICE_SHIMMER);
					else
						serverIntent.putExtra(TYPE_DEVICE, DEVICE_MOBILE);
					startActivityForResult(serverIntent, REQUEST_DEVICE_MENU);
				}
			}
	    	
		});
		
		ImageButton connectButton = (ImageButton) mLinearLayout.findViewById(R.id.imageButton);
		connectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent serverIntent = new Intent(getActivity().getApplicationContext(), TypeDeviceActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			}
		});
		
		
		return mLinearLayout;
	}
	
	
	public final Handler mHanlder = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 0: // NONE
				break;
				case 1: // CONNECTING
					String nameDevice1 = (String) msg.obj;
					devices.setStatus(nameDevice1, State.CONNECTING);
					
				break;
				case 2: // CONNECTED
					String nameDevice2 = (String) msg.obj;
					Log.d("handler", "nombre = "+nameDevice2);
					devices.setStatus(nameDevice2, State.CONNECTED);
				break;
				case 3: // STREAMING
					String nameDevice3 = (String) msg.obj;
					Log.d("handler", "nombre = "+nameDevice3);
					devices.setStatus(nameDevice3, State.STREAMING);
					break;
				case 4: // DISCONNTECT
					String nameDevice4 = (String) msg.obj;
					devices.setStatus(nameDevice4, State.DISCONNECTED);
				break;
				case 8: // REMOVE A DEVICE
					devices.remove(nameDeviceSelected);
				break;
			}
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					Toast.makeText(getActivity().getApplicationContext(), 
							"Bluetooth is now enabled", Toast.LENGTH_SHORT).show();
					Intent serverIntent = new Intent(getActivity().getApplicationContext(),
							com.mHealthDroid.mhealthapp.DeviceListActivity.class);
					startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SHIMMER);
				} else {
					// User did not enable Bluetooth or an error occured
					Toast.makeText(getActivity().getApplicationContext(),
							"Bluetooth not enabled\nExiting...", Toast.LENGTH_SHORT).show();
				}
				break;
			case REQUEST_CONNECT_DEVICE:
				if (resultCode == Activity.RESULT_OK){
					int type = data.getExtras().getInt(TYPE_DEVICE);
					String nameDevice = data.getExtras().getString(NAME_DEVICE);
					
					if(type==TypeDeviceActivity.DEVICE_MOBILE){
						if(devices.existDeviceMobile()){
							sendErrorDialog("The device mobile is already connected");
							return;
						}
						if(devices.existName(nameDevice)){
							sendErrorDialog("There is already a device with that name");
							return;
						}
							
						cm.addDeviceMobile(getActivity().getApplicationContext(), nameDevice);
						Device d = new Device(nameDevice, TypeDevice.Mobile, State.CONNECTED);
						devices.add(d);
						devices.notifyDataSetChanged();
					}
					else{
						if(devices.existName(nameDevice)){
							sendErrorDialog("There is already a device with that name");
							return;
						}
						nameDeviceIntroduced = nameDevice;
						myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
						if (myBluetoothAdapter == null) {
							Toast.makeText(getActivity().getApplicationContext(), "Device does not support Bluetooth\n",
									Toast.LENGTH_LONG).show();
						}
						
						if (!myBluetoothAdapter.isEnabled()) {
							Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
							startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
						} else {
							Intent serverIntent = new Intent(getActivity().getApplicationContext(),
									com.mHealthDroid.mhealthapp.DeviceListActivity.class);
							startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SHIMMER);
						}
					}
					
				}
			break;
			case REQUEST_CONNECT_DEVICE_SHIMMER:
				if(resultCode==Activity.RESULT_OK){
					String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					Device d = new Device(nameDeviceIntroduced, TypeDevice.Shimmer, State.DISCONNECTED, address);
					devices.add(d);
					devices.notifyDataSetChanged();
					cm.addDeviceShimmer(getActivity().getApplicationContext(), nameDeviceIntroduced, true);
					cm.connect(nameDeviceIntroduced, address);
				}
			break;
			case REQUEST_DEVICE_MENU:
				if(resultCode==Activity.RESULT_OK){
					if(!cm.containsDevice(nameDeviceSelected)){
						devices.remove(nameDeviceSelected);
						nameDeviceSelected = null;
						if(devices.getCount()==0)
							devices.add(noneDevice);
						
					}
				}
			break;
		}
	}
	
	public void sendErrorDialog(String message){
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		// set title
		alertDialogBuilder.setTitle("Insertion not allowed");
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
		numDevices = devices.getCount();
		mEditor.putInt(NUM_DEVICES, numDevices);
		for(int i=0; i<numDevices;i++){
			Device d = (Device) devices.getItem(i);
			mEditor.putString(NAME_STORED+i, d.name);
			mEditor.putString(TYPE_DEVICE+i, d.typeDevice.toString());
			mEditor.putString(ADDRESS+i, d.macAddress);
		}
		
		mEditor.commit();
	}
	
	public State convertState(int s){
		
		switch(s){
		case 0: // disconnected
			return State.DISCONNECTED;
		case 1: // connected
			return State.CONNECTED;
		case 2: // streaming
			return State.STREAMING;
		}
		
		return State.DISCONNECTED;
	}
}
