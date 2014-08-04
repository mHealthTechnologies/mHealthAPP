package com.mHealthDroid.mhealthapp;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.mHealthDroid.mhealthpp.R;

import weka.classifiers.Classifier;

import communicationManager.CommunicationManager;
import communicationManager.dataStructure.ObjectData.SensorType;
import dataprocessingManager.DataProcessingManager;
import dataprocessingManager.featuresExtraction.FeaturesExtraction.FeatureType;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityRecognitionFragment extends Fragment{

	
	public CommunicationManager cm;
	public DataProcessingManager dpm;
	public Button start;
	public Hashtable<String, Integer> states;
	public List<String> listDevices;
	public Spinner chestSpinner, ankleSpinner, wristSpinner;
	public String deviceChest, deviceAnkle, deviceWrist;
	public ArrayList<Pair<ArrayList<SensorType>, String>> sensorsAndDevices;
	public ArrayList<SensorType> sensors;
	public int window;
	public ImageView photoActivity;
	public TextView textActivity;
	public ProgressDialog progressDialog;
	public SharedPreferences myprefs;
	
	public static String IS_RECOGNIZING = "isRecognizing";
	public static String DEVICE_CHEST = "deviceChest";
	public static String DEVICE_ANKLE = "deviceAnkle";
	public static String DEVICE_WRIST = "deviceWrist";
	public boolean isRecognizing;
	
	
	
	public ProgressDialogFragment dialog;
	
	public ActivityRecognitionFragment() {
		// TODO Auto-generated constructor stub
		super();
		
	}
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		cm = CommunicationManager.getInstance();
		dpm = DataProcessingManager.getInstance();
		states = new Hashtable<String, Integer>();
		sensorsAndDevices = new ArrayList<Pair<ArrayList<SensorType>,String>>();
		sensors = new ArrayList<SensorType>();
		window = 2;
		isRecognizing = false;
		listDevices = new ArrayList<String>();
		dialog = new ProgressDialogFragment();
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		myprefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		isRecognizing = myprefs.getBoolean(IS_RECOGNIZING, false);
		
		states = cm.getCurrentDevicesState();
		if(cm.setKey()!=null)
			listDevices.addAll(cm.setKey());
		else
			listDevices.add("No device");
		
		LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.activity_recognition, null);
			
		photoActivity = (ImageView) mLinearLayout.findViewById(R.id.imageView1);
		textActivity = (TextView) mLinearLayout.findViewById(R.id.textView1);
		
		chestSpinner = (Spinner) mLinearLayout.findViewById(R.id.chestSpinner);
		ArrayAdapter<String> chestAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_view, listDevices);
		chestAdapter.setDropDownViewResource(R.layout.spinner_view);
		chestSpinner.setAdapter(chestAdapter);
		
		ankleSpinner = (Spinner) mLinearLayout.findViewById(R.id.ankleSpinner);
		ArrayAdapter<String> ankleAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_view, listDevices);
		ankleAdapter.setDropDownViewResource(R.layout.spinner_view);
		ankleSpinner.setAdapter(ankleAdapter);
		
		wristSpinner = (Spinner) mLinearLayout.findViewById(R.id.wristSpinner);
		ArrayAdapter<String> wristAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_view, listDevices);
		wristAdapter.setDropDownViewResource(R.layout.spinner_view);
		wristSpinner.setAdapter(wristAdapter);
		
		start = (Button) mLinearLayout.findViewById(R.id.startButton);
		
		if(isRecognizing){
			deviceChest = myprefs.getString(DEVICE_CHEST, "");
			deviceAnkle = myprefs.getString(DEVICE_ANKLE, "");
			deviceWrist = myprefs.getString(DEVICE_WRIST, "");
			chestSpinner.setSelection(chestAdapter.getPosition(deviceChest));
			ankleSpinner.setSelection(ankleAdapter.getPosition(deviceAnkle));
			wristSpinner.setSelection(wristAdapter.getPosition(deviceWrist));
			dpm.setInferenceOnlineHandler(mHanlder);
			start.setBackgroundResource(R.drawable.stop_selector);
			start.setText(R.string.stopRecognition);
		}	

		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!isRecognizing){
					Set<String> keys = states.keySet();
					int cont = 0;
					for(String k: keys)
						if(states.get(k)==2)
							cont++;
					
					if(cont==0){
						sendErrorDialog("There are no devices streaming");
						return;
					}
					
//					dialog.setShowsDialog(true);
//					dialog.show(getFragmentManager(), getTag());
//					progressDialog = ProgressDialog.show(getActivity(), "", "Loading model...", true);
//					progressDialog.setCancelable(false);
					
					
					//check if the file is already writen in the external memory
					File fileArff = new File(Environment.getExternalStorageDirectory()+"/ActivityDetector/activities.arff");
					if(!fileArff.exists())     
						copyArffToSdCard(getActivity());
										
					dpm.readFile(Environment.getExternalStorageDirectory(), "/ActivityDetector/activities.arff");
					dpm.setTrainInstances();
					
					File fileModel = new File(Environment.getExternalStorageDirectory()+"/ActivityDetector/modeloentrenado.model");
					if(!fileModel.exists())
						try {
							copyModelToSdCard(getActivity());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
					try{
						ObjectInputStream ois=new ObjectInputStream(new FileInputStream(Environment.getExternalStorageDirectory()+"/ActivityDetector/modeloentrenado.model")); 
						dpm.loadModel((Classifier)ois.readObject()); 
						ois.close();
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					
					deviceChest = (String) chestSpinner.getSelectedItem();
					deviceAnkle = (String) ankleSpinner.getSelectedItem();
					deviceWrist = (String) wristSpinner.getSelectedItem();
					
					setDevicesAndFeatures(deviceChest, deviceAnkle, deviceWrist);
					dpm.inferenceOnline(window, sensorsAndDevices, null, 0, 1, mHanlder);
					
//					progressDialog.dismiss();
//					dialog.dismiss();
					isRecognizing = true;
					start.setBackgroundResource(R.drawable.stop_selector);
					start.setText(R.string.stopRecognition);
				}
				else{
					dpm.endInferenceOnline();
					isRecognizing = false;
					start.setBackgroundResource(R.drawable.start_selector);
					start.setText(R.string.startRecognition);
					textActivity.setText(R.string.noActivity);
					photoActivity.setImageResource(R.drawable.noactivity);
				}
			}
		});
		
		return mLinearLayout;
	}

	public final Handler mHanlder = new Handler(){
		public void handleMessage(Message msg) {
			Log.d("handler", "llega el mensaje con obj = "+msg.obj);
			if(msg.what==0){
				Log.d("handler", "dentro del if");
				Double activity = (Double) msg.obj;
				
				if(activity == 0){ //de pie
					photoActivity.setImageResource(R.drawable.standing);
					textActivity.setText(R.string.standing);
					return;
				}
				if(activity == 1){ //sentado
					photoActivity.setImageResource(R.drawable.sitting);
					textActivity.setText(R.string.sitting);
					return;
				}
				if(activity == 2){ //tumbado
					photoActivity.setImageResource(R.drawable.lying);
					textActivity.setText(R.string.lying);
					return;
				}
				if(activity == 3){ //andando
					photoActivity.setImageResource(R.drawable.walking);
					textActivity.setText(R.string.walking);
					return;
				}
				if(activity == 4){ //subiendo escaleras
					photoActivity.setImageResource(R.drawable.climbing);
					textActivity.setText(R.string.climbing);
					return;
				}
				if(activity == 5){ //agachandose
					photoActivity.setImageResource(R.drawable.waist);
					textActivity.setText(R.string.waist);
					return;
				}
				if(activity == 6){ //moviendo los brazos arriba y abajo
					photoActivity.setImageResource(R.drawable.frontal);
					textActivity.setText(R.string.frontal);
					return;
				}
				if(activity == 7){ //agacharse doblando las rodillas
					photoActivity.setImageResource(R.drawable.knees);
					textActivity.setText(R.string.knees);
					return;
				}
				if(activity == 8){ //ir en bici
					photoActivity.setImageResource(R.drawable.cycling);
					textActivity.setText(R.string.cycling);
					return;
				}
				if(activity == 9){ //trotar
					photoActivity.setImageResource(R.drawable.jogging);
					textActivity.setText(R.string.jogging);
					return;
				}
				if(activity == 10){ //correr
					photoActivity.setImageResource(R.drawable.running);
					textActivity.setText(R.string.running);
					return;
				}
				if(activity == 11){ //saltar hacia delante y hacia atras
					photoActivity.setImageResource(R.drawable.jump);
					textActivity.setText(R.string.jump);
					return;
				}
			}
		}
	};
	


	public void copyArffToSdCard(Context ctx) {

	    InputStream is = ctx.getResources().openRawResource(R.raw.activities);
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr, 8192);
	    
	    File sdCard = Environment.getExternalStorageDirectory();
	    File directory = new File (sdCard.getAbsolutePath() +"/ActivityDetector");
	    directory.mkdirs();
	    File file = new File(directory, "activities.arff");
	    FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    OutputStreamWriter osw = new OutputStreamWriter(fOut);

	    try {
	        String text;
	        while (true) {
	            text = br.readLine();
	            if (text == null)
	                break;
	            text += "\n";
	            osw.write(text);
	        }
	        isr.close();
	        is.close();
	        br.close();
	        osw.flush();
            osw.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	
	public void copyModelToSdCard(Context ctx) throws IOException {

//	    InputStream is = ctx.getResources().openRawResource(R.raw.modeloentrenado);
//	    InputStreamReader isr = new InputStreamReader(is);
//	    BufferedReader br = new BufferedReader(isr, 8192);
//	    
//	    File sdCard = Environment.getExternalStorageDirectory();
//	    File directory = new File (sdCard.getAbsolutePath() +"/ActivityDetector");
//	    directory.mkdirs();
//	    File file = new File(directory, "modeloentrenado.model");
//	    FileOutputStream fOut = null;
//		try {
//			fOut = new FileOutputStream(file);
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	    OutputStreamWriter osw = new OutputStreamWriter(fOut);
//
//	    try {
//	        String text;
//	        while (true) {
//	            text = br.readLine();
//	            if (text == null)
//	                break;
//	            text += "\n";
//	            osw.write(text);
//	        }
//	        isr.close();
//	        is.close();
//	        br.close();
//	        osw.flush();
//            osw.close();
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
	    
	    
	    InputStream in = ctx.getResources().openRawResource(R.raw.modeloentrenado);
	    FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ActivityDetector/modeloentrenado.model");
	    byte[] buff = new byte[1024];
	    int read = 0;
	    try {
	        while ((read = in.read(buff)) > 0) {
	            out.write(buff, 0, read);
	        }
	    } finally {
	        in.close();
	        out.close();
	    }

	}
	
	public void sendErrorDialog(String message){
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		// set title
		alertDialogBuilder.setTitle("Recognition not possible");
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
	
	public void setDevicesAndFeatures(String chest, String ankle, String wrist){
		

		sensors.add(SensorType.ACCELEROMETER_X);
		sensors.add(SensorType.ACCELEROMETER_Y);
		sensors.add(SensorType.ACCELEROMETER_Z);
		sensorsAndDevices.add(new Pair<ArrayList<SensorType>, String>(sensors, chest));
		sensorsAndDevices.add(new Pair<ArrayList<SensorType>, String>(sensors, ankle));
		sensorsAndDevices.add(new Pair<ArrayList<SensorType>, String>(sensors, wrist));
		
		dpm.addFeature(chest, SensorType.ACCELEROMETER_X, FeatureType.MEAN);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Y, FeatureType.MEAN);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Z, FeatureType.MEAN);
		
		dpm.addFeature(chest, SensorType.ACCELEROMETER_X, FeatureType.STANDARD_DEVIATION);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Y, FeatureType.STANDARD_DEVIATION);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Z, FeatureType.STANDARD_DEVIATION);
		
		dpm.addFeature(chest, SensorType.ACCELEROMETER_X, FeatureType.MAXIMUM);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Y, FeatureType.MAXIMUM);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Z, FeatureType.MAXIMUM);
		
		dpm.addFeature(chest, SensorType.ACCELEROMETER_X, FeatureType.MINIMUM);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Y, FeatureType.MINIMUM);
		dpm.addFeature(chest, SensorType.ACCELEROMETER_Z, FeatureType.MINIMUM);
		
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_X, FeatureType.MEAN);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Y, FeatureType.MEAN);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Z, FeatureType.MEAN);
		
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_X, FeatureType.STANDARD_DEVIATION);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Y, FeatureType.STANDARD_DEVIATION);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Z, FeatureType.STANDARD_DEVIATION);
		
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_X, FeatureType.MAXIMUM);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Y, FeatureType.MAXIMUM);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Z, FeatureType.MAXIMUM);
		
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_X, FeatureType.MINIMUM);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Y, FeatureType.MINIMUM);
		dpm.addFeature(ankle, SensorType.ACCELEROMETER_Z, FeatureType.MINIMUM);
		
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_X, FeatureType.MEAN);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Y, FeatureType.MEAN);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Z, FeatureType.MEAN);
		
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_X, FeatureType.STANDARD_DEVIATION);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Y, FeatureType.STANDARD_DEVIATION);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Z, FeatureType.STANDARD_DEVIATION);
		
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_X, FeatureType.MAXIMUM);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Y, FeatureType.MAXIMUM);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Z, FeatureType.MAXIMUM);
		
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_X, FeatureType.MINIMUM);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Y, FeatureType.MINIMUM);
		dpm.addFeature(wrist, SensorType.ACCELEROMETER_Z, FeatureType.MINIMUM);
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		Editor mEditor = myprefs.edit();
		mEditor.putBoolean(IS_RECOGNIZING, isRecognizing);
		if(isRecognizing){
			mEditor.putString(DEVICE_CHEST, deviceChest);
			mEditor.putString(DEVICE_ANKLE, deviceAnkle);
			mEditor.putString(DEVICE_WRIST, deviceWrist);
		}
		mEditor.commit();
	}
}
