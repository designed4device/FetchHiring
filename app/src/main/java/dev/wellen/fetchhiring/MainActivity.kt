package dev.wellen.fetchhiring

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dev.wellen.fetchhiring.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
            .also {
                setContentView(it.root)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}