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
import com.bobbyloujo.bobengine.sound.SoundPlayer;


/**
 * Created by Benjamin on 2/10/2015.
 */
public class ObjectBall extends GameObject {

    // Constants
    private final double ROLL_SPEED = 1.4;  // Horizontal speed of the ball
    private final double GRAV = 0.08;       // Acceleration of gravity
    private final double JUMP_HEIGHT = 2.8; // Jump speed. Higher values will jump higher and faster

	private final double BALL_SIZE = 1f/8f; // Size of the ball as a fraction of the screen width

	/**
	 * Coordinates for a second collision detection
	 * point. These are fractions of the ball's width,
	 * and the center of the ball is the origin.
	 */
	private final double COL_X = 1f/5f;    // X.
	private final double COL_Y = 1f/4f;    // Y.

    // Variables
    private double vy;    // This ball's y velocity
    private boolean jump; // Flag that indicates if the ball should jump

	// Sound
	private SoundPlayer sfx;  // Sound player for the sound effects
	private int jumpSound;    // ID for the jump sound effect
	private int hit;          // ID for the hit sound effect


    /**
     * Initialization. Requires a unique Id number and the room containing this
     * GameObject.
     *
     * @param containingRoom - Room that this object is in.
     */
    public ObjectBall(Room containingRoom) {
        super(containingRoom);

		/* Set size */
        width = getRoom().getWidth() * BALL_SIZE;
        height = width;

        setGraphic(GameView.ball, 1); // Use the ball graphic which has 1 frame

		sfx = new SoundPlayer(getActivity().getApplicationContext());
		jumpSound = sfx.newSound(R.raw.jump);
		hit = sfx.newSound(R.raw.hit);
    }

	/**
	 * Set or reset the ball.
	 */
    public void set() {
        x = getRoom().getWidth() / 2; // Set the x position to the middle of the screen

        vy = 0;        // 0 y velocity
        jump = false;  // Don't be jumping just yet
    }

    @Override
    public void step(double dt) {

        // Rolling
        x += ROLL_SPEED;
        angle -= ROLL_SPEED / 2 * getRoom().getGridUnitX();
        if (x > getRoom().getWidth() + width / 2) {
            x = -width / 2;
        }

        // Bottom of platform detection
        if (((RoomGame) getRoom()).platformAtPos(x, y + height / 2)) {             // Top center of the ball
            int score = ((RoomGame) getRoom()).score;               // Get the score
            ((GameView) getView()).gameOver.set(score);             // Set the game over room
            getView().goToRoom(((GameView) getView()).gameOver);    // Go to the game over room
			sfx.play(hit);                                          // Play the hit sound
        }

        if (((RoomGame) getRoom()).platformAtPos(x + width * COL_X, y + height * COL_Y)) { // Second collision point
            int score = ((RoomGame) getRoom()).score;               // Get the score
            ((GameView) getView()).gameOver.set(score);             // Set the game over room
            getView().goToRoom(((GameView) getView()).gameOver);    // Go to the game over room
			sfx.play(hit);                                          // Play the hit sound
        }

        // Jump
        if (jump) {                         // Need to jump
            vy = JUMP_HEIGHT;               // Set vy to the jump speed times the correction ratio
            jump = false;                   // Don't need to jump anymore!
			sfx.play(jumpSound);            // Play jump sound
        }

        // Falling
        vy -= GRAV; // Accelerate down due to gravity

        if (y < getRoom().getHeight() / 3 || vy < 0) { // If the ball is below 1/3 the screen height, move it up. Or left the ball fall.
            y += vy;
        } else {                                   // The ball is above 1/3 the screen height
            ((RoomGame) getRoom()).movePlats(vy);  // Move the platforms down instead.
        }

        // Ground and platform detection
        if (((RoomGame) getRoom()).platformAtPos(x, y - height / 2 + vy) && vy <= 0) {     // Detect the ground
            y = ((RoomGame) getRoom()).getTopOfPlat(x, y - height / 2 + vy) + height / 2;  // Make sure the ball is on top of the platform
            vy = 0;                                                                        // Stop falling
        }
    }

	/**
	 * Fired when the screen is touched.
	 *
	 * @param index
	 */
    @Override
    public void newpress(int index) {
        jump = true;  // Need to jump!
    }
}
