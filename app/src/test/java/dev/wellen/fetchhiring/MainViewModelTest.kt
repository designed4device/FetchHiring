package dev.wellen.fetchhiring

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.wellen.fetchhiring.model.FetchHiringApi
import dev.wellen.fetchhiring.model.Item
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
            val observer: Observer<List<Item>> = mock()
            sections.observeForever(observer)

            val captor = argumentCaptor<List<Item>>()
            verify(observer).onChanged(captor.capture())

            assertThat(captor.firstValue)
                .containsExactly(
                    Item(1, 12, "a"),
                    Item(1, 11, "b"),
                    Item(1, 13, "c"),
                    Item(2, 22, "d"),
                    Item(2, 21, "z"),
                    Item(3, 31, "f"),
                    Item(3, 32, "j")
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
            val observer: Observer<List<Item>> = mock()
            sections.observeForever(observer)

            val captor = argumentCaptor<List<Item>>()
            verify(observer).onChanged(captor.capture())

            assertThat(captor.firstValue).containsExactlyInAnyOrder(
                Item(1, 11, "name"),
                Item(1, 13, "name"),
                Item(2, 22, "name"),
            )

            sections.removeObserver(observer)
        }
    }
}