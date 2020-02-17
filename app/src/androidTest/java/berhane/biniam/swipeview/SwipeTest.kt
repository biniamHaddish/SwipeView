package berhane.biniam.swipeview

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Designed and developed by Biniam Berhane on 2020-01-13.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class SwipeTest {

    @get: Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    /**
     * testing if the Recyclerview is on view
     */
    private lateinit var recyclerView: RecyclerView


    @Before fun setup(){
        recyclerView= RecyclerView(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun swipeRight() {
        onView(withId(R.id.email_recycler)).check(matches(isDisplayed()))
        onView(withId(R.id.email_recycler))
            .perform(ViewActions.swipeRight())
            .check(matches(isDisplayed()))
    }

    @Test
    fun swipeLeft(){
        onView(withId(R.id.email_recycler)).check(matches(isDisplayed()))
        onView(withId(R.id.email_recycler))
            .perform(a)
        onView(withId(R.id.email_recycler))
            .perform(ViewActions.swipeLeft())
            .check(matches(isDisplayed()))
    }
}