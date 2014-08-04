package com.mHealthDroid.mhealthapp;

import com.mHealthDroid.mhealthpp.R;

import communicationManager.CommunicationManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DeviceMenuActivity extends Activity{

	String nameDevice;
	CommunicationManager cm;
	
	static final int REQUEST_CONFIRMATION = 1;
	static final int REQUEST_SENSORS = 2;
	static final int REQUEST_CONFIGURATION = 3;
	public int typeDevice;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.devices_menu);
		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		cm = CommunicationManager.getInstance();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    nameDevice = extras.getString(ConnectFragment.NAME_DEVICE);
		    typeDevice = extras.getInt(ConnectFragment.TYPE_DEVICE);
		}
		
		Button connectDisconnectButton = (Button) findViewById(R.id.button_connect_disconnect);
		if(cm.isConnected(nameDevice))
			connectDisconnectButton.setText(R.string.disconnect_device);
		else
			connectDisconnectButton.setText(R.string.connect_device);
		
		if(typeDevice == ConnectFragment.DEVICE_MOBILE)
			connectDisconnectButton.setVisibility(Button.GONE);
		else
			connectDisconnectButton.setVisibility(Button.VISIBLE);
		
		connectDisconnectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button b = (Button) v;
				b.setBackgroundColor(getResources().getColor(R.color.DarkGray));
				if(cm.isConnected(nameDevice))
					cm.disconnect(nameDevice);
				else
					cm.connect(nameDevice);
				b.setBackgroundResource(R.drawable.button_menus);
				finish();
			}
		});
		
		Button streamingButton = (Button) findViewById(R.id.button_streaming);
		if(cm.isStreaming(nameDevice))
			streamingButton.setText(R.string.stop_streaming);
		else
			streamingButton.setText(R.string.start_streaming);
		
		streamingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Button b = (Button) v;
				b.setBackgroundColor(getResources().getColor(R.color.DarkGray));
				if(cm.isStreaming(nameDevice))
					cm.stopStreaming(nameDevice);
				else
					cm.startStreaming(nameDevice);
				b.setBackgroundResource(R.drawable.button_menus);
				finish();
			}
		});
		
		Button sensorsButton = (Button) findViewById(R.id.button_device_sensors);
		if(cm.isStreaming(nameDevice))
			sensorsButton.setVisibility(Button.GONE);
		else
			sensorsButton.setVisibility(Button.VISIBLE);
		
		sensorsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(getApplicationContext(), SensorListActivity.class);
				serverIntent.putExtra(ConnectFragment.TYPE_DEVICE, typeDevice);
				serverIntent.putExtra(ConnectFragment.NAME_DEVICE, nameDevice);
				startActivityForResult(serverIntent, REQUEST_SENSORS);
			}
		});
		
		Button configurationButton = (Button) findViewById(R.id.button_device_configuration);
		if(cm.isStreaming(nameDevice))
			configurationButton.setVisibility(Button.GONE);
		else
			configurationButton.setVisibility(Button.VISIBLE);
		configurationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(getApplicationContext(), DeviceConfigurationActivity.class);
				serverIntent.putExtra(ConnectFragment.TYPE_DEVICE, typeDevice);
				serverIntent.putExtra(ConnectFragment.NAME_DEVICE, nameDevice);
				startActivityForResult(serverIntent, REQUEST_CONFIGURATION);
//				startActivity(serverIntent);
			}
		});
		
		Button removeButton = (Button) findViewById(R.id.button_device_remove);
		removeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent serverIntent = new Intent(getApplicationContext(), ConfirmationActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONFIRMATION);
			}
		});
	}
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		switch (requestCode) {
			case REQUEST_CONFIRMATION:
				if (resultCode == Activity.RESULT_OK) {
					if(cm.isStreaming(nameDevice))
                    	cm.stopStreaming(nameDevice);
                    cm.disconnect(nameDevice);
                    cm.removeDevice(nameDevice);
                    setResult(RESULT_OK);
                    finish();
				}
			break;
			case REQUEST_SENSORS:
				if(resultCode == Activity.RESULT_OK)
					finish();
			break;
			case REQUEST_CONFIGURATION:
				if(resultCode == Activity.RESULT_OK)
					finish();
			break;
		}
	}
	
	
}
