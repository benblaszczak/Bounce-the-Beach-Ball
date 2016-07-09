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

import com.bobbyloujo.bobengine.entities.GameObject;
import com.bobbyloujo.bobengine.entities.Room;
import com.bobbyloujo.bobengine.extra.NumberDisplay;
import com.bobbyloujo.bobengine.view.BobView;

import javax.microedition.khronos.opengles.GL10;

/**
 * This is the room that shows when the player loses.
 *
 * Created by Benjamin on 2/10/2015.
 */
public class RoomGameOver extends Room {

    // Constants
	private final int AD_FREQ = 6;           // Number of plays between interstitial ad displays

	private final double PLAY_W = 1f/3f;     // Width of the play button as a fraction of the screen width
	private final double LEAD_W = 1f/3f;     // Leader board button width
	private final double ACHI_W = 1f/3f;     // Achievements button width
	private final double AWAR_W = 1f/4f;     // Award graphic width

	// Achievements
	private final int FIRST = 0;       // The score necessary to get the lowest achievement
	private final int SECOND = 5;      // The score necessary to get the second achievement...
	private final int THIRD = 10;      // so on...
	private final int FOURTH = 15;
	private final int FIFTH = 25;
	private final int SIXTH = 35;
	private final int SEVENTH = 50;    // Score necessary to get the highest achievement

	// Variables
	private int playsSinceLastAd;     // Plays since the last interstitial ad

    // Objects
    private GameObject gameOver;      // Game Over graphic
    private NumberDisplay scoreDis;   // The score display for this play
    private NumberDisplay highScore;  // The high score display
    private GameObject play;          // Play button
	private GameObject leader;        // Leader board button
	private GameObject achieve;       // Achievements button
	private GameObject award;         // Award graphic

    public RoomGameOver(BobView container) {
        super(container);

		setGridWidth(100);
		setGridUnitY(getGridUnitX());

		playsSinceLastAd = 0;

		/* Initialize game over graphic */
        gameOver = new GameObject(this);
        gameOver.height = gameOver.width = getWidth();
        gameOver.setGraphic(GameView.gameOverGraphic, 1);
        gameOver.layer = 0;

		/* Initialize score display */
        scoreDis = new NumberDisplay(this);
        scoreDis.setKerning(GameView.NUMBER_WIDTHS);

		/* Initialize high score display */
        highScore = new NumberDisplay(this);
        highScore.setKerning(GameView.NUMBER_WIDTHS);

		/* Initialize play button */
        play = new GameObject(this);
        play.setGraphic(GameView.playButton, 1);
        play.width = getWidth() * PLAY_W;
        play.height = play.width;

		/* Initialize leader board button */
		leader = new GameObject(this);
		leader.setGraphic(GameView.leaderBoard, 1);
		leader.width = getWidth() * LEAD_W;
		leader.height = leader.width;

		/* Initialize achievements button */
		achieve = new GameObject(this);
		achieve.setGraphic(GameView.achievements, 1);
		achieve.width = getWidth() * ACHI_W;
		achieve.height = achieve.width;

		/* Initialize award graphic */
		award = new GameObject(this);
		award.width = getWidth() * AWAR_W;
		award.height = award.width;
    }

	/**
	 * Set or reset the room.
	 * @param score The score from the last play
	 */
    public void set(int score) {
        int highscore;

		/* Set game over graphic position */
        gameOver.x = getWidth() / 2;
        gameOver.y = getHeight() - gameOver.height / 2;

		/* Set play button position */
        play.x = getWidth() / 2;
        play.y = getHeight() * 3 / 8;

		/* Set leader board button position */
		leader.x = getWidth() * 3/12;
		leader.y = getHeight() * 1/4;

		/* Set achievements button position */
		achieve.x = getWidth() * 9/12;
		achieve.y = getHeight() * 1/4;

		/* Award graphic position */
		award.x = 27.5;
		award.y = getHeight() - 41;

		/* set score display */
		scoreDis.x = 55;
		scoreDis.y = getHeight() - 31.5;
		scoreDis.height = scoreDis.width = 10;
		scoreDis.layer = 4;
        scoreDis.setNumber(score);
        scoreDis.setAlignment(NumberDisplay.LEFT);

		/* set high score display */
		highScore.x = 55;
		highScore.y = getHeight() - 51;
		highScore.height = highScore.width = 10;
		highScore.layer = 4;
        highScore.setAlignment(NumberDisplay.LEFT);

		/* get high score */
        if (score > ((ActivityMain) getActivity()).getHelper().getSavedInt("high score")) {
            highscore = score;
            ((ActivityMain) getActivity()).getHelper().saveInt("high score", highscore);
			((ActivityMain) getActivity()).submitHighscore(highscore);
        } else {
            highscore = ((ActivityMain) getActivity()).getHelper().getSavedInt("high score");
        }

        highScore.setNumber(highscore);

		/* Determine the award and unlock achievements */
		if (score >= SEVENTH) {
			award.setGraphic(GameView.largeTrophy, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.LARGE_TROPHY);
		} else if (score >= SIXTH) {
			award.setGraphic(GameView.medTrophy, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.MED_TROPHY);
		} else if (score >= FIFTH) {
			award.setGraphic(GameView.smallTrophy, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.SMALL_TROPHY);
		} else if (score >= FOURTH) {
			award.setGraphic(GameView.blueRibbon, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.BLUE_RIBBON);
		} else if (score >= THIRD) {
			award.setGraphic(GameView.redRibbon, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.RED_RIBBON);
		} else if (score >= SECOND) {
			award.setGraphic(GameView.greenRibbon, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.GREEN_RIBBON);
		} else if (score >= FIRST) {
			award.setGraphic(GameView.participation, 1);
			((ActivityMain) getActivity()).unlockAchievement(ActivityMain.THANKS_FOR_TRYING);
		}

		/* ADS */
		((ActivityMain) getActivity()).showBanner(true);

		playsSinceLastAd++;
		if (playsSinceLastAd == AD_FREQ) {
			((ActivityMain) getActivity()).showInterstitial();
			playsSinceLastAd = 0;
		}
    }

    @Override
    public void newpress(int i) {
        super.newpress(i);

		if (((ActivityMain) getActivity()).canInput()) {            // Don't allow button presses when there is an ad loading
			if (getTouch().objectTouched(play)) {                   // Play button touched
				((GameView) getView()).game.set();                  // Set game room
				getView().goToRoom(((GameView) getView()).game);    // Go to game room

				((ActivityMain) getActivity()).showBanner(false);   // Hide the banner ad
			}

			if (getTouch().objectTouched(leader)) {                 // Leader board button touched
				((ActivityMain) getActivity()).showLeaderboard();   // Show the leader board
			}

			if (getTouch().objectTouched(achieve)) {                // Achievements button touched
				((ActivityMain) getActivity()).showAchievements();  // Show achievements
			}
		}
    }

    @Override
    public void draw(GL10 gl) {
        ((GameView) getView()).game.draw(gl); // Draw the game room behind the game over room
        super.draw(gl);                       // Draw the game over room
    }
}
