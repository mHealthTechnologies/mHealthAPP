package com.mHealthDroid.mhealthapp;

import com.mHealthDroid.mhealthpp.R;

import visualizationManager.VisualizationManager;
import communicationManager.CommunicationManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class GraphConfigurationActivity extends Activity{
	
	public CommunicationManager cm;
	public VisualizationManager vm;
	
	public double viewPort;
	public double maxY;
	public double minY;
	public boolean setViewPort;
	public boolean setYValues;
	public boolean showLegend;
	public int align;
	
	ToggleButton viewButton, coordinatesButton, legendButton;
	RadioGroup rg;
	EditText txtViewPort, txtMaxY, txtMinY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup the window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.graph_configuration);

		// Set result CANCELED in case the user backs out
		setResult(Activity.RESULT_CANCELED);	
		
		vm = VisualizationManager.getInstance();
		legendButton = (ToggleButton) findViewById(R.id.legendToggle);
		viewButton = (ToggleButton) findViewById(R.id.viewPortToggle);
		coordinatesButton = (ToggleButton) findViewById(R.id.yValuesToggle);
		rg = (RadioGroup) findViewById(R.id.legendRagdioGroup);
		txtViewPort = (EditText) findViewById(R.id.txt_viewPort);
		txtMaxY = (EditText) findViewById(R.id.txt_max);
		txtMinY = (EditText) findViewById(R.id.txt_min);
		
		Bundle extra = getIntent().getExtras();
		if(extra.getBoolean(VisualizationFragment.IS_CUSTOMIZED)){
			if(extra.getBoolean(VisualizationFragment.IS_ONLINE)){
				LinearLayout llCoordinates = (LinearLayout) findViewById(R.id.layoutYcoordinates);
				txtViewPort.setVisibility(View.GONE);
				llCoordinates.setVisibility(View.GONE);
			}
			else{
				if(extra.getBoolean(VisualizationFragment.SET_COORDINATES)){
					LinearLayout llCoordinates = (LinearLayout) findViewById(R.id.layoutYcoordinates);
					llCoordinates.setVisibility(View.VISIBLE);
					coordinatesButton.setChecked(true);
					maxY = extra.getDouble(VisualizationFragment.Y_MAX);
					minY = extra.getDouble(VisualizationFragment.Y_MIN);
					txtMaxY.setText(""+maxY);
					txtMinY.setText(""+minY);
				}
				if(extra.getBoolean(VisualizationFragment.SET_VIEW_PORT)){
					viewButton.setChecked(true);
					txtViewPort.setVisibility(View.VISIBLE);
				}
			}
		}
		viewPort = extra.getDouble(VisualizationFragment.VIEW_PORT);
		txtViewPort.setText(""+viewPort);
		showLegend = extra.getBoolean(VisualizationFragment.SET_LEGEND);	
		legendButton.setChecked(showLegend);
		if(showLegend){
			rg.setVisibility(View.VISIBLE);
			align = extra.getInt(VisualizationFragment.ALIGN);
			switch(align){
			case 1:
				rg.check(R.id.radio_top);
			break;
			case 2:
				rg.check(R.id.radio_middle);
			break;
			case 3:
				rg.check(R.id.radio_bottom);
			break;
			}
		}
		
		viewButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.txt_viewPort);
				if (isChecked) {
					// The toggle is enabled
		            et.setVisibility(View.VISIBLE);
		        } else {
		            // The toggle is disabled
		            et.setVisibility(View.GONE);
		        }
			}
		});
		
		coordinatesButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				LinearLayout ll = (LinearLayout) findViewById(R.id.layoutYcoordinates);
				if (isChecked) {
					// The toggle is enabled
		            ll.setVisibility(View.VISIBLE);
		        } else {
		            // The toggle is disabled
		            ll.setVisibility(View.GONE);
		        }
			}
		});
		
		legendButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				RadioGroup rg = (RadioGroup) findViewById(R.id.legendRagdioGroup);
				if (isChecked) {
					// The toggle is enabled
		            rg.setVisibility(View.VISIBLE);
		        } else {
		            // The toggle is disabled
		            rg.setVisibility(View.GONE);
		        }
			}
		});
		
		Button applyButton = (Button) findViewById(R.id.button_apply2);
		applyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(viewButton.isChecked()){
					setViewPort = true;
					EditText textViewPort = (EditText) findViewById(R.id.txt_viewPort);
					if(textViewPort.getText().toString().equals(""))
						setViewPort = false;
					else
						viewPort = Double.valueOf(textViewPort.getText().toString());
				}
				else
					setViewPort = false;
				if(coordinatesButton.isChecked()){
					setYValues = true;
					EditText textMaxY = (EditText) findViewById(R.id.txt_max);
					EditText textMinY = (EditText) findViewById(R.id.txt_min);
					if(textMaxY.getText().toString().equals(""))
						setYValues = false;
					else
						maxY = Double.valueOf(textMaxY.getText().toString());
					if(textMinY.getText().toString().equals(""))
						setYValues = false;
					else
						minY = Double.valueOf(textMinY.getText().toString());
				}
				else
					setYValues = false;
				if(legendButton.isChecked()){
					showLegend = true;
					switch(rg.getCheckedRadioButtonId()){
						case R.id.radio_top:
							align = 1;
						break;
						case R.id.radio_middle:
							align = 2;
						break;
						case R.id.radio_bottom:
							align = 3;
						break;
						default:
							showLegend = false;
						break;
					}
				}
				else
					showLegend = false;
				
			
				Intent intent = new Intent();
//				intent.putExtra(VisualizationFragment.IS_CUSTOMIZED, true);
				intent.putExtra(VisualizationFragment.SET_VIEW_PORT, setViewPort);
				intent.putExtra(VisualizationFragment.VIEW_PORT, viewPort);
				intent.putExtra(VisualizationFragment.SET_COORDINATES, setYValues);
				intent.putExtra(VisualizationFragment.Y_MAX, maxY);
				intent.putExtra(VisualizationFragment.Y_MIN, minY);
				intent.putExtra(VisualizationFragment.SET_LEGEND, showLegend);
				intent.putExtra(VisualizationFragment.ALIGN, align);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

}
