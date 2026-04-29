package com.example.campussafeapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.campussafeapplication.models.HazardReport
import com.example.campussafeapplication.utils.SessionManager
import com.example.campussafeapplication.viewmodels.HazardReportViewModel
import kotlinx.coroutines.launch

class ReportHazardActivity : AppCompatActivity() {

    private lateinit var viewModel: HazardReportViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_hazard)

        viewModel = ViewModelProvider(this)[HazardReportViewModel::class.java]
        sessionManager = SessionManager(this)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        // Bottom Navigation
        findViewById<View>(R.id.navHome).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        findViewById<View>(R.id.navReports).setOnClickListener {
            startActivity(Intent(this, MyReportsActivity::class.java))
        }

        findViewById<View>(R.id.navAdd).setOnClickListener {
            // Already on Report Hazard
        }

        findViewById<View>(R.id.navMaps).setOnClickListener {
            startActivity(Intent(this, NearbyReportsActivity::class.java))
        }

        findViewById<View>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        setupSpinners()
        setupSubmitButton()
        observeViewModel()
    }

    private fun setupSpinners() {
        val spinnerBuilding = findViewById<Spinner>(R.id.spinnerBuilding)
        val spinnerFloor = findViewById<Spinner>(R.id.spinnerFloor)

        val bAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.building_array,
            R.layout.spinner_item
        )
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBuilding.adapter = bAdapter

        val fAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.floor_array,
            R.layout.spinner_item
        )
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFloor.adapter = fAdapter

        findViewById<View>(R.id.layoutBuildingContainer).setOnClickListener { spinnerBuilding.performClick() }
        findViewById<View>(R.id.layoutFloorContainer).setOnClickListener { spinnerFloor.performClick() }
    }

    private fun setupSubmitButton() {
        val btnSubmit = findViewById<Button>(R.id.btnSubmitReport)
        val etRoom = findViewById<EditText>(R.id.etRoom)
        val etTitle = findViewById<EditText>(R.id.etHazardTitle)
        val etDescription = findViewById<EditText>(R.id.etHazardDescription)
        val spinnerBuilding = findViewById<Spinner>(R.id.spinnerBuilding)
        val spinnerFloor = findViewById<Spinner>(R.id.spinnerFloor)

        btnSubmit.setOnClickListener {
            val room = etRoom.text.toString().trim()
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val building = spinnerBuilding.selectedItem.toString()
            val floor = spinnerFloor.selectedItem.toString()
            val userId = sessionManager.getUserId()

            if (title.isEmpty() || description.isEmpty() || room.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId.isEmpty()) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val report = HazardReport(
                userId = userId,
                title = title,
                building = building,
                floor = floor,
                room = room,
                description = description
            )

            viewModel.createReport(report)
        }
    }

    private fun observeViewModel() {
        val btnSubmit = findViewById<Button>(R.id.btnSubmitReport)
        
        lifecycleScope.launch {
            viewModel.reportState.collect { state ->
                when (state) {
                    is HazardReportViewModel.ReportState.Loading -> {
                        btnSubmit.isEnabled = false
                        btnSubmit.text = "Submitting..."
                    }
                    is HazardReportViewModel.ReportState.Success -> {
                        btnSubmit.isEnabled = true
                        btnSubmit.text = "Submit Report"
                        Toast.makeText(this@ReportHazardActivity, "Report submitted successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Return to previous screen or home
                    }
                    is HazardReportViewModel.ReportState.Error -> {
                        btnSubmit.isEnabled = true
                        btnSubmit.text = "Submit Report"
                        Toast.makeText(this@ReportHazardActivity, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        btnSubmit.isEnabled = true
                        btnSubmit.text = "Submit Report"
                    }
                }
            }
        }
    }
}
