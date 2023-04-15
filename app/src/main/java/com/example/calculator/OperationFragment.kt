package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import constants.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.calculator.databinding.FragmentOperationBinding

class OperationFragment : Fragment() {
    private var operation: String = R.string.empty_string.toString()
    private lateinit var binding: FragmentOperationBinding
    private val zeroInDouble = 0E308
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperationBinding.inflate(inflater)
        arguments?.getString(Constants.OPERATION.name)?.let {
            binding.operationButton.text = arguments?.getString(Constants.OPERATION.name).toString()
        }
        arguments = Bundle()
        operation = binding.operationButton.text.toString()
        if (binding.operationButton.text == getString(R.string.empty_string)) {
            disableEditTextAndButton()
        }
        val fragmentManager = parentFragmentManager
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val fragmentTransaction = fragmentManager.beginTransaction()

            if (binding.operationButton.text == getString(R.string.empty_string)) {
                    fragmentTransaction.hide(this)
            } else {
                val mainFragment = fragmentManager.findFragmentById(R.id.fragment_a_container)
                fragmentTransaction.show(this)
                mainFragment?.let{
                    fragmentTransaction.hide(mainFragment)
                }
            }
            fragmentTransaction.commit()
        }
        binding.operationButton.setOnClickListener {
            if (binding.operationButton.text != getString(R.string.empty_string)) {
                if ((!(binding.number1EditText.text.isEmpty() || binding.number2EditText.text.isEmpty()))) {
                    if (!(binding.number2EditText.text.toString()
                            .toDouble() == 0.0 && binding.operationButton.text.toString() == getString(
                            R.string.div
                        ))
                    ) {
                        val number1 = binding.number1EditText.text.toString().toDouble()
                        val number2 = binding.number2EditText.text.toString().toDouble()
                        val result = Bundle()
                        val resultString = getString(
                            R.string.result_string,
                            number1.toLong(),
                            when (binding.operationButton.text.toString()) {
                                getString(R.string.add) -> getString(R.string.plus)
                                getString(R.string.sub) -> getString(R.string.minus)
                                getString(R.string.mul) -> getString(R.string.asterisk)
                                else -> getString(R.string.slash)
                            },
                            number2.toInt(),
                            operation(number1, number2, operation)
                        )
                        result.putString(Constants.RESULT.name, resultString)
                        fragmentManager.setFragmentResult(Constants.RESULT.name, result)
                        disableEditTextAndButton()
                        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            val mainFragment = fragmentManager.findFragmentById(R.id.fragment_a_container)
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                fragmentTransaction.hide(this)
                                mainFragment?.let{
                                    fragmentTransaction.show(mainFragment)
                                }
                                fragmentTransaction.commit()
                        }
                    } else {
                        Toast.makeText(context, R.string.non_zero_alert, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, R.string.edit_text_empty_message, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, R.string.no_operation_chosen_error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.OPERATION.name, operation)
        if (this::binding.isInitialized) {
            arguments?.putString(Constants.OPERATION.name, binding.operationButton.text.toString())
            if (binding.number1EditText.text.isNotEmpty())
                arguments?.putString(
                    Constants.EDITTEXT1.name,
                    binding.number1EditText.text.toString()
                )
            if (binding.number2EditText.text.isNotEmpty())
                arguments?.putString(
                    Constants.EDITTEXT2.name,
                    binding.number2EditText.text.toString()
                )
        }
    }

    private fun operation(a: Double, b: Double, operation: String): String {
        return when (operation) {
            getString(R.string.add) -> {
                with(a + b) {
                    if (this.isDecimalZero())
                        this.toInt()
                    else
                        this
                }
            }
            getString(R.string.sub) -> {
                with(a - b) {
                    if (this.isDecimalZero())
                        this.toInt()
                    else
                        this
                }
            }
            getString(R.string.mul) -> {
                with(a * b) {
                    if ((this.isDecimalZero()) && (this < Long.MAX_VALUE.toDouble())) {
                        this.toLong()
                    } else
                        this
                }
            }
            else -> {
                if (b == 0.0) {
                    R.string.undefined
                } else {
                    with(a / b) {
                        if (this.isDecimalZero())
                            this.toInt()
                        else
                            this
                    }
                }
            }
        }.toString()
    }

    private fun Double.isDecimalZero() = when (this % 1) {
        zeroInDouble -> true
        else -> false
    }
    fun disableEditTextAndButton(){
        binding.operationButton.text = getString(R.string.empty_string)
        binding.number1EditText.makeInaccessible()
        binding.number2EditText.makeInaccessible()
    }

    private fun EditText.makeInaccessible() {
        text.clear()
        isClickable = false
        isFocusable = false
        setOnClickListener {
            Toast.makeText(context, R.string.no_operation_chosen_error, Toast.LENGTH_SHORT).show()
        }
    }
    fun getOperationText(): String{
        return binding.operationButton.text.toString()
    }
}