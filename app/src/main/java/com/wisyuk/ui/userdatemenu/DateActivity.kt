package com.wisyuk.ui.userdatemenu

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.wisyuk.databinding.ActivityDateBinding
import com.wisyuk.ui.ViewModelFactory
import com.wisyuk.ui.home.MainActivity
import com.wisyuk.ui.home.ui.home.HomeFragment.Companion.EXTRA_GOAT
import com.wisyuk.ui.home.ui.home.HomeFragment.Companion.EXTRA_USERID
import com.wisyuk.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Locale

class DateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDateBinding

    private val viewModel by viewModels<DateViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var userId = -1
    private var goAt = ""

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                userId = user.id
            }
        }

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
                    val goAtDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = simpleDateFormat.format(date.time)
                    binding.tvDate.text = formattedDate
                    goAt = goAtDateFormat.format(date.time)

                }, initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()
        }

        binding.continueButton.setOnClickListener {
            val intent = Intent(this@DateActivity, MainActivity::class.java)
            intent.putExtra(EXTRA_GOAT, goAt)
            intent.putExtra(EXTRA_USERID, userId)
            startActivity(intent)
        }
    }
}