package com.braincorp.petrolwatcher.activities

import android.support.test.espresso.intent.Intents
import android.support.test.filters.FlakyTest
import android.support.test.runner.AndroidJUnit4
import com.braincorp.petrolwatcher.authentication.AuthenticationManager
import com.braincorp.petrolwatcher.robots.action.LoginActivityActionRobot
import com.braincorp.petrolwatcher.robots.assertion.checkIfLaunchesHomeActivity
import com.braincorp.petrolwatcher.robots.assertion.checkIfShowsErrorDialogue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private companion object {
        const val DEFAULT_TIMEOUT = 5000L
    }

    private val robot = LoginActivityActionRobot()

    @Before
    fun setup() {
        if (AuthenticationManager.isSignedIn()) {
            AuthenticationManager.signOut()
            robot.wait(2000)
        }
        robot.launchActivity()
    }

    @After
    fun after() {
        AuthenticationManager.signOut()
        robot.wait(2000)
        Intents.release()
    }

    @FlakyTest(detail = "Sometimes there's not enough time to receive the authentication server response")
    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldLaunchHomeActivityWithCorrectEmailAndPassword() {
        robot.typeEmail(correct = true)
                .hideKeyboard()
        robot.typePassword(correct = true)
                .hideKeyboard()
        robot.clickOnSignIn()
                .wait(2000)
        checkIfLaunchesHomeActivity()
    }

    @FlakyTest(detail = "Sometimes there's not enough time to receive the authentication server response")
    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldShowErrorDialogueWithIncorrectEmail() {
        robot.typeEmail(correct = false)
                .hideKeyboard()
        robot.typePassword(correct = true)
                .hideKeyboard()
        robot.clickOnSignIn()
                .wait()
        checkIfShowsErrorDialogue()
    }

    @FlakyTest(detail = "Sometimes there's not enough time to receive the authentication server response")
    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldShowErrorDialogueWithIncorrectPassword() {
        robot.typeEmail(correct = true)
                .hideKeyboard()
        robot.typePassword(correct = false)
                .hideKeyboard()
        robot.clickOnSignIn()
                .wait()
        checkIfShowsErrorDialogue()
    }

    @FlakyTest(detail = "Sometimes there's not enough time to receive the authentication server response")
    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldShowErrorDialogueWithIncorrectEmailAndPassword() {
        robot.typeEmail(correct = false)
                .hideKeyboard()
        robot.typePassword(correct = false)
                .hideKeyboard()
        robot.clickOnSignIn()
                .wait()
        checkIfShowsErrorDialogue()
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldShowErrorDialogueWithNullEmail() {
        robot.typePassword(correct = true)
                .hideKeyboard()
        robot.clickOnSignIn()
                .wait()
        checkIfShowsErrorDialogue()
    }

    @FlakyTest(detail = "Sometimes there's not enough time to receive the authentication server response")
    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldShowErrorDialogueWithNullPassword() {
        robot.typeEmail(correct = true)
                .hideKeyboard()
        robot.clickOnSignIn()
                .wait()
        checkIfShowsErrorDialogue()
    }

    @FlakyTest(detail = "Sometimes there's not enough time to receive the authentication server response")
    @Test(timeout = DEFAULT_TIMEOUT)
    fun shouldShowErrorDialogueWithNullEmailAndPassword() {
        robot.clickOnSignIn()
                .wait()
        checkIfShowsErrorDialogue()
    }

}