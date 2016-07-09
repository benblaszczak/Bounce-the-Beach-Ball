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

import com.bobbyloujo.bobengine.extra.SplashActivity;

/**
 * This activity displays the splash screens.
 *
 * Created by Benjamin on 2/10/2015.
 */
public class ActivitySplashScreens extends SplashActivity {
    @Override
    protected void setup() {
        addSplash(R.layout.splash, 3000);     // Show the BobEngine screen for 3000ms

		/*
		 * To add your own splash screen, create one using an xml layout
		 * and place it in the res/layouts folder. Add it the same way
		 * that the BobEngine screen was added above.
		 */
    }

    @Override
    protected void end() {
		// All screens have been shown, create an intent for the main activity
        Intent main = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(main); // Start the intent

        finish();            // Close this activity so the user can't get back to it.
    }
}
