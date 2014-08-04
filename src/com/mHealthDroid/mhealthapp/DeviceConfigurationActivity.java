package com.mHealthDroid.mhealthapp;

import java.util.ArrayList;
import java.util.List;

import com.mHealthDroid.mhealthpp.R;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectMetadata.FormatType;
import communicationManager.datareceiver.DeviceShimmer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class DeviceConfigurationActivity extends Activity{

	CommunicationManager cm;
	String nameDevice;
	int typeDevice;
	boolean storeData;
	int sampleRate;
	FormatType format;
	
	ToggleButton toggleStore;
	RadioGroup radioFormat;
	Spinner spinnerRate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cm = CommunicationManager.getInstance();

		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_configuration);
		// Set result CANCELED incase the user backs out
		setResult(Activity.RESULT_CANCELED);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    nameDevice = extras.getString(ConnectFragment.NAME_DEVICE);
		    typeDevice = extras.getInt(ConnectFragment.TYPE_DEVICE);
		}
		storeData = cm.getStoreData(nameDevice);
		sampleRate = (int) cm.getRate(nameDevice);
		
		toggleStore = (ToggleButton) findViewById(R.id.storeToggle);
		toggleStore.setChecked(storeData);
		
		radioFormat = (RadioGroup) findViewById(R.id.formatRagdioGroup);
		spinnerRate = (Spinner) findViewById(R.id.spinnerRate);
		
		if(typeDevice == ConnectFragment.DEVICE_SHIMMER){
			setSpinnerShimmer(spinnerRate);
			LinearLayout formatLayout = (LinearLayout) findViewById(R.id.layout_format);
			formatLayout.setVisibility(View.VISIBLE);
			format = ((DeviceShimmer) cm.getDevice(nameDevice)).getFormat();
			if(format == FormatType.CALIBRATED)
				radioFormat.check(R.id.radio_calibrated);
			else
				radioFormat.check(R.id.radio_uncalibrated);
			spinnerRate.setSelection(rateToPositionShimmer());
		}
		else{
			setSpinnerMobile(spinnerRate);
			LinearLayout formatLayout = (LinearLayout) findViewById(R.id.layout_format);
			formatLayout.setVisibility(View.GONE);
			spinnerRate.setSelection(rateToPositionMobile());
		}

		Button applyButton = (Button) findViewById(R.id.button_apply);
		applyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toggleStore.isChecked())
					cm.setStoreData(nameDevice, true);
				else
					cm.setStoreData(nameDevice, false);
				
				if(typeDevice == ConnectFragment.DEVICE_SHIMMER)
					if(radioFormat.getCheckedRadioButtonId() == R.id.radio_calibrated)
						((DeviceShimmer) cm.getDevice(nameDevice)).setFormat(FormatType.CALIBRATED);
					else
						((DeviceShimmer) cm.getDevice(nameDevice)).setFormat(FormatType.UNCALIBRATED);
				
				double rate;
				if(typeDevice == ConnectFragment.DEVICE_SHIMMER)
					rate = positionToRateShimmer(spinnerRate.getSelectedItemPosition());
				else
					rate = spinnerRate.getSelectedItemPosition();
				
				cm.setRate(nameDevice, rate);
				
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	public void setSpinnerShimmer(Spinner spinner){
		
		List<String> list = new ArrayList<String>();
		list.add("10.2");
		list.add("51.2");
		list.add("102.4");
		list.add("128");
		list.add("170.7");
		list.add("204.8");
		list.add("256");
		list.add("512");
		list.add("1024");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	
	public void setSpinnerMobile(Spinner spinner){
		
		List<String> list = new ArrayList<String>();
		list.add("Normal (~5Hz)");
		list.add("UI (~16.7Hz)");
		list.add("Game (~50Hz)");
		list.add("Fastest (~100Hz)");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	
	public int rateToPositionShimmer(){
		
		if(sampleRate==10)
			return 0;
		if(sampleRate==51)
			return 1;
		if(sampleRate==102)
			return 2;
		if(sampleRate==128)
			return 3;
		if(sampleRate==170)
			return 4;
		if(sampleRate==204)
			return 5;
		if(sampleRate==256)
			return 6;
		if(sampleRate==512)
			return 7;
		if(sampleRate==1024)
			return 8;
		
		return 0;
	}
	
	public int rateToPositionMobile(){
		
		if(sampleRate==5)
			return 0;
		if(sampleRate==16)
			return 1;
		if(sampleRate==50)
			return 2;
		if(sampleRate==100)
			return 3;
		
		return 0;
	}
	
	public double positionToRateShimmer(int position){
		
		if(position==0)
			return (double) 10.2;
		if(position==1)
			return (double) 51.2;
		if(position==2)
			return (double) 102.4;
		if(position==3)
			return (double) 128;
		if(position==4)
			return (double) 170.7;
		if(position==5)
			return (double) 204.8;
		if(position==6)
			return (double) 256;
		if(position==7)
			return (double) 512;
		if(position==8)
			return (double) 1024;
		
		return (double) 0.0;
	}
	
}
