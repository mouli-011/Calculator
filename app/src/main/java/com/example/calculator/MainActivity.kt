package com.example.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var operation: String = ""
    private var viewChanged = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            operation = savedInstanceState.getString("operation").toString()
            viewChanged = savedInstanceState.getBoolean("viewChanged")
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        if (!viewChanged)
            startingScreen()
        else
            displayResult(operation)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("operation", operation)
        outState.putBoolean("viewChanged", viewChanged)
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
        operation = when(it){
                binding.addButton -> {
                    getInputIntent.putExtra("OPERATION", OPERATIONS.ADD.string)
                    "+"
                }
                binding.subButton -> {
                    getInputIntent.putExtra("OPERATION", OPERATIONS.SUB.string)
                    "-"
                }
                binding.mulButton -> {
                    getInputIntent.putExtra("OPERATION", OPERATIONS.MUL.string)
                    "*"
                }
                else -> {
                    getInputIntent.putExtra("OPERATION", OPERATIONS.DIV.string)
                    "/"
                }
        }
            startActivityForResult(getInputIntent, 1)
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
                val resultText =
                    "${viewModel.number1} $operation ${viewModel.number2} is ${viewModel.result}"
                text = resultText
            }
            resetButton.setOnClickListener {
                startingScreen()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                data?.let {
                    viewModel.number1 = it.getIntExtra("a", 0)
                    viewModel.number2 = it.getIntExtra("b", 0)
                    viewModel.result = it.getIntExtra("result", 0)
                    displayResult(operation)
                } ?: startingScreen()
            }
        }
    }
}