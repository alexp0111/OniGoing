package ru.alexp0111.onigoing

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexp0111.onigoing.ui.MainActivity


@RunWith(AndroidJUnit4::class)
class SearchingTest {

    @JvmField
    @Rule
    val mActivityRule = ActivityTestRule(
        MainActivity::class.java,
        true,
        false,
    )

    @Test
    fun shouldGoToAnimeScreen() {
        mActivityRule.launchActivity(null)
        onView(withId(R.id.et_search)).perform(typeText("ONE PIECE"))
        onView(isRoot()).perform(waitFor(5000))
        onView(first(withId(R.id.cv_search_root))).perform(click())
        onView(isRoot()).perform(waitFor(2000))
        onView(withId(R.id.txt_title_name)).check(matches(withText("ONE PIECE")))
    }

}