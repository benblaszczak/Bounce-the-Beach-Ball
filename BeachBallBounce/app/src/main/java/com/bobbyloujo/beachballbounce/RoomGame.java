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
import com.bobbyloujo.bobengine.sound.SoundPlayer;
import com.bobbyloujo.bobengine.systems.collision.CollisionSystem;
import com.bobbyloujo.bobengine.view.BobView;

import java.util.Random;

/**
 * This is the room that the game play takes place in.
 *
 * Created by Benjamin on 2/10/2015.
 */
public class RoomGame extends Room {

    // Constants
    private final int NUM_PLATS = 10;    // Number of platform objects to use. Remember there are two for each level.
    private final int SPACE = 46;        // Space between platform levels.
	private final double GAP = 66;       // Gap between platforms on the same level.

	private final double GROUND_HEIGHT = 14; // Height of the ground

    // Objects
    private GameObject bg;               // Object to display the background graphic
    private ObjectBall ball;             // The ball
    private GameObject ground;           // The ground
    private ObjectPlatform[] platforms;  // The platforms
    private NumberDisplay scoreDis;      // An object that displays the score

    private Random rand;                 // Used for getting random numbers

	// Sound
	private SoundPlayer sfx;             // Plays sound effects
	private int point;                   // ID for point sound effect

    // Variables
    private int lastPlatform;            // Number ID of the last platform that went below the screen
    public int score;                    // The player's score

    public RoomGame(BobView container) {
        super(container);

        setGridWidth(100);
        setGridUnitY(getGridUnitX());

        rand = new Random();

		/* Initialize the background graphic */
        bg = new GameObject(this);
        bg.setGraphic(GameView.background, 1);
        bg.width = getWidth();
        bg.height = bg.width * 1.78;
        bg.x = getWidth() / 2;
        bg.y = bg.height / 2;
        bg.layer = GameView.BG;  // This will make sure the background is behind everything else. The default layer is 2.

		/* Initialize the ground */
        ground = new GameObject(this);
        ground.setGraphic(GameView.ground);
        ground.width = getWidth();
        ground.height = GROUND_HEIGHT;
        ground.x = getWidth() / 2;
        ground.y = ground.height / 2;

		/* Initialize the ball */
        ball = new ObjectBall(this);

		/* Initialize all the platforms */
        platforms = new ObjectPlatform[NUM_PLATS];
        for (int p = 0; p < NUM_PLATS; p++) {
            platforms[p] = new ObjectPlatform(this);
        }

		/* Initialize the score display */
        scoreDis = new NumberDisplay(this);
        scoreDis.width = 15;
        scoreDis.height = 15;
        scoreDis.setKerning(GameView.NUMBER_WIDTHS);

		/* Sound */
		sfx = new SoundPlayer(getActivity().getApplicationContext());
		point = sfx.newSound(R.raw.point);
    }

	/**
	 * Set up or reset the room.
	 */
    public void set() {
        ball.set(); // Set the ball

		/* Set the score display */
        scoreDis.x = 50;
        scoreDis.y = getHeight() - 20;
        scoreDis.layer = GameView.TOP;
        scoreDis.setAlignment(NumberDisplay.CENTER); // Center alignment
        scoreDis.setNumber(0);

		/* Set the ground and ball y */
        ground.y = ground.height / 2;
        ball.y = ground.height + ball.height / 2;

		/* Set the platforms */
        for (int p = 0; p < NUM_PLATS; p++) {
            platforms[p].set();
        }

        int px = (int) getWidth() / 4 + rand.nextInt((int) getWidth() / 2);      // Random x position for the first platforms

        platforms[0].y = SPACE + ground.y;             // Set the y position of the first platforms
        platforms[1].y = platforms[0].y;
        platforms[0].x = px - GAP;              // X positions
        platforms[1].x = px + GAP;

        for(lastPlatform = 2; lastPlatform < NUM_PLATS; lastPlatform += 2) {                 // The rest of the platforms
            px = (int) getWidth() / 4 + rand.nextInt((int) getWidth() / 2);

            platforms[lastPlatform].y = platforms[lastPlatform - 1].y + SPACE;
            platforms[lastPlatform + 1].y = platforms[lastPlatform].y;
            platforms[lastPlatform].x = px - GAP;
            platforms[lastPlatform + 1].x = px + GAP;
        }

        lastPlatform -= 2;

        score = 0;
    }

	/**
	 * The step event happens each frame.
	 *
	 * @param dt
	 */
    @Override
    public void step(double dt) {
        for (int p = 0; p < NUM_PLATS; p += 2) {                       // For each platform
            if(platforms[p].y < ball.y && !platforms[p].scored) {      // If the platform is below the ball
                score++;                                               // Count it towards the score
                platforms[p].scored = true;                            // Don't count it again
                scoreDis.setNumber(score);                             // Update the display
				sfx.play(point);                                       // Play the point sound effect
            }

            if (platforms[p].y < -platforms[p].height / 2) {                // If the platform is below the screen
                int px = (int) getWidth() / 4 + rand.nextInt((int) getWidth() / 2);     // Go to a random x position

                if (lastPlatform >= NUM_PLATS) lastPlatform = 0;

                platforms[p].y = platforms[lastPlatform].y + SPACE; // Go above the last platform
                platforms[p+1].y = platforms[p].y;
                platforms[p].x = px - GAP;
                platforms[p+1].x = px + GAP;

                platforms[p].scored = false;         // Can count towards the score again.

                lastPlatform += 2;
            }
        }
    }

	/**
	 * Returns true if there is a platform at (x,y)
	 *
	 * @param x
	 * @param y
	 */
    public boolean platformAtPos(double x, double y) {

        if (y < ground.y + ground.height / 2) { // The ground
            return true;
        }

        for (int p = 0; p < NUM_PLATS; p++) {   // The platforms
            if (CollisionSystem.checkPosition(platforms[p].collisionBox, x, y)) {
                return true;
            }
        }

        return false;
    }

	/**
	 * Returns the y position of the top of the platform at (x,y).
	 * If there is no platform at (x,y) it will return -1.
	 *
	 * @param x
	 * @param y
	 */
    public double getTopOfPlat(double x, double y){

        if (y < ground.y + ground.height / 2) {    // Ground
            return ground.y + ground.height / 2;
        }

        for (int p = 0; p < NUM_PLATS; p++) {      // Platforms
            if (CollisionSystem.checkPosition(platforms[p].collisionBox, x, y)) {
                return platforms[p].y + platforms[p].height / 2;
            }
        }

        return -1;
    }

	/**
	 * Move all of the platforms down by amount
	 *
	 * @param amount
	 */
    public void movePlats(double amount) {
        for (int p = 0; p < NUM_PLATS; p++) {
            platforms[p].y -= amount;
        }

        ground.y -= amount;
    }
}
