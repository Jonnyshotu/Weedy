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
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.weedy.databinding.ActivityMainBinding
import com.example.weedy.ui.DragView
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.FlowingDrawer

/**
 * Main activity for the application that sets up the navigation and drawer layout.
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "Main Activity"

    private lateinit var binding: ActivityMainBinding // Binding for the activity's layout
    private lateinit var flowingDrawer: FlowingDrawer // The main drawer layout
    private lateinit var dragView: DragView // Custom view for handling drawer dragging
    private lateinit var navController: NavController // Controller for managing navigation
    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge layout

        // Inflate the activity's layout and set the content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set window insets to handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up navigation controller from the NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainNavHostContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // Initialize the drawer and drag view
        flowingDrawer = binding.drawerlayout
        dragView = binding.mainDrawerDV
        dragView.setFlowingDrawer(flowingDrawer)

        // Set up listeners for drawer state and navigation destination changes
        setupDrawerStateListener()
        setupDestinationListener()

        // Set up click listeners for drawer buttons
        val homeBTN = flowingDrawer.findViewById<CardView>(R.id.drawer_homeCV)
        val exploreBTN = flowingDrawer.findViewById<CardView>(R.id.drawer_exploreCV)
        val refreshBTN = flowingDrawer.findViewById<CardView>(R.id.drawer_refreshCV)

        homeBTN.setOnClickListener {
            flowingDrawer.closeMenu() // Close the drawer
            Log.d(TAG, "home clicked")
            navController.popBackStack(R.id.nav_graph, false) // Pop back to the root of the navigation graph
            navController.navigate(R.id.homeFragment) // Navigate to HomeFragment
        }

        exploreBTN.setOnClickListener {
            flowingDrawer.closeMenu() // Close the drawer
            Log.d(TAG, "explore clicked")
            navController.navigate(R.id.exploreFragment) // Navigate to ExploreFragment
        }

        refreshBTN.setOnClickListener {
            viewModel.loadRemoteGenetics() // Refresh genetics data
            Log.d(TAG, "refresh clicked")
        }
    }

    /**
     * Sets up a listener for changes in the drawer's state.
     */
    private fun setupDrawerStateListener() {
        flowingDrawer.setOnDrawerStateChangeListener(object :
            ElasticDrawer.OnDrawerStateChangeListener {
            override fun onDrawerStateChange(oldState: Int, newState: Int) {
                when (newState) {
                    FlowingDrawer.STATE_OPENING, FlowingDrawer.STATE_OPEN -> {
                        binding.mainDrawerDV.visibility = View.GONE
                    }

                    FlowingDrawer.STATE_CLOSING, FlowingDrawer.STATE_CLOSED -> {
                        binding.mainDrawerDV.visibility = View.VISIBLE
                    }
                }
            }

            override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {
                // No action needed for drawer sliding
            }
        })
    }

    /**
     * Sets up a listener for changes in the navigation destination.
     */
    private fun setupDestinationListener() {
        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            if (navDestination.id == R.id.newPlantFragment ||
                navDestination.id == R.id.newPlantGeneticFragment ||
                navDestination.id == R.id.detailFragment ||
                navDestination.id == R.id.newPlantStateFragment) {
                binding.mainDrawerDV.visibility = View.GONE // Hide drawer view for specific destinations
            } else {
                binding.mainDrawerDV.visibility = View.VISIBLE // Show drawer view for other destinations
            }
        }
    }
}
