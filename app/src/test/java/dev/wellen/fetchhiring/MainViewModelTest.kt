package dev.wellen.fetchhiring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.wellen.fetchhiring.model.FetchHiringApi
import dev.wellen.fetchhiring.model.Item
import dev.wellen.fetchhiring.viewmodel.ListItem
import dev.wellen.fetchhiring.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val mockApi: FetchHiringApi = mock()

    @Test
    fun `it sorts by listId, then by item name`() {
        runBlocking {
            whenever(mockApi.getListItems()).thenReturn(
                listOf(
                    Item(1, 11, "b"),
                    Item(3, 32, "j"),
                    Item(2, 21, "z"),
                    Item(1, 12, "a"),
                    Item(2, 22, "d"),
                    Item(1, 13, "c"),
                    Item(3, 31, "f"),
                )
            )
        }

        MainViewModel(mockApi).apply {
            val observer: Observer<List<ListItem>> = mock()
            sections.observeForever(observer)

            val captor = argumentCaptor<List<ListItem>>()
            verify(observer).onChanged(captor.capture())

            assertThat(captor.firstValue)
                .containsExactly(
                    ListItem.SectionHeader(1),
                    ListItem.Item(12, "a"),
                    ListItem.Item(11, "b"),
                    ListItem.Item(13, "c"),
                    ListItem.SectionHeader(2),
                    ListItem.Item(22, "d"),
                    ListItem.Item(21, "z"),
                    ListItem.SectionHeader(3),
                    ListItem.Item(31, "f"),
                    ListItem.Item(32, "j")
                )

            sections.removeObserver(observer)
        }
    }

    @Test
    fun `it filters items with blank or null names`() {
        runBlocking {
            whenever(mockApi.getListItems()).thenReturn(
                listOf(
                    Item(1, 11, "name"),
                    Item(1, 12, ""),
                    Item(1, 13, "name"),
                    Item(2, 21, null),
                    Item(2, 22, "name"),
                    Item(3, 31, ""),
                    Item(3, 32, null),
                )
            )
        }

        MainViewModel(mockApi).apply {
            val observer: Observer<List<ListItem>> = mock()
            sections.observeForever(observer)

            val captor = argumentCaptor<List<ListItem>>()
            verify(observer).onChanged(captor.capture())

            assertThat(captor.firstValue).containsExactlyInAnyOrder(
                ListItem.SectionHeader(1),
                ListItem.Item(11, "name"),
                ListItem.Item(13, "name"),
                ListItem.SectionHeader(2),
                ListItem.Item(22, "name"),
            )

            sections.removeObserver(observer)
        }
    }
}