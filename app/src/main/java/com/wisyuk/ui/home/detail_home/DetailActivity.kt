package com.wisyuk.ui.home.detail_home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wisyuk.R
import com.wisyuk.databinding.ActivityDetailBinding
import com.wisyuk.ui.home.MainActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back_button -> {
                val intent = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.favorite_button -> {
                // TODO: Handle favorite, saran: save aja kali?
            }
        }
        return super.onOptionsItemSelected(item)
    }
}