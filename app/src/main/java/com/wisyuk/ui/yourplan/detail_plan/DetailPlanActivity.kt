package com.wisyuk.ui.yourplan.detail_plan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wisyuk.R
import com.wisyuk.databinding.ActivityDetailPlanBinding

class DetailPlanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}