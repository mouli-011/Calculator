package com.example.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.calculator.databinding.FragmentMainBinding
import constants.Constants

class MainFragment : Fragment() {
    private var number1 = 0
    private var number2 = 0
    private var result = 0
    private var operation = R.string.empty_string.toString()
    private var viewChanged = false
    private lateinit var binding: FragmentMainBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState != null) {
            number1 = savedInstanceState.getInt(Constants.NUMBER1.name)
            number2 = savedInstanceState.getInt(Constants.NUMBER2.name)
            result = savedInstanceState.getInt(Constants.RESULT.name)
            operation = savedInstanceState.getString(Constants.OPERATION.name).toString()
            viewChanged = savedInstanceState.getBoolean(Constants.VIEWCHANGED.name)
        }
        onBackPressedCallback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (viewChanged) {
                    startingScreen()
                } else {
                    activity?.finish()
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,onBackPressedCallback)
        val fragmentManager = parentFragmentManager
        fragmentManager.setFragmentResultListener(Constants.RESULT.name,this){ _, fragmentResult ->
            number1 = fragmentResult.getInt(Constants.NUMBER1.name)
            number2 = fragmentResult.getInt(Constants.NUMBER2.name)
            result = fragmentResult.getInt(Constants.RESULT.name)
            operation = fragmentResult.getString(Constants.OPERATION.name).toString()
            displayResult(operation)
        }
        binding = FragmentMainBinding.inflate(inflater)
        if (!viewChanged)
            startingScreen()
        else
            displayResult(operation)
        return binding.root
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
        val fragmentManager = parentFragmentManager
        val listener = OnClickListener {
            val fragmentTransaction = fragmentManager.beginTransaction()
            val operationSelected = Bundle()
            when (it) {
                binding.addButton -> operationSelected.putString(Constants.OPERATION.name,getString(R.string.add))
                binding.subButton -> operationSelected.putString(Constants.OPERATION.name,getString(R.string.sub))
                binding.mulButton -> operationSelected.putString(Constants.OPERATION.name,getString(R.string.mul))
                binding.divButton -> operationSelected.putString(Constants.OPERATION.name,getString(R.string.div))
            }
            fragmentManager.setFragmentResult(Constants.OPERATION.name, operationSelected)
            fragmentTransaction.replace(R.id.fragment_holder,OperationFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        with(binding) {
            addButton.setOnClickListener(listener)
            subButton.setOnClickListener(listener)
            mulButton.setOnClickListener(listener)
            divButton.setOnClickListener(listener)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.OPERATION.name, operation)
        outState.putBoolean(Constants.VIEWCHANGED.name, viewChanged)
        outState.putInt(Constants.NUMBER1.name, number1)
        outState.putInt(Constants.NUMBER2.name, number2)
        outState.putInt(Constants.RESULT.name, result)
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