package com.mHealthDroid.mhealthapp;

import systemManager.SystemManager;
import systemManager.guidelines.youtube.Youtube;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mHealthDroid.mhealthpp.R;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener  {

	SystemManager sm;
	YouTubePlayerView youTubeView; 
	YouTubePlayer player;
	String URL_VIDEO = "CaA-k1l0xa4";
	String KEY_DEVELOPER = "AIzaSyBIIs0u0NXhsZguv8nCNvSzUmflTt7K1Ek";
	YouTubePlayerView youtubePlayerView;
	ListView videosListView;
	int height = 0;
	ToggleButton listExtendButton;
	ToggleButton playerExtendButton;
	Button changeChannelButton;
	Youtube youtube;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//Set of view where videos and the listView (is there is any) will appear
		setContentView(R.layout.youtube);		
			
		player = null;
		
		//Object View for the youtubePlayerView inside the current layout View
		youtubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youtubePlayerView.initialize(KEY_DEVELOPER, this);
		
		//Youtube class needs context and a youtubePlayerView object
		sm = SystemManager.getInstance();
		youtube = sm.getYoutubePlayer(getApplicationContext(), youtubePlayerView);

			
		//To display only a single video, there is no need of using a listView
		//youtube.reproduceSingleVideoMode(URL_VIDEO);
		
		//If a list of videos is needed, its necessary to provide the playlist ID and a layout of every entry
		// in the playlist
		videosListView = (ListView) findViewById(R.id.listListView);
	
		youtube.reproducePlaylistMode(videosListView, R.layout.entry, R.id.textView_superior,
				R.id.textView_inferior, R.id.imageView_imagen, "PLOhl4anP1Mp3FjD_g3KOEcYWGRrPZPTI0");
		
		listExtendButton =  (ToggleButton) findViewById(R.id.listExtendButton);		
		listExtendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(listExtendButton.isChecked() && playerExtendButton.isChecked()){
					
					playerExtendButton.performClick();
				}
				
				if(listExtendButton.isChecked()){	
					
					youtubePlayerView.setVisibility(View.GONE);
					LayoutParams params = (LayoutParams) videosListView.getLayoutParams();
					height = params.height;
					params.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;	
				}
				
				else{

					youtubePlayerView.setVisibility(View.VISIBLE);
					LayoutParams params = (LayoutParams) videosListView.getLayoutParams();
					params.height = height;
				}
				
			}
		});

		playerExtendButton =  (ToggleButton) findViewById(R.id.playerExtendButton);
		
		playerExtendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(listExtendButton.isChecked() && playerExtendButton.isChecked()){
					
					listExtendButton.performClick();
				}
				
				if(playerExtendButton.isChecked()){			
					videosListView.setVisibility(View.GONE);
					LayoutParams params = (LayoutParams) youtubePlayerView.getLayoutParams();
					height = params.height;
					params.height = android.view.ViewGroup.LayoutParams.FILL_PARENT;	
				}
				
				else{
					videosListView.setVisibility(View.VISIBLE);
					LayoutParams params = (LayoutParams) youtubePlayerView.getLayoutParams();
					params.height = height;
				}
			}
		});
		
		changeChannelButton = (Button) findViewById(R.id.changeChannelButton);
		
		changeChannelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
				
				muestraDialogo();
				
			}
		});
		
	}
	
	void muestraDialogo(){
		final CharSequence[] items={"General Health", "Back", "Knee", "Ankle", "Neck"};
		AlertDialog.Builder builder3=new AlertDialog.Builder(this);
		builder3.setTitle("Select playlist").setItems(items, new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			switch(which) {
			//General Health
		    case 0:
				youtube.reproducePlaylistMode(videosListView, R.layout.entry,  R.id.textView_superior,
						R.id.textView_inferior, R.id.imageView_imagen,  "PLOhl4anP1Mp3FjD_g3KOEcYWGRrPZPTI0"); 
				break;
		    //Back
		    case 1:
				youtube.reproducePlaylistMode(videosListView, R.layout.entry, R.id.textView_superior,
						R.id.textView_inferior, R.id.imageView_imagen, "PLOhl4anP1Mp3GR-PVzgH2bBFZbW1fX8HS");
		        break;
		    //Knee
		    case 2:
				youtube.reproducePlaylistMode(videosListView, R.layout.entry,  R.id.textView_superior,
						R.id.textView_inferior, R.id.imageView_imagen,"PLOhl4anP1Mp1vJKmmqGAcu10h5OSbx4zf");
				break;
			//Ankle
		    case 3:
				youtube.reproducePlaylistMode(videosListView, R.layout.entry, R.id.textView_superior,
						R.id.textView_inferior, R.id.imageView_imagen, "PLOhl4anP1Mp3hUCb87AM_Gj87__7cJJJc");
				break;
			//Neck
		    case 4:
				youtube.reproducePlaylistMode(videosListView, R.layout.entry,  R.id.textView_superior,
						R.id.textView_inferior, R.id.imageView_imagen,"PLOhl4anP1Mp1N774MgOoEDWwC6HwC5pn7");
				break;

		}
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Selected playlist: "+items[which], Toast.LENGTH_LONG).show();
		}
		});
		builder3.show();
	}
	
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {

		Toast.makeText(this, "Oh dear, something terrible happened, sorry!", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer playa,
			boolean wasRestored) {
		
		this.player = playa;
		this.player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);		
		//Now that the player is initialized, we need to set it on our Youtube class
		youtube.setPlayer(player);

	}
}