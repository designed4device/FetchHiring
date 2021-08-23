package dev.wellen.fetchhiring

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@HiltAndroidTest
class MainActivityInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun helloWorld() {
        // Context of the app under test.
        ActivityScenario.launch(MainActivity::class.java)

        onView(withText("Hello World!")).check(matches(isCompletelyDisplayed()))
    }
}