package com.wisyuk.ui.home.detail_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wisyuk.R
import com.wisyuk.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}