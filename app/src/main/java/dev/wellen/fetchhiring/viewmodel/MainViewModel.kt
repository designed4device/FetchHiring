package dev.wellen.fetchhiring.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wellen.fetchhiring.model.FetchHiringApi
import dev.wellen.fetchhiring.model.Item
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(api: FetchHiringApi) : ViewModel() {

    val sections: LiveData<List<ListItem>> = liveData {
        val data = api.getListItems()
            .filter { !it.name.isNullOrBlank() && it.listId != null && it.id != null }
            .groupBy { it.listId!! }
            .toSortedMap()
            .flatMap { entry ->
                listOf<ListItem>(ListItem.SectionHeader(entry.key)).plus(
                    entry.value
                        //sorting by name as string value because requirement is to sort by name
                        // this results in a possibly unexpected ordering since the names
                        // appear to be concatenation of "Item {id}"
                        // a more natural order might be to sort by listId, then id, then name
                        // unless there is some kind of guarantee on the format of the name string
                        // so you could provide a custom sort for it
                        .sortedBy { it.name }
                        .map { ListItem.Item(it.id!!, it.name!!) }
                )
            }

        emit(data)
    }
}

sealed class ListItem {
    data class SectionHeader(val id: Int) : ListItem()
    data class Item(val id: Int, val name: String) : ListItem()
}