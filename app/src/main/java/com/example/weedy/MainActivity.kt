package com.example.weedy

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.weedy.ui.DragView
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.FlowingDrawer

class MainActivity : AppCompatActivity() {

    private val TAG = "Main Activity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var flowingDrawer: FlowingDrawer
    private lateinit var dragView: DragView
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostContainerView) as NavHostFragment
        navController = navHostFragment.navController

        flowingDrawer = binding.drawerlayout
        dragView = binding.mainDrawerDV
        dragView.setFlowingDrawer(flowingDrawer)

        setupDrawerStateListener()

        val homeBTN = flowingDrawer.findViewById<CardView>(R.id.drawer_homeCV)
        val exploreBTN = flowingDrawer.findViewById<CardView>(R.id.drawer_exploreCV)
        val refreshBTN = flowingDrawer.findViewById<CardView>(R.id.drawer_refreshCV)

        homeBTN.setOnClickListener {
            flowingDrawer.closeMenu()
            Log.d(TAG, "home clicked")
            navController.navigate(R.id.homeFragment)
            navController.popBackStack(R.id.nav_graph, false)
        }

        exploreBTN.setOnClickListener {
            flowingDrawer.closeMenu()
            Log.d(TAG, "explore clicked")
            navController.navigate(R.id.exploreFragment)
        }

        refreshBTN.setOnClickListener {
            viewModel.loadRemoteenetics()
            Log.d(TAG, "refresh clicked")
        }
    }

    private fun setupDrawerStateListener() {
        flowingDrawer.setOnDrawerStateChangeListener(object :
            ElasticDrawer.OnDrawerStateChangeListener {
            override fun onDrawerStateChange(oldState: Int, newState: Int) {
                when (newState) {
                    FlowingDrawer.STATE_OPENING -> {
                        binding.mainDrawerDV.visibility = View.GONE
                    }

                    FlowingDrawer.STATE_OPEN -> {
                        binding.mainDrawerDV.visibility = View.GONE
                    }

                    FlowingDrawer.STATE_CLOSING -> {
                        binding.mainDrawerDV.visibility = View.VISIBLE
                    }

                    FlowingDrawer.STATE_CLOSED -> {
                        binding.mainDrawerDV.visibility = View.VISIBLE
                    }

                }
            }

            override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {}
        })
    }
}
