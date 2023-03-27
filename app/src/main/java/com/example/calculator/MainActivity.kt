package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var number1 = 0
    private var number2 = 0
    private var result = 0
    private var operation = R.string.empty_string.toString()
    private var viewChanged = false
    private lateinit var binding: ActivityMainBinding
    private val activityBLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val resultIntent: Intent? = activityResult.data
            resultIntent?.let {
                number1 = it.getIntExtra(getString(R.string.number1), 0)
                number2 = it.getIntExtra(getString(R.string.number2), 0)
                result = it.getIntExtra(getString(R.string.result), 0)
                operation = it.getStringExtra(getString(R.string.operation)).toString()
                displayResult(operation)
            } ?: startingScreen()
        }
    }
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            number1 = savedInstanceState.getInt(getString(R.string.number1))
            number2 = savedInstanceState.getInt(getString(R.string.number2))
            result = savedInstanceState.getInt(getString(R.string.result))
            operation = savedInstanceState.getString(getString(R.string.operation)).toString()
            viewChanged = savedInstanceState.getBoolean(getString(R.string.view_changed))
        }
        onBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (viewChanged) {
                    startingScreen()
                } else {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        if (!viewChanged)
            startingScreen()
        else
            displayResult(operation)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.operation), operation)
        outState.putBoolean(getString(R.string.view_changed), viewChanged)
        outState.putInt(getString(R.string.number1), number1)
        outState.putInt(getString(R.string.number2), number2)
        outState.putInt(getString(R.string.result), result)
    }

    private fun startingScreen() {
        viewChanged = false
        with(binding) {
            operationLayout.visibility = View.VISIBLE
            resultLayout.visibility = View.GONE
        }
        setListeners()
    }

    private fun setListeners() {
        val getInputIntent = Intent(this, ActivityB::class.java)
        val listener = OnClickListener {
            when (it) {
                binding.addButton -> {
                    getInputIntent.putExtra(getString(R.string.operation), getString(R.string.add))
                }
                binding.subButton -> {
                    getInputIntent.putExtra(getString(R.string.operation), getString(R.string.sub))
                }
                binding.mulButton -> {
                    getInputIntent.putExtra(getString(R.string.operation), getString(R.string.mul))
                }
                else -> {
                    getInputIntent.putExtra(getString(R.string.operation), getString(R.string.div))
                }
            }
            activityBLauncher.launch(getInputIntent)
        }
        binding.addButton.setOnClickListener(listener)
        binding.subButton.setOnClickListener(listener)
        binding.mulButton.setOnClickListener(listener)
        binding.divButton.setOnClickListener(listener)
    }

    private fun displayResult(operation: String) {
        viewChanged = true
        with(binding) {
            operationLayout.visibility = View.GONE
            resultLayout.visibility = View.VISIBLE
            with(resultTextView) {
                text = getString(R.string.result_string, number1, operation, number2, result)
            }
            resetButton.setOnClickListener {
                startingScreen()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback.remove()
    }
}