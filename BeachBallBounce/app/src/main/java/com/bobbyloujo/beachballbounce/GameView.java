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

import android.content.Context;

import com.bobbyloujo.bobengine.graphics.Graphic;
import com.bobbyloujo.bobengine.view.BobView;

/**
 * This class is the view on which the contents of the game are displayed.
 * It also acts as the central command for the game. This view holds all the
 * graphics and rooms, loads the graphics, and controls which room is being
 * displayed.
 *
 * Created by Benjamin on 2/10/2015.
 */
public class GameView extends BobView {

    public static final double NUMBER_WIDTHS[] = {.69, .51, .75, .77, .90, .76, .76, .69, .66, .75};

    // Layers
    public static final int BG = 0;
    public static final int MID = 1;
    public static final int TOP = 2;

    // Graphics
    public static Graphic ball;                 // Beach ball graphic
    public static Graphic playButton;           // Play button
    public static Graphic leaderBoard;          // Leader board button
    public static Graphic achievements;         // Achievements button
	public static Graphic about;                // About button graphic
    public static Graphic title;                // Title graphic
    public static Graphic background;           // Background graphic
    public static Graphic platform;             // Platform graphic
    public static Graphic numbers;              // Numbers (for the score)
    public static Graphic ground;               // Ground (shown at the bottom on game start)
    public static Graphic gameOverGraphic;      // Game Over display (with boxes for the scores and award)
	public static Graphic participation;        // Participation award graphic
	public static Graphic greenRibbon;          // Green ribbon
	public static Graphic redRibbon;            // Red ribbon
	public static Graphic blueRibbon;           // Blue ribbon
	public static Graphic smallTrophy;          // Small trophy
	public static Graphic medTrophy;            // Medium trophy
	public static Graphic largeTrophy;          // Large trophy

    // Rooms
    public RoomStart start;                     // The start room. First room seen.
    public RoomGame game;                       // Game room. Where the game play happens
    public RoomGameOver gameOver;               // Game Over room. Shows the score, high score, award

    public GameView(Context context) {
        super(context);
    }

    @Override
    protected void onCreateGraphics() {

		/* LOAD THE GRAPHICS */

        ball = getGraphicsHelper().getGraphic(R.drawable.beachball);
        playButton = getGraphicsHelper().getGraphic(R.drawable.play);
        leaderBoard = getGraphicsHelper().getGraphic(R.drawable.leaderboard);
        achievements = getGraphicsHelper().getGraphic(R.drawable.achievements);
		about = getGraphicsHelper().getGraphic(R.drawable.about);
        title = getGraphicsHelper().getGraphic(R.drawable.title);
        background = getGraphicsHelper().getGraphic(R.drawable.beachscene);
        platform = getGraphicsHelper().getGraphic(R.drawable.bar);
        numbers = getGraphicsHelper().getGraphic(R.drawable.numbers);
        ground = getGraphicsHelper().getGraphic(R.drawable.ground);
        gameOverGraphic = getGraphicsHelper().getGraphic(R.drawable.gameover);
		participation = getGraphicsHelper().getGraphic(R.drawable.participation);
		greenRibbon = getGraphicsHelper().getGraphic(R.drawable.greenribbon);
		redRibbon = getGraphicsHelper().getGraphic(R.drawable.redribbon);
		blueRibbon = getGraphicsHelper().getGraphic(R.drawable.blueribbon);
		smallTrophy = getGraphicsHelper().getGraphic(R.drawable.smalltrophy);
		medTrophy = getGraphicsHelper().getGraphic(R.drawable.medtrophy);
		largeTrophy = getGraphicsHelper().getGraphic(R.drawable.largetrophy);
    }

    @Override
    protected void onCreateRooms() {

        getRenderer().outputFPS(false); // Outputs the FPS to logcat with tag "fps"

		/* INITIALIZE THE ROOMS */

        start = new RoomStart(this);
        game = new RoomGame(this);
        gameOver = new RoomGameOver(this);

        start.set();     // Set up the start room
        goToRoom(start); // Go to the start room
    }
}
