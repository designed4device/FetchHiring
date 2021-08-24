package dev.wellen.fetchhiring

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import androidx.recyclerview.widget.RecyclerView

import androidx.test.espresso.matcher.BoundedMatcher

import androidx.test.espresso.matcher.ViewMatchers.*
import dev.wellen.fetchhiring.model.FetchHiringApi
import dev.wellen.fetchhiring.model.Item
import dev.wellen.fetchhiring.view.MainActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.mockito.kotlin.whenever
import javax.inject.Inject


/**
 * The purpose of this test is to ensure that MainActivity is working as expected
 * when launched on a device or emulator.
 */
@HiltAndroidTest
class MainActivityInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mockApi: FetchHiringApi

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun recyclerView_displays_sections_and_items_from_viewModel() {

        runBlocking {
            whenever(mockApi.getListItems()).thenReturn(
                listOf(
                    Item(1, 11, "a"),
                    Item(1, 12, "b"),
                    Item(1, 13, "c"),
                    Item(2, 21, "d"),
                    Item(2, 22, "e"),
                    Item(3, 31, "f"),
                    Item(3, 32, "g"),
                )
            )
        }

        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recycler_view)).check(
            matches(
                allOf(
                    atPosition(
                        0, allOf(
                            hasDescendant(withText(R.string.list_id_label)),
                            hasDescendant(withText("1"))
                        )
                    )
                )
            )
        )

    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                return view.findViewHolderForAdapterPosition(position)
                    ?.let { viewHolder -> itemMatcher.matches(viewHolder.itemView) }
                    ?: return false
            }
        }
    }
}