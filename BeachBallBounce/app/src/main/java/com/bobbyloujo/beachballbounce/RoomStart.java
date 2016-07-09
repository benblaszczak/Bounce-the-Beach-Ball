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

import com.bobbyloujo.bobengine.entities.GameObject;
import com.bobbyloujo.bobengine.entities.Room;
import com.bobbyloujo.bobengine.view.BobView;

/**
 * The first room the player sees.
 *
 * Created by Benjamin on 2/10/2015.
 */
public class RoomStart extends Room {

	// Constants
	private final double PLAY_X = 1f/2f;        // The x position of the play button as a fraction of the width of the screen
	private final double PLAY_Y = 3f/8f;        // The y position as a fraction of the height of the screen
	private final double PLAY_WIDTH = 1f/3f;    // The width of the play button as a fraction of the width of the screen (assuming height is the same as width)

	private final double LEADER_X = 3f/12f;     // Leader board button x
	private final double LEADER_Y = 1f/4f;      // y
	private final double LEADER_WIDTH = 1f/3f;  // width

	private final double ACHIEVE_X = 9f/12f;    // Achievements button x
	private final double ACHIEVE_Y = 1f/4f;     // y
	private final double ACHIEVE_WIDTH = 1f/3f; // width

	private final double TITLE_X = 50;          // Title graphic x (as grid units)
	private final double TITLE_Y = 120;         // y
	private final double TITLE_WIDTH = 100;     // width

	private final double ABOUT_X = 1f/2f;       // About button x
	private final double ABOUT_Y = 1f/7f;       // y
	private final double ABOUT_WIDTH = 1f/4f;   // width


    // Objects
    private GameObject title;     // The title graphic
    private GameObject bg;        // The background graphic
    private GameObject play;      // The Play button
    private GameObject leader;    // The Leader board button
    private GameObject achieve;   // The Achievements button
	private GameObject about;     // The about button

    public RoomStart(BobView container) {
        super(container);

        setGridWidth(100);
        setGridUnitY(getGridUnitX());

		// Initialize title graphic
        title = new GameObject(this);
        title.setGraphic(GameView.title, 1);
        title.height = title.width = TITLE_WIDTH;

		// Initialize background
        bg = new GameObject(this);
        bg.setGraphic(GameView.background, 1);
        bg.width = getWidth();
        bg.height = bg.width * 1.78;
        bg.layer = 0;

		// Initialize play button
        play = new GameObject(this);
        play.setGraphic(GameView.playButton, 1);
        play.width = getWidth() * PLAY_WIDTH;
        play.height = play.width;

		// Initialize leader board button
        leader = new GameObject(this);
        leader.setGraphic(GameView.leaderBoard, 1);
        leader.width = getWidth() * LEADER_WIDTH;
        leader.height = leader.width;

		// Initialize achievements button
        achieve = new GameObject(this);
        achieve.setGraphic(GameView.achievements, 1);
        achieve.width = getWidth() * ACHIEVE_WIDTH;
        achieve.height = achieve.width;

		// Initialize the about button
		about = new GameObject(this);
		about.setGraphic(GameView.about, 1);
		about.width = getWidth() * ABOUT_WIDTH;
		about.height = about.width;
    }

	/**
	 * Set up and reset the room
	 */
    public void set() {
		// Set title coordinates
        title.x = TITLE_X;
        title.y = TITLE_Y;

		// Set background coordinates
        bg.x = getWidth() / 2;
        bg.y = getHeight() / 2;

		// Set play button coordinates
        play.x = getWidth() * PLAY_X;
        play.y = getHeight() * PLAY_Y;

		// Set leader board button coordinates
        leader.x = getWidth() * LEADER_X;
        leader.y = getHeight() * LEADER_Y;

		// Set achievements button coordinates
        achieve.x = getWidth() * ACHIEVE_X;
        achieve.y = getHeight() * ACHIEVE_Y;

		// Set about button coordinates
		about.x = getWidth() * ABOUT_X;
		about.y = getHeight() * ABOUT_Y;
    }

    @Override
    public void newpress(int index) {
        super.newpress(index);

		if (((ActivityMain) getActivity()).canInput()) {            // Make sure we aren't waiting for an ad to load
			if (getTouch().objectTouched(play)) {                   // Play button touched
				((GameView) getView()).game.set();                  // Set up the game room
				getView().goToRoom(((GameView) getView()).game);    // Go to the game room

				((ActivityMain) getActivity()).showBanner(false);   // Hide the banner ad
			}

			if (getTouch().objectTouched(leader)) {                 // Leader board button touched
				((ActivityMain) getActivity()).showLeaderboard();   // Show the leader board
			}

			if (getTouch().objectTouched(achieve)) {                // Achievements button touched
				((ActivityMain) getActivity()).showAchievements();  // Show achievements
			}

			if (getTouch().objectTouched(about)) {                  // About button touched
				Intent about = new Intent(getActivity().getApplicationContext(), ActivityAbout.class);
				getActivity().startActivity(about);                 // Start about screen activity
			}
		}
    }
}
