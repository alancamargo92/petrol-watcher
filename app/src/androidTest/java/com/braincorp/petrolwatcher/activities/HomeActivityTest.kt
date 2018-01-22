package com.braincorp.petrolwatcher.activities

import android.support.test.espresso.intent.Intents
import android.support.test.runner.AndroidJUnit4
import com.braincorp.petrolwatcher.robots.action.HomeActivityActionRobot
import com.braincorp.petrolwatcher.robots.assertion.checkIfLaunchesLoginActivity
import com.braincorp.petrolwatcher.robots.assertion.checkIfShowsQuestionDialogue
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    private val robot = HomeActivityActionRobot()

    @After
    fun after() {
        Intents.release()
    }

    @Test
    fun shouldShowDialogueWhenClickingOnSignOut() {
        robot.launchActivity()
                .clickOnSignOut()
        checkIfShowsQuestionDialogue()
    }

    @Test
    fun shouldLaunchLoginActivityWhenClickingYesInSignOutDialogue() {
        robot.launchActivity()
                .clickOnSignOut()
                .clickOnYesDialogueButton()
        checkIfLaunchesLoginActivity()
    }

}