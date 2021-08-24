package dev.wellen.fetchhiring

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.wellen.fetchhiring.model.FetchHiringApi
import dev.wellen.fetchhiring.model.Item
import dev.wellen.fetchhiring.view.MainActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    fun recyclerView_displays_items() {

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
                    atPosition(0, itemViewMatcher("1", "11", "a")),
                    atPosition(1, itemViewMatcher("1", "12", "b")),
                    atPosition(2, itemViewMatcher("1", "13", "c")),
                    atPosition(3, itemViewMatcher("2", "21", "d")),
                    atPosition(4, itemViewMatcher("2", "22", "e")),
                    atPosition(5, itemViewMatcher("3", "31", "f")),
                    atPosition(6, itemViewMatcher("3", "32", "g"))
                )
            )
        )

    }

    private fun itemViewMatcher(listId: String, id: String, name: String): Matcher<View> {
        return allOf(
            hasDescendant(allOf(withId(R.id.list_id), withText(listId))),
            hasDescendant(allOf(withId(R.id.id), withText(id))),
            hasDescendant(allOf(withId(R.id.name), withText(name)))
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