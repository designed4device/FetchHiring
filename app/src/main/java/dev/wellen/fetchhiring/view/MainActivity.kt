package dev.wellen.fetchhiring.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.wellen.fetchhiring.databinding.ActivityMainBinding
import dev.wellen.fetchhiring.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerViewAdapter = RecyclerViewAdapter()

        binding = ActivityMainBinding.inflate(layoutInflater)
            .also {
                setContentView(it.root)
                it.recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = recyclerViewAdapter
                }
            }

        viewModel.sections.observe(this) {
            recyclerViewAdapter.setItems(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

