package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        if (savedInstanceState == null) {
            setFragmentsInContainer(R.id.fragment_a_container, MainFragment())
            setFragmentsInContainer(R.id.fragment_b_container, OperationFragment())
        } else {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val operationFragment = fragmentManager.findFragmentById(R.id.fragment_b_container) as OperationFragment?
                if (operationFragment != null) {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.show(operationFragment)
                    fragmentTransaction.commit()
                }
            }
        }
        onBackListener()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onBackListener(){
        val fragmentManager = supportFragmentManager
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainFragment: MainFragment? = fragmentManager.findFragmentById(R.id.fragment_a_container) as MainFragment?
                val operationFragment: OperationFragment? = fragmentManager.findFragmentById(R.id.fragment_b_container) as OperationFragment?
                mainFragment?.let {
                    operationFragment?.let {
                        if (operationFragment.isVisible) {
                            val fragmentTransaction = fragmentManager.beginTransaction()
                            if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                                //mainFragment.startingScreen()
                                operationFragment.disableEditTextAndButton()
                                fragmentTransaction.hide(operationFragment)
                                fragmentTransaction.show(mainFragment)
                                fragmentTransaction.commit()
                            }
                            else{
                                if (operationFragment.getOperationText() == getString(R.string.empty_string)){
                                    finish()
                                }
                                else {
                                    operationFragment.disableEditTextAndButton()
                                }
                            }
                        } else {
                            if (mainFragment.getViewChanged()) {
                                mainFragment.dataSet.value = listOf(getString(R.string.add), getString(R.string.sub), getString(R.string.mul), getString(R.string.div))
                            } else {
                                finish()
                            }
                        }
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
    private fun setFragmentsInContainer(containerId: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressedCallback.handleOnBackPressed()
        return true
    }

}