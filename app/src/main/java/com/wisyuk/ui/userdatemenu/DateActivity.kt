package com.wisyuk.ui.userdatemenu

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wisyuk.R
import com.wisyuk.databinding.ActivityDateBinding
import com.wisyuk.ui.home.MainActivity
import java.text.SimpleDateFormat
import java.util.Locale

class DateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDateBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.dateLayout.setOnClickListener{
            val initialDate = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val date = Calendar.getInstance()
                    date.set(Calendar.YEAR, year)
                    date.set(Calendar.MONTH, monthOfYear)
                    date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val simpleDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                    val formattedDate = simpleDateFormat.format(date.time)
                    binding.tvDate.text = formattedDate

                }, initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        binding.continueButton.setOnClickListener {
            val intent = Intent(this@DateActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}