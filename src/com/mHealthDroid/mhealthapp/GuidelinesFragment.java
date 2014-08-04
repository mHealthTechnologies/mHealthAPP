package com.mHealthDroid.mhealthapp;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import systemManager.SystemManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GuidelinesFragment extends YouTubePlayerSupportFragment {

	SystemManager sm;
	YouTubePlayerView youTubeView; 
	String URL_VIDEO = "CaA-k1l0xa4";
	String KEY_DEVELOPER = "AIzaSyBIIs0u0NXhsZguv8nCNvSzUmflTt7K1Ek";
	static final int REQUEST_YOUTUBE_ACTIVITY = 1;
	
	public GuidelinesFragment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent guideIntent = new Intent(getActivity(), YoutubeActivity.class);
		startActivityForResult(guideIntent, REQUEST_YOUTUBE_ACTIVITY);

	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode){
			case REQUEST_YOUTUBE_ACTIVITY:
				if(resultCode == Activity.RESULT_CANCELED){
					Intent intent = new Intent(getActivity(), MainActivity.class);
					startActivity(intent);
				}
					
			break;
		}
	}
}
