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

    val sections: LiveData<List<Item>> = liveData {
        val data = api.getListItems()
            .filter { !it.name.isNullOrBlank() && it.listId != null && it.id != null }
            .sortedWith(compareBy(
                { it.listId },
                { it.name }
            ))

        emit(data)
    }
}