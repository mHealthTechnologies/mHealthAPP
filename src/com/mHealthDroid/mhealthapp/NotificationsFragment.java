package com.mHealthDroid.mhealthapp;

import java.util.Calendar;

import com.mHealthDroid.mhealthpp.R;

import systemManager.SystemManager;
import android.support.v4.app.Fragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class NotificationsFragment extends Fragment {

	SystemManager sm;
	EditText notificationTitleField;
	EditText notificationDescription;
	Spinner ringtoneSpinner;
	CheckBox recommendationsCheckBox;
	CheckBox timeCheckBox;
	Uri soundUri; 
	View layoutView;
	int hour;
	int minute;
	int day;
	int month;
	int year;
	static final int TIME_DIALOG_ID = 1000;
	static final int DATE_DIALOG_ID = 1001;

	
	public NotificationsFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sm = SystemManager.getInstance();
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
			layoutView = inflater.inflate(R.layout.notifications, container, false);
	//		LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.connect, null);	
				
			timeCheckBox = (CheckBox) layoutView.findViewById(R.id.timeCheckBox);
			timeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		        @Override
		        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		        	if(isChecked){
						createDialog(TIME_DIALOG_ID).show();
						createDialog(DATE_DIALOG_ID).show();
		        	}
		        }
		    });
			
//			setTime.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					createDialog(TIME_DIALOG_ID).show();
//					
//				}
//			});
			
			Button newNotificationButton = (Button) layoutView.findViewById(R.id.newNotificationButton);
			
			newNotificationButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					notificationTitleField = (EditText) layoutView.findViewById(R.id.EditNotificationTitle);  
					String title = notificationTitleField.getText().toString();  
					notificationDescription = (EditText) layoutView.findViewById(R.id.EditNotificationText);  
					String notificationText = notificationDescription.getText().toString();  
					ringtoneSpinner = (Spinner) layoutView.findViewById(R.id.SpinnerFeedbackType);  
					String soundType = ringtoneSpinner.getSelectedItem().toString();
					recommendationsCheckBox = (CheckBox) layoutView.findViewById(R.id.youtubeCheckBox);  
					boolean launchYoutube = recommendationsCheckBox.isChecked();
					timeCheckBox = (CheckBox) layoutView.findViewById(R.id.timeCheckBox);
					boolean timeSet = timeCheckBox.isChecked();
						
					//Sound to be used (if any selected)
					if(!soundType.equals("None")){
						if(soundType.equals("Alarm Sound")) soundUri =
								RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
						else if(soundType.equals("Ringtone Sound")) soundUri =
								RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
						else if(soundType.equals("Notification Sound")) soundUri =
								RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					}
					
					//Simple Notification
					if (!launchYoutube && !timeSet && soundType.equals("None")){	
						sm.sendSimpleNotification(title, notificationText, 0, R.drawable.ic_launcher, getActivity());
					}
					
					if(launchYoutube && !timeSet){
						//Complex Notification
						if(soundType.equals("None")){
							Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
							PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
							int flags = Notification.FLAG_AUTO_CANCEL;
							sm.sendComplexNotification(title, notificationText, 1, R.drawable.ic_launcher, flags,
							pendingIntent, getActivity());							
						}
						
						//Custom Notification
						else{
							
							Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
							PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
							int flags = Notification.FLAG_AUTO_CANCEL;
							sm.sendComplexNotificationCustomSound(title,notificationText, 2, R.drawable.ic_launcher,
									flags, pendingIntent, soundUri, getActivity());
						}	
					}
					
					//Custom Notification
					if(!launchYoutube && !timeSet && !soundType.equals("None")){
						int flags = Notification.FLAG_AUTO_CANCEL;
						sm.sendComplexNotificationCustomSound(title,notificationText, 3, R.drawable.ic_launcher,
								flags, null, soundUri, getActivity());							
					}
					
					//Scheduled Notifications to be used
					if(timeSet){
						
						Toast.makeText(getActivity(), "Notification Scheduled for: " + day +
								"/" + month + "/" + year + " " + hour + ":" + minute, Toast.LENGTH_LONG).show();

						
						Calendar notificationDate = Calendar.getInstance();
						notificationDate.set(Calendar.YEAR, year);
						notificationDate.set(Calendar.MONTH, month);
						notificationDate.set(Calendar.DAY_OF_MONTH, day);
						notificationDate.set(Calendar.HOUR_OF_DAY, hour);	
						notificationDate.set(Calendar.MINUTE, minute);
						
						//Simple Scheduled Notification
						if (!launchYoutube && soundType.equals("None")){	
							sm.scheduledSimpleNotification(title, notificationText, 4, R.drawable.ic_launcher, getActivity(),
									notificationDate);
						}
						
						if(launchYoutube){
							//Complex Scheduled Notification
							if(soundType.equals("None")){
								Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
								PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
								int flags = Notification.FLAG_AUTO_CANCEL;
								sm.scheduledComplexNotification(title, notificationText, 5, R.drawable.ic_launcher, flags,
										pendingIntent, getActivity(), notificationDate);
							}
							
							//Custom Scheduled Notification
							else{		
								Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
								PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
								int flags = Notification.FLAG_AUTO_CANCEL;
								sm.scheduledComplexNotificationCustomSound(title,notificationText, 6, R.drawable.ic_launcher,
										flags, pendingIntent, soundUri, getActivity(), notificationDate);
							}	
						}
						
						//Custom Scheduled Notification
						if(!launchYoutube &&  !soundType.equals("None")){
							int flags = Notification.FLAG_AUTO_CANCEL;
							sm.scheduledComplexNotificationCustomSound(title,notificationText, 7, R.drawable.ic_launcher,
									flags, null, soundUri, getActivity(), notificationDate);							
						}
						
					}
				}
			});
			
			return layoutView;	
	}
	
	 public Dialog createDialog(int id) {
         switch (id) {
         case TIME_DIALOG_ID:
         
        	 Calendar date = Calendar.getInstance();
        	 hour = date.get(Calendar.HOUR_OF_DAY);
        	 minute = date.get(Calendar.MINUTE);
             // set time picker as current time
             return new TimePickerDialog(getActivity(), timePickerListener, hour, minute, true);
         
         case DATE_DIALOG_ID:

        	 Calendar date2 = Calendar.getInstance();
        	 month = date2.get(Calendar.MONTH);
        	 day = date2.get(Calendar.DAY_OF_MONTH);
        	 year = date2.get(Calendar.YEAR);
        	 //Set date picker as current date
        	 return new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
        	 
         }
         return null;
     }
	 
	 private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
         @Override
         public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
             // TODO Auto-generated method stub
             hour   = hourOfDay;
             minute = minutes;
             //Toast.makeText(getActivity(), hour  + " " +  minute, Toast.LENGTH_SHORT).show();
          }
     };
     
     private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int yearPicked, int monthOfYearPicked,
				int dayOfMonthPicked) {
			
			year = yearPicked;
			day = dayOfMonthPicked;
			month = monthOfYearPicked;
            //Toast.makeText(getActivity(), day  + " " +  month + " " + year, Toast.LENGTH_SHORT).show();
		}
	};
}


//notificationSimple.setOnClickListener(new OnClickListener() {
//	@Override
//	public void onClick(View v) {
//		Toast.makeText(getActivity(), "HOLA", Toast.LENGTH_LONG).show();				
//		sm.sendSimpleNotification("TITULO", "Esto es una notification simple", 1, R.drawable.ic_launcher, getActivity());
//	}
//});
//
//Button notificationComplex = (Button) view.findViewById(R.id.notificationComplex);
//notificationComplex.setOnClickListener(new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		
//		Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
//		int flags = Notification.FLAG_AUTO_CANCEL;
//		sm.sendComplexNotification("TITULO2", "Esto es una notificacion complex", 2, R.drawable.ic_launcher, flags,
//		pendingIntent, getActivity());
//	}
//});
// 
//Button notificationCustom = (Button) view.findViewById(R.id.notificationCustom);
//notificationCustom.setOnClickListener(new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
//		int flags = Notification.FLAG_AUTO_CANCEL;
//		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		sm.sendComplexNotificationCustomSound("TITULO3",
//				"Esto es una notificacion custom", 3, R.drawable.ic_launcher, flags, pendingIntent, soundUri,
//				getActivity());
//	}
//});
//
//Button notificationSimpleSchedule = (Button) view.findViewById(R.id.notificationSimpleSchedule);
//
//notificationSimpleSchedule.setOnClickListener(new OnClickListener() {
//	@Override
//	public void onClick(View v) {
//	
//		
//		Calendar date = Calendar.getInstance();
//		Log.d("fecha", date.toString());
//		sm.scheduledSimpleNotification("notificacion", "probando notificaciones programadas", 
//				4, R.drawable.ic_launcher, getActivity(), date);
//	}
//});
//
//Button notificationComplexSchedule = (Button) view.findViewById(R.id.notificationComplexSchedule);
//notificationComplexSchedule.setOnClickListener(new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		
//		Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
//		int flags = Notification.FLAG_AUTO_CANCEL;
//		Calendar date = Calendar.getInstance();
//		date.add(Calendar.SECOND, 5);
//		sm.scheduledComplexNotification("TITULO2", "Esto es una notificacion complex", 2, R.drawable.ic_launcher, flags,
//		pendingIntent, getActivity(), date);
//
//
//	}
//});
// 
//Button notificationCustomShedule = (Button) view.findViewById(R.id.notificationCustomSchedule);
//notificationCustomShedule.setOnClickListener(new OnClickListener() {
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, guideIntent, 0);
//		int flags = Notification.FLAG_AUTO_CANCEL;
//		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		sm.sendComplexNotificationCustomSound("TITULO3",
//				"Esto es una notificacion custom", 3, R.drawable.ic_launcher, flags, pendingIntent, soundUri,
//				getActivity());
//	}
//});
