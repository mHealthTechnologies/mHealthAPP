package com.mHealthDroid.mhealthapp;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import com.mHealthDroid.mhealthapp.Device.State;
import com.mHealthDroid.mhealthapp.Device.TypeDevice;
import com.mHealthDroid.mhealthpp.R;

import communicationManager.CommunicationManager;
import communicationManager.datareceiver.DeviceShimmer;
import remoteStorageManager.RemoteStorageManager;
import systemManager.SystemManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RemoteStorageFragment extends Fragment{

	RemoteStorageManager rsm;
	SystemManager sysm;
	CommunicationManager cm;
	View view;
	Button upload;
	ToggleButton wifi;
	ListView listDevices;
	public DeviceAdapter devices;
	public String nameDeviceIntroduced;	
	public String nameDeviceSelected;
	public TypeDevice typeDeviceSelected;
	public static final String TYPE_DEVICE = "type";
	public static final String NAME_DEVICE = "nameDevice";
	static final int DEVICE_MOBILE = 1;
	static final int DEVICE_SHIMMER = 2;
	static final int TABLES_DIALOG = 2000;
	Handler mHandler;
	
	public RemoteStorageFragment(){
		
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		rsm = RemoteStorageManager.getInstance();
		RemoteStorageManager.CreateStorage(getActivity());
		sysm = SystemManager.getInstance();
		cm = CommunicationManager.getInstance();
		cm.CreateStorage(getActivity());
		devices = new DeviceAdapter();
		mHandler = new Handler();
		
		rsm.setServerIP("http://192.168.1.11");			
		//rsm.setServerIP("http://192.168.1.11");
//		rsm.setMobileMetadataPath("insert_mobile_metadata.php");
//		rsm.setMobileSignalsPath("insert_mobile_signals.php");
//		rsm.setMobileUnitsPath("insert_mobile_units.php");
//		rsm.setShimmerMetadataPath("insert_shimmer_metadata.php");
//		rsm.setShimmerSignalsPath("insert_shimmer_signals.php");
//		rsm.setShimmerUnitsPath("insert_shimmer_units.php");
//		rsm.setLastIDPath("get_last_ID.php");
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		view = LayoutInflater.from(getActivity()).inflate(R.layout.remotestorage, null);
		wifi = (ToggleButton) view.findViewById(R.id.WifiToggleButton);	
		listDevices = (ListView) view.findViewById(R.id.list_devices_server);
		
		Hashtable<String, Integer> devicesStates = cm.getCurrentDevicesState();
		Set<String> nameDevices = devicesStates.keySet();
		for(String key: nameDevices){
			Device d;
			if(cm.getDevice(key).getClass() == DeviceShimmer.class)
				d = new Device(key, TypeDevice.Shimmer, State.CONNECTED);
			else
				d = new Device(key, TypeDevice.Mobile, State.CONNECTED);
			devices.add(d);
		}
		
	    listDevices.setAdapter(devices);

		listDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				nameDeviceSelected = ((Device) devices.getItem(position)).name;
				typeDeviceSelected = ((Device) devices.getItem(position)).typeDevice;
				
				if(!nameDeviceSelected.equals("No device")){
					
					showDialog(TABLES_DIALOG, nameDeviceSelected);
					
				}
			}	
		});
		
		wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(wifi.isChecked())
					sysm.setWifiEnabled(true, getActivity());
				else
					sysm.setWifiEnabled(false, getActivity());
			}
		});
		
		return view;
	}
	
	void showDialog(int id, String nameDeviceSelected){
		
		Dialog alertDialog = new Dialog(getActivity());
		final CharSequence[] items = {" Units "," Metadata "," Signals "};
		final String nameDeviceToUpload = nameDeviceSelected;
		
		switch (id) {
				
		case TABLES_DIALOG:
			
			final ArrayList selectedItems = new ArrayList();
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Select the tables to be upload");
			builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
	
					Log.d("WHICH", String.valueOf(which));
					if (isChecked){
						
						selectedItems.add(items[which]);
					}
					
					else{
					
						selectedItems.remove(items[which]);					
					}
				}
			})
			
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
	
					Log.d("ITEMS", "Device: " + nameDeviceToUpload);
					
					//Upload Units
					if(selectedItems.contains(items[0])){
						rsm.uploadUnitsTable(nameDeviceToUpload);
						Log.d("ITEMS", "Dentro de units");					
					}
					
					//Upload Metadata
					if(selectedItems.contains(items[1])){
						rsm.uploadMetadataTable(nameDeviceToUpload);
						Log.d("ITEMS", "Dentro de metadata");
					}
					//Upload Signals
					if(selectedItems.contains(items[2])){
						rsm.uploadSignalsTable(nameDeviceToUpload);
						Log.d("ITEMS", "Dentro de signals");
					}
					Toast.makeText(getActivity(), "Uploading data...", Toast.LENGTH_LONG).show();
					mHandler.post(isFinished);
				}
				
			})
			
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			alertDialog = builder.create();
			alertDialog.show();
			
			break;
		}
		
	}
	
	Runnable isFinished = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(rsm.isProcessFinished()){
				Toast.makeText(getActivity(), "Uploading completed", Toast.LENGTH_LONG).show();
			}
			else
				mHandler.postDelayed(this, 1000);
		}
	};
}
