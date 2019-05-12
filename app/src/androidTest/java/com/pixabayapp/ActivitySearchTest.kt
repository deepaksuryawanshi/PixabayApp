package com.pixabayapp

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage.RESUMED
import com.movieapp.RecyclerViewMatcher
import com.movieapp.WifiTimeIdlingResource
import com.pixabayapp.Adapter.AdapterSearchResult
import com.pixabayapp.Model.SearchResult
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.android.synthetic.main.activity_search.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class ActivitySearchTest {

    @JvmField
    @Rule
    val mActivityRule = ActivityTestRule<ActivitySearch>(ActivitySearch::class.java)

    var rvSearch: RecyclerView? = null

    var searchResultHits: ArrayList<SearchResult.Hit> = ArrayList<SearchResult.Hit>()

    var count: Int = 0

    @Before
    fun setUp() {

        Thread.sleep(10000)
//        onView(withId(R.id.rvMovieList)).check(matches(isDisplayed()))

        rvSearch = getActivityInstance()!!.findViewById(R.id.rvSearch)

        if (rvSearch != null && rvSearch!!.adapter != null && (rvSearch?.adapter?.itemCount!! > 0)) {
            count = rvSearch!!.getAdapter()!!.getItemCount()

            searchResultHits = mActivityRule.activity.getSearchResultList()
        }

    }

    @Test
    fun checkVisibility() {
        onView(withId(R.id.btnSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.etSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.rvSearch)).check(matches(isDisplayed()))
        assert(isKeyboardShown())
    }


    @Test
    fun checkEditTextHint() {
        onView(withId(R.id.etSearch)).check(matches(withHint("Search")))
    }

    @Test
    fun clearBtnVisibility() {
        onView(withId(R.id.etSearch)).perform(click(), typeText("Engineer"), closeSoftKeyboard());
        onView(withId(R.id.imgvClose)).check(matches(isDisplayed()))
    }


    @Test
    fun clearBtnVisibilityGone() {
        onView(withId(R.id.etSearch)).perform(click(), typeText("Engineer"), closeSoftKeyboard());
        onView(withId(R.id.etSearch)).perform(click(), clearText(), closeSoftKeyboard());
        onView(withId(R.id.imgvClose)).check(matches(Matchers.not(isDisplayed())))
    }


    @Test
    fun clearBtnClick() {
        onView(withId(R.id.etSearch)).perform(click(), typeText("Engineer"), closeSoftKeyboard());
        onView(withId(R.id.imgvClose)).perform(click())

        // passes if the textView does not match the empty string
        onView(withId(R.id.etSearch)).check(matches((withText(""))));
        onView(withId(R.id.imgvClose)).check(matches(Matchers.not(isDisplayed())))
    }

    @Test
    fun searchBtnClick() {
        onView(withId(R.id.etSearch)).perform(click(), typeText("yellow"))
        onView(withId(R.id.btnSearch)).perform(click())


//        onView(withText("Please wait....")).check(matches(isDisplayed()))

//        onView(withText("Please wait....")).inRoot(isDialog()).check(matches(isDisplayed()))

//        UIAutomator@
//        val device = UiDevice.getInstance(getInstrumentation())
//        val uiObject = device.findObject(UiSelector().text("Page"))
//        try {
//            uiObject.click()
//        } catch (e: UiObjectNotFoundException) {
//            throw RuntimeException("UI Object not found", e)
//        }
    }

    @Test
    fun searchResultSuccess() {
        onView(withId(R.id.etSearch)).perform(click(), typeText("yellow"))
        onView(withId(R.id.btnSearch)).perform(click())
        Thread.sleep(10000)
        onView(withContentDescription("Search")).check(RecyclerViewItemCountAssertion(greaterThan<Int>(0)))
    }

    @Test
    fun searchResultSuccessSwipeUp() {
        onView(withId(R.id.etSearch)).perform(click(), typeText("yellow"))
        onView(withId(R.id.btnSearch)).perform(click())
//        onView(withContentDescription("Search")).check(RecyclerViewItemCountAssertion(greaterThan<Int>(0)))

        onView(withId(R.id.rvSearch)).perform(
            RecyclerViewActions.scrollToPosition<AdapterSearchResult.ViewHolder>(
                getActivityInstance()!!.rvSearch!!.getAdapter()!!.getItemCount() - 1
            )
        )

        swipeUp()
    }


    @Test
    fun searchResultFailure() {
        onView(withId(R.id.etSearch)).perform(click(), clearText(), closeSoftKeyboard())
        onView(withId(R.id.btnSearch)).perform(click())

        onView(withText("Please enter search text"))
            .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
            .check(matches(isDisplayed()))
    }

    inner class RecyclerViewItemCountAssertion : ViewAssertion {

        private val matcher: Matcher<Int>

        constructor(expectedCount: Int) {
            this.matcher = `is`<Int>(expectedCount)
        }

        constructor(matcher: Matcher<Int>) {
            this.matcher = matcher
        }

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            assertThat(adapter?.getItemCount(), matcher)
        }

    }

//    fun checkMaxLength(lines: Int): TypeSafeMatcher {
//        return object : TypeSafeMatcher() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            protected fun matchesSafely(item: View): Boolean {
//                val filters = (item as TextView).filters
//                val lengthFilter = filters[0] as InputFilter.LengthFilter
//
//                return lengthFilter.max == lines
//            }
//
//            fun describeTo(description: Description) {
//                description.appendText("checkMaxLength")
//            }
//        }
//    }


    @Test
    fun keyboardVisibilityCheck() {
        onView(withId(R.id.etSearch)).perform(click())
        assertTrue(isKeyboardShown())
    }


    fun isKeyboardShown(): Boolean {
        val inputMethodManager =
            InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }


    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    fun getActivityInstance(): Activity? {

        val activity = arrayOfNulls<Activity>(1)
        getInstrumentation().runOnMainSync {
            var currentActivity: Activity? = null
            val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity = resumedActivities.iterator().next() as Activity
                activity[0] = currentActivity
            }
        }

        return activity[0]
    }


    @Test
    fun netAvailable0() {

        val waitingTime: Long = 60000

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)

        //        setWiFiOnOff(false);
        // Now we wait
        val idlingResource = WifiTimeIdlingResource(false)
        IdlingRegistry.getInstance().register(idlingResource)

        Thread.sleep(10000)
        assertFalse(isConnected(getActivityInstance()))

        // Clean up
        IdlingRegistry.getInstance().unregister(idlingResource)
    }


    private fun isConnected(context: Context?): Boolean {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    @Test
    fun netAvailable1() {

        val waitingTime: Long = 80000

        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS)

        //        setWiFiOnOff(true);
        // Now we wait
        val idlingResource = WifiTimeIdlingResource(true)
        IdlingRegistry.getInstance().register(idlingResource)

        Thread.sleep(20000)
        assertTrue(isConnected(getActivityInstance()))

        // Clean up
        IdlingRegistry.getInstance().unregister(idlingResource)
    }


}

