package com.example.timepicker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etTimePicker : EditText
    private lateinit var etMTimePicker : EditText
    private lateinit var etDatePicker : EditText
    private lateinit var btnClick : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etTimePicker = findViewById(R.id.etTimePicker)
        etMTimePicker = findViewById(R.id.etMTimePicker)
        btnClick = findViewById(R.id.btnClick)
        etDatePicker = findViewById(R.id.etDatePicker)

        etTimePicker.setOnClickListener {
            timePicker().show().toString()
        }

        etMTimePicker.setOnClickListener {
            mTimePicker()
        }

//        etDatePicker.setOnClickListener {
//            datePicker()
//        }

        datePicker()

        btnClick.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "Here is the share content body"  // If you Want to share to body
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, etTimePicker.text.toString())
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }
    }

    private fun timePicker() : TimePickerDialog{

        val timePicker: TimePickerDialog
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        timePicker = TimePickerDialog(this,
            { _, hourOfDay, minute ->
                etTimePicker.setText(String.format("%02d : %02d", hourOfDay, minute))
            }, hour, minute, true)

        return timePicker
    }

    @SuppressLint("SimpleDateFormat")
    private fun mTimePicker(){

        val mTimePicker : MaterialTimePicker

        // Check System Format
        val isSystem24Hour = is24HourFormat(this)
        // timePicker Format according to System Format
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        // build the MaterialTimePicker Dialog
        mTimePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Set Time")
            .build()

        // Show the MaterialTimePicker
        mTimePicker.show(supportFragmentManager,"Boss")

        // Action by Positive response "OK"
        mTimePicker.addOnPositiveButtonClickListener {
            val hour = mTimePicker.hour
            val minute = mTimePicker.minute
            etMTimePicker.setText(String.format("%02d : %02d",hour,minute))
        }

    }

    // For Date Picker
    private var year = 0
    private var month = 0
    private var day = 0
    private fun datePicker() {
        val cal = Calendar.getInstance()
        val cal1 = Calendar.getInstance()

        year = cal1.get(Calendar.YEAR)
        month = cal1.get(Calendar.MONTH)
        day = cal1.get(Calendar.DAY_OF_MONTH)

        // For Creating Date Picker Dialog Using Dynamic Code
        val dateShow =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                val myFormat = "dd-MM-yyyy"
                val dateFormat = SimpleDateFormat(myFormat, Locale.US)
                etDatePicker.setText(dateFormat.format(cal.time))    // Set Date in EditText
            }

        // Date Piking When Click that EditText
        etDatePicker.setOnClickListener {
            val dateDialog = DatePickerDialog(
                this, dateShow,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            //for max date set (At current Time & Date)
            dateDialog.datePicker.maxDate = cal1.timeInMillis
            dateDialog.show()
        }
    }

}