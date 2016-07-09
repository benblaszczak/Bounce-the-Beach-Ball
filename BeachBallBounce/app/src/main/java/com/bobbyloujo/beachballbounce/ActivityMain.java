/**
 * Bounce the Beach Ball (c) 2015, 2016 Benjamin Blaszczak
 *
 * Bounce the Beach Ball is an Android game that was created in 2015 and updated
 * in 2016 by Benjamin Blaszczak also known as Bobby Lou Jo.
 *
 * This file is part of Bounce the Beach Ball.
 *
 * Bounce the Beach Ball is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bounce the Beach Ball is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Bounce the Beach Ball.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bobbyloujo.beachballbounce;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bobbyloujo.bobengine.extra.BobHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;


/**
 * This is the main activity of the application. It is started
 * immediately after the splash screen has shown.
 *
 * In this file you will find the ID strings for Google play
 * services features and AdMob ads.
 *
 * @author Benjamin
 */
public class ActivityMain extends BaseGameActivity {

	// Play Services data
	private final String LEADERBOARD_ID = "YOUR_ID";                   // Google play leader board ID

	// Achievement IDs
	public static final String THANKS_FOR_TRYING = "YOUR_ID";          // ID string for the "Thanks for trying" achievement
	public static final String GREEN_RIBBON = "YOUR_ID";               // "Green Ribbon" achievement
	public static final String RED_RIBBON = "YOUR_ID";                 // "Red Ribbon"
	public static final String BLUE_RIBBON = "YOUR_ID";                // so on...
	public static final String SMALL_TROPHY = "YOUR_ID";
	public static final String MED_TROPHY = "YOUR_ID";
	public static final String LARGE_TROPHY = "YOUR_ID";

	// AdMob data
	private final String BANNER_ID = "YOUR_ID";    // ID for the AdMob banner ad
	private final String INTER_ID = "YOUR_ID";     // ID for the AdMob interstitial ad

	// Variables
	private boolean showLeader;     // Flag indicating if we should show the leader board after signing in
	private boolean showAchieve;    // Flag indicated if we should show the achievements after signing in

	// Objects
	private BobHelper helper;       // Helper class that provides extra functionality such as saving, immersive mode, etc.
	private RelativeLayout layout;  // A layout to hold the GameView, banner ad, and loading spinner
	private GameView view;          // The view that contains the game content
	private ProgressBar spinner;    // Loading spinner to show when the interstitial ad is loading
	private AdView banner;          // The AdMob banner ad
	private InterstitialAd inter;   // The AdMob interstitial ad
	private AdRequest request;      // AdRequest used by banner and inter to load AdMob ads
	private MediaPlayer bg;         // Media player for the background music

	/**
	 * Activity start.
	 *
	 * @param savedInstanceState
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// Initialize these two to false.
		showLeader = false;
		showAchieve = false;

		// Initialize the helper object
		helper = new BobHelper(this);
        helper.useImmersiveMode();         // Uses KitKat's "immersive mode" which removes the title and nav bars. This handles version checking.

		/* ADMOB */

		// Build the ad request. Don't forget to add your test devices.
		request = new AdRequest.Builder().addTestDevice("BF478725C3969A67EF25C8FDE10283E6").build();

		// Initialize the banner ad
		banner = new AdView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE); // Puts the ad on the bottom of the screen
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);   // Centers the ad
		banner.setLayoutParams(params);

		banner.setAdSize(AdSize.BANNER);  // This is a banner.
		banner.setAdUnitId(BANNER_ID);    // Set the ID. You can change this at the top of this file.
		banner.loadAd(request);           // Load the ad. It will automatically refresh

		// Initialize the interstitial ad
		inter = new InterstitialAd(this);
		inter.setAdUnitId(INTER_ID);                  // Set the ID. Change this at the top of the file.
		inter.setAdListener(new AdListener() {        // We need a listener to tell us when to show the ad and hide the spinner
			@Override
			public void onAdFailedToLoad(int error) {
				super.onAdFailedToLoad(error);
				spinner.setVisibility(View.GONE);     // Ad failed to load, hide the spinner
			}

			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				spinner.setVisibility(View.GONE);     // Ad finished loading, hide the spinner
				inter.show();                         // and show the ad.
			}
		});

		/* VIEWS */

		// Initialize the loading spinner that shows when the interstitial ad is loading
		spinner = new ProgressBar(this);
		params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);  // Centers the spinner horizontally
		params.addRule(RelativeLayout.CENTER_VERTICAL);    // Centers the spinner vertically
		spinner.setLayoutParams(params);                   // Apply the parameters
		spinner.setVisibility(View.GONE);                  // Hide the spinner until it is needed.

		// Initialize the GameView
		view = new GameView(this);

		// Initialize the layout and add all the views to it.
		layout = new RelativeLayout(this);
		layout.addView(view);
		layout.addView(banner);
		layout.addView(spinner);

        setContentView(layout);       // Display the layout

		// Uncomment the next line to show the interstitial ad on startup
		// showInterstitial();

		/* MUSIC */
		bg = MediaPlayer.create(this, R.raw.background); // Play background.mp3
		bg.start();
		bg.setLooping(true);
    }

	@Override
	public void onPause() {
		super.onPause();
		if(bg != null) bg.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		helper.onResume();       // Must call helper.onResume() to prevent losing immersive mode
		if(bg != null) bg.start();
	}

	/**
	 * Returns the BobHelper. This will being used for saving values.
	 *
	 * @return This activity's BobHelper.
	 */
	public BobHelper getHelper() {
		return helper;
	}

	/* PLAY GAMES SERVICES METHODS */

	/**
	 * Called when signing into Google play games fails.
	 */
	@Override
	public void onSignInFailed() {
		Log.i("Play Games","Failed to sign into Play games");
		showLeader = showAchieve = false;   // Can't show leader board or achievements
	}

	/**
	 * Called when signing into Google play games is successful. This will
	 * show the leader board or achievements if the player is signing in
	 * after hitting one of the buttons.
	 */
	@Override
	public void onSignInSucceeded() {
		if (showLeader) showLeaderboard();
		if (showAchieve) showAchievements();

		showLeader = showAchieve = false;                  // Already showed, don't show again.

		submitHighscore(helper.getSavedInt("high score")); // In case the player was signed out when he/she got a high score, submit the score now.
	}

	/**
	 * Start the leader board activity or ask the player to sign in.
	 *
	 * NOTE: there is a strange glitch resulting from the isSignedIn() function. It will
	 * sometimes return true when it should return false. To duplicate:
	 *
	 * Sign in to Play games.
	 * Open the leader board or achievements and press the three dots in the top right corner
	 * From Settings, select Sign out. Click "okay".
	 * Try to open the leader board or achievements again.
	 *
	 * isSignedIn() will still return true causing an exception to be thrown when the app tries
	 * to open the leader board or achievements without being signed in. This seems to be a glitch
	 * with Google services.
	 *
	 */
	public void showLeaderboard() {
		if (isSignedIn()) {                                                                               // Signed in?
			try {
				Intent leader = Games.Leaderboards.getLeaderboardIntent(getApiClient(), LEADERBOARD_ID);  // Create leader board intent
				startActivityForResult(leader, 1);                                                        // Start intent
			} catch (SecurityException e) {                                                               // Catch weird glitch
				Log.e("Play Games","Tried to show leaderboard when not signed in.");                      // Yell about it in logcat
				e.printStackTrace();                                                                      // Print stack trace, wait for Google to fix this :(
			}
		} else {                            // Not signed in
			beginUserInitiatedSignIn();     // Sign in
			showLeader = true;              // Show the leader board after signing in
		}
	}

	/**
	 * Submit the player's score to the leader board.
	 *
	 * @param score Score to submit
	 */
	public void submitHighscore(int score) {
		if (isSignedIn()) {
			try {
				Games.Leaderboards.submitScore(getApiClient(), LEADERBOARD_ID, score);
			} catch (SecurityException e) {
				Log.e("Play Games","Tried to submit high score when not signed in.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Start the achievements activity or ask the player to sign in.
	 */
	public void showAchievements() {
		if(isSignedIn()) {
			try {
				Intent achieve = Games.Achievements.getAchievementsIntent(getApiClient());
				startActivityForResult(achieve, 1);
			} catch (SecurityException e) {
				Log.e("Play Games","Tried to show achievements when not signed in.");
				e.printStackTrace();
			}
		} else {
			beginUserInitiatedSignIn();
			showAchieve = true;
		}
	}

	/**
	 * Unlock an achievement.
	 *
	 * @param achievement Achievement to unlock. Strings declared at the top of this file.
	 */
	public void unlockAchievement(String achievement) {
		if (isSignedIn()) {
			try {
				Games.Achievements.unlock(getApiClient(), achievement);
			} catch (SecurityException e) {
				Log.e("Play Games","Tried to unlock achievement when not signed in.");
				e.printStackTrace();
			}
		}
	}

	/* ADMOB METHODS */

	/**
	 * Show the interstitial ad. This method will being loading the
	 * ad and show the loading spinner. The ad will not show until
	 * it has been loaded.
	 */
	public void showInterstitial() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				spinner.setVisibility(View.VISIBLE); // Show the spinner
				inter.loadAd(request);               // Load the ad
			}
		});
	}

	/**
	 * Show or hide the banner ad.
	 *
	 * @param yesNo true to show the ad, false to hide it.
	 */
	public void showBanner(final boolean yesNo) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (yesNo) {                            // Show it?
					banner.setVisibility(View.VISIBLE); // Yeah, show it.
				} else {                                // Nope,
					banner.setVisibility(View.GONE);    // hide it.
				}
			}
		});
	}

	/**
	 * Determines if the player is allowed to press any buttons.
	 *
	 * @return false when the interstitial is loading, true otherwise.
	 */
	public boolean canInput() {
		if (spinner.getVisibility() == View.VISIBLE) { // Loading spinner is showing, so an ad is loading
			return false;                              // Don't allow input.
		} else {                                       // No ad loading.
			return true;                               // Allow input.
		}
	}
}
