package com.braincorp.petrolwatcher

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.rule.ActivityTestRule
import android.support.v7.app.AppCompatActivity
import br.com.concretesolutions.kappuccino.utils.doWait
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseActivityTest<T: AppCompatActivity>(activityClass: Class<T>,
                                                  private val autoLaunch: Boolean = true) {

    @Rule
    @JvmField
    val rule = if (autoLaunch) {
        IntentsTestRule(activityClass, true, false)
    } else {
        ActivityTestRule(activityClass, true, false)
    }

    @Before
    open fun setup() {
        if (autoLaunch) launch()
        else Intents.init()
    }

    @After
    open fun afterTest() {
        if (!autoLaunch) Intents.release()
    }

    open fun intent(): Intent {
        return Intent()
    }

    fun launch() {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        rule.launchActivity(intent())
        doWait(300)
    }

}