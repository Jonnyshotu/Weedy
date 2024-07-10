package com.example.weedy

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.weedy.databinding.ActivityMainBinding
import com.mxn.soul.flowingdrawer_core.FlowingDrawer

class MainActivity : AppCompatActivity() {

    private val TAG = "Main Activity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: FlowingDrawer
    private lateinit var navController: NavController
    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainNavHostContainerView) as NavHostFragment
        navController = navHostFragment.navController

        drawer = binding.drawerlayout

        val homeBTN = drawer.findViewById<CardView>(R.id.drawer_homeCV)
        val exploreBTN = drawer.findViewById<CardView>(R.id.drawer_exploreCV)
        val refreshBTN = drawer.findViewById<CardView>(R.id.drawer_refreshCV)

        homeBTN.setOnClickListener {
            drawer.closeMenu()
            Log.d(TAG, "home clicked")
            navController.navigate(R.id.homeFragment)
        }

        exploreBTN.setOnClickListener {
            drawer.closeMenu()
            Log.d(TAG, "explore clicked")
            navController.navigate(R.id.exploreFragment)
        }

        refreshBTN.setOnClickListener {
            viewModel.loadRemoteenetics()
            Log.d(TAG, "refresh clicked")
        }
    }
}