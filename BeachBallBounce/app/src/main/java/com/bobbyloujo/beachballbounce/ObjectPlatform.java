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
import com.bobbyloujo.bobengine.systems.collision.CollisionBox;
import com.bobbyloujo.bobengine.systems.collision.CollisionSystem;

/**
 * Platform class.
 *
 * Created by Benjamin on 2/10/2015.
 */
public class ObjectPlatform extends GameObject {

	// Constants
	private final double HEIGHT = 14; // Height of the platform

    // Variables
    public boolean scored;  // Flag indicating if this platform has been passed by the ball and counted in the score.

    public CollisionBox collisionBox;

    /**
     * Initialization. Requires a unique Id number and the room containing this
     * GameObject.
     *
     * @param containingRoom - Room that this object is in.
     */
    public ObjectPlatform(Room containingRoom) {
        super(containingRoom);

		/* Set size */
        width = getRoom().getWidth();
        height = HEIGHT;

        setGraphic(GameView.platform, 1); // Use the platform graphic with 1 frame.

        // Give this platform a collision box so we can tell where it is.
        collisionBox = CollisionSystem.generateCollisionBox(0, 0, 1, 1, this, this, null);
    }

    public void set() {
        scored = false;
    }
}