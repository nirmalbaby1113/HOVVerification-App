package com.nirmal.baby.hovverification.features.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.nirmal.baby.hovverification.R
import com.nirmal.baby.hovverification.database.AppDatabase
import com.nirmal.baby.hovverification.databinding.ActivityHomeBinding
import com.nirmal.baby.hovverification.features.home.entity.HomeEntity
import com.nirmal.baby.hovverification.features.home.interactor.HomeInteractor
import com.nirmal.baby.hovverification.features.home.presenter.HomePresenter
import com.nirmal.baby.hovverification.features.home.router.HomeRouter

class HomeActivity : AppCompatActivity(), HomeViewInterface {
    private lateinit var presenter: HomePresenter
    private lateinit var adapter: HomeAdapter
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize VIPER components
        val database = AppDatabase.getDatabase(this)
        val interactor = HomeInteractor(database.faceDataDao())
        val router = HomeRouter(this)
        presenter = HomePresenter(this, interactor)

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        presenter.loadFaceData()

        // Navigate to HOV Verification
        binding.navigateButton.setOnClickListener {
            router.navigateToHOVVerification()
        }
    }

    override fun displayFaceData(data: List<HomeEntity>) {
        if (data.isEmpty()) {
            // Show "No recent Verifications done" message
            binding.noDataTextView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            // Show RecyclerView with data
            binding.noDataTextView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

            adapter = HomeAdapter(this, data)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun showLoading() {
        // Show loading spinner
    }

    override fun hideLoading() {
        // Hide loading spinner
    }

    override fun showError(message: String) {
        // Display error message
    }
}