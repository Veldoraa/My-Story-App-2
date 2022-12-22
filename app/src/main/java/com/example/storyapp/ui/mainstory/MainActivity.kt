package com.example.storyapp.ui.mainstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.story.addstory.AddStoryActivity
import com.example.storyapp.ui.login.LoginActivity
import com.example.storyapp.R
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.map.MapStorys.Companion.TOKEN
import com.example.storyapp.ui.map.MapsActivity
import com.example.storyapp.utils.LoadingStateAdapter

class MainActivity : AppCompatActivity() {
    private var _binding:ActivityMainBinding? = null
    private  val binding get() = _binding!!
    private var token : String = ""

    private fun listStory() {
        val listStoryAdapter = StoryListAdapter()
        val factoryModels = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factoryModels)[MainViewModel::class.java]

        binding.rvStory.adapter = listStoryAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                listStoryAdapter.retry()
            }
        )

        viewModel.storyList.observe(this) { listB ->
            listStoryAdapter.submitData(lifecycle, listB)
        }
    }
    private fun swipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            listStory()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listStory()
        swipeRefreshLayout()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                Intent(this, AddStoryActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.menu_maps ->{
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(TOKEN, token)
                startActivity(intent)
            }
            R.id.menu_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(resources.getString(R.string.caution))
                builder.setMessage(resources.getString(R.string.To_do))
                builder.setPositiveButton(resources.getString(R.string.Yes)) { _, _ ->
                    this.getSharedPreferences("data_user", 0).edit().clear()
                        .apply()
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it)
                    }
                    finish()
                }
                builder.setNegativeButton(resources.getString(R.string.No)) { dialog, _ ->
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}