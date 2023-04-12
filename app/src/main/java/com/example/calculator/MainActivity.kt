package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        if (savedInstanceState == null) {
            setFragmentsInContainer(R.id.fragment_a_container, MainFragment())
            setFragmentsInContainer(R.id.fragment_b_container, OperationFragment())
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val fragmentBContainer: FrameLayout = findViewById(R.id.fragment_b_container)!!
                val layoutParams = fragmentBContainer.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 0f
                fragmentBContainer.layoutParams = layoutParams
            }
        } else {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (fragmentManager.findFragmentById(R.id.fragment_b_container) != null) {
                    val operationFragment =
                        fragmentManager.findFragmentById(R.id.fragment_b_container)
                    if (operationFragment != null) {
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.attach(operationFragment)
                        fragmentTransaction.commit()
                    }
                }
            }
        }
    }

    private fun setFragmentsInContainer(containerId: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.commit()
    }

}