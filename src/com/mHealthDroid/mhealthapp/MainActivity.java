package com.mHealthDroid.mhealthapp;

import java.util.Calendar;
import java.util.Vector;

import com.mHealthDroid.mhealthpp.R;

import systemManager.SystemManager;

import communicationManager.CommunicationManager;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public SharedPreferences myprefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myprefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section4)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section5)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section6)
				.setTabListener(this));
	}	
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
////		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		
		switch(tab.getPosition()){
			case 0: // Connect tab
				Fragment connectFragment = new ConnectFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, connectFragment).commit();
			break;
			case 1: // Visualization tab
				Fragment visualizationFragment = new VisualizationFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, visualizationFragment).commit();
			break;
			case 2: // Activity tab
				Fragment recongnitionFragment = new ActivityRecognitionFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, recongnitionFragment).commit();
			break;			
			case 3: // Notifications tab
				Fragment NotificationsFragment = new NotificationsFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, NotificationsFragment).commit();
			break;
			case 4: // Guideline tab
				Fragment guidelinesFragment = new GuidelinesFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, guidelinesFragment).commit();
			break;
			case 5: // Remote Storage
				Fragment RemoteStorageFragment2 = new RemoteStorageFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.container, RemoteStorageFragment2).commit();
			break;
		}
		
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
